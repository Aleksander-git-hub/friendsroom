package com.orion.friendsroom.repository;

import com.orion.friendsroom.entity.RoomEntity;
import com.orion.friendsroom.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    RoomEntity findByName(String name);

    List<RoomEntity> findByOwner(UserEntity owner);

    RoomEntity findByActivationCode(String code);
}
