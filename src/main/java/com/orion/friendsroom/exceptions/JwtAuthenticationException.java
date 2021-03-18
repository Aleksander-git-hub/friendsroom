package com.orion.friendsroom.exceptions;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class JwtAuthenticationException extends JwtException {
    public JwtAuthenticationException(String message) {
        super(message);
    }
}
