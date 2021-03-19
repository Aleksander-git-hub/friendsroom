package com.orion.friendsroom.service.validation;

import com.orion.friendsroom.dto.AuthenticationRequestDto;
import com.orion.friendsroom.dto.RegisterDto;
import com.orion.friendsroom.dto.user.UserDto;
import com.orion.friendsroom.entity.Status;
import com.orion.friendsroom.entity.UserEntity;
import com.orion.friendsroom.exceptions.NotFoundException;
import org.apache.commons.lang3.StringUtils;

public class HandleValidator {
    public static void validateAuthentication(AuthenticationRequestDto requestDto) {

        if (StringUtils.isEmpty(requestDto.getEmail()) ||
                StringUtils.isEmpty(requestDto.getPassword())) {
            throw new NotFoundException("Some fields are empty! Please, check this!");
        }
    }

    public static void validateStatus(UserEntity userEntity) {

        if (userEntity.getStatus().equals(Status.DELETED) ||
                userEntity.getStatus().equals(Status.NOT_CONFIRMED)) {
            throw new NotFoundException("Reject status");
        }

        if (userEntity.getStatus().equals(Status.BANNED)) {
            throw new NotFoundException("You are banned");
        }
    }

    public static void registerValidator(RegisterDto registerDto) {
        if (StringUtils.isEmpty(registerDto.getEmail()) ||
                StringUtils.isEmpty(registerDto.getFirstName()) ||
                StringUtils.isEmpty(registerDto.getSecondName()) ||
                StringUtils.isEmpty(registerDto.getPassword()) ||
                StringUtils.isEmpty(registerDto.getRepeatPassword())) {
            throw new NotFoundException("Some fields are empty! Please, check this!");
        }

        if (registerDto.getPassword().length() < 6) {
            throw new NotFoundException("Password must have more than 5 chars");
        }

        if (!registerDto.getPassword().equals(registerDto.getRepeatPassword())) {
            throw new NotFoundException("Passwords mismatch!");
        }
    }

    public static void validateForUpdate(UserDto userForUpdate) {
        if (StringUtils.isEmpty(userForUpdate.getEmail()) ||
            StringUtils.isEmpty(userForUpdate.getFirstName()) ||
            StringUtils.isEmpty(userForUpdate.getSecondName())) {
            throw new NotFoundException("Some fields are empty! Please, check this!");
        }
    }
}
