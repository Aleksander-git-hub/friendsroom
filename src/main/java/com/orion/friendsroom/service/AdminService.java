package com.orion.friendsroom.service;

import com.orion.friendsroom.dto.AuthenticationRequestDto;
import com.orion.friendsroom.dto.AuthenticationResponseDto;
import com.orion.friendsroom.dto.RegisterDto;
import com.orion.friendsroom.dto.admin.AdminDto;
import com.orion.friendsroom.dto.admin.EmailUserDto;
import com.orion.friendsroom.dto.room.RoomNameDto;
import com.orion.friendsroom.dto.admin.StatusDto;
import com.orion.friendsroom.dto.user.PasswordDto;
import com.orion.friendsroom.entity.RoomEntity;
import com.orion.friendsroom.entity.UserEntity;

import java.util.List;

public interface AdminService {

    UserEntity registerAdmin(RegisterDto adminRegisterDto);

    UserEntity updateAdminEmail(EmailUserDto emailUserDto);

    UserEntity getUserByEmail(EmailUserDto email);

    UserEntity changePassword(PasswordDto passwordDto);

    UserEntity getUserById(Long userId);

    List<UserEntity> getAllUsers();

    List<UserEntity> getAllBannedUsers();

    List<UserEntity> getAllActiveUsers();

    UserEntity changeStatusForUserById(StatusDto newStatus, Long userId);

    void deleteUserById(Long userId);

    void deleteUserByEmail(EmailUserDto email);

    List<RoomEntity> getRoomsByOwner(EmailUserDto emailUserDto);

    List<RoomEntity> getAllRooms();

    RoomEntity getRoomById(Long id);

    List<RoomEntity> getAllBannedRooms();

    List<RoomEntity> getAllActiveRooms();

    RoomEntity changeStatusForRoomById(StatusDto status, Long roomId);

    void deleteRoomById(Long roomId);

    void deleteRoomByName(RoomNameDto roomNameDto);

    void deleteRoomsByOwner(EmailUserDto emailUserDto);
}
