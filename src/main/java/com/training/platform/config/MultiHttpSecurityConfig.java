
package com.training.platform.config;

import com.training.platform.config.MyAuthenticationSuccessHandler;
import com.training.platform.security.AuthExceptionEntryPoint;
import com.training.platform.security.JwtConfigurer;
import com.training.platform.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MultiHttpSecurityConfig {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MyAuthenticationSuccessHandler successHandler;

    @Value("${spring.queries.users-query}")
    private String usersQuery;

    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Configuration
    @Order(1)
    public class ApiSecurityAdapter extends WebSecurityConfigurerAdapter {

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.httpBasic().disable();
            http.exceptionHandling().authenticationEntryPoint(new AuthExceptionEntryPoint());
            http.exceptionHandling().accessDeniedPage("/access-denied");
            http.csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .antMatcher("/rest/**")
                    .antMatcher("/api/**")
                    .authorizeRequests()
                    .antMatchers("/api/auth/**").permitAll()

                    .antMatchers("/api/**","/rest/**").hasAuthority("ADMIN").anyRequest().authenticated()

                    .and()
                    .apply(new JwtConfigurer(jwtTokenProvider));
        }
    }

    @Configuration
    //default Order at last
    public class WebSecurityAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(AuthenticationManagerBuilder auth)
                throws Exception {
            auth.
                    jdbcAuthentication()
                    .usersByUsernameQuery(usersQuery)
                    .authoritiesByUsernameQuery(rolesQuery)
                    .dataSource(dataSource)
                    .passwordEncoder(bCryptPasswordEncoder);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.
                    authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/demo/***").permitAll()
                    .antMatchers("/login").permitAll()
                    //.antMatchers("/api/***").hasAnyAuthority("USER", "ADMIN")
                    .antMatchers("/admin/**").hasAuthority("ADMIN").anyRequest()
                    .authenticated().and().csrf().disable().formLogin()
                    .loginPage("/login").failureUrl("/login?error=true")
                    .successHandler(successHandler)
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .and().logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login").and().exceptionHandling()
                    .accessDeniedPage("/access-denied");
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web
                    .ignoring()
                    .antMatchers("/favicon.**","/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
        }

    }
}