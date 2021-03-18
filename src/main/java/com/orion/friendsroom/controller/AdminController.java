package com.orion.friendsroom.controller;

import com.orion.friendsroom.dto.admin.AdminSuccessRegisterDto;
import com.orion.friendsroom.dto.user.RegisterDto;
import com.orion.friendsroom.mapper.AdminMapper;
import com.orion.friendsroom.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminMapper adminMapper;

    @PostMapping(value = "/register")
    public AdminSuccessRegisterDto registerAdmin(@RequestBody RegisterDto adminRegisterDto) {
        return adminMapper.toSuccessRegister(adminService.registerAdmin(adminRegisterDto));
    }

    @GetMapping(value = "/activate/{code}")
    public String activateEmail(@PathVariable String code) {
        adminService.activateAdmin(code);

        return "confirm";
    }

}
