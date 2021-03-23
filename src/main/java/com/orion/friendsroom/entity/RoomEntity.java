package com.orion.friendsroom.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "rooms")
public class RoomEntity extends BaseEntity{

    @Column
    private String name;

    @Column(name = "activation_code")
    private String activationCode;

    @ManyToOne
    private UserEntity owner;

    @ManyToMany(mappedBy = "rooms", fetch = FetchType.LAZY)
    private List<UserEntity> users;
}
