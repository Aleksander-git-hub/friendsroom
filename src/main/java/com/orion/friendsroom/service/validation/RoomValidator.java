package com.orion.friendsroom.service.validation;

import com.orion.friendsroom.dto.room.RoomCreationDto;
import com.orion.friendsroom.entity.RoomEntity;
import com.orion.friendsroom.entity.enums.Status;
import com.orion.friendsroom.entity.UserEntity;
import com.orion.friendsroom.exceptions.ForbiddenError;
import com.orion.friendsroom.exceptions.NotFoundException;
import org.apache.commons.lang3.StringUtils;

public class RoomValidator {
    public static void validateRoom(RoomCreationDto creationRoom) {
        if (StringUtils.isEmpty(creationRoom.getName())) {
            throw new NotFoundException("Some fields are empty! Please, check this!");
        }
    }

    public static void validateStatus(RoomEntity existingRoom) {
        if (!existingRoom.getStatus().equals(Status.ACTIVE)) {
            throw new NotFoundException("This Room is not active!");
        }
    }

    public static void validateListOfGuests(RoomEntity roomForConfirmation) {
        if (roomForConfirmation.getUsers().size() <= 1) {
            throw new NotFoundException("Not added any users");
        }
    }

    public static void validateOwner(UserEntity owner, RoomEntity room) {
        if (!room.getOwner().equals(owner)) {
            throw new ForbiddenError("Access denied!");
        }
    }

    public static void validateRoomBeforeDeleting(RoomEntity room) {
        room.getDebts().forEach(debt -> {
            if (debt.getStatus().equals(Status.ACTIVE)) {
                throw new NotFoundException("There is an ACTIVE debt! Deletion is not possible!");
            }
        });
    }

    public static void validateTotalAmount(Double amount) {
        if (amount == null) {
            throw new NotFoundException("Enter amount!");
        }

        if (amount <= 0) {
            throw new NotFoundException("The Amount is incorrect!");
        }
    }

    public static void validateGuest(UserEntity guest, RoomEntity room) {
        if (!room.getUsers().contains(guest)) {
            throw new ForbiddenError("Access Denied!");
        }
    }
}
