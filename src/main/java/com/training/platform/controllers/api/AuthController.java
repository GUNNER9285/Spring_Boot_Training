package com.training.platform.controllers.api;

import com.training.platform.entities.User;
import com.training.platform.repositories.UserRepository;
import com.training.platform.security.AuthenticationRequest;
import com.training.platform.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    UserRepository users;
    @PostMapping("/signin")
    public Map<Object, Object> signin(@RequestBody AuthenticationRequest data) {
        String email = data.getEmail();
        Map<Object, Object> response = new HashMap<>();
        try {

            User user = null;

            user = this.users.findByEmail(email);
            if(user != null){
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, data.getPassword()));
            }else{
                response.put("error", "Invalid username or password.");
                return response;
            }

            String token = jwtTokenProvider.createToken(email, user.getRoles());
            response.put("email", email);
            response.put("token", token);
            return response;
        }catch (Exception ex){
            response.put("error", ex.getMessage());
            response.put("failure", "น่านไง ใส่ผิดจริงด้วย");
            return response;
        }
    }
}
