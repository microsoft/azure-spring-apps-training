# 10 - Blue/Green deployment

__This guide is part of the [Azure Spring Cloud training](../README.md)__

Deploy new versions of applications in a staging environment, and switch between staging and production with Azure Spring Cloud.

---

We are going to deploy a new release of the "weather-service" microservice that was developped in [07 - Build a Spring Boot microservice using MySQL](../07-build-a-spring-boot-microservice-using-mysql/README.md).

The microservice that we develop in this guide is [available here](weather-service/), which is a slightly modified version of the service that was developped earlier.

## Modify the current application

In the "weather-service" application, commit your current code and switch to a new branch where you will do your changes.

Open the `WeatherController` class and modify its `getWeatherForCity()` method so it always returns sunny weather (this will be an easy-to-spot graphical view of our modifications in the code):

```java
package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/weather")
public class WeatherController {

    private final WeatherRepository weatherRepository;

    public WeatherController(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    @GetMapping("/city")
    public @ResponseBody Weather getWeatherForCity(@RequestParam("name") String cityName) {
        return weatherRepository.findById(cityName).map(weather -> {
            weather.setDescription("It's always sunny on Azure Spring Cloud");
            weather.setIcon("weather-sunny");
            return weather;
        }).get();
    }
}
```

## Deploy the new application to a new "green" deployment set

Build a new version of the application and deploy it to a new `deployment set` called `green`:

```bash
./mvnw package -DskipTests -Pcloud
az spring-cloud app deployment create --name green --app weather-service --jar-path target/demo-0.0.1-SNAPSHOT.jar
```

Once the application is deployed, if you go to [https://spring-training.azureedge.net/](https://spring-training.azureedge.net/) you will still have the same data, as the new version of the microservice is now in a staging area.

To put this `green` deployment into production, you can use the command line:

```bash
az spring-cloud app set-deployment -n weather-service --deployment green
```

Another solution is to use [the Azure portal](https://portal.azure.com/?WT.mc_id=azurespringcloud-github-judubois):

- Find your Azure Spring Cloud cluster
- Click on the "deployement" menu
- Select the `weather-service` application and click on "Set deployment"

> If you want to reuse a deployment set, you need first to delete it, for instance:
>
> ```bash
> az spring-cloud app deployment delete --name green --app weather-service
> ```

Once you have swapped deployments, and that `green` is active, you need to wait a few seconds for the Spring Cloud Service Registry to synchronize and use this new version from the `gateway` application. You will then be able to see the new modified data:

![Green deployment](media/01-green-deployment.png)

---

⬅️ Previous guide: [09 - Putting it all together, a complete microservice stack](../09-putting-it-all-together-a-complete-microservice-stack/README.md)
