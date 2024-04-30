package com.one.smartinventory.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity(name = "PRODUCT")
@Getter
@Setter
@NamedQueries({
        @NamedQuery(name = "ProductEntity.find", query = "SELECT p FROM PRODUCT p WHERE p.productId = ?1"),
        @NamedQuery(name = "ProductEntity.findMatch", query = "SELECT p FROM PRODUCT p WHERE p.name ILIKE '%' || ?1 || '%'")})
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "UKEY")
    private long id;

    @Column(name = "PRODUCT_ID")
    private long productId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "LENGTH")
    private double length;

    @Column(name = "WIDTH")
    private double width;

    @Column(name = "HEIGHT")
    private double height;

    @Column(name = "COUNT")
    private long count;

    @Column(name = "AISLE")
    private String aisle;

    @Column(name = "EXPIRY")
    private LocalDate expiry;

    @Column(name = "FREEZE")
    private boolean freeze;

    @Column(name = "VIEWED")
    private long viewed;
}
