package com.orion.friendsroom.service;

import com.orion.friendsroom.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    @Autowired
    private AdminService adminService;

    public String getCurrentUserEmail() {
        String email = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        }

        return email;
    }

    public UserEntity getCurrentUser() {
        UserEntity principal = (UserEntity) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        if (principal != null) {
            return adminService.getUserById(principal.getId());
        }

        return null;
    }
}
