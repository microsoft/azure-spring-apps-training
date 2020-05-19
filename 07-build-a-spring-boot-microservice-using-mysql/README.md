# 07 - Build a Spring Boot microservice using MySQL

__This guide is part of the [Azure Spring Cloud training](../README.md)__

Build a classical Spring Boot application that uses JPA to acess a [MySQL database managed by Azure](https://docs.microsoft.com/en-us/azure/mysql/?WT.mc_id=azurespringcloud-github-judubois).

---

## Configure the MySQL Server instance

After following the steps in Section 00, you should have an Azure Database for MySQL instance named `sclabm-<unique string>` in your resource group.

Before we can use it however, we will need to perform several tasks:

1. Create a MySQL firewall rule to allow connections from our local environment.
1. Create a MySQL firewall rule to allow connections from Azure Services. This will enable connections from Azure Spring Cloud.
1. Connect to the MySQL server via MySQL CLI and initialize the database

- Connect to your database using [MySQL CLI](https://dev.mysql.com/downloads/):

    > 💡When prompted for a password, enter the MySQL password you specified when deploying the ARM template in [Section 00](../00-setup-your-environment/README.md).

    ```bash
    # Obtain the info on the MYSQL server in our resource group:
    MYSQL_INFO=$(az mysql server list --query '[0]')
    MYSQL_SERVERNAME=$(echo $MYSQL_INFO | jq -r .name)
    MYSQL_USERNAME="$(echo $MYSQL_INFO | jq -r .administratorLogin)@${MYSQL_SERVERNAME}"
    MYSQL_HOST="$(echo $MYSQL_INFO | jq -r .fullyQualifiedDomainName)"
    read -p "Enter your MySQL password: " -s MYSQL_PASSWORD

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

    # Connect through MySQL CLI
    mysql --ssl -h "$MYSQL_HOST" -u "$MYSQL_USERNAME" --password="$MYSQL_PASSWORD"
    ```

- Once you are connected to MySQL, create a new schema named `azure-spring-cloud-training`:

    ```sql
    mysql> CREATE DATABASE `azure-spring-cloud-training`;
    Query OK, 1 row affected (0.22 sec)
    ```

- Press CTRL+D to exit the MySQL CLI.

## Create the application on Azure Spring Cloud

As in [02 - Build a simple Spring Boot microservice](../02-build-a-simple-spring-boot-microservice/README.md), create a specific `weather-service` application in your Azure Spring Cloud instance:

```bash
az spring-cloud app create -n weather-service
```

## Bind the MySQL database to the application

In [Section 6](../06-build-a-reactive-spring-boot-microservice-using-cosmosdb/README.md), we created a service binding to inject CosmosDB configuration into an Azure Spring Cloud microservice.

Now, let's do the same thing to inject connection information for our MySQL database into the new `weather-service` microservice. This time, we'll do it from the command line:

> 💡Make sure you use the same shell session that you've been using for the previous steps in this section.

```bash
MYSQL_ARM_RESOURCE_ID=$(echo $MYSQL_INFO | jq -r .id)

az spring-cloud app binding mysql add \
    --app weather-service \
    --name mysql-weather \
    --resource-id "$MYSQL_ARM_RESOURCE_ID" \
    --database-name 'azure-spring-cloud-training' \
    --username "$MYSQL_USERNAME" \
    --key "$MYSQL_PASSWORD"
```

You can, if you wish, view the newly created service binding in [Azure Portal](https://portal.azure.com/?WT.mc_id=azurespringcloud-github-judubois):

- Navigate to your Azure Spring Cloud instance
- Click on Apps
- Click on `weather-service`.
- Click on Service Bindings.

You should see the newly created weather binding listed: `mysql-weather`. If you click on it, you can see the MySQL connection information:

![MySQL Service Binding](media/03-bind-service-mysql.png)

## Create a Spring Boot microservice

Now that we've provisioned the Azure Spring Cloud instance and configured the service binding, let's get the code for `weather-service` ready. The microservice that we create in this guide is [available here](weather-service/).

To create our microservice, we will use [https://start.spring.io/](https://start.spring.io/) with the command line:

```bash
curl https://start.spring.io/starter.tgz -d dependencies=web,data-jpa,mysql,cloud-eureka,cloud-config-client -d baseDir=weather-service -d bootVersion=2.3.0.RELEASE | tar -xzvf -
```

> We use the `Spring Web`, `Spring Data JPA`, `MySQL Driver`, `Eureka Discovery Client` and the `Config Client` components.

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
./mvnw clean package -DskipTests -Pcloud
az spring-cloud app deploy -n weather-service --jar-path target/demo-0.0.1-SNAPSHOT.jar
cd ..
```

## Test the project in the cloud

- Go to "Apps" in your Azure Spring Cloud instance.
  - Verify that `weather-service` has a `Discovery status` which says `UP(1),DOWN(0)`. This shows that it is correctly registered in the Spring Cloud Service Registry.
  - Select `weather-service` to have more information on the microservice.
- Copy/paste the "Test Endpoint" that is provided.

You can now use cURL to test the `/weather/city` endpoint. For example, to test for `Paris, France` city, append to the end of the test endpoint: `/weather/city?name=Paris%2C%20France`.

```json
{"city":"Paris, France","description":"Very cloudy!","icon":"weather-fog"}
```

If you need to check your code, the final project is available in the ["weather-service" folder](weather-service/).

---

⬅️ Previous guide: [06 - Build a reactive Spring Boot microservice using Cosmos DB](../06-build-a-reactive-spring-boot-microservice-using-cosmosdb/README.md)

➡️ Next guide: [08 - Build a Spring Cloud Gateway](../08-build-a-spring-cloud-gateway/README.md)
