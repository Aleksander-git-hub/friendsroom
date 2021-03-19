package com.orion.friendsroom.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDto {

    private Long id;

    private String firstName;

    private String secondName;

    private String email;
}
