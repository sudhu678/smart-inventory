package com.one.smartinventory.service.impl;

import com.one.smartinventory.dao.entity.VisitEntity;
import com.one.smartinventory.dao.repository.VisitorRepository;
import com.one.smartinventory.mapper.VisitMapper;
import com.one.smartinventory.model.Area;
import com.one.smartinventory.model.Product;
import com.one.smartinventory.model.Visit;
import com.one.smartinventory.service.AreaBusinessService;
import com.one.smartinventory.service.ProductBusinessService;
import com.one.smartinventory.service.VisitorBusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VisitorBusinessServiceImpl implements VisitorBusinessService {

    private final VisitorRepository visitorRepository;

    private final VisitMapper visitMapper;

    private final ProductBusinessService productBusinessService;

    private final AreaBusinessService areaBusinessService;

    @Autowired
    public VisitorBusinessServiceImpl(VisitorRepository visitorRepository, VisitMapper visitMapper,
                                      ProductBusinessService productBusinessService,
                                      AreaBusinessService areaBusinessService) {
        this.visitorRepository = visitorRepository;
        this.visitMapper = visitMapper;
        this.productBusinessService = productBusinessService;
        this.areaBusinessService = areaBusinessService;
    }

    @Override
    public List<Visit> getAll() {
        List<VisitEntity> entities = visitorRepository.findAll();
        return entities.stream().map(visitMapper::to).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Visit> getAll(LocalDate date, long count) {
        List<VisitEntity> entities = visitorRepository.find(date, count);
        return entities.stream().map(visitMapper::to).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Visit getVisit(String locator, LocalDate date) {
        VisitEntity entity = visitorRepository.findByAisleAndDate(locator, date);
        if (entity == null) {
            log.warn("The location {} has no visitor entry for the given date {}", locator, date);
            return null;
        }
        return visitMapper.to(entity);
    }

    @Override
    @Transactional
    public Visit updateVisit(String locator, LocalDate date) {
        updateArea(locator);
        updateProduct(locator);
        VisitEntity entity = visitorRepository.findByAisleAndDate(locator, date);
        if (entity == null) {
            entity = new VisitEntity();
            entity.setCount(1);
            entity.setAisle(locator);
            entity.setDate(date);
        } else {
            entity.setCount(entity.getCount() + 1);
        }
        entity = visitorRepository.saveAndFlush(entity);

        return visitMapper.to(entity);
    }

    private void updateArea(String locator) {
        Area area = areaBusinessService.getArea(locator);
        area.setViewed(area.getViewed() + 1);
        areaBusinessService.create(area);
    }

    private void updateProduct(String locator) {
        List<Product> products = productBusinessService.get(locator);
        products.forEach(product -> {
            product.setViewed(product.getViewed() + 1);
            productBusinessService.create(product);
        });
    }

    @Override
    @Transactional
    public void deleteVisit(String locator, LocalDate date) {
        VisitEntity entity = visitorRepository.findByAisleAndDate(locator, date);
        if (entity != null) {
            visitorRepository.delete(entity);
        }
    }
}
