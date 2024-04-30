package com.one.smartinventory.mapper;

import com.one.smartinventory.dao.entity.ProductEntity;
import com.one.smartinventory.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.control.DeepClone;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product to(ProductEntity entity);

    ProductEntity toEntity(Product product);


    ProductEntity deepCopy(ProductEntity entity);
}
