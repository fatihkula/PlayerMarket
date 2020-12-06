# Player Market API
Player Market Demo Application

# Team Controller Service
Service responsible from CRUD operation of Team entity. 

# Player Controller Service
Service responsible for CRUD operation of Player entity and contract operation with fee calculation.

# Tests
Tests on service and controller level.

# Prerequisites: Java 1.8, Maven 3
For persistence layer I've used PostgreSQL (H2 for Test) and JPA with Hibernate. 
For unit testing I've used Junit and Mockito.

# Running the application

From terminal, the docker image should be run in docker/local folder with below command
 docker-compose up -d


# Postman collections
Postman collection included as a file named: Api Documentation.postman_collection.json 


# Swagger
After running the project, swagger link is: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
