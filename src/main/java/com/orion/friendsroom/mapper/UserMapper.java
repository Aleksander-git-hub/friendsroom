package com.orion.friendsroom.mapper;

import com.orion.friendsroom.dto.UserRegistrationDto;
import com.orion.friendsroom.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity toEntity(UserRegistrationDto userRegistrationDto);
}
