package com.orion.friendsroom.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Data
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
