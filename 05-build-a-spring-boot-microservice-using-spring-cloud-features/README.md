# 05 - Build a Spring Boot microservice using Spring Cloud features

__This guide is part of the [Azure Spring Cloud training](../README.md)__

Build a Spring Boot microservice that is cloud-enabled: it uses a discovery server ([Eureka](https://github.com/Netflix/eureka)) and a [Spring Cloud Config Server](https://cloud.spring.io/spring-cloud-config) which are both managed and supported by Azure Spring Cloud.

---

## What we are going to build

This guide builds upon the previous guides: we are going to build again a simple Spring Boot microservice like in [02 - Build a simple Spring Boot microservice](../02-build-a-simple-spring-boot-microservice/README.md), but this time it will use two major Spring Cloud features:

- It will be connected to a [Eureka server](https://github.com/Netflix/eureka) so it can discover other microservices, as well as being discovered itself!
- It will get its configuration from the Spring Cloud Config server that we configured in the previous guide, [04 - Configure a Spring Cloud Config server](../04-configure-a-spring-cloud-config-server/README.md)

For both features, it will just be a matter of adding an official Spring Boot starter, and Azure Spring Cloud will take care of everything else.

## Create a simple Spring Cloud microservice

The microservice that we create in this guide is [available here](spring-cloud-microservice/).

To create our microservice, we will use [https://start.spring.io/](https://start.spring.io/) with the command line:

```
curl https://start.spring.io/starter.tgz -d dependencies=web,cloud-eureka,cloud-config-client -d baseDir=spring-cloud-microservice | tar -xzvf -
```

> This time, we add the `Eureka Discovery Client` and the `Config Client` Spring Boot starters, which will respectively automatically trigger the use of Eureka and the Spring Cloud Config Server.

## Add a new Spring MVC Controller

Open the project with your favorite IDE, and next to the `DemoApplication` class, create a new class called `HelloController` with the following content:

```java
package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Value("${application.message:Not configured by a Spring Cloud Server}")
    private String message;

    @GetMapping("/hello")
    public String hello() {
        return message;
    }
}
```

## Test the project locally

Run the project:

```
./mvnw spring-boot:run
```

Requesting the `/hello` endpoint should return the "Not configured by a Spring Cloud Server" message.

```
curl http://127.0.0.1:8080/hello
```

## Create and deploy the application on Azure Spring Cloud

As in [02 - Build a simple Spring Boot microservice](../02-build-a-simple-spring-boot-microservice/README.md), create a specific `spring-cloud-microservice` application in your Azure Spring Cloud cluster:

```
az spring-cloud app create -n spring-cloud-microservice
```

You can now build your "spring-cloud-microservice" project and send it to Azure Spring Cloud:

```
./mvnw package
az spring-cloud app deploy -n spring-cloud-microservice --jar-path target/demo-0.0.1-SNAPSHOT.jar
```

## Test the project in the cloud

Go to [the Azure portal](https://portal.azure.com/?WT.mc_id=azurespringcloud-github-judubois):

- Look for your Azure Spring Cloud cluster in your resource group
- Go to "App Management"
  - Verify that `spring-cloud-microservice` has a `Discovery status` which says `UP(1),DOWN(0)`. This shows that it is correctly registered in Eureka
  - Select `spring-cloud-microservice` to have more information on the microservice
- Copy/paste the "Test Endpoint" that is provided

You can now use cURL again to test the `/hello` endpoint, this time it is served by Azure Spring Cloud and configured using the Spring Config Server from [04 - Configure a Spring Cloud Config server](../04-configure-a-spring-cloud-config-server/README.md).

As a result, requesting the `/hello` endpoint should return the message that we configured in the `application.yml` file, coming from the Spring Cloud Config Server:

```
Configured by Azure Spring Cloud
```

## Conclusion

Congratulations, you have deployed a complete Spring Cloud microservice, using Eureka and Spring Cloud Config Server!

If you need to check your code, the final project is available in the ["spring-cloud-microservice" folder](spring-cloud-microservice/).

Here is the final script to build and deploy everything that was done in this guide:

```
curl https://start.spring.io/starter.tgz -d dependencies=web,cloud-eureka,cloud-config-client -d baseDir=spring-cloud-microservice | tar -xzvf -
cd spring-cloud-microservice
cat > HelloController.java << EOF
package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Value("${application.message:Not configured by a Spring Cloud Server}")
    private String message;

    @GetMapping("/hello")
    public String hello() {
        return message;
    }
}
EOF
mv HelloController.java src/main/java/com/example/demo/HelloController.java
az spring-cloud app create -n spring-cloud-microservice
./mvnw package
az spring-cloud app deploy -n spring-cloud-microservice --jar-path target/demo-0.0.1-SNAPSHOT.jar
```

---

⬅️ Previous guide: [04 - Configure a Spring Cloud Config server](../04-configure-a-spring-cloud-config-server/README.md)

➡️ Next guide: [06 - Build a reactive Spring Boot microservice using CosmosDB](../06-build-a-reactive-spring-boot-microservice-using-cosmosdb/README.md)