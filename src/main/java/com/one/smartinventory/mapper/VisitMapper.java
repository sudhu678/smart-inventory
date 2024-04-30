package com.one.smartinventory.mapper;

import com.one.smartinventory.dao.entity.VisitEntity;
import com.one.smartinventory.model.Visit;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VisitMapper {
    Visit to(VisitEntity entity);

    VisitEntity toEntity(Visit visit);
}
