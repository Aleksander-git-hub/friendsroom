package com.orion.friendsroom.service;

import com.orion.friendsroom.dto.AuthenticationRequestDto;
import com.orion.friendsroom.dto.AuthenticationResponseDto;
import com.orion.friendsroom.dto.RegisterDto;
import com.orion.friendsroom.dto.admin.EmailUserForAdminDto;
import com.orion.friendsroom.dto.admin.StatusUserForAdminDto;
import com.orion.friendsroom.entity.Status;
import com.orion.friendsroom.entity.UserEntity;

import java.util.List;

public interface AdminService {

    UserEntity registerAdmin(RegisterDto adminRegisterDto);

    UserEntity getUserByEmail(EmailUserForAdminDto email);

    AuthenticationResponseDto validateAdminLogin(AuthenticationRequestDto requestDto);

    UserEntity getUserById(Long userId);

    List<UserEntity> getAllUsers();

    List<UserEntity> getAllBannedUsers();

    List<UserEntity> getAllActiveUsers();

    UserEntity changeStatusForUserById(StatusUserForAdminDto newStatus, Long userId);

    void deleteUserById(Long userId);

    void deleteUserByEmail(EmailUserForAdminDto email);

}
