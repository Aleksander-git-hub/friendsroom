package com.orion.friendsroom.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ForbiddenError extends AuthenticationException {

    public ForbiddenError(String message) {
        super(message);
    }
}
