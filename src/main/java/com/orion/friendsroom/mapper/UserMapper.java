package com.orion.friendsroom.mapper;

import com.orion.friendsroom.dto.RegisterDto;
import com.orion.friendsroom.dto.admin.UserForAdminDto;
import com.orion.friendsroom.dto.user.UserDto;
import com.orion.friendsroom.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity toEntity(RegisterDto registerDto);

    UserDto toDto(UserEntity userEntity);

    UserForAdminDto toDtoForAdmin(UserEntity userEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateUserEntityFromUserUpdateDto(UserDto source,
                                           @MappingTarget UserEntity target);
}
