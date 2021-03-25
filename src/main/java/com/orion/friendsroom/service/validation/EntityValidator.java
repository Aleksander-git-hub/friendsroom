package com.orion.friendsroom.service.validation;

import com.orion.friendsroom.dto.AuthenticationRequestDto;
import com.orion.friendsroom.dto.RegisterDto;
import com.orion.friendsroom.dto.admin.StatusDto;
import com.orion.friendsroom.dto.user.EmailUserDto;
import com.orion.friendsroom.dto.user.PasswordDto;
import com.orion.friendsroom.dto.user.UserUpdateDto;
import com.orion.friendsroom.entity.BaseEntity;
import com.orion.friendsroom.entity.Status;
import com.orion.friendsroom.entity.UserEntity;
import com.orion.friendsroom.exceptions.ForbiddenError;
import com.orion.friendsroom.exceptions.NotFoundException;
import org.apache.commons.lang3.StringUtils;

public class EntityValidator {
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
            throw new NotFoundException("User is banned");
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

        validatePasswordsFields(registerDto.getPassword(), registerDto.getRepeatPassword());
    }

    public static void validateForUpdate(UserUpdateDto userForUpdate) {
        if (StringUtils.isEmpty(userForUpdate.getFirstName()) ||
            StringUtils.isEmpty(userForUpdate.getSecondName())) {
            throw new NotFoundException("Some fields are empty! Please, check this!");
        }
    }

    public static void validateForUpdateEmail(EmailUserDto emailUserDto, String oldEmail) {
        if (StringUtils.isEmpty(emailUserDto.getEmail())) {
            throw new NotFoundException("Some fields are empty! Please, check this!");
        }

        if (emailUserDto.getEmail().equals(oldEmail)) {
            throw new NotFoundException("Same email!");
        }
    }

    public static void validateForPasswordChange(PasswordDto passwordDto) {
        if (StringUtils.isEmpty(passwordDto.getOldPassword()) ||
            StringUtils.isEmpty(passwordDto.getNewPassword()) ||
            StringUtils.isEmpty(passwordDto.getRepeatNewPassword())) {
            throw new NotFoundException("Some fields are empty! Please, check this!");
        }

        if (passwordDto.getOldPassword().equals(passwordDto.getNewPassword())) {
            throw new NotFoundException("Same password!");
        }

        validatePasswordsFields(passwordDto.getNewPassword(),
                passwordDto.getRepeatNewPassword());
    }

    private static void validatePasswordsFields(String first, String second) {
        if (first.length() < 6) {
            throw new NotFoundException("Password must have more than 5 chars");
        }

        if (!first.equals(second)) {
            throw new NotFoundException("Passwords mismatch!");
        }
    }

    public static void validateStatusField(StatusDto status, BaseEntity entity) {
        if (status.getStatus() == null) {
            throw new NotFoundException("Can not resolve new status");
        }

        if (status.getStatus().equals(Status.DELETED)) {
            throw new NotFoundException("Can not execute here!");
        }

        if (status.getStatus().equals(entity.getStatus())) {
            throw new NotFoundException(String.format(
                    "Entity with id: %s already has status: %s",
                    entity.getId(),
                    status.getStatus()
            ));
        }
    }

    public static void validateCurrentUser(UserEntity currentUser) {
        if (currentUser == null) {
            throw new ForbiddenError("Access denied");
        }
    }
}
