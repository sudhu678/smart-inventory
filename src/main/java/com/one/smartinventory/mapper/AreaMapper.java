package com.one.smartinventory.mapper;

import com.one.smartinventory.dao.entity.AreaEntity;
import com.one.smartinventory.model.Area;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AreaMapper {
    Area to(AreaEntity entity);

    AreaEntity toEntity(Area area);
}
