package com.orion.friendsroom.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {

    @NotBlank
    private String firstName;

    @NotBlank
    private String secondName;
}
