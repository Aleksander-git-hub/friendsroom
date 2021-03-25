package com.orion.friendsroom.service.impl;

import com.orion.friendsroom.dto.AuthenticationRequestDto;
import com.orion.friendsroom.dto.AuthenticationResponseDto;
import com.orion.friendsroom.dto.RegisterDto;
import com.orion.friendsroom.dto.admin.EmailUserDto;
import com.orion.friendsroom.dto.user.PasswordDto;
import com.orion.friendsroom.dto.user.UserUpdateDto;
import com.orion.friendsroom.email.MailSender;
import com.orion.friendsroom.entity.RoleEntity;
import com.orion.friendsroom.entity.Status;
import com.orion.friendsroom.entity.UserEntity;
import com.orion.friendsroom.exceptions.ForbiddenError;
import com.orion.friendsroom.exceptions.NotFoundException;
import com.orion.friendsroom.mapper.UserMapper;
import com.orion.friendsroom.repository.RoleRepository;
import com.orion.friendsroom.repository.UserRepository;
import com.orion.friendsroom.security.JwtProvider;
import com.orion.friendsroom.service.CurrentUserService;
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

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private CurrentUserService currentUserService;

    @Transactional
    @Override
    public UserEntity registerUser(RegisterDto registerDto) {
        EntityValidator.registerValidator(registerDto);
        UserEntity existingUser = userRepository.findByEmail(registerDto.getEmail());
        RoleEntity roleUser = roleRepository.findByName("ROLE_USER");

        if (existingUser != null &&
            existingUser.getRoles().contains(roleUser)) {
            throw new NotFoundException("User already registered with email: " + registerDto.getEmail());
        }

        if (existingUser != null &&
            existingUser.getRoles().contains(roleRepository.findByName("ROLE_ADMIN"))) {
            existingUser.setUpdated(new Date());
            existingUser.getRoles().add(roleUser);
        }


        List<RoleEntity> userRoles = new ArrayList<>();

        registerDto.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        UserEntity userEntity = userMapper.toEntity(registerDto);

        MessageGenerate.setFields(userEntity, userRoles, roleUser);
        userRepository.save(userEntity);

        String message = MessageGenerate.getMessageForUser(userEntity);
        mailSender.send(registerDto.getEmail(), "Activation code", message);

        return userEntity;
    }

    @Override
    public UserEntity findByEmailAndPassword(String email, String password) {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) {
            throw new NotFoundException("User not found by email: " + email);
        }

        if (passwordEncoder.matches(password, userEntity.getPassword())) {
            return userEntity;
        } else {
            throw new NotFoundException("Invalid password!");
        }
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        UserEntity existingUser = userRepository.findByEmail(email);

        if (existingUser != null) {
            return existingUser;
        } else {
            throw new NotFoundException("User not found by email: " + email);
        }
    }

    @Transactional
    @Override
    public void activateUser(String code) {
        UserEntity userEntity = userRepository.findByActivationCode(code);

        userEntity.setActivationCode(null);
        userEntity.setStatus(Status.ACTIVE);

        userRepository.save(userEntity);
    }

    @Override
    public AuthenticationResponseDto validateUserLogin(AuthenticationRequestDto requestDto) {
        EntityValidator.validateAuthentication(requestDto);

        UserEntity userEntity = findByEmailAndPassword(requestDto.getEmail(), requestDto.getPassword());

        EntityValidator.validateStatus(userEntity);

        return new AuthenticationResponseDto(jwtProvider.generateToken(userEntity.getEmail()));
    }

    @Override
    public UserEntity getUserByEmailForSearch(EmailUserDto emailDto) {
        if (emailDto.getEmail() == null) {
            throw new NotFoundException("Field for search is empty!");
        }

        return getUserByEmail(emailDto.getEmail());
    }

    @Transactional
    @Override
    public UserEntity updateUser(UserUpdateDto userForUpdate) {
        EntityValidator.validateForUpdate(userForUpdate);

        UserEntity currentUser = currentUserService.getCurrentUser();
        EntityValidator.validateCurrentUser(currentUser);

        userMapper.updateUserEntityFromUserUpdateDto(userForUpdate, currentUser);

        currentUser.setUpdated(new Date());

        userRepository.save(currentUser);

        String message = MessageGenerate.getMessageForUpdateUser(currentUser);
        mailSender.send(currentUser.getEmail(), "Updating", message);

        return currentUser;
    }

    @Transactional
    @Override
    public UserEntity updateEmailOfUser(EmailUserDto emailUserDto) {
        UserEntity currentUser = currentUserService.getCurrentUser();
        EntityValidator.validateCurrentUser(currentUser);
        EntityValidator.validateForUpdateEmail(emailUserDto, currentUser.getEmail());

        if (userRepository.findByEmail(emailUserDto.getEmail()) != null) {
            throw new NotFoundException("User already exist with email: " +
                    emailUserDto.getEmail());
        }

        currentUser.setEmail(emailUserDto.getEmail());
        currentUser.setUpdated(new Date());

        userRepository.save(currentUser);

        String message = MessageGenerate.getMessageForUser(currentUser);
        mailSender.send(currentUser.getEmail(), "Email updating", message);

        return currentUser;
    }

    @Override
    public UserEntity changePassword(PasswordDto passwordDto) {
        EntityValidator.validateForPasswordChange(passwordDto);

        String currentEmail = currentUserService.getCurrentUserEmail();

        if (currentEmail == null) {
            throw new ForbiddenError("Access denied");
        }

        UserEntity existingUser = findByEmailAndPassword(currentEmail,
                passwordDto.getOldPassword());

        existingUser.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        existingUser.setUpdated(new Date());

        userRepository.save(existingUser);

        String message = MessageGenerate.getMessageForUpdateUser(existingUser);
        mailSender.send(currentEmail, "Password update", message);

        return existingUser;
    }
}
