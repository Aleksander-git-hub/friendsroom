package com.orion.friendsroom.service.impl;

import com.orion.friendsroom.dto.room.RoomDto;
import com.orion.friendsroom.email.MailSender;
import com.orion.friendsroom.entity.RoomEntity;
import com.orion.friendsroom.entity.Status;
import com.orion.friendsroom.entity.UserEntity;
import com.orion.friendsroom.exceptions.NotFoundException;
import com.orion.friendsroom.mapper.RoomMapper;
import com.orion.friendsroom.repository.RoomRepository;
import com.orion.friendsroom.repository.UserRepository;
import com.orion.friendsroom.service.MessageGenerate;
import com.orion.friendsroom.service.RoomService;
import com.orion.friendsroom.service.validation.RoomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private MailSender mailSender;

    @Transactional
    @Override
    public RoomEntity createRoom(Long userId, RoomDto roomDto) {
        RoomValidator.validateRoom(roomDto);

        UserEntity existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        if (!existingUser.equals(userRepository.findByEmail(roomDto.getOwner().getEmail()))) {
            throw new NotFoundException("You create a room, you must own it!");
        }

        if (roomRepository.findByName(roomDto.getName()) != null) {
            throw new NotFoundException("The Room with name already exist!");
        }

        RoomEntity creationRoom = roomMapper.toEntity(roomDto);
        creationRoom.setCreated(new Date());
        creationRoom.setUpdated(creationRoom.getCreated());
        creationRoom.setStatus(Status.NOT_CONFIRMED);
        creationRoom.setOwner(existingUser);

        existingUser.getUserRooms().add(creationRoom);

        String message = MessageGenerate.getMessageForRoom(creationRoom);
        mailSender.send(existingUser.getEmail(), "Creating Room", message);

        return roomRepository.save(creationRoom);
    }

    @Override
    public void activateRoom(String code) {
        RoomEntity roomEntity = roomRepository.findByActivationCode(code);
        roomEntity.setActivationCode(null);
        roomEntity.setStatus(Status.ACTIVE);
        roomRepository.save(roomEntity);
    }
}
