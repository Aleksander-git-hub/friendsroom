package com.orion.friendsroom.repository;

import com.orion.friendsroom.entity.UserEntity;
import com.orion.friendsroom.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    List<UserEntity> findAllByStatus(Status status);

    UserEntity findByEmail(String email);

    UserEntity findByActivationCode(String code);
}
