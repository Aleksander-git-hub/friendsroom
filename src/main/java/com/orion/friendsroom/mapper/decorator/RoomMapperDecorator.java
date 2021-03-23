package com.orion.friendsroom.mapper.decorator;

import com.orion.friendsroom.dto.admin.RoomForAdminDto;
import com.orion.friendsroom.entity.RoomEntity;
import com.orion.friendsroom.mapper.RoomMapper;
import com.orion.friendsroom.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class RoomMapperDecorator implements RoomMapper {

    @Autowired
    private RoomMapper delegate;

    @Autowired
    private UserMapper userMapper;

    @Override
    public RoomForAdminDto toAdminDto(RoomEntity roomEntity) {
        RoomForAdminDto room = delegate.toAdminDto(roomEntity);
        room.setId(roomEntity.getId());
        room.setOwner(userMapper.toEmailUserDto(roomEntity.getOwner()));
        return room;
    }
}
