package com.one.smartinventory.rest;

import com.one.smartinventory.model.Plan;
import com.one.smartinventory.model.PlanOption;
import com.one.smartinventory.service.PlanBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@Tag(name = "Plan Controller")
public class PlanController {

    private static final String PLAN_DATE = "plan date";

    @Autowired
    public PlanController(PlanBusinessService planBusinessService) {
        this.planBusinessService = planBusinessService;
    }

    private final PlanBusinessService planBusinessService;

    @Operation(summary = "Get latest plan")
    @GetMapping(value = "/latestPlan")
    public ResponseEntity<Plan> getLatest() {
        return ResponseEntity.ok(planBusinessService.getLatest());
    }

    @Operation(summary = "Get specified plan")
    @GetMapping(value = "/plan")
    public ResponseEntity<Plan> get(@RequestParam(name = PLAN_DATE) LocalDate date) {
        Plan plan = planBusinessService.get(date);
        if (plan == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(plan);
    }


    @Operation(summary = "Generate new plan")
    @PutMapping(value = "/plan")
    public ResponseEntity<Plan> generate(@RequestParam(name = PLAN_DATE) PlanOption planOption) {
        Plan plan = planBusinessService.generate(planOption);
        return ResponseEntity.ok(plan);
    }

    @Operation(summary = "Deletes latest plan")
    @DeleteMapping(value = "/latestPlan")
    public ResponseEntity<HttpStatus> deleteLatest() {
        planBusinessService.delete(LocalDate.now());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Deletes specified plan")
    @DeleteMapping(value = "/plan")
    public ResponseEntity<HttpStatus> delete(@RequestParam(PLAN_DATE) LocalDate date) {
        planBusinessService.delete(date);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
