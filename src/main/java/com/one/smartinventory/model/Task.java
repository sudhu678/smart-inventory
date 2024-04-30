package com.one.smartinventory.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Task {
    private long id;
    private LocalDate planDate;
    private long productId;
    private String origin;
    private String destination;
    private long count;
}
