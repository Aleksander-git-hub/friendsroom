package com.orion.friendsroom.controller;

import com.orion.friendsroom.dto.admin.EmailUserDto;
import com.orion.friendsroom.dto.user.PasswordDto;
import com.orion.friendsroom.dto.user.UserDto;
import com.orion.friendsroom.dto.user.EmailDto;
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

    @PutMapping(value = "/update-user/{id}")
    public UserDto updateUserById(@RequestBody UserDto userDto,
                                  @PathVariable Long id) {
        return userMapper.toDto(userService.updateUserById(userDto, id));
    }

    @PutMapping(value = "/update-email/{id}")
    public UserDto updateEmailOfUser(@RequestBody EmailDto emailDto,
                                     @PathVariable Long id) {
        return userMapper.toDto(userService.updateEmailOfUser(emailDto, id));
    }

    @PutMapping(value = "/update-password/{id}")
    public UserDto changePassword(@RequestBody PasswordDto passwordDto,
                                  @PathVariable Long id) {
        return userMapper.toDto(userService.changePassword(passwordDto, id));
    }
}
