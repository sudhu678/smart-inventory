package com.one.smartinventory.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Visit {
    private long id;
    private LocalDate date;
    private String aisle;
    private long count;
}
