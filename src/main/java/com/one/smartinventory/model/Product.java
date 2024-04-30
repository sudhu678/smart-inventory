package com.one.smartinventory.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Product {
    private long id;
    private long productId;
    private String name;
    private double length;
    private double width;
    private double height;
    private long count;
    private String aisle;
    private LocalDate expiry;
    private boolean freeze;
    private long viewed;
}
