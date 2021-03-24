package com.orion.friendsroom.dto.room;

import com.orion.friendsroom.dto.admin.EmailUserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {

    private Long id;

    private String name;

    private EmailUserDto owner;

    private List<EmailUserDto> users;
}
