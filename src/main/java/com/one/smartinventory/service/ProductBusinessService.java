package com.one.smartinventory.service;

import com.one.smartinventory.model.Product;

import java.util.List;

public interface ProductBusinessService {

    List<Product> getAll();

    List<Product> getAll(boolean ascending);

    Product get(long productId);

    List<Product> get(String name, long count);

    List<Product> findMatch(String name);

    Product findBestMatchProduct(String name);

    List<Product> get(String locator);

    Product create(Product product);

    Product freeze(long productId, boolean freeze);

    void delete(long productId);


}
