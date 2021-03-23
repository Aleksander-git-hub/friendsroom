package com.orion.friendsroom.service.impl;

import com.orion.friendsroom.dto.AuthenticationRequestDto;
import com.orion.friendsroom.dto.AuthenticationResponseDto;
import com.orion.friendsroom.dto.RegisterDto;
import com.orion.friendsroom.dto.admin.EmailUserDto;
import com.orion.friendsroom.dto.room.RoomNameDto;
import com.orion.friendsroom.dto.admin.StatusDto;
import com.orion.friendsroom.email.MailSender;
import com.orion.friendsroom.entity.RoleEntity;
import com.orion.friendsroom.entity.RoomEntity;
import com.orion.friendsroom.entity.Status;
import com.orion.friendsroom.entity.UserEntity;
import com.orion.friendsroom.exceptions.NotFoundException;
import com.orion.friendsroom.mapper.UserMapper;
import com.orion.friendsroom.repository.RoleRepository;
import com.orion.friendsroom.repository.RoomRepository;
import com.orion.friendsroom.repository.UserRepository;
import com.orion.friendsroom.security.JwtProvider;
import com.orion.friendsroom.service.AdminService;
import com.orion.friendsroom.service.MessageGenerate;
import com.orion.friendsroom.service.UserService;
import com.orion.friendsroom.service.validation.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private RoomRepository roomRepository;

    @Transactional
    @Override
    public UserEntity registerAdmin(RegisterDto adminRegisterDto) {
        EntityValidator.registerValidator(adminRegisterDto);

        UserEntity admin = userRepository.findByEmail(adminRegisterDto.getEmail());
        RoleEntity roleAdmin = roleRepository.findByName("ROLE_ADMIN");

        if (admin != null &&
            admin.getRoles().contains(roleAdmin)) {
            throw new NotFoundException("This admin already exist: " + adminRegisterDto.getEmail());
        }

        if (admin != null &&
                admin.getRoles().contains(roleRepository.findByName("ROLE_USER"))) {
            admin.setUpdated(new Date());
            admin.getRoles().add(roleAdmin);
            MessageGenerate.getMessageForAdmin(admin);
        }

        List<RoleEntity> adminRoles = new ArrayList<>();

        adminRegisterDto.setPassword(passwordEncoder.encode(adminRegisterDto.getPassword()));
        UserEntity newAdmin = userMapper.toEntity(adminRegisterDto);

        MessageGenerate.setFields(newAdmin, adminRoles, roleAdmin);
        String message = MessageGenerate.getMessageForAdmin(newAdmin);
        mailSender.send(adminRegisterDto.getEmail(), "Activation code", message);

        return userRepository.save(newAdmin);
    }

    @Override
    public UserEntity getUserByEmail(EmailUserDto email) {
        UserEntity existingUser = userRepository.findByEmail(email.getEmail());

        if (existingUser == null) {
            throw new NotFoundException("User not found by email: " + email.getEmail());
        }

        return existingUser;
    }

    @Override
    public AuthenticationResponseDto validateAdminLogin(AuthenticationRequestDto requestDto) {
        EntityValidator.validateAuthentication(requestDto);

        UserEntity admin = userService.findByEmailAndPassword(requestDto.getEmail(), requestDto.getPassword());

        EntityValidator.validateStatus(admin);

        return new AuthenticationResponseDto(jwtProvider.generateToken(admin.getEmail()));
    }

    @Override
    public UserEntity getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found by id: " + userId));
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<UserEntity> getAllBannedUsers() {
        return userRepository.findAll().stream()
                .filter(user -> user.getStatus().equals(Status.BANNED))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserEntity> getAllActiveUsers() {
        return userRepository.findAll().stream()
                .filter(user -> user.getStatus().equals(Status.ACTIVE))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public UserEntity changeStatusForUserById(StatusDto newStatus, Long userId) {
        UserEntity existingUser = getUserById(userId);

        EntityValidator.validateStatusField(newStatus, existingUser);

        existingUser.setStatus(newStatus.getStatus());
        existingUser.setUpdated(new Date());

        return userRepository.save(existingUser);
    }

    @Transactional
    @Override
    public void deleteUserById(Long userId) {
        UserEntity existingUser = getUserById(userId);

        if (existingUser.getStatus().equals(Status.DELETED)) {
            throw new NotFoundException("User is already deleted for id: " + userId);
        }

        existingUser.setStatus(Status.DELETED);
        existingUser.setUpdated(new Date());
        userRepository.save(existingUser);
    }

    @Transactional
    @Override
    public void deleteUserByEmail(EmailUserDto email) {
        UserEntity existingUser = getUserByEmail(email);

        if (existingUser.getStatus().equals(Status.DELETED)) {
            throw new NotFoundException("User is already deleted for email: " + email);
        }

        existingUser.setStatus(Status.DELETED);
        existingUser.setUpdated(new Date());
        userRepository.save(existingUser);
    }

    @Override
    public List<RoomEntity> getRoomsByOwner(EmailUserDto emailUserDto) {
        UserEntity existingUser = getUserByEmail(emailUserDto);

        return existingUser.getUserRooms();
    }

    @Override
    public List<RoomEntity> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public RoomEntity getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Room not found by id: " + id));
    }

    @Override
    public List<RoomEntity> getAllBannedRooms() {
        return roomRepository.findAll().stream()
                .filter(room -> room.getStatus().equals(Status.BANNED))
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomEntity> getAllActiveRooms() {
        return roomRepository.findAll().stream()
                .filter(room -> room.getStatus().equals(Status.ACTIVE))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public RoomEntity changeStatusForRoomById(StatusDto status, Long roomId) {
        RoomEntity existingRoom = getRoomById(roomId);

        EntityValidator.validateStatusField(status, existingRoom);

        existingRoom.setStatus(status.getStatus());
        existingRoom.setUpdated(new Date());

        return roomRepository.save(existingRoom);
    }

    @Transactional
    @Override
    public void deleteRoomById(Long roomId) {
        RoomEntity existingRoom = getRoomById(roomId);

        if (existingRoom.getStatus().equals(Status.DELETED)) {
            throw new NotFoundException("Room is deleted already for id: " + roomId);
        }

        existingRoom.setStatus(Status.DELETED);
        existingRoom.setUpdated(new Date());
        roomRepository.save(existingRoom);
    }

    @Transactional
    @Override
    public void deleteRoomByName(RoomNameDto roomNameDto) {
        RoomEntity existingRoom = roomRepository.findByName(roomNameDto.getName());

        if (existingRoom == null) {
            throw new NotFoundException("Room not found by name: " + roomNameDto.getName());
        }

        if (existingRoom.getStatus().equals(Status.DELETED)) {
            throw new NotFoundException("Room is deleted already for name: " + roomNameDto.getName());
        }

        existingRoom.setStatus(Status.DELETED);
        existingRoom.setUpdated(new Date());

        roomRepository.save(existingRoom);
    }

    @Override
    public void deleteRoomsByOwner(EmailUserDto emailUserDto) {
        UserEntity existingUser = userRepository.findByEmail(emailUserDto.getEmail());

        if (existingUser == null) {
            throw new NotFoundException("User not found by email: " + emailUserDto.getEmail());
        }

        List<RoomEntity> rooms = roomRepository.findByOwner(existingUser);

        if (rooms.size() == 0) {
            throw new NotFoundException("User with email: " + emailUserDto.getEmail() + " do not have rooms");
        }

        rooms.forEach(roomEntity -> {
            roomEntity.setStatus(Status.DELETED);
            roomEntity.setUpdated(new Date());
            roomRepository.save(roomEntity);
        });
    }
}
