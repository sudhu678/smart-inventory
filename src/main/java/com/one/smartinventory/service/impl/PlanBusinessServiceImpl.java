package com.one.smartinventory.service.impl;

import com.one.smartinventory.dao.entity.PlanEntity;
import com.one.smartinventory.dao.repository.PlanRepository;
import com.one.smartinventory.mapper.PlanMapper;
import com.one.smartinventory.model.*;
import com.one.smartinventory.service.AreaBusinessService;
import com.one.smartinventory.service.PlanBusinessService;
import com.one.smartinventory.service.ProductBusinessService;
import com.one.smartinventory.service.TasksBusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PlanBusinessServiceImpl implements PlanBusinessService {

    private final PlanRepository planRepository;

    private final PlanMapper planMapper;

    private final AreaBusinessService areaBusinessService;

    private final ProductBusinessService productBusinessService;

    private final TasksBusinessService tasksBusinessService;

    private static final Comparator<Area> AREA_ASC = Comparator.comparingLong(Area::getViewed);

    private static final Comparator<Product> STOCK_ASC = Comparator.comparingLong(Product::getCount);

    private static final Comparator<Product> EXPIRY_ASC = Comparator.comparing(Product::getExpiry);

    private static final Comparator<Product> VIEW_ASC = Comparator.comparing(Product::getViewed);


    @Autowired
    public PlanBusinessServiceImpl(PlanRepository planRepository, PlanMapper planMapper,
                                   AreaBusinessService areaBusinessService,
                                   ProductBusinessService productBusinessService,
                                   TasksBusinessService tasksBusinessService) {
        this.planRepository = planRepository;
        this.planMapper = planMapper;
        this.areaBusinessService = areaBusinessService;
        this.productBusinessService = productBusinessService;
        this.tasksBusinessService = tasksBusinessService;
    }

    @Override
    @Transactional(readOnly = true)
    public Plan getLatest() {
        LocalDate date = LocalDate.now();
        return get(date);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Plan> getAll() {
        List<PlanEntity> entities = planRepository.findAll();
        return entities.stream().map(planMapper::to).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Plan get(LocalDate date) {
        PlanEntity entity = planRepository.find(date);
        if (entity == null) {
            log.warn("Plan for today {} not exist!", date);
            return null;
        }
        return planMapper.to(entity);
    }

    @Override
    @Transactional
    public Plan generate(PlanOption planOption) {
        Plan plan = null;
        List<Area> areas = areaBusinessService.getAreas().stream()
                .filter(area -> !area.isFreeze()).collect(Collectors.toList());
        List<Product> products = productBusinessService.getAll().stream()
                .filter(product -> !product.isFreeze()).collect(Collectors.toList());
        if (!areas.isEmpty() && !products.isEmpty()) {
            areas.sort(AREA_ASC.reversed());
            if (planOption == PlanOption.Product_Stock) {
                products.sort(STOCK_ASC.reversed());
            } else if (planOption == PlanOption.Product_Expiry) {
                products.sort(EXPIRY_ASC);
            } else if (planOption == PlanOption.Product_View) {
                products.sort(VIEW_ASC);
            }
            tasksBusinessService.deletePlanTasks();
            plan = generatePlan(areas, products);
            PlanEntity entity = planRepository.saveAndFlush(planMapper.toEntity(plan));
            plan = planMapper.to(entity);
        }
        return plan;
    }


    private Plan generatePlan(List<Area> areas, List<Product> products) {
        Plan plan = new Plan();
        plan.setPlanDate(LocalDate.now());
        for (Product product : products) {
            for (Area area : areas) {
                long counter = 0;
                while (counter < product.getCount()) {
                    if (area.isAllowed(product)) {
                        area.placeProduct(product);
                        counter++;
                    } else {
                        break;
                    }
                }
                if (counter > 0 && !product.getAisle().equals(area.getAisle())) {
                    Task task = createTask(plan, product, area, counter);
                    task = tasksBusinessService.createPlanTask(task);
                    plan.getTasks().add(task);
                }
                product.setCount(product.getCount() - counter);
                if (product.getCount() <= 0) {
                    break;
                }
            }
        }
        return plan;
    }

    private Task createTask(Plan plan, Product product, Area area, long count) {
        Task task = new Task();
        task.setPlanDate(plan.getPlanDate());
        task.setProductId(product.getProductId());
        task.setOrigin(product.getAisle());
        task.setDestination(area.getAisle());
        task.setCount(count);
        return task;
    }

    @Override
    @Transactional
    public void delete(LocalDate date) {
        PlanEntity entity = planRepository.find(date);
        if (entity != null) {
            planRepository.delete(entity);
        }
    }
}
