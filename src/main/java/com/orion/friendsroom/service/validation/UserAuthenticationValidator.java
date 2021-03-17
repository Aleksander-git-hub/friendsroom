package com.orion.friendsroom.service.validation;

import com.orion.friendsroom.dto.AuthenticationRequestDto;
import com.orion.friendsroom.entity.Status;
import com.orion.friendsroom.entity.UserEntity;
import com.orion.friendsroom.exceptions.NotFoundException;
import org.apache.commons.lang3.StringUtils;

public class UserAuthenticationValidator {

    public static void validateAuthenticationUser(AuthenticationRequestDto requestDto) {

        if (StringUtils.isEmpty(requestDto.getEmail()) ||
            StringUtils.isEmpty(requestDto.getPassword())) {
            throw new NotFoundException("Some fields are empty! Please, check this!");
        }
    }

    public static void validateStatusAuthUser(UserEntity userEntity) {

        if (userEntity.getStatus().equals(Status.DELETED) ||
            userEntity.getStatus().equals(Status.NOT_CONFIRMED)) {
            throw new NotFoundException("Reject status");
        }

        if (userEntity.getStatus().equals(Status.BANNED)) {
            throw new NotFoundException("You are banned");
        }
    }
}
