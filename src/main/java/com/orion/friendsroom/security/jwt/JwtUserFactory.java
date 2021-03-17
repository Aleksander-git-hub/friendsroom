package com.orion.friendsroom.security.jwt;

import com.orion.friendsroom.entity.RoleEntity;
import com.orion.friendsroom.entity.Status;
import com.orion.friendsroom.entity.UserEntity;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class JwtUserFactory {

    public static JwtUser jwtUserFromUserEntity(UserEntity userEntity) {
        return new JwtUser(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getFirstName(),
                userEntity.getSecondName(),
                userEntity.getPassword(),
                userEntity.getStatus().equals(Status.ACTIVE),
                userEntity.getUpdated(),
                mapToGrantedAuthorities(userEntity.getRoles())
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<RoleEntity> userRoles) {
        return userRoles.stream()
                .map(roleEntity -> new SimpleGrantedAuthority(roleEntity.getName()))
                .collect(Collectors.toList());
    }
}
