package com.one.smartinventory.rest;

import com.one.smartinventory.model.Product;
import com.one.smartinventory.model.Task;
import com.one.smartinventory.service.ProductBusinessService;
import com.one.smartinventory.service.TasksBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Tag(name = "Task Controller")
public class TaskController {

    private final TasksBusinessService tasksBusinessService;

    private final ProductBusinessService productBusinessService;

    @Autowired
    public TaskController(TasksBusinessService tasksBusinessService, ProductBusinessService productBusinessService) {
        this.tasksBusinessService = tasksBusinessService;
        this.productBusinessService = productBusinessService;
    }

    @Operation(summary = "Get all generic tasks")
    @GetMapping(value = "/genericTasks")
    public ResponseEntity<List<Task>> getAllGenericTasks(@RequestParam(name = "origin aisle", required = false)
                                                         String origin, @RequestParam(name = "destination aisle",
            required = false) String destination) {
        return ResponseEntity.ok(tasksBusinessService.getAllGenericTasks(origin, destination));
    }

    @Operation(summary = "Get all plan tasks for today")
    @GetMapping(value = "/planTasks")
    public ResponseEntity<List<Task>> getAllPlanTasks() {
        return ResponseEntity.ok(tasksBusinessService.getAllPlanTasks(LocalDate.now()));
    }


    @Operation(summary = "Create generic Task")
    @PostMapping(value = "/task")
    public ResponseEntity<Task> create(@RequestParam(name = "product id") long productId,
                                       @RequestParam(name = "origin aisle") String origin,
                                       @RequestParam(name = "destination aisle") String destination,
                                       @RequestParam(name = "count") long count) {
        return ResponseEntity.ok(tasksBusinessService.createGenericTask(productId, origin, destination, count));

    }


    @Operation(summary = "Complete Task")
    @PutMapping(value = "/task")
    public ResponseEntity<Task> completeTask(@RequestParam(name = "id") long id) {
        Task task = tasksBusinessService.completeTask(id);
        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Product product = productBusinessService.get(task.getProductId());
        product.setCount(product.getCount() + task.getCount());
        product.setAisle(task.getDestination());
        productBusinessService.create(product);
        return ResponseEntity.ok(task);
    }
}
