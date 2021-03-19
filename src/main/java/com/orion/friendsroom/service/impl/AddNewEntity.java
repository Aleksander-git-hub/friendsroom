package com.orion.friendsroom.service.impl;

import com.orion.friendsroom.entity.RoleEntity;
import com.orion.friendsroom.entity.Status;
import com.orion.friendsroom.entity.UserEntity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AddNewEntity {

    public static void addFields(UserEntity entity, List<RoleEntity> roles, RoleEntity role) {
        roles.add(role);
        entity.setCreated(new Date());
        entity.setUpdated(entity.getCreated());
        entity.setRoles(roles);
        entity.setStatus(Status.NOT_CONFIRMED);
        entity.setActivationCode(UUID.randomUUID().toString());
    }

    public static String getMessageForUser(UserEntity user) {
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
}
