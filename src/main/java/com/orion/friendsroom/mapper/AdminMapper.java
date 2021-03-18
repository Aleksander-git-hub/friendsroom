package com.orion.friendsroom.mapper;

import com.orion.friendsroom.dto.admin.AdminSuccessRegisterDto;
import com.orion.friendsroom.dto.user.UserRegisterDto;
import com.orion.friendsroom.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    AdminSuccessRegisterDto toSuccessRegister(UserEntity userEntity);
}
