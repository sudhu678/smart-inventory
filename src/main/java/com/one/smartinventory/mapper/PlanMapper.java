package com.one.smartinventory.mapper;

import com.one.smartinventory.dao.entity.PlanEntity;
import com.one.smartinventory.model.Plan;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlanMapper {
    Plan to(PlanEntity entity);

    PlanEntity toEntity(Plan plan);
}
