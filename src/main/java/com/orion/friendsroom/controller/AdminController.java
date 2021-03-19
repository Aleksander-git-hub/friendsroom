package com.orion.friendsroom.controller;

import com.orion.friendsroom.dto.RegisterDto;
import com.orion.friendsroom.dto.admin.AdminDto;
import com.orion.friendsroom.dto.admin.EmailUserDto;
import com.orion.friendsroom.dto.admin.StatusDto;
import com.orion.friendsroom.dto.admin.UserForAdminDto;
import com.orion.friendsroom.entity.UserEntity;
import com.orion.friendsroom.mapper.AdminMapper;
import com.orion.friendsroom.mapper.UserMapper;
import com.orion.friendsroom.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private UserMapper userMapper;

    @PostMapping(value = "/register")
    public AdminDto registerAdmin(@RequestBody RegisterDto adminRegisterDto) {
        return adminMapper.toSuccessRegister(adminService.registerAdmin(adminRegisterDto));
    }

    @GetMapping(value = "/user/{id}")
    public UserForAdminDto getUserById(@PathVariable Long id) {
        return userMapper.toDtoForAdmin(adminService.getUserById(id));
    }

    @GetMapping(value = "/user/user-email")
    public UserForAdminDto getUserByEmail(
            @RequestBody EmailUserDto email) {
        return userMapper.toDtoForAdmin(adminService.getUserByEmail(email));
    }

    @GetMapping(value = "/all-users")
    public List<UserForAdminDto> getAllUsers() {
        List<UserEntity> users = adminService.getAllUsers();
        return users.stream()
                .map(userMapper::toDtoForAdmin)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/all-banned-users")
    public List<UserForAdminDto> getAllBannedUsers() {
        List<UserEntity> bannedUsers = adminService.getAllBannedUsers();
        return bannedUsers.stream()
                .map(userMapper::toDtoForAdmin)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/all-active-users")
    public List<UserForAdminDto> getAllActiveUsers() {
        List<UserEntity> activeUsers = adminService.getAllActiveUsers();
        return activeUsers.stream()
                .map(userMapper::toDtoForAdmin)
                .collect(Collectors.toList());
    }

    @PutMapping(value = "/change-status/{id}")
    public UserForAdminDto changeStatusForUserById(@RequestBody StatusDto status,
                                                   @PathVariable Long id) {
        return userMapper.toDtoForAdmin(adminService.changeStatusForUserById(status, id));
    }

    @DeleteMapping(value = "/user/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        adminService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/user/email")
    public ResponseEntity<?> deleteUserByEmail(
            @RequestBody EmailUserDto email) {
        adminService.deleteUserByEmail(email);
        return ResponseEntity.ok().build();
    }
}
