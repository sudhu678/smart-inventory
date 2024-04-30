# Getting Started

### Smart Inventory System

The inventory system include interface to receive feed from high precision camera installed at designated aisles in the warehouse, stores, businesses that manage large inventory facility. The scanned images will be classified using Vision AI powered by Google's Gemini Pro Vision model. This makes inventory system smart with ability to keep track of misplaced products. Automatic task will be created when product/item is misplaced by employee or customer. Now employees will have better visibility of all misplaced products and can take quick actions at their convenience. The online order fulfillment can also be done quickly since system knows real time position of misplaced products that is very crucial especially when stock is limited.

The stores commonly re-shuffle their products to get customer attractions and boost their sales. The system keep track of hot spots and provides ability to plan product re-shuffling by product stock, expiry, and least viewed products. The plan creates automatic tasks for employees to execute.

### Local Development Setup

#### Pre-requisites

* Java 17
* Google Cloud Project Setup (Access Vision AI)
* Application Default Credentials (Authentication for Google Cloud)

#### Project Build
./gradlew build or IntelliJ/Eclipse project build

#### Application Start
Run SmartInventoryApplication (Spring-boot) through IDE or java -jar smart-inventory-0.0.1-SNAPSHOT.jar

on browser hit http://localhost:8080/swagger-ui/index.html#/

### Important Rest APIs

* Product Controller: Provides endpoints to create, access product information and online order fulfillment
![image](https://github.com/sudhu678/smart-inventory/assets/28265483/f9a61176-7d6b-4c82-9b52-e1be01b8ce62)

* Area Controller: Provides endpoints to create, access inventory areas, hot spots, freeze area to exclude from the reshuffling plan
![image](https://github.com/sudhu678/smart-inventory/assets/28265483/340d8904-9120-4f6b-b7f6-b5e7fb1b9f7a)

* Vision Controller: Provides endpoints to upload image for designated aisle and trigger Vision AI
![image](https://github.com/sudhu678/smart-inventory/assets/28265483/b67e90b7-4190-4bfc-a599-afe6871a0985)


### Reference Documentation

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.5/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.2.5/gradle-plugin/reference/html/#build-image)
* [Vertex AI Gemini](https://docs.spring.io/spring-ai/reference/api/clients/vertexai-gemini-chat.html)
* [Open NLP](https://manual.openlp.org/)

### Additional Links

These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

