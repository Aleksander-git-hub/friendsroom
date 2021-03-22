package com.orion.friendsroom.mapper;

import com.orion.friendsroom.dto.admin.RoomForAdminDto;
import com.orion.friendsroom.dto.room.RoomDto;
import com.orion.friendsroom.entity.RoomEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    RoomEntity toEntity(RoomDto roomDto);

    RoomDto toDto(RoomEntity roomEntity);

    RoomForAdminDto toAdminDto(RoomEntity roomEntity);
}
