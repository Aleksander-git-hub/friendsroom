package com.orion.friendsroom.service;

import com.orion.friendsroom.dto.AuthenticationRequestDto;
import com.orion.friendsroom.dto.AuthenticationResponseDto;
import com.orion.friendsroom.dto.UserRegisterDto;
import com.orion.friendsroom.entity.UserEntity;

public interface UserService {

    UserEntity registerUser(UserRegisterDto userRegistrationDto);

    UserEntity findByEmailAndPassword(String email, String password);

    UserEntity getUserByEmail (String email);

    void activateUser(String code);

    AuthenticationResponseDto validateUserLogin(AuthenticationRequestDto requestDto);
}
