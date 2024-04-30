package com.one.smartinventory.dao.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity(name = "AREA")
@Getter
@Setter
@NamedQueries({
        @NamedQuery(name = "AreaEntity.find", query = "SELECT a FROM AREA a WHERE a.aisle = ?1")})
public class AreaEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "UKEY")
    private long id;

    @Column(name = "AISLE")
    private String aisle;

    @Column(name = "LENGTH")
    private double length;

    @Column(name = "WIDTH")
    private double width;

    @Column(name = "HEIGHT")
    private double height;

    @Column(name = "FREEZE")
    private boolean freeze;

    @Column(name = "VIEWED")
    private long viewed;

}
