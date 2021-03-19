package com.orion.friendsroom.service;

import com.orion.friendsroom.dto.AuthenticationRequestDto;
import com.orion.friendsroom.dto.AuthenticationResponseDto;
import com.orion.friendsroom.dto.user.RegisterDto;
import com.orion.friendsroom.entity.UserEntity;

public interface AdminService {

    UserEntity registerAdmin(RegisterDto adminRegisterDto);

    UserEntity getUserByEmail(String email);

    AuthenticationResponseDto validateAdminLogin(AuthenticationRequestDto requestDto);

    //void activateAdmin(String code);
}
