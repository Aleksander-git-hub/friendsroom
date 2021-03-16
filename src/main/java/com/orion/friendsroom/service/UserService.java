package com.orion.friendsroom.service;

import com.orion.friendsroom.dto.UserRegistrationDto;
import com.orion.friendsroom.entity.UserEntity;

import java.util.List;

public interface UserService {

    UserEntity registerUser(UserRegistrationDto userRegistrationDto);

    UserEntity getUserById(Long id);

    List<UserEntity> getAllUsers();

    UserEntity getUserByEmail (String email);

    UserEntity updateUserById(UserEntity userEntity, Long id);

    void deleteUserById(Long id);

    void activateUser(String code);
}
