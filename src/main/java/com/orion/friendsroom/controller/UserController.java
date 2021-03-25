package com.orion.friendsroom.controller;

import com.orion.friendsroom.dto.admin.EmailUserDto;
import com.orion.friendsroom.dto.user.PasswordDto;
import com.orion.friendsroom.dto.user.UserDto;
import com.orion.friendsroom.dto.user.UserUpdateDto;
import com.orion.friendsroom.mapper.UserMapper;
import com.orion.friendsroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping(value = "/user-for-search")
    public UserDto getUserByEmail(@RequestBody EmailUserDto emailUserDto) {
        return userMapper.toDto(userService.getUserByEmailForSearch(emailUserDto));
    }

    @PutMapping(value = "/update-user")
    public UserDto updateUser(@RequestBody UserUpdateDto userUpdateDto) {
        return userMapper.toDto(userService.updateUser(userUpdateDto));
    }

    @PutMapping(value = "/update-email")
    public UserDto updateEmailOfUser(@RequestBody EmailUserDto emailUserDto) {
        return userMapper.toDto(userService.updateUserEmail(emailUserDto));
    }

    @PutMapping(value = "/update-password")
    public UserDto changePassword(@RequestBody PasswordDto passwordDto) {
        return userMapper.toDto(userService.changePassword(passwordDto));
    }
}
