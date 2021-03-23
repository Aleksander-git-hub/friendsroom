package com.orion.friendsroom.controller;

import com.orion.friendsroom.dto.admin.EmailUserDto;
import com.orion.friendsroom.dto.room.RoomDto;
import com.orion.friendsroom.dto.room.RoomNameDto;
import com.orion.friendsroom.dto.user.UserDto;
import com.orion.friendsroom.entity.RoomEntity;
import com.orion.friendsroom.entity.UserEntity;
import com.orion.friendsroom.mapper.RoomMapper;
import com.orion.friendsroom.mapper.UserMapper;
import com.orion.friendsroom.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private UserMapper userMapper;

    @PostMapping(value = "/{userId}/room")
    public RoomDto createRoom(@PathVariable Long userId,
                              @RequestBody RoomDto roomDto) {
        return roomMapper.toDto(roomService.createRoom(userId, roomDto));
    }

    @GetMapping(value = "/rooms")
    public List<RoomDto> getAllRooms() {
        List<RoomEntity> rooms = roomService.getAllRooms();
        return rooms.stream()
                .map(roomMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/room/{roomId}")
    public RoomDto getRoomById(@PathVariable Long roomId) {
        return roomMapper.toDto(roomService.getRoomById(roomId));
    }

    @GetMapping(value = "/room/name")
    public RoomDto getRoomByName(@RequestBody RoomNameDto roomName) {
        return roomMapper.toDto(roomService.getRoomByName(roomName));
    }

    @PostMapping(value = "/add-user-room/{roomId}")
    public ResponseEntity<?> addGuestToRoom(@RequestBody EmailUserDto emailUserDto,
                                            @PathVariable Long roomId) {
        roomService.addGuestToRoom(emailUserDto, roomId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/guests/{roomId}")
    public List<UserDto> getGuestsOfRoom(@PathVariable Long roomId) {
        List<UserEntity> guests = roomService.getGuestsOfRoom(roomId);
        return guests.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping(value = "/drop-user-room/{roomId}")
    public ResponseEntity<?> deleteGuestFromRoom(@RequestBody EmailUserDto emailUserDto,
                                                 @PathVariable Long roomId) {
        roomService.deleteGuestFromRoom(emailUserDto, roomId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/owner-room/{roomId}")
    public ResponseEntity<?> deleteRoomById(@RequestBody EmailUserDto emailUserDto,
                                            @PathVariable Long roomId) {
        roomService.deleteRoomById(emailUserDto, roomId);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/update-room/{roomId}")
    public RoomDto updateRoomById(@RequestBody RoomDto roomDto,
                                  @PathVariable Long roomId) {
        return roomMapper.toDto(roomService.updateRoomById(roomDto, roomId));
    }
}
