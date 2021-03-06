package com.orion.friendsroom.dto.admin;

import com.orion.friendsroom.dto.user.EmailUserDto;
import com.orion.friendsroom.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomForAdminDto {

    private Long id;

    private String name;

    private Date created;

    private Date updated;

    private Status status;

    private EmailUserDto owner;
}
