package com.orion.friendsroom.service;

import com.orion.friendsroom.entity.RoleEntity;
import com.orion.friendsroom.entity.RoomEntity;
import com.orion.friendsroom.entity.Status;
import com.orion.friendsroom.entity.UserEntity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MessageGenerate {

    public static void addFields(UserEntity entity, List<RoleEntity> roles, RoleEntity role) {
        roles.add(role);
        entity.setCreated(new Date());
        entity.setUpdated(entity.getCreated());
        entity.setRoles(roles);
    }

    public static String getMessageForUser(UserEntity user) {
        user.setActivationCode(generateCode());
        user.setStatus(Status.NOT_CONFIRMED);
        return String.format(
                "Hello, %s!\n" +
                        "Welcome to FriendsRoom! Please, visit next link: " +
                        "http://localhost:8070/friends-room/api/v1/activate/%s",
                user.getFirstName(),
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
        room.setActivationCode(generateCode());
        return String.format(
                "Hello, %s!\n" +
                        "Please, visit next link to confirm create room: %s\n " +
                        "http://localhost:8070/friends-room/api/v1/activate/room/%s",
                room.getOwner().getFirstName(),
                room.getName(),
                room.getActivationCode()
        );
    }

    private static String generateCode() {
        return UUID.randomUUID().toString();
    }
}