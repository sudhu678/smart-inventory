package com.one.smartinventory.service.impl;

import com.google.common.collect.Sets;
import com.one.smartinventory.model.Product;
import com.one.smartinventory.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class VisionBusinessServiceImpl implements VisionBusinessService {

    private final ProductBusinessService productBusinessService;

    private final TasksBusinessService tasksBusinessService;

    private final VisitorBusinessService visitorBusinessService;

    private final GoogleCloudVisionService cloudVisionService;

    private final Set<String> people = Set.of("customers", "customer", "people", "man", "men", "woman", "women");


    @Autowired
    public VisionBusinessServiceImpl(ProductBusinessService productBusinessService,
                                     TasksBusinessService tasksBusinessService,
                                     VisitorBusinessService visitorBusinessService,
                                     GoogleCloudVisionService cloudVisionService) {
        this.productBusinessService = productBusinessService;
        this.tasksBusinessService = tasksBusinessService;
        this.visitorBusinessService = visitorBusinessService;
        this.cloudVisionService = cloudVisionService;

    }

    @Override
    public void scanImage(String locator, long productId, boolean customer) {
        if (customer) {
            visitorBusinessService.updateVisit(locator, LocalDate.now());
        }
        Product product = productBusinessService.get(productId);
        updateProduct(product, locator);
    }

    @Override
    public void scanImage(String projectId, String location, String modelName, String locator, byte[] bytes) {
        Product product = null;
        if (bytes.length > 0) {
            Set<String> descriptions = cloudVisionService.process(projectId, location, modelName, bytes);
            for (String name : descriptions) {
                product = getProduct(name);
                if (product != null) {
                    break;
                }
            }
            if (!Sets.intersection(descriptions, people).isEmpty()) {
                visitorBusinessService.updateVisit(locator, LocalDate.now());
            }
        }
        updateProduct(product, locator);
    }

    private void updateProduct(Product product, String locator) {
        if (product != null && !product.getAisle().equals(locator)) {
            product.setCount(product.getCount() - 1);
            productBusinessService.create(product);
            tasksBusinessService.createGenericTask(product.getProductId(), locator, product.getAisle(), 1);
        }
    }

    private Product getProduct(String name) {
        List<Product> products = productBusinessService.findMatch(name);
        if (products.isEmpty()) {
            return null;
        }
        return products.get(0);
    }
}
