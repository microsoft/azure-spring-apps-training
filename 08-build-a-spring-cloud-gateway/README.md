# 08 - Build a Spring Cloud Gateway

__This guide is part of the [Azure Container Apps training](../README.md)__

A Spring Cloud gateway allows you to selectively expose your microservices and to route traffic to them and among them. In this section, we will create a Spring Cloud Gateway that will expose the microservices we created in the preceding two sections.

---

## Create a Spring Cloud Gateway

The application that we create in this guide is [available here](gateway/).

To create our gateway, we will invoke the Spring Initalizr service from the command line:

```bash
curl https://start.spring.io/starter.tgz -d type=maven-project -d dependencies=cloud-gateway,cloud-eureka,cloud-config-client -d baseDir=gateway -d bootVersion=3.1.3 -d javaVersion=17 | tar -xzvf -
```

> We use the `Cloud Gateway`, `Eureka Discovery Client` and the `Config Client` components. Note: For Azure Container Apps, we configure routes directly instead of using service discovery.

## Configure the application

Rename `src/main/resources/application.properties` to `src/main/resources/application.yml` and add the following configuration:

```yaml
spring:
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true
      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOrigins: "*"
            allowedMethods:
              - GET

```

## Configure the gateway for Azure Container Apps

For Azure Container Apps, we need to configure the gateway to route to the internal URLs of our microservices. Update `src/main/resources/application.yml`:

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: city-service
          uri: http://city-service
          predicates:
            - Path=/city-service/**
          filters:
            - StripPrefix=1
        - id: weather-service
          uri: http://weather-service
          predicates:
            - Path=/weather-service/**
          filters:
            - StripPrefix=1
      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOrigins: "*"
            allowedMethods:
              - GET

```

Note: In Azure Container Apps, services within the same environment can communicate directly using the service name as the hostname (e.g., `http://city-service`).

## Create a Dockerfile

Create a `Dockerfile` in the `gateway` directory:

```bash
cd gateway
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

## Deploy the application

You can now build your "gateway" project and deploy it to Azure Container Apps with external ingress:

```bash
cd gateway
./mvnw clean package -DskipTests

az containerapp create \
    --name gateway \
    --resource-group $AZ_RESOURCE_GROUP \
    --environment $AZ_CONTAINERAPP_ENV \
    --source . \
    --target-port 8080 \
    --ingress external

cd ..
```

## Test the project in the cloud

Get the gateway URL:

```bash
GATEWAY_URL=$(az containerapp show \
    --name gateway \
    --resource-group $AZ_RESOURCE_GROUP \
    --query properties.configuration.ingress.fqdn \
    -o tsv)

echo "Gateway URL: https://$GATEWAY_URL"
```

You can now test the gateway routes to access the microservices:

- Test the `city-service` microservice endpoint: 
  ```bash
  curl https://$GATEWAY_URL/city-service/cities
  ```
- Test the `weather-service` microservice endpoint: 
  ```bash
  curl "https://$GATEWAY_URL/weather-service/weather/city?name=Paris%2C%20France"
  ```

If you need to check your code, the final project is available in the ["gateway" folder](gateway/).

---

⬅️ Previous guide: [07 - Build a Spring Boot microservice using MySQL](../07-build-a-spring-boot-microservice-using-mysql/README.md)

➡️ Next guide: [09 - Putting it all together, a complete microservice stack](../09-putting-it-all-together-a-complete-microservice-stack/README.md)
