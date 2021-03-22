package com.orion.friendsroom.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "rooms")
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private Date created;

    @Column
    private Date updated;

    @ManyToOne
    private UserEntity owner;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_rooms",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "room_id", referencedColumnName = "id")})
    private List<UserEntity> users;
}
