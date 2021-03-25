package com.orion.friendsroom.controller;

import com.orion.friendsroom.dto.AuthenticationRequestDto;
import com.orion.friendsroom.dto.AuthenticationResponseDto;
import com.orion.friendsroom.service.AdminService;
import com.orion.friendsroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login")
    public AuthenticationResponseDto loginUser(@RequestBody AuthenticationRequestDto requestDto) {
        return userService.validateUserLogin(requestDto);
    }
}
