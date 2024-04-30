package com.one.smartinventory.service;

import com.one.smartinventory.model.Visit;

import java.time.LocalDate;
import java.util.List;

public interface VisitorBusinessService {

    List<Visit> getAll();

    List<Visit> getAll(LocalDate date, long count);

    Visit getVisit(String locator, LocalDate date);

    Visit updateVisit(String locator, LocalDate date);

    void deleteVisit(String locator, LocalDate date);

}
