package com.orion.friendsroom.service.impl;

import com.orion.friendsroom.dto.AuthenticationRequestDto;
import com.orion.friendsroom.dto.AuthenticationResponseDto;
import com.orion.friendsroom.dto.RegisterDto;
import com.orion.friendsroom.dto.admin.EmailUserDto;
import com.orion.friendsroom.dto.user.PasswordDto;
import com.orion.friendsroom.dto.user.UserDto;
import com.orion.friendsroom.dto.user.EmailDto;
import com.orion.friendsroom.email.MailSender;
import com.orion.friendsroom.entity.RoleEntity;
import com.orion.friendsroom.entity.Status;
import com.orion.friendsroom.entity.UserEntity;
import com.orion.friendsroom.exceptions.NotFoundException;
import com.orion.friendsroom.mapper.UserMapper;
import com.orion.friendsroom.repository.RoleRepository;
import com.orion.friendsroom.repository.UserRepository;
import com.orion.friendsroom.security.JwtProvider;
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
    public UserEntity updateUserById(UserDto userForUpdate, Long id) {
        EntityValidator.validateForUpdate(userForUpdate);

        UserEntity existingUser = getUserByEmail(userForUpdate.getEmail());

        EntityValidator.validateWhoseEmail(existingUser.getId(), id);

        EntityValidator.validateStatus(existingUser);

        userMapper.updateUserEntityFromUserUpdateDto(userForUpdate, existingUser);

        existingUser.setUpdated(new Date());

        String message = MessageGenerate.getMessageForUpdateUser(existingUser);
        mailSender.send(existingUser.getEmail(), "Updating", message);

        return userRepository.save(existingUser);
    }

    @Transactional
    @Override
    public UserEntity updateEmailOfUser(EmailDto emailDto, Long id) {
        EntityValidator.validateForUpdateEmail(emailDto);

        if (userRepository.findByEmail(emailDto.getNewEmail()) != null) {
            throw new NotFoundException("User already exist with email: " +
                    emailDto.getNewEmail());
        }

        UserEntity existingUser = getUserByEmail(emailDto.getOldEmail());

        EntityValidator.validateWhoseEmail(existingUser.getId(), id);
        EntityValidator.validateStatus(existingUser);

        existingUser.setEmail(emailDto.getNewEmail());
        existingUser.setUpdated(new Date());

        String message = MessageGenerate.getMessageForUser(existingUser);
        mailSender.send(existingUser.getEmail(), "Email updating", message);

        return userRepository.save(existingUser);
    }

    @Override
    public UserEntity changePassword(PasswordDto passwordDto, Long id) {
        EntityValidator.validateForPasswordChange(passwordDto);

        UserEntity existingUser = findByEmailAndPassword(passwordDto.getEmail(),
                passwordDto.getOldPassword());

        EntityValidator.validateWhoseEmail(existingUser.getId(), id);
        EntityValidator.validateStatus(existingUser);

        existingUser.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        existingUser.setUpdated(new Date());

        String message = MessageGenerate.getMessageForUpdateUser(existingUser);
        mailSender.send(passwordDto.getEmail(), "Password update", message);

        return userRepository.save(existingUser);
    }

}
