package com.orion.friendsroom.service.validation;

import com.orion.friendsroom.dto.room.RoomCreationDto;
import com.orion.friendsroom.entity.RoomEntity;
import com.orion.friendsroom.entity.Status;
import com.orion.friendsroom.exceptions.NotFoundException;
import org.apache.commons.lang3.StringUtils;

public class RoomValidator {
    public static void validateRoom(RoomCreationDto creationRoom) {
        if (StringUtils.isEmpty(creationRoom.getName()) ||
            StringUtils.isEmpty(creationRoom.getOwner().getEmail())) {
            throw new NotFoundException("Some fields are empty! Please, check this!");
        }
    }

    public static void validateStatus(RoomEntity existingRoom) {
        if (!existingRoom.getStatus().equals(Status.ACTIVE)) {
            throw new NotFoundException("This Room is not active!");
        }
    }
}
