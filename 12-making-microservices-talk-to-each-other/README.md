# 12 - Making Microservices Talk to Each Other

__This guide is part of the [Azure Spring Cloud training](../README.md)__

Creating a microservice that talks to other microservices.

---

In [Section 6](../06-build-a-reactive-spring-boot-microservice-using-cosmos/README.md) we deployed a microservice that returns a list of cities. In [Section 7](../07-build-a-spring-boot-microservice-using-mysql/README.md), we deployed a microservice that, given a city, returns the weather for that city. And in [Section 9](../09-putting-it-all-together-a-complete-microservice-stack/README.md), we created a front-end application that queries these two microservices.

There is a glaring inefficiency in this design: the browser first calls `city-service`, waits for it to respond, and upon getting that response, calls `weather-service` for each of the cities returned. All these remote calls are made over public internet, whose speed is never guaranteed.

To resolve this inefficiency, we will create a single microservice that implements the [Transaction Script](https://www.martinfowler.com/eaaCatalog/transactionScript.html) pattern: it will orchestrate the calls to individual microservices and return the weather for all cities. To do this, we will use [Spring Cloud OpenFeign]. OpenFeign will automatically obtain the URLs of invoked microservices from Spring Cloud Registry, allowing us to build our `all-cities-weather-services` microservice without needing to resolve the locations of the constituent microservices.

## Create a Spring Boot Microservice

To create our microservice, we will use [https://start.spring.io/](https://start.spring.io/) with the command line:

```bash
curl https://start.spring.io/starter.tgz -d dependencies=cloud-feign,web,cloud-eureka,cloud-config-client -d baseDir=all-cities-weather-service -d bootVersion=2.3.1.RELEASE | tar -xzvf -
```

## Add a "cloud" Maven profile

*To deploy to Azure Spring Cloud, we add a "cloud" Maven profile like in the guide [05 - Build a Spring Boot microservice using Spring Cloud features](../05-build-a-spring-boot-microservice-using-spring-cloud-features/README.md)*

At the end of the application's `pom.xml` file (just before the closing `</project>` XML node), add the following code:

```xml
    <profiles>
        <profile>
            <id>cloud</id>
            <dependencies>
                <dependency>
                    <groupId>com.microsoft.azure</groupId>
                    <artifactId>spring-cloud-starter-azure-spring-cloud-client</artifactId>
                    <version>2.2.0</version>
                </dependency>
            </dependencies>
        </profile>
    </profiles>
```

## Add distributed tracing

As with previous services in Section 9, open up the `pom.xml` file and add the following Maven dependency as a child element of the __first__ `<dependencies>` element.

```java
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-zipkin</artifactId>
        </dependency>
```

## Add Spring code to call other microservices

Next to the `DemoApplication` class, create a `Weather` class:

```java
package com.example.demo;

public class Weather {

    private String city;

    private String description;

    private String icon;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
```

Note: this is the same `Weather` class that we created in Section 7 when we defined the original `weather-service` with one important difference: we no longer annotate the class as a JPA entity for data retrieval.

Next, in the same location create the `City` class. This is the same `City` class that we created in Section 6.

```java
package com.example.demo;

public class City {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
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

import com.example.demo.Weather;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient("weather-service")
@RequestMapping("/weather")
public interface WeatherServiceClient{

    @GetMapping("/city")
    Weather getWeatherForCity(@RequestParam("name") String cityName);
}

```

To enable Spring Cloud to discovery the underlying servies and to automatically generate OpenFeign clients, add the annotations @EnableDiscoveryClient and @EnableFeignClients to the `DemoApplication` class (as well as the corresponding `import` statements):

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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.example.demo.City;
import com.example.demo.CityServiceClient;
import com.example.demo.Weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AllCitiesWeatherController {

    @Autowired
    private CityServiceClient cityServiceClient;

    @Autowired 
    private WeatherServiceClient weatherServiceClient;

    @GetMapping("/")
    public List<Weather> getAllCitiesWeather(){
        Stream<City> allCities = cityServiceClient.getAllCities().stream().flatMap(list -> list.stream());

        //Obtain weather for all cities in parallel
        List<Weather> allCitiesWeather = allCities.parallel()
            .peek(city -> System.out.println("City: >>"+city.getName()+"<<"))
            .map(city -> weatherServiceClient.getWeatherForCity(city.getName()))
            .collect(Collectors.toList());

        return allCitiesWeather;
    }
}
```

## Create the application on Azure Spring Cloud

As before, create a specific `all-cities-weather-service` application in your Azure Spring Cloud instance:

```bash
az spring-cloud app create -n all-cities-weather-service
```

## Deploy the application

You can now build your "all-cities-weather-service" project and send it to Azure Spring Cloud:

```bash
cd all-cities-weather-service
./mvnw clean package -DskipTests -Pcloud
az spring-cloud app deploy -n all-cities-weather-service --jar-path target/demo-0.0.1-SNAPSHOT.jar
cd ..
```

## Test the project in the cloud

You can use the gateway created in Section 8 to access the all-cities-weather-service directly.

>💡__Note:__ the trailing slash (`/`) is not optional.

```bash
https://<Your gateway URL>/ALL-CITIES-WEATHER-SERVICE/
```

You should get the JSON output with the weather for all the cities:

```json
[{"city":"Paris, France","description":"It's always sunny on Azure Spring Cloud","icon":"weather-sunny"},
{"city":"London, UK","description":"It's always sunny on Azure Spring Cloud","icon":"weather-sunny"}]
```

---

⬅️ Previous guide: [11 - Configure CI/CD](../11-configure-ci-cd/README.md)

➡️ Next guide: [Conclusion](../99-conclusion/README.md)