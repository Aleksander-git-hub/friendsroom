package com.orion.friendsroom.mapper;

import com.orion.friendsroom.dto.admin.AdminDto;
import com.orion.friendsroom.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    AdminDto toSuccessRegister(UserEntity userEntity);
}
