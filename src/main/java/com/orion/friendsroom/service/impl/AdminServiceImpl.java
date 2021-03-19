package com.orion.friendsroom.service.impl;

import com.orion.friendsroom.dto.AuthenticationRequestDto;
import com.orion.friendsroom.dto.AuthenticationResponseDto;
import com.orion.friendsroom.dto.RegisterDto;
import com.orion.friendsroom.dto.admin.EmailUserForAdminDto;
import com.orion.friendsroom.dto.admin.StatusUserForAdminDto;
import com.orion.friendsroom.email.MailSender;
import com.orion.friendsroom.entity.RoleEntity;
import com.orion.friendsroom.entity.Status;
import com.orion.friendsroom.entity.UserEntity;
import com.orion.friendsroom.exceptions.NotFoundException;
import com.orion.friendsroom.mapper.UserMapper;
import com.orion.friendsroom.repository.RoleRepository;
import com.orion.friendsroom.repository.UserRepository;
import com.orion.friendsroom.security.JwtProvider;
import com.orion.friendsroom.service.AdminService;
import com.orion.friendsroom.service.UserService;
import com.orion.friendsroom.service.validation.AuthenticationValidator;
import com.orion.friendsroom.service.validation.RegisterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Override
    public UserEntity registerAdmin(RegisterDto adminRegisterDto) {
        RegisterValidator.registerValidator(adminRegisterDto);

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
            AddNewEntity.getMessageForAdmin(admin);
        }

        List<RoleEntity> adminRoles = new ArrayList<>();

        adminRegisterDto.setPassword(passwordEncoder.encode(adminRegisterDto.getPassword()));
        UserEntity newAdmin = userMapper.toEntity(adminRegisterDto);

        AddNewEntity.addFields(newAdmin, adminRoles, roleAdmin);
        String message = AddNewEntity.getMessageForAdmin(newAdmin);
        mailSender.send(adminRegisterDto.getEmail(), "Activation code", message);

        return userRepository.save(newAdmin);
    }

    @Override
    public UserEntity getUserByEmail(EmailUserForAdminDto email) {
        UserEntity existingUser = userRepository.findByEmail(email.getEmail());

        if (existingUser == null) {
            throw new NotFoundException("User not found by email: " + email.getEmail());
        }

        return existingUser;
    }

    @Override
    public AuthenticationResponseDto validateAdminLogin(AuthenticationRequestDto requestDto) {
        AuthenticationValidator.validateAuthentication(requestDto);

        UserEntity admin = userService.findByEmailAndPassword(requestDto.getEmail(), requestDto.getPassword());

        AuthenticationValidator.validateStatusAuth(admin);

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

    @Override
    public UserEntity changeStatusForUserById(StatusUserForAdminDto newStatus, Long userId) {
        if (newStatus.getStatus() == null) {
            throw new NotFoundException("Can not resolve new status");
        }

        if (newStatus.getStatus().equals(Status.DELETED)) {
            throw new NotFoundException("Can not execute here!");
        }

        UserEntity existingUser = getUserById(userId);

        if (newStatus.getStatus().equals(existingUser.getStatus())) {
            throw new NotFoundException(String.format(
               "User with id: %s already has status: %s",
               userId,
               newStatus.getStatus()
            ));
        }

        existingUser.setStatus(newStatus.getStatus());
        existingUser.setUpdated(new Date());

        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUserById(Long userId) {
        UserEntity existingUser = getUserById(userId);

        if (existingUser.getStatus().equals(Status.DELETED)) {
            throw new NotFoundException("User is already deleted for id: " + userId);
        }

        existingUser.setStatus(Status.DELETED);
        userRepository.save(existingUser);
    }

    @Override
    public void deleteUserByEmail(EmailUserForAdminDto email) {
        UserEntity existingUser = getUserByEmail(email);

        if (existingUser.getStatus().equals(Status.DELETED)) {
            throw new NotFoundException("User is already deleted for email: " + email);
        }

        existingUser.setStatus(Status.DELETED);
        userRepository.save(existingUser);
    }
}
