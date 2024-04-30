package com.one.smartinventory.service.impl;

import com.one.smartinventory.dao.entity.TaskEntity;
import com.one.smartinventory.dao.repository.TaskRepository;
import com.one.smartinventory.mapper.TaskMapper;
import com.one.smartinventory.model.Task;
import com.one.smartinventory.service.TasksBusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskBusinessServiceImpl implements TasksBusinessService {

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;


    @Autowired
    public TaskBusinessServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllGenericTasks(String origin, String destination) {
        List<TaskEntity> entities;
        if (origin == null && destination == null) {
            entities = taskRepository.findAll();
        } else if (origin != null && destination != null) {
            entities = taskRepository.findByOriginAndDestination(origin, destination);
        } else if (origin != null) {
            entities = taskRepository.findByOrigin(origin);
        } else {
            entities = taskRepository.findByDestination(destination);
        }
        return entities.stream().filter(entity -> entity.getPlanDate() == null).map(taskMapper::to)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getAllPlanTasks(LocalDate date) {
        List<TaskEntity> entities = taskRepository.findAllByPlanDate(date);
        return entities.stream().map(taskMapper::to).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Task createPlanTask(Task task) {
        TaskEntity entity = taskMapper.toEntity(task);
        return save(entity);
    }

    @Override
    @Transactional
    public Task createGenericTask(long productId, String origin, String destination, long count) {
        TaskEntity entity = taskRepository.findByProductIdAndOriginAndDestination(productId, origin, destination);
        if (entity == null) {
            entity = new TaskEntity();
            entity.setProductId(productId);
            entity.setOrigin(origin);
            entity.setDestination(destination);
            entity.setCount(count);
        } else {
            entity.setCount(entity.getCount() + count);
        }
        return save(entity);
    }

    @Override
    public List<Task> getGenericTasks(long productId) {
        List<TaskEntity> entities = taskRepository.findAllByProductId(productId);
        return entities.stream().map(taskMapper::to).collect(Collectors.toList());
    }


    private Task save(TaskEntity entity) {
        entity = taskRepository.saveAndFlush(entity);
        return taskMapper.to(entity);
    }


    @Override
    @Transactional
    public Task completeTask(long id) {
        TaskEntity entity;
        entity = taskRepository.findById(id);
        if (entity == null) {
            log.warn("Task {} not found!", id);
            return null;
        }
        taskRepository.delete(entity);
        return taskMapper.to(entity);
    }

    @Override
    @Transactional
    public void deletePlanTasks() {
        taskRepository.deletePlanTasks();
    }
}
