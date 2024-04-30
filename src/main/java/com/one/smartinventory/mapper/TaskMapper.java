package com.one.smartinventory.mapper;

import com.one.smartinventory.dao.entity.TaskEntity;
import com.one.smartinventory.model.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task to(TaskEntity entity);

    TaskEntity toEntity(Task task);
}
