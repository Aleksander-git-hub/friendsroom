package com.orion.friendsroom.controller;

import com.orion.friendsroom.dto.room.AmountDto;
import com.orion.friendsroom.dto.room.RoomCreationDto;
import com.orion.friendsroom.dto.room.RoomDto;
import com.orion.friendsroom.dto.room.RoomNameDto;
import com.orion.friendsroom.dto.user.EmailUserDto;
import com.orion.friendsroom.dto.user.RepayDebtDto;
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

    @PostMapping(value = "/create-room")
    public RoomDto createRoom(@RequestBody RoomCreationDto roomCreationDto) {
        return roomMapper.toDto(roomService.createRoom(roomCreationDto));
    }

    @GetMapping(value = "/rooms")
    public List<RoomCreationDto> getAllRooms() {
        List<RoomEntity> rooms = roomService.getAllRooms();
        return rooms.stream()
                .map(roomMapper::toCreationDto)
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
    public RoomDto addGuestToRoom(@RequestBody EmailUserDto emailUserDto,
                                            @PathVariable Long roomId) {
        return roomMapper.toDto(roomService.addGuestToRoom(emailUserDto, roomId));
    }

    @GetMapping(value = "/guests/{roomId}")
    public List<UserDto> getGuestsOfRoom(@PathVariable Long roomId) {
        List<UserEntity> guests = roomService.getGuestsOfRoom(roomId);
        return guests.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping(value = "/drop-user-room/{roomId}")
    public RoomDto deleteGuestFromRoom(@RequestBody EmailUserDto emailUserDto,
                                                 @PathVariable Long roomId) {
        return roomMapper.toDto(roomService.deleteGuestFromRoom(emailUserDto, roomId));
    }

    @PostMapping(value = "/add-amount/{roomId}")
    public RoomDto addAmountToRoom(@RequestBody AmountDto amountDto,
                                     @PathVariable Long roomId) {
        return roomMapper.toDto(roomService.addAmountToRoom(amountDto, roomId));
    }

    @DeleteMapping(value = "/repay-debt/{roomId}")
    public RoomDto repayDebtToUser(@RequestBody RepayDebtDto repayDebtDto,
                                   @PathVariable Long roomId) {
        return roomMapper.toDto(roomService.repayDebtToUser(repayDebtDto, roomId));
    }

    @DeleteMapping(value = "/delete-room/{roomId}")
    public ResponseEntity<?> deleteRoomById(@PathVariable Long roomId) {
        roomService.deleteRoomById(roomId);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/update-room/{roomId}")
    public RoomDto updateRoomById(@RequestBody RoomCreationDto roomCreationDto,
                                          @PathVariable Long roomId) {
        return roomMapper.toDto(roomService.updateRoomById(roomCreationDto, roomId));
    }
}
