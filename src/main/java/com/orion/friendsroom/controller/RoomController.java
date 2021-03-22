package com.orion.friendsroom.controller;

import com.orion.friendsroom.dto.room.RoomDto;
import com.orion.friendsroom.mapper.RoomMapper;
import com.orion.friendsroom.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomMapper roomMapper;

    @PostMapping(value = "/{userId}/room")
    public RoomDto createRoom(@PathVariable Long userId,
                              @RequestBody RoomDto roomDto) {
        return roomMapper.toDto(roomService.createRoom(userId, roomDto));
    }


}
