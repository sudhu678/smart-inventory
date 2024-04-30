package com.one.smartinventory.service;

import com.one.smartinventory.model.Task;

import java.time.LocalDate;
import java.util.List;

public interface TasksBusinessService {

    List<Task> getAllGenericTasks(String origin, String destination);

    List<Task> getAllPlanTasks(LocalDate date);

    Task createPlanTask(Task task);

    Task createGenericTask(long productId, String origin, String destination, long count);

    List<Task> getGenericTasks(long productId);

    Task completeTask(long id);

    void deletePlanTasks();


}
