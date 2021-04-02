package com.orion.friendsroom.entity;

import com.orion.friendsroom.entity.enums.Status;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "second_name")
    private String secondName;

    @Column
    private String password;

    @Column(name = "total_amount")
    private Double totalAmount;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<DebtEntity> debts;

    @Column(name = "activation_code")
    private String activationCode;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private List<RoomEntity> userRooms;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_rooms",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "room_id", referencedColumnName = "id")})
    private List<RoomEntity> rooms;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<RoleEntity> roles;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "whoOwesMoney")
    private List<DebtEntity> myDebtors;

    @Column
    @CreatedDate
    private Date created;

    @Column
    @LastModifiedDate
    private Date updated;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;
}
