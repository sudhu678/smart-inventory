package com.one.smartinventory.rest;

import com.one.smartinventory.model.Product;
import com.one.smartinventory.service.ProductBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Product Controller")
public class ProductController {

    private final ProductBusinessService productBusinessService;


    @Autowired
    public ProductController(ProductBusinessService productBusinessService) {
        this.productBusinessService = productBusinessService;
    }

    @Operation(summary = "Get all products from the inventory system ordered by visitors")
    @GetMapping(value = "/products")
    public ResponseEntity<List<Product>> getAll(@RequestParam(name = "ascending", defaultValue = "true") boolean asc) {
        return ResponseEntity.ok(productBusinessService.getAll(asc));
    }


    @Operation(summary = "Get specific product for fulfillment")
    @GetMapping(value = "/productFulfillment")
    public ResponseEntity<List<Product>> get(@RequestParam(name = "name") String name,
                                             @RequestParam(name = "count", defaultValue = "1") long count) {
        return ResponseEntity.ok(productBusinessService.get(name, count));
    }


    @Operation(summary = "Get specified product from the inventory system")
    @GetMapping(value = "/product")
    public ResponseEntity<Product> get(@RequestParam(name = "productId") long productId) {
        Product product = productBusinessService.get(productId);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(product);
    }


    @Operation(summary = "Creates new product in the system")
    @PostMapping(value = "/product")
    public ResponseEntity<Product> create(@RequestBody Product product) {
        return ResponseEntity.ok(productBusinessService.create(product));
    }

    @Operation(summary = "Freeze product to exclude from plan generation")
    @PutMapping(value = "/product")
    public ResponseEntity<Product> freeze(@RequestParam(name = "productId") long productId,
                                          @RequestParam(name = "freeze") boolean freeze) {
        Product product = productBusinessService.freeze(productId, freeze);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(product);
    }


    @Operation(summary = "Deletes product within the system")
    @DeleteMapping(value = "/product")
    public ResponseEntity<HttpStatus> delete(@RequestParam(name = "productId") long productId) {
        productBusinessService.delete(productId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
