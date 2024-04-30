package com.one.smartinventory.rest;

import com.one.smartinventory.model.Visit;
import com.one.smartinventory.service.VisitorBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Tag(name = "Visitor Controller")
public class VisitorController {

    private final VisitorBusinessService visitorBusinessService;

    @Autowired
    public VisitorController(VisitorBusinessService visitorBusinessService) {
        this.visitorBusinessService = visitorBusinessService;
    }

    private LocalDate getDate(LocalDate date) {
        return date != null ? date : LocalDate.now();
    }

    @Operation(summary = "Get all visits ordered by visited count")
    @GetMapping(value = "/visitors")
    public ResponseEntity<List<Visit>> get(@RequestParam(name = "limit", defaultValue = "5") long limit,
                                           @RequestParam(name = "date", required = false) LocalDate date) {
        return ResponseEntity.ok(visitorBusinessService.getAll(getDate(date), limit));
    }

    @Operation(summary = "Get all visits for specified aisle and date")
    @GetMapping(value = "/visitByAisle")
    public ResponseEntity<Visit> get(@RequestParam(name = "aisle") String locator,
                                     @RequestParam(name = "date", required = false) LocalDate date) {
        return ResponseEntity.ok(visitorBusinessService.getVisit(locator, getDate(date)));
    }


    @Operation(summary = "Create/update visit for specified aisle and date")
    @PostMapping(value = "/visitors")
    public ResponseEntity<Visit> update(@RequestParam(name = "aisle") String locator,
                                        @RequestParam(name = "date", required = false) LocalDate date) {
        return ResponseEntity.ok(visitorBusinessService.updateVisit(locator, getDate(date)));
    }


    @Operation(summary = "Delete all visits for specified aisle and date")
    @DeleteMapping(value = "/visitors")
    public ResponseEntity<HttpStatus> delete(@RequestParam(name = "aisle") String locator,
                                             @RequestParam(name = "date", required = false) LocalDate date) {
        visitorBusinessService.deleteVisit(locator, getDate(date));
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
