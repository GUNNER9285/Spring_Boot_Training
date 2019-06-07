package com.training.platform.controllers.api;

import com.training.platform.entities.User;
import com.training.platform.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController("api-user")
@RequestMapping("/api/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "")
    public List<User> index() throws Exception {
        return userService.findAll();
    }

    @GetMapping(value = "/v2")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> index2() throws Exception {
        return userService.findAll();
    }

    @GetMapping(value="/{id}")
    public Optional<User> findById(@PathVariable String id) throws Exception {
        Optional<User> user = userService.findById(Integer.parseInt(id));
        return user;
    }
}