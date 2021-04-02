package com.orion.friendsroom.dto.admin;

import com.orion.friendsroom.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusDto {
    private Status status;
}
