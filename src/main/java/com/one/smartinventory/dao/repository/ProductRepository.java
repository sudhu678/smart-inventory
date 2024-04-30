package com.one.smartinventory.dao.repository;

import com.one.smartinventory.dao.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    ProductEntity find(long productId);

    List<ProductEntity> findMatch(String name);

    List<ProductEntity> findAllByName(String name);

    List<ProductEntity> findAllByAisle(String aisle);
}
