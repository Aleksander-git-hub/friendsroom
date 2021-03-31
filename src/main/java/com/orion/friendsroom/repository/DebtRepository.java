package com.orion.friendsroom.repository;

import com.orion.friendsroom.entity.DebtEntity;
import com.orion.friendsroom.entity.RoomEntity;
import com.orion.friendsroom.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebtRepository extends JpaRepository<DebtEntity, Long> {

    DebtEntity findByRoomAndUserAndWhoOwesMoney(RoomEntity room, UserEntity debtor, UserEntity whoOwes);
}
