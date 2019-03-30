package com.training.platform.controllers.admin;

import com.training.platform.entities.Pager;
import com.training.platform.entities.User;
import com.training.platform.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/admin/user")
public class UserController {

    @GetMapping("/demo")
    public String demo(){
        return "admin/sample";
    }

    @Autowired
    private UserService userService;

    private static final int BUTTONS_TO_SHOW = 5;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 10;
    private static final int[] PAGE_SIZES = {5, 10, 20};

    @GetMapping(value = "")
    public String index(
            Model model,
            @RequestParam("pageSize") Optional<Integer> pageSize,
            @RequestParam("page") Optional<Integer> page) throws Exception {
            // http://localhost:8081/admin/user?pageSize=10&page=2
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        // Page<User> users = userService.findAll(PageRequest.of(evalPage, evalPageSize));
        Page<User> users = userService.findAll(PageRequest.of(evalPage, evalPageSize, Sort.by(Sort.Direction.DESC, "id")));
        Pager pager = new Pager(users.getTotalPages(), users.getNumber(), BUTTONS_TO_SHOW);

        model.addAttribute("items", users);
        model.addAttribute("selectedPageSize", evalPageSize);
        model.addAttribute("pageSizes", PAGE_SIZES);
        model.addAttribute("pager", pager);

        return "admin/user/lists";
    }

    @GetMapping(value="/create")
    public String create(Model model, User user) {
        model.addAttribute("cities", userService.getCities());
        model.addAttribute("user", user);
        return "admin/user/create";
    }

    // public String store(@Validated({ Default.class, Extended.class }) User user,
    @PostMapping(value="")
    public String store(@Valid User user,
                        BindingResult bindingResult,
                        @RequestParam Map<String,String> inputs,
                        RedirectAttributes redirAttrs,
                        Model model) throws Exception {

        if (bindingResult.hasErrors()) {
            model.addAttribute("cities", userService.getCities());
            model.addAttribute("user", user);
            return "admin/user/create";
        }
        //Saving data
        System.out.println(user);
        userService.save(inputs);
        redirAttrs.addFlashAttribute("success", "User [" +
                inputs.get("name") + " " +
                inputs.get("surname") + "] " +
                "created successfully.");
        return "redirect:/admin/user";
    }
}
