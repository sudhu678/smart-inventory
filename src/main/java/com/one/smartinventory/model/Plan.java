package com.one.smartinventory.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Plan {
    private long id;
    List<Task> tasks = new ArrayList<>();
    private LocalDate planDate;
}
