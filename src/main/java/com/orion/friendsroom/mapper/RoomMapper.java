package com.orion.friendsroom.mapper;

import com.orion.friendsroom.dto.admin.RoomForAdminDto;
import com.orion.friendsroom.dto.room.RoomCreationDto;
import com.orion.friendsroom.dto.room.RoomDto;
import com.orion.friendsroom.entity.RoomEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    RoomEntity toEntity(RoomCreationDto roomCreationDto);

    RoomDto toDto(RoomEntity roomEntity);

    RoomCreationDto toCreationDto(RoomEntity roomEntity);

    RoomForAdminDto toAdminDto(RoomEntity roomEntity);

    @Mapping(target = "id", ignore = true)
    void updateRoomEntityFromRoomDto(RoomCreationDto source,
                                     @MappingTarget RoomEntity target);
}
