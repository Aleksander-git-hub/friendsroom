package com.orion.friendsroom.service;

import com.orion.friendsroom.dto.AuthenticationRequestDto;
import com.orion.friendsroom.dto.UserRegisterDto;
import com.orion.friendsroom.entity.UserEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    UserEntity registerUser(UserRegisterDto userRegistrationDto);

    UserEntity getUserById(Long id);

    List<UserEntity> getAllUsers();

    UserEntity getUserByEmail (String email);

    UserEntity updateUserById(UserEntity userEntity, Long id);

    void deleteUserById(Long id);

    void activateUser(String code);

    ResponseEntity<?> validateUserLogin(AuthenticationRequestDto requestDto);
}
