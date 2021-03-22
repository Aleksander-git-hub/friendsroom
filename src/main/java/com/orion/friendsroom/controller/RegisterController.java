package com.orion.friendsroom.controller;

import com.orion.friendsroom.dto.RegisterDto;
import com.orion.friendsroom.dto.user.UserDto;
import com.orion.friendsroom.mapper.UserMapper;
import com.orion.friendsroom.service.RoomService;
import com.orion.friendsroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1")
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping(value = "/register")
    public UserDto registerUser(@RequestBody RegisterDto registerDto) {
        return userMapper.toDto(userService.registerUser(registerDto));
    }

    @GetMapping(value = "/activate/{code}")
    public String activateEmail(@PathVariable String code) {
        userService.activateUser(code);

        return "confirm";
    }

    @GetMapping(value = "/activate/room/{code}")
    public String activateRoom(@PathVariable String code) {
        roomService.activateRoom(code);

        return "confirm";
    }
}
