package com.orion.friendsroom.controller;

import com.orion.friendsroom.dto.RegisterDto;
import com.orion.friendsroom.dto.admin.*;
import com.orion.friendsroom.dto.room.RoomNameDto;
import com.orion.friendsroom.entity.RoomEntity;
import com.orion.friendsroom.entity.UserEntity;
import com.orion.friendsroom.mapper.AdminMapper;
import com.orion.friendsroom.mapper.RoomMapper;
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

    @Autowired
    private RoomMapper roomMapper;

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

    @GetMapping(value = "/rooms-by-owner")
    public List<RoomForAdminDto> getRoomsByOwner(@RequestBody EmailUserDto emailUserDto) {
        List<RoomEntity> rooms = adminService.getRoomsByOwner(emailUserDto);
        return rooms.stream()
                .map(roomMapper::toAdminDto)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/rooms")
    public List<RoomForAdminDto> getAllRooms() {
        List<RoomEntity> rooms = adminService.getAllRooms();
        return rooms.stream()
                .map(roomMapper::toAdminDto)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/room/{id}")
    public RoomForAdminDto getRoomById(@PathVariable Long id) {
        return roomMapper.toAdminDto(adminService.getRoomById(id));
    }

    @GetMapping(value = "/all-banned-rooms")
    public List<RoomForAdminDto> getAllBannedRoom() {
        List<RoomEntity> bannedRooms = adminService.getAllBannedRooms();
        return bannedRooms.stream()
                .map(roomMapper::toAdminDto)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/all-active-rooms")
    public List<RoomForAdminDto> getAllActiveRoom() {
        List<RoomEntity> activeRooms = adminService.getAllActiveRooms();
        return activeRooms.stream()
                .map(roomMapper::toAdminDto)
                .collect(Collectors.toList());
    }

    @PutMapping(value = "/room-change-status/{id}")
    public RoomForAdminDto changeStatusForRoomById(@RequestBody StatusDto status,
                                                   @PathVariable Long id) {
        return roomMapper.toAdminDto(adminService.changeStatusForRoomById(status, id));
    }

    @DeleteMapping(value = "/room/{id}")
    public ResponseEntity<?> deleteRoomById(@PathVariable Long id) {
        adminService.deleteRoomById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/room/name")
    public ResponseEntity<?> deleteRoomByName(@RequestBody RoomNameDto roomNameDto) {
        adminService.deleteRoomByName(roomNameDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/rooms/owner")
    public ResponseEntity<?> deleteRoomsByOwner(@RequestBody EmailUserDto emailUserDto) {
        adminService.deleteRoomsByOwner(emailUserDto);
        return ResponseEntity.ok().build();
    }
}
