# user-service
User Service is a microservice for managing user registration, authentication and user profile management.

## Getting Started
It a spring boot project with Maven.
Clone the github Repo and import in IDE of your choice. \
**Notes:**
- If you have the zipped project, just unzip it and import in any IDE

## Built With
* [Java 17](https://openjdk.org/projects/jdk/17/)
* [Maven](https://maven.apache.org/)
* [Spring boot 3.1.5](https://spring.io/projects/spring-boot)
* [Spring security](https://spring.io/projects/spring-security)
* [Redis](https://redis.io)
* [Swagger](https://swagger.io/)
* [Docker](https://docs.docker.com/)
* [RabbitMQ](https://www.rabbitmq.com/)
* [Mysql](https://www.mysql.com/)

### Prerequisites
- Java 17
- Maven 3.6.3
- Docker Desktop 24.X.X (If you plan to deploy and test in local)

### Installing
- Install java 17 \
  You can use [opendjdk 17](https://download.java.net/openjdk/jdk17/ri/openjdk-17+35_windows-x64_bin.zip) and configure your PATH to use this version
- Install Maven \
  Visit [this link](https://maven.apache.org/install.html) in order to install maven in your local.
- Install Docker Desktop \
  Visit [this link](https://docs.docker.com/desktop/install/mac-install/). If you are a windows desktop user refer [this](https://docs.docker.com/desktop/install/windows-install/)
  
#### Exposed REST apis
Here below are the exposed REST Apis:

* Register a new user 
* Login with username & password
* Create a new user
* Retrieve a single user with id
* Retrieve the list of all the existing users
* Update user data with id
* Delete a user with id
* Validate accessToken API to get validated UserDetails
* Standard validation for email, password

## REST apis details
Using a browser it's possible to interact with the REST apis exposed by this service with Swagger:

http://localhost:9011/swagger-ui.html

![Swagger](https://github.com/nrpndr/user-service/blob/main/swagger-ui.png "Swagger interface")

Another alternative is to use Postman (https://www.postman.com/).

This project contains also the [Postman export file](https://github.com/nrpndr/user-service/blob/main/UserService.postman_collection.json) with all the configured test calls:

![Postman](https://github.com/nrpndr/user-service/blob/main/postman-ui.png "Postman Collection")

### Running the project
- To run the program, execute below commands in terminal at root level
    
    ```
    mvn clean install
    java -jar target/user-service.jar
    ```
- Prerequisite to the above is that you need a have a local installation of mysqldb, redis server and rabbitMQ.
- If you want to avoid all that hassle, simply do the following(You need to have docker desktop installed for this)
	
    ```
    mvn clean install
    docker compose up
    ```


## Acknowledgments
- [Baeldung](https://www.baeldung.com)
- [StackoverFlow](https://stackoverflow.com/)
- [Mkyong](https://mkyong.com/)
- [spring.io](https://spring.io/)
- [Docker](https://docs.docker.com/)
- [Kubernetes](https://kubernetes.io/)
- [Redis](https://redis.io/)
- [Springdoc](https://springdoc.org/)
- [Swagger](https://swagger.io/)
- [RabbitMQ](https://www.rabbitmq.com/)
