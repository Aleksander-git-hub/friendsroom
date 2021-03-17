package com.orion.friendsroom.mapper;

import com.orion.friendsroom.dto.UserRegisterDto;
import com.orion.friendsroom.dto.UserSuccessRegisterDto;
import com.orion.friendsroom.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntity toEntity(UserRegisterDto userRegisterDto);

    UserRegisterDto toRegister(UserEntity userEntity);

    UserSuccessRegisterDto toSuccessRegister(UserEntity userEntity);

}
