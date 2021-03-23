package com.orion.friendsroom.service;

import com.orion.friendsroom.dto.admin.EmailUserDto;
import com.orion.friendsroom.dto.room.RoomDto;
import com.orion.friendsroom.dto.room.RoomNameDto;
import com.orion.friendsroom.entity.RoomEntity;
import com.orion.friendsroom.entity.UserEntity;

import java.util.List;

public interface RoomService {
    RoomEntity createRoom(Long userId, RoomDto roomDto);

    void activateRoom(String code);

    List<RoomEntity> getAllRooms();

    RoomEntity getRoomById(Long roomId);

    RoomEntity getRoomByName(RoomNameDto roomName);

    List<UserEntity> getGuestsOfRoom(Long roomId);

    void addGuestToRoom(EmailUserDto emailUserDto, Long roomId);

    void deleteGuestFromRoom(EmailUserDto emailUserDto, Long roomId);

    void deleteRoomById(EmailUserDto emailUserDto, Long roomId);

    RoomEntity updateRoomById(RoomDto roomDto, Long roomId);
}
