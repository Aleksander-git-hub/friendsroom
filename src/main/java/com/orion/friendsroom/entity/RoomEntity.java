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
@Table(name = "rooms")
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "activation_code")
    private String activationCode;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<DebtEntity> debts;

    @ManyToOne
    private UserEntity owner;

    @ManyToMany(mappedBy = "rooms", fetch = FetchType.LAZY)
    private List<UserEntity> users;

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
