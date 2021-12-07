# 07 - Build a Spring Boot microservice using MySQL

__This guide is part of the [Azure Spring Cloud training](../README.md)__

In this section, we'll build another data-driven microservice. This time, we will use a relational database, a [MySQL database managed by Azure](https://docs.microsoft.com/en-us/azure/mysql/?WT.mc_id=azurespringcloud-github-judubois). And we'll use Java Persistence API (JPA) to access the data in a way that is more frequently used in the Java ecosystem.

---

## Create the application on Azure Spring Cloud

As in [02 - Build a simple Spring Boot microservice](../02-build-a-simple-spring-boot-microservice/README.md), create a specific `weather-service` application in your Azure Spring Cloud instance:

```bash
az spring-cloud app create -n weather-service
```


## Configure the MySQL Server instance

After following the steps in Section 00, you should have an Azure Database for MySQL instance named `sclabm-<unique string>` in your resource group.

Before we can use it however, we will need to perform several tasks:

1. Create a MySQL firewall rule to allow connections from our local environment.
1. Create a MySQL firewall rule to allow connections from Azure Services. This will enable connections from Azure Spring Cloud.
1. Create a MySQL database.

> 💡When prompted for a password, enter the MySQL password you specified when deploying the ARM template in [Section 00](../00-setup-your-environment/README.md).

```bash
# Obtain the info on the MYSQL server in our resource group:
MYSQL_INFO=$(az mysql server list --query '[0]')
MYSQL_SERVERNAME=$(az mysql server list --query '[0].name' -o tsv)
MYSQL_USERNAME="$(az mysql server list --query '[0].administratorLogin' -o tsv)@${MYSQL_SERVERNAME}"
MYSQL_HOST="$(az mysql server list --query '[0].fullyQualifiedDomainName' -o tsv)"

# Create a firewall rule to allow connections from your machine:
MY_IP=$(curl whatismyip.akamai.com 2>/dev/null)
az mysql server firewall-rule create \
    --server-name $MYSQL_SERVERNAME \
    --name "connect-from-lab" \
    --start-ip-address "$MY_IP" \
    --end-ip-address "$MY_IP"

# Create a firewall rule to allow connections from Azure services:
az mysql server firewall-rule create \
    --server-name $MYSQL_SERVERNAME \
    --name "connect-from-azure" \
    --start-ip-address "0.0.0.0" \
    --end-ip-address "0.0.0.0"

# Create a MySQL database
az mysql db create \
    --name "azure-spring-cloud-training" \
    --server-name $MYSQL_SERVERNAME

# Display MySQL username (to be used in the next section)
echo "Your MySQL username is: ${MYSQL_USERNAME}"

```

## Bind the MySQL database to the application

As we did for CosmosDB in the previous section, create a service binding for the MySQL database to make it available to Azure Spring Cloud microservices.
In the [Azure Portal](https://portal.azure.com/?WT.mc_id=azurespringcloud-github-judubois):

- Navigate to your Azure Spring Cloud instance
- Click on Apps
- Click on `weather-service`.
- Click on "Service Bindings" and then on "Create Service Binding".
- Populate the service binding fields as shown.
  - The username will be displayed in last line of output from the section above.
  - The password is the one you specified in section 0. The default value is `super$ecr3t`.
- Click on `Create` to create the database binding

![MySQL Service Binding](media/01-create-service-binding-mysql.png)

## Create a Spring Boot microservice

Now that we've provisioned the Azure Spring Cloud instance and configured the service binding, let's get the code for `weather-service` ready. The microservice that we create in this guide is [available here](weather-service/).

To create our microservice, we will invoke the Spring Initalizer service from the command line:

```bash
curl https://start.spring.io/starter.tgz -d dependencies=web,data-jpa,mysql,cloud-eureka,cloud-config-client -d baseDir=weather-service -d bootVersion=2.3.8 -d javaVersion=1.8 | tar -xzvf -
```

> We use the `Spring Web`, `Spring Data JPA`, `MySQL Driver`, `Eureka Discovery Client` and the `Config Client` components.

## Add Spring code to get the data from the database

Next to the `DemoApplication` class, create a `Weather` JPA entity:

```java
package com.example.demo;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Weather {

    @Id
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

Then, create a Spring Data repository to manage this entity, called `WeatherRepository`:

```java
package com.example.demo;

import org.springframework.data.repository.CrudRepository;

public interface WeatherRepository extends CrudRepository<Weather, String> {
}
```

And finish coding this application by adding a Spring MVC controller called `WeatherController`:

```java
package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/weather")
public class WeatherController {

    private final WeatherRepository weatherRepository;

    public WeatherController(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    @GetMapping("/city")
    public @ResponseBody Weather getWeatherForCity(@RequestParam("name") String cityName) {
        return weatherRepository.findById(cityName).get();
    }
}
```

## Add sample data in MySQL

In order to have Hibernate automatically create your database, open up the `src/main/resources/application.properties` file and add:

```properties
spring.jpa.hibernate.ddl-auto=create
```

Then, in order to have Spring Boot add sample data at startup, create a `src/main/resources/import.sql` file and add:

```sql
INSERT INTO `azure-spring-cloud-training`.`weather` (`city`, `description`, `icon`) VALUES ('Paris, France', 'Very cloudy!', 'weather-fog');
INSERT INTO `azure-spring-cloud-training`.`weather` (`city`, `description`, `icon`) VALUES ('London, UK', 'Quite cloudy', 'weather-pouring');
```

> The icons we are using are the ones from [https://materialdesignicons.com/](https://materialdesignicons.com/) - you can pick their other weather icons if you wish.

## Deploy the application

You can now build your "weather-service" project and send it to Azure Spring Cloud:

```bash
cd weather-service
./mvnw clean package -DskipTests
az spring-cloud app deploy -n weather-service --jar-path target/demo-0.0.1-SNAPSHOT.jar
cd ..
```

## Test the project in the cloud

- Go to "Apps" in your Azure Spring Cloud instance.
  - Verify that `weather-service` has a `Registration status` which says `1/1`. This shows that it is correctly registered in the Spring Cloud Service Registry.
  - Select `weather-service` to have more information on the microservice.
- Copy/paste the "Test Endpoint" that is provided. You might have to click on `See more` to find it.

You can now use cURL to test the `/weather/city` endpoint. For example, to test for `Paris, France` city, append to the end of the test endpoint: `/weather/city?name=Paris%2C%20France`.

```json
{"city":"Paris, France","description":"Very cloudy!","icon":"weather-fog"}
```

If you need to check your code, the final project is available in the ["weather-service" folder](weather-service/).

---

⬅️ Previous guide: [06 - Build a reactive Spring Boot microservice using Cosmos DB](../06-build-a-reactive-spring-boot-microservice-using-cosmosdb/README.md)

➡️ Next guide: [08 - Build a Spring Cloud Gateway](../08-build-a-spring-cloud-gateway/README.md)
