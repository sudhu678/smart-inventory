package com.one.smartinventory;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.ai.autoconfigure.vertexai.gemini.VertexAiGeminiAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(exclude = VertexAiGeminiAutoConfiguration.class)
@OpenAPIDefinition(info=@Info(title="Smart Inventory System"))
public class SmartInventoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartInventoryApplication.class, args);
    }

}
