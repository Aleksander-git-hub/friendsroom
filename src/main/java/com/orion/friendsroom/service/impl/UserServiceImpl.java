package com.orion.friendsroom.service.impl;

import com.orion.friendsroom.dto.AuthenticationRequestDto;
import com.orion.friendsroom.dto.AuthenticationResponseDto;
import com.orion.friendsroom.dto.UserRegisterDto;
import com.orion.friendsroom.email.MailSender;
import com.orion.friendsroom.entity.RoleEntity;
import com.orion.friendsroom.entity.Status;
import com.orion.friendsroom.entity.UserEntity;
import com.orion.friendsroom.exceptions.JwtAuthenticationException;
import com.orion.friendsroom.exceptions.NotFoundException;
import com.orion.friendsroom.mapper.UserMapper;
import com.orion.friendsroom.repository.RoleRepository;
import com.orion.friendsroom.repository.UserRepository;
import com.orion.friendsroom.security.JwtProvider;
import com.orion.friendsroom.service.UserService;
import com.orion.friendsroom.service.validation.UserAuthenticationValidator;
import com.orion.friendsroom.service.validation.UserRegisterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    public UserEntity registerUser(UserRegisterDto userRegisterDto) {
        UserRegisterValidator.userRegisterValidator(userRegisterDto);
        UserEntity existingUser = userRepository.findByEmail(userRegisterDto.getEmail());

        if (existingUser != null) {
            throw new NotFoundException("User already registered with email: " + userRegisterDto.getEmail());
        }

        RoleEntity roleUser = roleRepository.findByName("ROLE_USER");
        List<RoleEntity> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        userRegisterDto.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        UserEntity userEntity = userMapper.toEntity(userRegisterDto);
        userEntity.setCreated(new Date());
        userEntity.setUpdated(userEntity.getCreated());
        userEntity.setRoles(userRoles);
        userEntity.setStatus(Status.NOT_CONFIRMED);
        userEntity.setActivationCode(UUID.randomUUID().toString());

        String message = String.format(
                "Hello, %s!\n" +
                        "Welcome to FriendsRoom! Please, visit next link: " +
                        "http://localhost:8070/friends-room/api/v1/activate/%s",
                userEntity.getFirstName(),
                userEntity.getActivationCode()
        );
        mailSender.send(userRegisterDto.getEmail(), "Activation code", message);

        return userRepository.save(userEntity);
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
            throw new JwtAuthenticationException("Invalid password!");
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


    @Override
    public void activateUser(String code) {
        UserEntity userEntity = userRepository.findByActivationCode(code);

        userEntity.setActivationCode(null);
        userEntity.setStatus(Status.ACTIVE);

        userRepository.save(userEntity);
    }

    @Override
    public AuthenticationResponseDto validateUserLogin(AuthenticationRequestDto requestDto) {
        UserAuthenticationValidator.validateAuthenticationUser(requestDto);

        UserEntity userEntity = findByEmailAndPassword(requestDto.getEmail(), requestDto.getPassword());

        UserAuthenticationValidator.validateStatusAuthUser(userEntity);

        return new AuthenticationResponseDto(jwtProvider.generateToken(userEntity.getEmail()));
    }
}
