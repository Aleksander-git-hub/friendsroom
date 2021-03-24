package com.orion.friendsroom.service;

import com.orion.friendsroom.entity.RoleEntity;
import com.orion.friendsroom.entity.RoomEntity;
import com.orion.friendsroom.entity.Status;
import com.orion.friendsroom.entity.UserEntity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MessageGenerate {

    public static void setFields(UserEntity entity, List<RoleEntity> roles, RoleEntity role) {
        roles.add(role);
        entity.setCreated(new Date());
        entity.setUpdated(entity.getCreated());
        entity.setRoles(roles);
        entity.setActivationCode(generateCode());
        entity.setStatus(Status.NOT_CONFIRMED);
    }

    public static String getMessageForUser(UserEntity user) {
        return String.format(
                "Hello, %s!\n" +
                        "Welcome to FriendsRoom! Your ID: %s. Please, visit next link:\n " +
                        "http://localhost:8070/friends-room/api/v1/activate/%s",
                user.getFirstName(),
                user.getId(),
                user.getActivationCode()
        );
    }

    public static String getMessageForAdmin(UserEntity newAdmin) {
        return String.format(
                "Hello, %s!\n" +
                        "Welcome to our FriendsRoom team! You are an admin! Please, visit next link: " +
                        "http://localhost:8070/friends-room/api/v1/activate/%s",
                newAdmin.getFirstName(),
                newAdmin.getActivationCode()
        );
    }

    public static String getMessageForUpdateUser(UserEntity updatingUser) {
        updatingUser.setActivationCode(generateCode());
        updatingUser.setStatus(Status.NOT_CONFIRMED);
        return String.format(
                "Hello, %s!\n" +
                        "Please, visit next link to confirm your update: " +
                        "http://localhost:8070/friends-room/api/v1/activate/%s",
                updatingUser.getFirstName(),
                updatingUser.getActivationCode()
        );
    }

    public static String getMessageForRoom(RoomEntity room) {
        return String.format(
                "Hello, %s!\n" +
                        "Your Room was created with id: %s.\n" +
                        "Please, visit next link to confirm create room: %s\n " +
                        "http://localhost:8070/friends-room/api/v1/activate/room/%s",
                room.getOwner().getFirstName(),
                room.getId(),
                room.getName(),
                room.getActivationCode()
        );
    }

    public static String getMessageForUpdateRoom(RoomEntity updatingRoom) {
        updatingRoom.setActivationCode(generateCode());
        updatingRoom.setStatus(Status.NOT_CONFIRMED);
        return String.format(
                "Hello, %s!\n" +
                        "Please, visit next link for confirm updating Room: %s:\n " +
                        "http://localhost:8070/friends-room/api/v1/activate/room/%s",
                updatingRoom.getOwner().getFirstName(),
                updatingRoom.getName(),
                updatingRoom.getActivationCode()
        );
    }

    private static String generateCode() {
        return UUID.randomUUID().toString();
    }

    public static String getMessageForGuests(UserEntity user, RoomEntity room) {
        return String.format(
                "Hello, %s!\n" +
                        "The Room: %s, you are in has changed status!\n" +
                        "Room's status: %s",
                user.getFirstName(),
                room.getName(),
                room.getStatus()
        );
    }

    public static String getMessageAddGuest(UserEntity user, RoomEntity room) {
        return String.format(
                "Hello, %s!\n" +
                        "User %s create a new Room: %s and invited you!\n" + "Welcome!",
                user.getFirstName(),
                room.getOwner().getEmail(),
                room.getName()
        );
    }

    public static String getMessageDeleteGuest(UserEntity user, RoomEntity room) {
        return String.format(
                "Hello, %s!\n" +
                        "You are expelled from the Room: %s",
                user.getFirstName(),
                room.getName()
        );
    }
}
