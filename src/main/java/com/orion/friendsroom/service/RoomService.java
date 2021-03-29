package com.orion.friendsroom.service;

import com.orion.friendsroom.dto.room.AmountDto;
import com.orion.friendsroom.dto.room.RoomCreationDto;
import com.orion.friendsroom.dto.room.RoomNameDto;
import com.orion.friendsroom.dto.user.EmailUserDto;
import com.orion.friendsroom.entity.RoomEntity;
import com.orion.friendsroom.entity.UserEntity;

import java.util.List;

public interface RoomService {
    RoomEntity createRoom(RoomCreationDto roomCreationDto);

    void activateRoom(String code);

    List<RoomEntity> getAllRooms();

    RoomEntity getRoomById(Long roomId);

    RoomEntity getRoomByName(RoomNameDto roomName);

    List<UserEntity> getGuestsOfRoom(Long roomId);

    RoomEntity addGuestToRoom(EmailUserDto emailUserDto, Long roomId);

    RoomEntity deleteGuestFromRoom(EmailUserDto emailUserDto, Long roomId);

    RoomEntity addAmountToRoom(AmountDto amountDto, Long roomId);

    void deleteRoomById(Long roomId);

    RoomEntity updateRoomById(RoomCreationDto roomCreationDto, Long roomId);
}
