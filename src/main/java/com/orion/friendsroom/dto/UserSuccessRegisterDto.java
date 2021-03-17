package com.orion.friendsroom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSuccessRegisterDto {

    private String email;

    private String firstName;

    private String secondName;
}
