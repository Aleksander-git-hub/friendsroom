package com.orion.friendsroom.security.jwt;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

@Getter
public class JwtUser implements UserDetails {

    private final Long id;

    private final String email;

    private final String firstName;

    private final String secondName;

    private final String password;

    private final boolean enabled;

    private final Date lastPasswordResetDate;

    private final Collection<? extends GrantedAuthority> authorities;

    public JwtUser(
            Long id,
            String email,
            String firstName,
            String secondName,
            String password,
            boolean enabled,
            Date lastPasswordResetDate,
            Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.secondName = secondName;
        this.password = password;
        this.enabled = enabled;
        this.lastPasswordResetDate = lastPasswordResetDate;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Long getId() {
        return id;
    }
}
