package com.orion.friendsroom.service.impl;

import com.orion.friendsroom.dto.AuthenticationRequestDto;
import com.orion.friendsroom.dto.AuthenticationResponseDto;
import com.orion.friendsroom.dto.user.UserRegisterDto;
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
    public UserEntity registerAdmin(UserRegisterDto adminRegisterDto) {
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
    public UserEntity getUserByEmail(String email) {
        return null;
    }

    @Override
    public AuthenticationResponseDto validateAdminLogin(AuthenticationRequestDto requestDto) {
        AuthenticationValidator.validateAuthentication(requestDto);

        UserEntity admin = userService.findByEmailAndPassword(requestDto.getEmail(), requestDto.getPassword());

        AuthenticationValidator.validateStatusAuth(admin);

        return new AuthenticationResponseDto(jwtProvider.generateToken(admin.getEmail()));
    }

    @Override
    public void activateAdmin(String code) {
        UserEntity admin = userRepository.findByActivationCode(code);

        admin.setActivationCode(null);
        admin.setStatus(Status.ACTIVE);

        userRepository.save(admin);
    }
}
