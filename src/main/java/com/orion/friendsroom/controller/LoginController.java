package com.orion.friendsroom.controller;

import com.orion.friendsroom.dto.UserRegistrationDto;
import com.orion.friendsroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class LoginController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping(value = "/login")
    public String loginPage(Model model) {
        return "login";
    }

    @GetMapping(value = "/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }

    @PostMapping(value = "/register")
    public String registerNewUser(@Valid @ModelAttribute("user") UserRegistrationDto userRegistrationDto,
                                  BindingResult bindingResult) {
        if (userService.getUserByEmail(userRegistrationDto.getEmail()) != null) {
            bindingResult.rejectValue("email", "", "User already exist with email!");
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }
        
        if (!userRegistrationDto.getPassword().equals(userRegistrationDto.getRepeatPassword())) {
            bindingResult.rejectValue("password", "", "Password mismatch");
            return "register";
        }

        if (userRegistrationDto.getPassword().length() < 6) {
            bindingResult.rejectValue("password", "", "Password must have more than 5 chars!");
            return "register";
        }
        
        userService.registerUser(userRegistrationDto);
        return "redirect:/login";
    }

    @GetMapping(value = "/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated) {
            model.addAttribute("message", "User successfully activated");
        } else {
            model.addAttribute("message", "Activation code is not found");
        }

        return "login";
    }
}
