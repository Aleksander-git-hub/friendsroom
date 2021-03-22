package com.orion.friendsroom.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Column
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "second_name")
    private String secondName;

    @Column
    private String password;

    @Column(name = "activation_code")
    private String activationCode;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private List<RoomEntity> userRooms;

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private List<RoomEntity> rooms;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<RoleEntity> roles;
}
