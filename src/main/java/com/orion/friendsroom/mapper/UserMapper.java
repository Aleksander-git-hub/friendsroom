package com.orion.friendsroom.mapper;

import com.orion.friendsroom.dto.user.RegisterDto;
import com.orion.friendsroom.dto.user.UserSuccessRegisterDto;
import com.orion.friendsroom.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity toEntity(RegisterDto registerDto);

    UserSuccessRegisterDto toSuccessRegister(UserEntity userEntity);

}
