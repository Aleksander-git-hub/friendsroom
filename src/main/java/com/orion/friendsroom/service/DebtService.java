package com.orion.friendsroom.service;

import com.orion.friendsroom.entity.DebtEntity;
import com.orion.friendsroom.entity.RoomEntity;
import com.orion.friendsroom.entity.UserEntity;

public interface DebtService {

    DebtEntity createDept(UserEntity user, RoomEntity room, Double amount, UserEntity ownerOfMoney);

    DebtEntity deleteDebt(UserEntity guest, RoomEntity room, Double amount, UserEntity ownerOfMoney);
}
