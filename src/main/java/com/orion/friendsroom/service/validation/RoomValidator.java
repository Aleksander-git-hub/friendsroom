package com.orion.friendsroom.service.validation;

import com.orion.friendsroom.dto.room.RoomCreationDto;
import com.orion.friendsroom.dto.room.RoomDto;
import com.orion.friendsroom.entity.RoomEntity;
import com.orion.friendsroom.entity.Status;
import com.orion.friendsroom.exceptions.NotFoundException;
import org.apache.commons.lang3.StringUtils;

public class RoomValidator {
    public static void validateRoom(RoomCreationDto creationRoom) {
        if (StringUtils.isEmpty(creationRoom.getName()) ||
                creationRoom.getTotalAmount() == null) {
            throw new NotFoundException("Some fields are empty! Please, check this!");
        }

        if (creationRoom.getTotalAmount() <= 0) {
            throw new NotFoundException("The Amount is incorrect!");
        }

    }

    public static void validateStatus(RoomEntity existingRoom) {
        if (!existingRoom.getStatus().equals(Status.ACTIVE)) {
            throw new NotFoundException("This Room is not active!");
        }
    }

    public static void validateConfirmation(RoomEntity roomForConfirmation) {
        if (roomForConfirmation.getUsers().size() <= 1) {
            throw new NotFoundException("Not added any users");
        }
    }
}
