package com.orion.friendsroom.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "debts")
public class DebtEntity extends BaseEntity {

    @Column
    private Double sum;

    @ManyToOne
    private UserEntity user;

    @ManyToOne
    private RoomEntity room;

    @ManyToOne
    private UserEntity whoOwesMoney;
}
