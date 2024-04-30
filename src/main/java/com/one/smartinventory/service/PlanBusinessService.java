package com.one.smartinventory.service;

import com.one.smartinventory.model.Plan;
import com.one.smartinventory.model.PlanOption;

import java.time.LocalDate;
import java.util.List;

public interface PlanBusinessService {
    Plan getLatest();

    List<Plan> getAll();

    Plan get(LocalDate date);

    Plan generate(PlanOption planOption);

    void delete(LocalDate date);

}
