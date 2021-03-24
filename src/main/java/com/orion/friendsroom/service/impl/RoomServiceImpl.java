package com.orion.friendsroom.service.impl;

import com.orion.friendsroom.dto.admin.EmailUserDto;
import com.orion.friendsroom.dto.room.RoomCreationDto;
import com.orion.friendsroom.dto.room.RoomNameDto;
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
import com.orion.friendsroom.service.validation.EntityValidator;
import com.orion.friendsroom.service.validation.RoomValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public RoomEntity createRoom(Long userId, RoomCreationDto roomCreationDto) {
        RoomValidator.validateRoom(roomCreationDto);

        UserEntity existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        if (!existingUser.equals(userRepository.findByEmail(roomCreationDto.getOwner().getEmail()))) {
            throw new NotFoundException("You create a room, you must own it!");
        }

        if (roomRepository.findByName(roomCreationDto.getName()) != null) {
            throw new NotFoundException("The Room with name already exist!");
        }

        RoomEntity creationRoom = roomMapper.toEntity(roomCreationDto);
        creationRoom.setCreated(new Date());
        creationRoom.setUpdated(creationRoom.getCreated());
        creationRoom.setStatus(Status.NOT_CONFIRMED);
        creationRoom.setOwner(existingUser);
        creationRoom.setActivationCode(UUID.randomUUID().toString());
        creationRoom.setUsers(new ArrayList<>());
        creationRoom.getUsers().add(existingUser);
        roomRepository.save(creationRoom);

        existingUser.getUserRooms().add(creationRoom);
        existingUser.getRooms().add(creationRoom);
        existingUser.setUpdated(new Date());
        userRepository.save(existingUser);

        String message = MessageGenerate.getMessageForRoom(creationRoom);
        mailSender.send(existingUser.getEmail(), "Creating Room", message);

        return creationRoom;
    }

    @Transactional
    @Override
    public void activateRoom(String code) {
        RoomEntity roomEntity = roomRepository.findByActivationCode(code);
        roomEntity.setActivationCode(null);
        roomEntity.setStatus(Status.ACTIVE);
        roomRepository.save(roomEntity);
    }

    @Override
    public List<RoomEntity> getAllRooms() {
        return roomRepository.findAll().stream()
                .filter(roomEntity -> roomEntity.getStatus().equals(Status.ACTIVE))
                .collect(Collectors.toList());
    }

    @Override
    public RoomEntity getRoomById(Long roomId) {
        RoomEntity existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("Room not found with id: " + roomId));

        RoomValidator.validateStatus(existingRoom);

        return existingRoom;
    }

    @Override
    public RoomEntity getRoomByName(RoomNameDto roomName) {
        RoomEntity existingRoom = roomRepository.findByName(roomName.getName());

        if (existingRoom == null) {
            throw new NotFoundException("Room not found by name: " + roomName.getName());
        }

        RoomValidator.validateStatus(existingRoom);

        return existingRoom;
    }

    @Transactional
    @Override
    public RoomEntity addGuestToRoom(EmailUserDto emailUserDto, Long roomId) {
        if (StringUtils.isEmpty(emailUserDto.getEmail())) {
            throw new NotFoundException("Enter user's email!");
        }

        UserEntity existingUser = userRepository.findByEmail(emailUserDto.getEmail());

        if (existingUser == null) {
            throw new NotFoundException("User not found: " + emailUserDto.getEmail());
        }

        EntityValidator.validateStatus(existingUser);

        RoomEntity existingRoom = getRoomById(roomId);
        RoomValidator.validateStatus(existingRoom);

        if (existingRoom.getUsers().contains(existingUser)) {
            throw new NotFoundException("This guest is already here!");
        }

        existingUser.getRooms().add(existingRoom);
        existingUser.setUpdated(new Date());
        userRepository.save(existingUser);

        existingRoom.getUsers().add(existingUser);
        existingRoom.setUpdated(new Date());
        roomRepository.save(existingRoom);

        String message = MessageGenerate.getMessageAddGuest(existingUser, existingRoom);
        mailSender.send(existingUser.getEmail(), "Welcome to the Room!", message);

        return existingRoom;
    }

    @Override
    public List<UserEntity> getGuestsOfRoom(Long roomId) {
        RoomEntity existingRoom = getRoomById(roomId);

        return existingRoom.getUsers();
    }

    @Transactional
    @Override
    public RoomEntity deleteGuestFromRoom(EmailUserDto emailUserDto, Long roomId) {
        if (StringUtils.isEmpty(emailUserDto.getEmail())) {
            throw new NotFoundException("Enter user's email!");
        }

        UserEntity existingUser = userRepository.findByEmail(emailUserDto.getEmail());

        if (existingUser == null) {
            throw new NotFoundException("User not found: " + emailUserDto.getEmail());
        }

        RoomEntity existingRoom = getRoomById(roomId);

        if (!existingRoom.getUsers().contains(existingUser)) {
            throw new NotFoundException("This guest not found in room");
        }

        existingUser.getRooms().remove(existingRoom);
        existingUser.setUpdated(new Date());
        userRepository.save(existingUser);

        existingRoom.getUsers().remove(existingUser);
        existingRoom.setUpdated(new Date());
        roomRepository.save(existingRoom);

        String message = MessageGenerate.getMessageDeleteGuest(existingUser, existingRoom);
        mailSender.send(existingUser.getEmail(), "Exclusion", message);

        return existingRoom;
    }

    @Transactional
    @Override
    public void deleteRoomById(EmailUserDto emailUserDto, Long roomId) {
        RoomEntity existingRoom = getRoomById(roomId);

        UserEntity existingUser = userRepository.findByEmail(emailUserDto.getEmail());

        if (existingRoom.getStatus().equals(Status.DELETED)) {
            throw new NotFoundException("This room is already deleted.");
        }

        if (!existingRoom.getOwner().equals(existingUser)) {
            throw new NotFoundException("Owner not found by email: " + emailUserDto.getEmail());
        }

        existingUser.getUserRooms().remove(existingRoom);
        existingUser.getRooms().remove(existingRoom);
        existingUser.setUpdated(new Date());

        existingRoom.setStatus(Status.DELETED);
        existingRoom.setUpdated(new Date());

        existingRoom.getUsers().forEach(user -> {
            String message = MessageGenerate.getMessageForGuests(user, existingRoom);
            mailSender.send(user.getEmail(), "Deleting a Room", message);
        });

        userRepository.save(existingUser);
        roomRepository.save(existingRoom);
    }

    @Transactional
    @Override
    public RoomEntity updateRoomById(RoomCreationDto roomCreationDto, Long roomId) {
        RoomValidator.validateRoom(roomCreationDto);

        RoomEntity updatingRoom = getRoomById(roomId);

        if (!updatingRoom.getOwner().getEmail().equals(roomCreationDto.getOwner().getEmail())) {
            throw new NotFoundException("Enter owner's email!");
        }

        UserEntity existingUser = userRepository.findByEmail(roomCreationDto.getOwner().getEmail());

        if (existingUser == null) {
            throw new NotFoundException("User not found by email: " +
                    roomCreationDto.getOwner().getEmail());
        }

        RoomValidator.validateStatus(updatingRoom);

        roomMapper.updateRoomEntityFromRoomDto(roomCreationDto, updatingRoom);

        String message = MessageGenerate.getMessageForUpdateRoom(updatingRoom);
        mailSender.send(existingUser.getEmail(), "Updating Room", message);

        updatingRoom.setUpdated(new Date());
        return roomRepository.save(updatingRoom);
    }
}
