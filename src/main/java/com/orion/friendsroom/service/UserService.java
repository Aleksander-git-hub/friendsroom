package com.orion.friendsroom.service;

import com.orion.friendsroom.dto.AuthenticationRequestDto;
import com.orion.friendsroom.dto.AuthenticationResponseDto;
import com.orion.friendsroom.dto.RegisterDto;
import com.orion.friendsroom.dto.admin.EmailUserDto;
import com.orion.friendsroom.dto.user.PasswordDto;
import com.orion.friendsroom.dto.user.UserDto;
import com.orion.friendsroom.dto.user.EmailDto;
import com.orion.friendsroom.entity.UserEntity;

public interface UserService {

    UserEntity registerUser(RegisterDto userRegistrationDto);

    UserEntity findByEmailAndPassword(String email, String password);

    UserEntity getUserByEmail (String email);

    void activateUser(String code);

    AuthenticationResponseDto validateUserLogin(AuthenticationRequestDto requestDto);

    UserEntity getUserByEmailForSearch(EmailUserDto emailDto);

    UserEntity updateUserById(UserDto userDto, Long id);

    UserEntity updateEmailOfUser(EmailDto emailDto, Long id);

    UserEntity changePassword(PasswordDto passwordDto, Long id);
}
