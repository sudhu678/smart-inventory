package com.one.smartinventory.service.impl;

import com.one.smartinventory.dao.entity.AreaEntity;
import com.one.smartinventory.dao.repository.AreaRepository;
import com.one.smartinventory.mapper.AreaMapper;
import com.one.smartinventory.model.Area;
import com.one.smartinventory.service.AreaBusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AreaBusinessServiceImpl implements AreaBusinessService {

    private final AreaRepository areaRepository;

    private final AreaMapper areaMapper;

    @Autowired
    public AreaBusinessServiceImpl(AreaRepository areaRepository, AreaMapper areaMapper) {
        this.areaRepository = areaRepository;
        this.areaMapper = areaMapper;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Area> getAreas() {
        List<AreaEntity> entities = areaRepository.findAll();
        return entities.stream().map(areaMapper::to).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Area getArea(String locator) {
        AreaEntity entity = areaRepository.find(locator);
        if (entity == null) {
            log.warn("Area with locator {} not found!", locator);
            return null;
        }
        return areaMapper.to(entity);
    }

    @Override
    @Transactional
    public Area create(Area area) {
        AreaEntity entity = areaRepository.saveAndFlush(areaMapper.toEntity(area));
        return areaMapper.to(entity);
    }

    @Override
    @Transactional
    public void delete(String locator) {
        AreaEntity entity = areaRepository.find(locator);
        if (entity != null) {
            areaRepository.delete(entity);
        }
    }

    @Override
    @Transactional
    public Area freeze(String locator, boolean freeze) {
        AreaEntity entity = areaRepository.find(locator);
        if (entity == null) {
            log.warn("Area with locator {} not found!", locator);
            return null;
        }
        entity.setFreeze(freeze);
        entity = areaRepository.saveAndFlush(entity);
        return areaMapper.to(entity);
    }

}
