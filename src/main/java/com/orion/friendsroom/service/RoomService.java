package com.orion.friendsroom.service;

import com.orion.friendsroom.dto.room.RoomDto;
import com.orion.friendsroom.entity.RoomEntity;

public interface RoomService {
    RoomEntity createRoom(Long userId, RoomDto roomDto);

    void activateRoom(String code);
}
