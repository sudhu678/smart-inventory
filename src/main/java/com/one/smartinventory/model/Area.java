package com.one.smartinventory.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Area {
    private long id;

    private String aisle;

    private double length;

    private double width;

    private double height;

    private boolean freeze;

    private long viewed;

    public boolean isAllowed(Product product) {
        return this.length >= product.getLength() &&
                this.width >= product.getWidth() &&
                this.height >= product.getHeight();
    }

    public void placeProduct(Product product) {
        this.length -= product.getLength();
    }

}
