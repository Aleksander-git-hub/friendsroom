package com.orion.friendsroom.controller;

import com.orion.friendsroom.dto.user.UserRegisterDto;
import com.orion.friendsroom.dto.user.UserSuccessRegisterDto;
import com.orion.friendsroom.mapper.UserMapper;
import com.orion.friendsroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1")
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping(value = "/register")
    public UserSuccessRegisterDto registerUser(@RequestBody UserRegisterDto userRegisterDto) {
        return userMapper.toSuccessRegister(userService.registerUser(userRegisterDto));
    }

    @GetMapping(value = "/activate/{code}")
    public String activateEmail(@PathVariable String code) {
        userService.activateUser(code);

        return "confirm";
    }
}
