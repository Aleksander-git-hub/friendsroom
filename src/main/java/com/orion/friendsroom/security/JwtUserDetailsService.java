package com.orion.friendsroom.security;

import com.orion.friendsroom.entity.UserEntity;
import com.orion.friendsroom.security.jwt.JwtUserFactory;
import com.orion.friendsroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userService.getUserByEmail(email);

        return JwtUserFactory.jwtUserFromUserEntity(userEntity);
    }
}
