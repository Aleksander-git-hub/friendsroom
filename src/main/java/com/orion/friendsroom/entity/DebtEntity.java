package com.orion.friendsroom.entity;

import com.orion.friendsroom.entity.enums.Status;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
@Entity
@Table(name = "debts")
public class DebtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Double sum;

    @ManyToOne
    private UserEntity user;

    @ManyToOne
    private RoomEntity room;

    @ManyToOne
    private UserEntity whoOwesMoney;

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
