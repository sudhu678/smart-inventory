package com.one.smartinventory.rest;

import com.one.smartinventory.model.Image;
import com.one.smartinventory.service.VisionBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@Tag(name = "Vision Controller")
public class VisionController {

    private final VisionBusinessService visionBusinessService;


    @Autowired
    public VisionController(VisionBusinessService visionBusinessService) {
        this.visionBusinessService = visionBusinessService;
    }


    @PostMapping(value = "/uploadImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Scans image and processes through Google's Vision API")
    @SneakyThrows
    public ResponseEntity<HttpStatus> uploadImage(@RequestPart(value = "local image", required = false) MultipartFile remoteImage,
                                                  @RequestParam(name = "project id") String projectId,
                                                  @RequestParam(name = "location", defaultValue = "us-central1") String location,
                                                  @RequestParam(name = "model name", defaultValue = "gemini-1.0-pro-vision") String modelName,
                                                  @RequestParam(name = "aisle") String locator,
                                                  @RequestParam(name = "server image", required = false) Image serverImage) {

        byte[] imageBytes;
        if (remoteImage == null && serverImage == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        if (remoteImage == null) {
            imageBytes = Objects.requireNonNull(this.getClass().getResourceAsStream("/images/" +
                    serverImage.label)).readAllBytes();
        } else {
            imageBytes = remoteImage.getBytes();
        }

        visionBusinessService.scanImage(projectId, location, modelName, locator, imageBytes);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping(value = "/simulateVisionResponse")
    @Operation(summary = "Simulate Google's Vision API")
    @SneakyThrows
    public ResponseEntity<HttpStatus> simulateVisionResponse(@RequestParam(name = "aisle") String locator,
                                                             @RequestParam(name = "product id") long productId,
                                                             @RequestParam(name = "customer", defaultValue = "false") boolean customer) {
        visionBusinessService.scanImage(locator, productId, customer);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
