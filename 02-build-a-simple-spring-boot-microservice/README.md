# 02 - Build a simple Spring Boot microservice

__This guide is part of the [Azure Spring Cloud training](../README.md)__

Build the simplest possible Spring Boot microservice, made with [https://start.spring.io/](https://start.spring.io/).

## Create a simple Spring Boot microservice

The microservice that we create in this guide is [available here](simple-microservice/).

Create a new working directory and use it:

```
mkdir simple-microservice && cd simple-microservice
```

To create your microservice, we will use [https://start.spring.io/](https://start.spring.io/) with the command line:

```
curl https://start.spring.io/starter.tgz -d dependencies=web \ -d baseDir=simple-microservice | tar -xzvf -
```

We recommend that you use Git to work on that project:

```
git init
git add .
git commit -m 'Initial commit'
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

## Test the project locally

Run the project:

```
./mvnw spring-boot:run
```

Requestion the `/hello` endpoint should return the "Hello from Azure Spring Cloud" message.

```
curl http://127.0.0.1:8080/hello
```

## Create and deploy an application on Azure Spring Cloud

In order to create the application graphically, you can use [the Azure portal](https://portal.azure.com/?WT.mc_id=azurespringcloud-github-judubois):

- Look for your Azure Spring Cloud cluster in your resource group
- Go to "App Management" and create an application

You can also use the command line, which is easier:

```
az spring-cloud app create -n simple-microservice
```

You can now build your "simple-microservice" project and send it to Azure Spring Cloud:

```
./mvnw package
az spring-cloud app deploy -n simple-microservice --jar-path target/demo-0.0.1-SNAPSHOT.jar
```

