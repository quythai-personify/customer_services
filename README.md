Customer service
===============

The project for learning Java Spring Boot

## Overview:

![Architecture](/misc/service-arch-draw/customer-service-arch.drawio.png)

- customer: manage customer database via RestAPI
- eureka-server: provide network discovery
- fraud: check if customer is fraudster
- kafka-producer: communicate with Kafka

## How to run:
* Build and debug locally (require PostgresQL to be installed and run at port 5432)
* Or run in docker by compose yaml file in root folder

## How to test:
* Postman collections for testing the APIs are located in `customer_services\misc\postman collection`