package com.one.smartinventory.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity(name = "VISITORS")
@Getter
@Setter
@NamedQueries({
        @NamedQuery(name = "VisitEntity.find", query = "SELECT v FROM VISITORS v WHERE v.date = ?1" +
                " ORDER BY count DESC LIMIT ?2")})
public class VisitEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "UKEY")
    private long id;

    @Column(name = "DATE")
    private LocalDate date;

    @Column(name = "AISLE")
    private String aisle;

    @Column(name = "COUNT")
    private long count;

}
