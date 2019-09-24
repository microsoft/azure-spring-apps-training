# 02 - Build a simple Spring Boot microservice

__This guide is part of the [Azure Spring Cloud training](../README.md)__

Build the simplest possible Spring Boot microservice, made with [https://start.spring.io/](https://start.spring.io/).

---

## Create a simple Spring Boot microservice

The microservice that we create in this guide is [available here](simple-microservice/).

To create our microservice, we will use [https://start.spring.io/](https://start.spring.io/) with the command line:

```
curl https://start.spring.io/starter.tgz -d dependencies=web -d baseDir=simple-microservice | tar -xzvf -
```

Go into the `simple-microservice` directory to view what has been generated:

```
cd simple-microservice
```

## Add a new Spring MVC Controller

Open the project with your favorite IDE, and next to the `DemoApplication` class, create a new class called `HelloController` with the following content:

```java
package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Azure Spring Cloud";
    }
}
```

The final project is available in the ["simple-microservice" folder](simple-microservice/).

## Test the project locally

Run the project:

```
./mvnw spring-boot:run
```

Requesting the `/hello` endpoint should return the "Hello from Azure Spring Cloud" message.

```
curl http://127.0.0.1:8080/hello
```

## Create and deploy the application on Azure Spring Cloud

In order to create the application graphically, you can use [the Azure portal](https://portal.azure.com/?WT.mc_id=azurespringcloud-github-judubois):

- Look for your Azure Spring Cloud cluster in your resource group
- Go to "App Management" and create an application
- Create a new application named "simple-microservice" that uses Java 11

![Create application](media/01-create-application.png)

You can also use the command line, which is easier:

```
az spring-cloud app create -n simple-microservice
```

You can now build your "simple-microservice" project and send it to Azure Spring Cloud:

```
./mvnw package
az spring-cloud app deploy -n simple-microservice --jar-path target/demo-0.0.1-SNAPSHOT.jar
```

## Test the project in the cloud

Go to [the Azure portal](https://portal.azure.com/?WT.mc_id=azurespringcloud-github-judubois):

- Look for your Azure Spring Cloud cluster in your resource group
- Go to "App Management" and select "simple-microservice"
- Copy/paste the "Test Endpoint" that is provided

You can now use cURL again to test the `/hello` endpoint, this time served by Azure Spring Cloud.

## Conclusion

Congratulations, you have deployed your first Spring Boot microservice to Azure Spring Cloud!

If you need to check your code, the final project is available in the ["simple-microservice" folder](simple-microservice/).

Here is the final script to build and deploy everything that was done in this guide:

```
curl https://start.spring.io/starter.tgz -d dependencies=web -d baseDir=simple-microservice | tar -xzvf -
cd simple-microservice
cat > HelloController.java << EOF
package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Azure Spring Cloud";
    }
}
EOF
mv HelloController.java src/main/java/com/example/demo/HelloController.java
az spring-cloud app create -n simple-microservice
./mvnw package
az spring-cloud app deploy -n simple-microservice --jar-path target/demo-0.0.1-SNAPSHOT.jar
```

---

⬅️ Previous guide: [01 - Create a cluster](../01-create-a-cluster/README.md)

➡️ Next guide: [03 - Configure application logs](../03-configure-application-logs/README.md)