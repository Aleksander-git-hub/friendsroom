package com.orion.friendsroom.service.validation;

import com.orion.friendsroom.dto.room.RoomDto;
import com.orion.friendsroom.entity.RoomEntity;
import com.orion.friendsroom.entity.Status;
import com.orion.friendsroom.exceptions.NotFoundException;
import org.apache.commons.lang3.StringUtils;

public class RoomValidator {
    public static void validateRoom(RoomDto creationRoom) {
        if (StringUtils.isEmpty(creationRoom.getName()) ||
            StringUtils.isEmpty(creationRoom.getOwner().getEmail()) ||
            StringUtils.isEmpty(creationRoom.getOwner().getFirstName()) ||
            StringUtils.isEmpty(creationRoom.getOwner().getSecondName())) {
            throw new NotFoundException("Some fields are empty! Please, check this!");
        }
    }

    public static void validateStatus(RoomEntity existingRoom) {
        if (!existingRoom.getStatus().equals(Status.ACTIVE)) {
            throw new NotFoundException("This Room is not active!");
        }
    }
}
