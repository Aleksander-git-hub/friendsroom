package com.orion.friendsroom.dto.admin;

import com.orion.friendsroom.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserForAdminDto {

    private Long id;

    private String email;

    private String firstName;

    private String secondName;

    private Double totalAmount;

    private Date created;

    private Date updated;

    private Status status;
}
