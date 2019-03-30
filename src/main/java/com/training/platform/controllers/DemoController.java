package com.training.platform.controllers;

import com.training.platform.entities.User;
import com.training.platform.repositories.UserRepository;

// New import class for repository
import com.training.platform.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/demo")
public class DemoController {
    @Autowired
    private UserRepository userRepository;

//    @GetMapping(value = "")
//    public List<User> index() throws Exception {
//        List<User> users = userRepository.findAll();
//        return users;
//    }

//    @GetMapping(value = "")
//    public List<User> index() throws Exception {
//        List<User> users = userRepository.findByCityAndActive("nakornpathom", 1);
//        return users;
//    }

//    @GetMapping(value = "")
//    public List<User> index() throws Exception {
//        List<Integer> ages = new ArrayList<Integer>( Arrays.asList(18, 19, 22) );
//        List<User> users = userRepository.findByAgeIn(ages);
//        return users;
//    }

    // Example for findAllByJpqlQuery
//    @GetMapping(value = "")
//    public List<User> index() throws Exception {
//        return userRepository.findAllByJpqlQuery();
//    }


    // Example for findAllByParamsQuery
//    @GetMapping(value = "")
//    public List<User> index() throws Exception {
//        return userRepository.findAllByParamsQuery(0, "nakornpathom");
//    }

    @Autowired
    private UserService userService;

    @GetMapping(value = "")
    public List<User> index() throws Exception {
        return userService.findAllByJpqlParamsQuery(0, "bangkok");
    }

    @GetMapping(value = "/create")
    public String create() throws Exception {
        return "Method Get, Function : create => HTML Render Form Create Page";
    }

    @PostMapping(value = "")
    public String store(@RequestParam Map<String,String> inputs) throws Exception {
        System.out.println("########### POST Param ###########");
        System.out.println(inputs);

        return "Method Post, Function : store => Receive post param and INSERT data to DB";
    }

    @GetMapping(value = "/{id}")
    public Optional<User> show(@PathVariable String id) throws Exception {
        Optional<User> user = userRepository.findById(Integer.parseInt(id));
        return user;
    }

    @GetMapping(value = "/{id}/edit")
    public String edit(@PathVariable String id) throws Exception {
        return "Method Get, Function : edit, ID : "+ id +" => HTML Render Form Edit Page from DB by ID";
    }

    @PutMapping(value = "/{id}")
    public String update(@PathVariable String id, @RequestParam Map<String,String> inputs) throws Exception {
        System.out.println("########### PUT Param ###########");
        System.out.println(inputs);

        return "Method PUT, Function : update => Receive id and PUT param and UPDATE data to DB";
    }

    @DeleteMapping(value = "/{id}")
    public String destroy(@PathVariable String id) throws Exception {
        return "Method DELETE, Function : destroy, ID : "+ id +" => Receive id and DELETE data to DB";
    }
}
