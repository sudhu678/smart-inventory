package com.one.smartinventory.service.impl;

import com.one.smartinventory.dao.entity.ProductEntity;
import com.one.smartinventory.dao.repository.ProductRepository;
import com.one.smartinventory.mapper.ProductMapper;
import com.one.smartinventory.model.Product;
import com.one.smartinventory.model.Task;
import com.one.smartinventory.service.ProductBusinessService;
import com.one.smartinventory.service.TasksBusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductBusinessServiceImpl implements ProductBusinessService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final TasksBusinessService tasksBusinessService;

    private static final Comparator<ProductEntity> PRODUCT_ASC = Comparator.comparingLong(ProductEntity::getViewed);


    @Autowired
    public ProductBusinessServiceImpl(ProductRepository productRepository, ProductMapper productMapper,
                                      TasksBusinessService tasksBusinessService) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.tasksBusinessService = tasksBusinessService;
    }

    @Override
    public List<Product> getAll() {
        List<ProductEntity> entities = productRepository.findAll();
        return entities.stream().map(productMapper::to).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAll(boolean ascending) {
        List<ProductEntity> entities = productRepository.findAll();
        if (ascending) {
            entities.sort(PRODUCT_ASC);
        } else {
            entities.sort(PRODUCT_ASC.reversed());
        }
        return entities.stream().map(productMapper::to).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Product get(long productId) {
        ProductEntity entity = productRepository.find(productId);
        if (entity == null) {
            log.warn("Product with id {} not found!", productId);
            return null;
        }
        return productMapper.to(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> get(String name, long count) {
        List<ProductEntity> entities = productRepository.findMatch(name);
        if (!entities.isEmpty()) {
            ProductEntity base = entities.get(0);
            long productCount = 0;
            for (ProductEntity product : entities) {
                productCount += product.getCount();
            }
            if (productCount < count) {
                List<Task> tasks = tasksBusinessService.getGenericTasks(base.getProductId());
                for (Task task : tasks) {
                    ProductEntity entity = copyTaskToProduct(base, task);
                    entities.add(entity);
                    productCount += entity.getCount();
                    if (productCount >= count) {
                        break;
                    }
                }
            }
        }
        return entities.stream().map(productMapper::to).collect(Collectors.toList());
    }

    @Override
    public List<Product> findMatch(String name) {
        List<ProductEntity> entities = productRepository.findMatch(name);
        return entities.stream().map(productMapper::to).collect(Collectors.toList());
    }

    @Override
    public List<Product> get(String aisle) {
        List<ProductEntity> entities = productRepository.findAllByAisle(aisle);
        return entities.stream().map(productMapper::to).collect(Collectors.toList());
    }


    private ProductEntity copyTaskToProduct(ProductEntity base, Task task) {
        ProductEntity entity = productMapper.deepCopy(base);
        entity.setAisle(task.getOrigin());
        entity.setCount(task.getCount());
        return entity;
    }

    @Override
    @Transactional
    public Product create(Product product) {
        ProductEntity entity = productRepository.saveAndFlush(productMapper.toEntity(product));
        return productMapper.to(entity);
    }

    @Override
    @Transactional
    public Product freeze(long productId, boolean freeze) {
        ProductEntity entity = productRepository.find(productId);
        if (entity == null) {
            log.warn("Product with id {} not found!", productId);
            return null;
        }
        entity.setFreeze(freeze);
        entity = productRepository.saveAndFlush(entity);
        return productMapper.to(entity);
    }

    @Override
    @Transactional
    public void delete(long productId) {
        ProductEntity entity = productRepository.find(productId);
        if (entity != null) {
            productRepository.delete(entity);
        }
    }
}
