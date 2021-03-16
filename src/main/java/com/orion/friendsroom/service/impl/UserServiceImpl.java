package com.orion.friendsroom.service.impl;

import com.orion.friendsroom.dto.UserRegistrationDto;
import com.orion.friendsroom.email.MailSender;
import com.orion.friendsroom.entity.RoleEntity;
import com.orion.friendsroom.entity.Status;
import com.orion.friendsroom.entity.UserEntity;
import com.orion.friendsroom.mapper.UserMapper;
import com.orion.friendsroom.repository.RoleRepository;
import com.orion.friendsroom.repository.UserRepository;
import com.orion.friendsroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

    @Transactional
    @Override
    public UserEntity registerUser(UserRegistrationDto userRegistrationDto) {
        RoleEntity roleUser = roleRepository.findByName("ROLE_USER");
        List<RoleEntity> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        userRegistrationDto.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        UserEntity userEntity = userMapper.toEntity(userRegistrationDto);
        userEntity.setCreated(new Date());
        userEntity.setUpdated(userEntity.getCreated());
        userEntity.setRoles(userRoles);
        userEntity.setStatus(Status.NOT_CONFIRMED);
        userEntity.setActivationCode(UUID.randomUUID().toString());

        String message = String.format(
                "Hello, %s!\n" +
                        "Welcome to FriendsRoom! Please, visit next link: " +
                        "http://localhost:8070/friends-room/activate/%s",
                userEntity.getFirstName(),
                userEntity.getActivationCode()
        );
        mailSender.send(userRegistrationDto.getEmail(), "Activation code", message);

        return userRepository.save(userEntity);
    }

    @Override
    public UserEntity getUserById(Long id) {
        return null;
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return null;
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        return null;
    }

    @Override
    public UserEntity updateUserById(UserEntity userEntity, Long id) {
        return null;
    }

    @Override
    public void deleteUserById(Long id) {

    }

    @Override
    public void activateUser(String code) {
        UserEntity userEntity = userRepository.findByActivationCode(code);

        userEntity.setActivationCode(null);
        userEntity.setStatus(Status.CONFIRMED);

        userRepository.save(userEntity);
    }
}
