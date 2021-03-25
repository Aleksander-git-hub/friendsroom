package com.orion.friendsroom.mapper;

import com.orion.friendsroom.dto.admin.AdminDto;
import com.orion.friendsroom.dto.admin.RoomForAdminDto;
import com.orion.friendsroom.dto.admin.UserForAdminDto;
import com.orion.friendsroom.entity.RoomEntity;
import com.orion.friendsroom.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    AdminDto toAdminDto(UserEntity userEntity);

    UserForAdminDto toDtoForAdmin(UserEntity userEntity);

    RoomForAdminDto roomToAdminDto(RoomEntity roomEntity);
}
