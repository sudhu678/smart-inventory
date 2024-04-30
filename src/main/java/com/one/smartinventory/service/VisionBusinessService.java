package com.one.smartinventory.service;

public interface VisionBusinessService {

    void scanImage(String locator, long productId, boolean customer);

    void scanImage(String projectId, String location, String modelName, String locator, byte[] bytes);

}
