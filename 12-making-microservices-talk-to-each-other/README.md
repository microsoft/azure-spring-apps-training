# 12 - Making Microservices Talk to Each Other

__This guide is part of the [Azure Spring Apps training](../README.md)__

Creating a microservice that talks to other microservices.

---

In [Section 6](../06-build-a-reactive-spring-boot-microservice-using-cosmos/README.md) we deployed a microservice that returns a list of cities. In [Section 7](../07-build-a-spring-boot-microservice-using-mysql/README.md), we deployed a microservice that, given a city, returns the weather for that city. And in [Section 9](../09-putting-it-all-together-a-complete-microservice-stack/README.md), we created a front-end application that queries these two microservices.

There is a glaring inefficiency in this design: the browser first calls `city-service`, waits for it to respond, and upon getting that response, calls `weather-service` for each of the cities returned. All these remote calls are made over public internet, whose speed is never guaranteed.

To resolve this inefficiency, we will create a single microservice that implements the [Transaction Script](https://www.martinfowler.com/eaaCatalog/transactionScript.html) pattern: it will orchestrate the calls to individual microservices and return the weather for all cities. To do this, we will use [Spring Cloud OpenFeign](https://spring.io/projects/spring-cloud-openfeign). OpenFeign will automatically obtain the URLs of invoked microservices from Spring Cloud Registry, allowing us to build our `all-cities-weather-services` microservice without needing to resolve the locations of the constituent microservices.

Note how the code we create in this section is endpoint-agnostic. All we specify is the name of the services we want to invoke in the `@FeignClient` annotation. OpenFeign and the Azure Spring Apps discovery service then work together behind the scenes to connect our new microservice to the services we've created previously.

## Create a Spring Boot Microservice

To create our microservice, we will invoke the Spring Initializr service from the command line:

```bash
curl https://start.spring.io/starter.tgz -d type=maven-project -d dependencies=cloud-feign,web,cloud-eureka,cloud-config-client -d baseDir=all-cities-weather-service -d bootVersion=3.3.2 -d javaVersion=17 | tar -xzvf -
```

## Add Spring code to call other microservices

Next to the `DemoApplication` class, create a `Weather` record:

```java
package com.example.demo;

public record Weather (String city, String description, String icon ) {
}
```

Note: this corresponds to the `Weather` class that we created in Section 7 when we defined the original `weather-service` with two important differences: we no longer annotate the class as a JPA entity for data retrieval, and we define it as a Java record instead of a class. The differences between records and classes are many but in this particular case, they can be used interchangeably.

Next, in the same location create the `City` class. This is the same `City` class that we created in Section 6.

```java

package com.example.demo;

public record City (String name) {
}
```

Then, in the same location, create an interface called `CityServiceClient` with the following contents. When we run our new service, OpenFeign will automatically provide an implementation for this interface.

```java
package com.example.demo;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("city-service")
public interface CityServiceClient{

    @GetMapping("/cities")
    List<List<City>> getAllCities();
}

```

Create a similar OpenFeign client interface for the weather service, named `WeatherServiceClient`.

```java
package com.example.demo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient("weather-service")
public interface WeatherServiceClient {

    @GetMapping("/weather/city")
    Weather getWeatherForCity(@RequestParam("name") String cityName);
}

```

To enable Spring Cloud to discover the underlying services and to automatically generate OpenFeign clients, add the annotations @EnableDiscoveryClient and @EnableFeignClients to the `DemoApplication` class (as well as the corresponding `import` statements):

```java
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

Everything is now in place to implement the `all-cities-weather-service`. Create the class `AllCitiesWeatherController` as follows:

```java

package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class AllCitiesWeatherController {

    private CityServiceClient cityServiceClient;
    private WeatherServiceClient weatherServiceClient;

    public AllCitiesWeatherController(CityServiceClient cityServiceClient, WeatherServiceClient weatherServiceClient) {
        this.cityServiceClient = cityServiceClient;
        this.weatherServiceClient = weatherServiceClient;
    }

    @GetMapping("/")
    public List<Weather> getAllCitiesWeather() {
        Stream<City> allCities = cityServiceClient.getAllCities().stream().flatMap(Collection::stream);

        //Obtain weather for all cities in parallel
        return allCities.parallel()
                .peek(city -> System.out.println("City: >>" + city.name() + "<<"))
                .map(city -> weatherServiceClient.getWeatherForCity(city.name()))
                .collect(Collectors.toList());

    }
}
```
## Add time-out settings

In order to stop the Feign services timing out automatically, open up the `src/main/resources/application.properties` file and add:

```properties
spring.cloud.openfeign.client.config.default.connectTimeout=160000000
spring.cloud.openfeign.client.config.default.readTimeout=160000000
```

## Create the application on Azure Spring Apps

As before, create a specific `all-cities-weather-service` application in your Azure Spring Apps instance:

```bash
az spring app create -n all-cities-weather-service --runtime-version Java_17
```

## Deploy the application

You can now build your "all-cities-weather-service" project and send it to Azure Spring Apps:

```bash
cd all-cities-weather-service
./mvnw clean package -DskipTests
az spring app deploy -n all-cities-weather-service --artifact-path target/demo-0.0.1-SNAPSHOT.jar
cd ..
```

## Test the project in the cloud

You can use the gateway created in Section 8 to access the all-cities-weather-service directly.

```bash
https://<Your gateway URL>/ALL-CITIES-WEATHER-SERVICE
```

You should get the JSON output with the weather for all the cities:

```json
[{"city":"Paris, France","description":"It's always sunny on Azure Spring Apps","icon":"weather-sunny"},
{"city":"London, UK","description":"It's always sunny on Azure Spring Apps","icon":"weather-sunny"}]
```

---

⬅️ Previous guide: [11 - Configure CI/CD](../11-configure-ci-cd/README.md)

➡️ Next guide: [Conclusion](../99-conclusion/README.md)