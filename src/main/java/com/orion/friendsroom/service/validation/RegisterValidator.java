package com.orion.friendsroom.service.validation;

import com.orion.friendsroom.dto.user.RegisterDto;
import com.orion.friendsroom.exceptions.NotFoundException;
import org.apache.commons.lang3.StringUtils;

public class RegisterValidator {

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
}
