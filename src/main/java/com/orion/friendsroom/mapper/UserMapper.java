package com.orion.friendsroom.mapper;

import com.orion.friendsroom.dto.RegisterDto;
import com.orion.friendsroom.dto.admin.UserForAdminDto;
import com.orion.friendsroom.dto.user.UserSuccessRegisterDto;
import com.orion.friendsroom.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity toEntity(RegisterDto registerDto);

    UserSuccessRegisterDto toSuccessRegister(UserEntity userEntity);

    UserForAdminDto toDtoForAdmin(UserEntity userEntity);

}
