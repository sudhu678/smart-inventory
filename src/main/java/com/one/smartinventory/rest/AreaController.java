package com.one.smartinventory.rest;

import com.one.smartinventory.model.Area;
import com.one.smartinventory.model.Product;
import com.one.smartinventory.service.AreaBusinessService;
import com.one.smartinventory.service.ProductBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Area Controller")
public class AreaController {

    private final AreaBusinessService areaBusinessService;

    private final ProductBusinessService productBusinessService;


    @Autowired
    public AreaController(AreaBusinessService areaBusinessService, ProductBusinessService productBusinessService) {
        this.areaBusinessService = areaBusinessService;
        this.productBusinessService = productBusinessService;
    }

    @Operation(summary = "Retrieves all the areas within the System.")
    @GetMapping(value = "/areas")
    public ResponseEntity<List<Area>> getAll() {
        return ResponseEntity.ok(areaBusinessService.getAreas());
    }

    @Operation(summary = "Retrieves specified area from the System.")
    @GetMapping(value = "/area")
    public ResponseEntity<Area> get(@RequestParam(name = "aisle") String locator) {
        Area area = areaBusinessService.getArea(locator);
        if (area == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(area);
    }

    @Operation(summary = "Creates new area in the System.")
    @PostMapping(value = "/area")
    public ResponseEntity<Area> create(@RequestBody Area area) {
        return ResponseEntity.ok(areaBusinessService.create(area));
    }


    @Operation(summary = "Freeze area to exclude from plan generation")
    @PutMapping(value = "/area")
    public ResponseEntity<Area> freeze(@RequestParam(name = "aisle") String locator,
                                       @RequestParam(name = "freeze") boolean freeze) {
        Area area = areaBusinessService.freeze(locator, freeze);
        if (area == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<Product> products = productBusinessService.get(area.getAisle());
        products.forEach(product -> productBusinessService.freeze(product.getProductId(), freeze));
        return ResponseEntity.ok(area);
    }


    @Operation(summary = "Deletes an area within the system")
    @DeleteMapping(value = "/area")
    public ResponseEntity<HttpStatus> delete(@RequestParam(name = "aisle") String locator) {
        areaBusinessService.delete(locator);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
