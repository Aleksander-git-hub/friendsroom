package com.orion.friendsroom.mapper;

import com.orion.friendsroom.dto.RegisterDto;
import com.orion.friendsroom.dto.admin.EmailUserDto;
import com.orion.friendsroom.dto.admin.UserForAdminDto;
import com.orion.friendsroom.dto.user.UserDto;
import com.orion.friendsroom.dto.user.UserUpdateDto;
import com.orion.friendsroom.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity toEntity(RegisterDto registerDto);

    UserDto toDto(UserEntity userEntity);

    EmailUserDto toEmailUserDto(UserEntity userEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateUserEntityFromUserUpdateDto(UserUpdateDto source,
                                           @MappingTarget UserEntity target);
}
