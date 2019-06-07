package com.training.platform.security;

import com.training.platform.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Created by Panupong_Kon on 1/7/2019.
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository users;
    public CustomUserDetailsService(UserRepository users) {
        this.users = users;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return this.users.findByEmail(username);
        }catch(UsernameNotFoundException ex){
            throw new UsernameNotFoundException("Username: " + username + " not found");
        }
    }
}