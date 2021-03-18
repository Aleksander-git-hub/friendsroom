package com.orion.friendsroom.service.validation;

import com.orion.friendsroom.dto.user.UserRegisterDto;
import com.orion.friendsroom.exceptions.NotFoundException;
import org.apache.commons.lang3.StringUtils;

public class RegisterValidator {

    public static void registerValidator(UserRegisterDto userRegisterDto) {
        if (StringUtils.isEmpty(userRegisterDto.getEmail()) ||
            StringUtils.isEmpty(userRegisterDto.getFirstName()) ||
            StringUtils.isEmpty(userRegisterDto.getSecondName()) ||
            StringUtils.isEmpty(userRegisterDto.getPassword()) ||
            StringUtils.isEmpty(userRegisterDto.getRepeatPassword())) {
            throw new NotFoundException("Some fields are empty! Please, check this!");
        }

        if (userRegisterDto.getPassword().length() < 6) {
            throw new NotFoundException("Password must have more than 5 chars");
        }

        if (!userRegisterDto.getPassword().equals(userRegisterDto.getRepeatPassword())) {
            throw new NotFoundException("Passwords mismatch!");
        }
    }
}
