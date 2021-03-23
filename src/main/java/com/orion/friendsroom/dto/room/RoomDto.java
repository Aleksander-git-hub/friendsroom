package com.orion.friendsroom.dto.room;

import com.orion.friendsroom.dto.admin.EmailUserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {

    @NotBlank
    private String name;

    @NotBlank
    private EmailUserDto owner;
}
