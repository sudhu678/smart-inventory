package com.one.smartinventory.service;

import com.one.smartinventory.model.Area;

import java.util.List;


public interface AreaBusinessService {

    List<Area> getAreas();

    Area getArea(String locator);

    Area create(Area area);

    void delete(String locator);

    Area freeze(String locator, boolean freeze);

}
