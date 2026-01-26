# 02 - Build a simple Spring Boot microservice

__This guide is part of the [Azure Container Apps training](../README.md)__

In this section, we'll build a simple Spring boot microservice and deploy it to Azure Container Apps. This will give us a starting point for adding Spring Cloud technologies in later sections.

---

## Create a simple Spring Boot microservice

The microservice that we create in this guide is [available here](simple-microservice/).

A typical way to create Spring Boot applications is to use the Spring Initializr at  [https://start.spring.io/](https://start.spring.io/). Feel free to explore it outside this training. **For the purposes of this training, we will only invoke the Spring Initializr site via the `curl` command**.

>💡 __Note:__ All subsequent commands in this workshop should be run from the same directory, except where otherwise indicated via `cd` commands.

In an __empty__ directory execute the curl command line below:

```bash
curl https://start.spring.io/starter.tgz -d type=maven-project -d dependencies=web -d baseDir=simple-microservice -d bootVersion=3.1.3 -d javaVersion=17 | tar -xzvf -
```

> We force the Spring Boot version to be 3.1.3, and keep default settings that use the `com.example.demo` package.

## Add a new Spring MVC Controller

In the `simple-microservice/src/main/java/com/example/demo` directory, create a
new file  called `HelloController.java` next to `DemoApplication.java` file with
the following content:

```java
package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Azure Container Apps\n";
    }
}
```

The final project is available in the ["simple-microservice" folder](simple-microservice/).

## Test the project locally

Run the project:

```bash
cd simple-microservice
./mvnw spring-boot:run &
cd ..
```

Requesting the `/hello` endpoint should return the "Hello from Azure Container Apps" message.

```bash
curl http://127.0.0.1:8080/hello
```

Finally, kill running app:

```bash
kill %1
```

## Create and deploy the application on Azure Container Apps

This section shows how to build a container image and deploy it to Azure Container Apps.

First, we need to build the application and create a container image. We'll use Docker to build the container image.

Create a `Dockerfile` in the `simple-microservice` directory:

```bash
cd simple-microservice
cat > Dockerfile << 'EOF'
FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.example.demo.DemoApplication"]
EOF
cd ..
```

Now build your "simple-microservice" project and deploy it to Azure Container Apps:

```bash
cd simple-microservice
./mvnw clean package

# Build and deploy directly to Azure Container Apps
az containerapp up \
    --name simple-microservice \
    --resource-group $AZ_RESOURCE_GROUP \
    --environment $AZ_CONTAINERAPP_ENV \
    --source . \
    --target-port 8080 \
    --ingress external

cd ..
```

This command builds your application from source, creates a container image, and deploys it to Azure Container Apps with an external ingress endpoint.

## Test the project in the cloud

Go to [the Azure portal](https://portal.azure.com/?WT.mc_id=azurespringcloud-github-judubois):

- Look for your Azure Container Apps environment in your resource group
- Click on your container app "simple-microservice"
- Find the "Application Url" in the "Essentials" section.

You can also get the URL using the Azure CLI:

```bash
az containerapp show \
    --name simple-microservice \
    --resource-group $AZ_RESOURCE_GROUP \
    --query properties.configuration.ingress.fqdn \
    -o tsv
```

You can now use cURL to test the `/hello` endpoint:

```bash
SIMPLE_MICROSERVICE_URL=$(az containerapp show \
    --name simple-microservice \
    --resource-group $AZ_RESOURCE_GROUP \
    --query properties.configuration.ingress.fqdn \
    -o tsv)

curl https://$SIMPLE_MICROSERVICE_URL/hello
```

If successful, you should see the message: `Hello from Azure Container Apps`.

## Conclusion

Congratulations, you have deployed your first Spring Boot microservice to Azure Container Apps!

If you need to check your code, the final project is available in the ["simple-microservice" folder](simple-microservice/).

Here is the final script to build and deploy everything that was done in this guide:

```bash
curl https://start.spring.io/starter.tgz -d type=maven-project -d dependencies=web -d baseDir=simple-microservice -d bootVersion=3.1.3 -d javaVersion=17 | tar -xzvf -
cd simple-microservice
cat > HelloController.java << EOF
package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Azure Container Apps";
    }
}
EOF
mv HelloController.java src/main/java/com/example/demo/HelloController.java

cat > Dockerfile << 'EOF'
FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.example.demo.DemoApplication"]
EOF

./mvnw clean package
az containerapp up \
    --name simple-microservice \
    --resource-group $AZ_RESOURCE_GROUP \
    --environment $AZ_CONTAINERAPP_ENV \
    --source . \
    --target-port 8080 \
    --ingress external
cd ..
```

---

⬅️ Previous guide: [01 - Create an Azure Container Apps environment](../01-create-an-azure-spring-cloud-instance/README.md)

➡️ Next guide: [03 - Configure monitoring](../03-configure-monitoring/README.md)
