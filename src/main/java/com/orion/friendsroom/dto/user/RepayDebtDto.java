package com.orion.friendsroom.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepayDebtDto {

    private String toWhom;

    private Double amount;
}
