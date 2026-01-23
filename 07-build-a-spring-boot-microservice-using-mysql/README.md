# 07 - Build a Spring Boot microservice using MySQL

__This guide is part of the [Azure Spring Apps training](../README.md)__

In this section, we'll build another data-driven microservice. This time, we will use a relational database, a [MySQL database managed by Azure](https://docs.microsoft.com/en-us/azure/mysql/?WT.mc_id=azurespringcloud-github-judubois). And we'll use Java Persistence API (JPA) to access the data in a way that is more frequently used in the Java ecosystem.

---

## Create the application on Azure Spring Apps

As in [02 - Build a simple Spring Boot microservice](../02-build-a-simple-spring-boot-microservice/README.md), create a specific `weather-service` application in your Azure Spring Apps instance:

```bash
az spring app create -n weather-service --runtime-version Java_17
```


## Configure the MySQL Server instance

After following the steps in Section 00, you should have an Azure Database for MySQL instance named `sclabm-<unique string>` in your resource group.

Before we can use it however, we will need to perform several tasks:

1. Create a MySQL firewall rule to allow connections from our local environment.
1. Create a MySQL firewall rule to allow connections from Azure Services. This will enable connections from Azure Spring Apps.
1. Create a MySQL database.

> 💡When prompted for a password, enter the MySQL password you specified when deploying the ARM template in [Section 00](../00-setup-your-environment/README.md).

```bash
# Obtain the info on the MYSQL flexible server in our resource group:
export MYSQL_SERVERNAME=$(az mysql flexible-server list --query '[0].name' -o tsv)
export MYSQL_USERNAME="$(az mysql flexible-server list --query '[0].administratorLogin' -o tsv)@${MYSQL_SERVERNAME}"
export MYSQL_HOST="$(az mysql flexible-server list --query '[0].fullyQualifiedDomainName' -o tsv)"

# Create a firewall rule to allow connections from your machine:
export MY_IP=$(curl whatismyip.akamai.com 2>/dev/null)
az mysql flexible-server firewall-rule create \
    --name $MYSQL_SERVERNAME \
    --rule-name "connect-from-lab" \
    --start-ip-address "$MY_IP" \
    --end-ip-address "$MY_IP"

# Create a firewall rule to allow connections from Azure services:
az mysql flexible-server firewall-rule create \
    --name $MYSQL_SERVERNAME \
    --rule-name "connect-from-azure" \
    --start-ip-address "0.0.0.0" \
    --end-ip-address "0.0.0.0"

# Create a MySQL database
az mysql flexible-server db create \
    --database-name "azure-spring-apps-training" \
    --server-name $MYSQL_SERVERNAME

# Display MySQL username (to be used in the next section)
echo "Your MySQL username is: ${MYSQL_USERNAME}"

```

## Connect the MySQL database to the application

As we did for Cosmos DB in the previous section, create a Service Connector for the MySQL database to make it available to the `weather-service` microservice.
In the [Azure Portal](https://portal.azure.com/?WT.mc_id=java-0000-judubois):

- Navigate to your Azure Spring Apps instance
- Click on `Apps`
- Click on `weather-service`.
- Click on `Service Connector` and then on `+ Create`.
- Populate the Service Connector fields as shown:
  - For Service type, select `DB for MySQL flexible server`
  - Specify a connection name, e.g. "weatherdb"
  - Verify the correct subscription is shown
  - Choose the MySQL server created in the preceding steps
  - Select the MySQL database created earlier
  - Select `SpringBoot` as the Client type
  - Click the `Next: Authentication` button

![MySQL Service Connector, 1 of 5](media/01-create-service-connector-mysql.png)

- Verify `Connection string` is selected
- Select `Continue with...Database credentials` and fill in Username and Password
  - The Username was echoed to the terminal as the last command executed earlier
  - The password is the one you specified in section 0. The default value is `super$ecr3t`.
- Expand `Advanced` to visually inspect the injected Spring Data properties (optional)

![MySQL Service Connector, 2 of 5](media/02-create-service-connector-mysql.png)

- Review the properties automatically injected by the Service Connector to the `weather-service`
- Click `Next: Networking`

![MySQL Service Connector, 3 of 5](media/03-create-service-connector-mysql.png)

- Verify that `Configure firewall rules to enable access to target service` is selected
- Click on `Next: Review + Create`

![MySQL Service Connector, 4 of 5](media/04-create-service-connector-mysql.png)

- After receiving the "Validation passed" message, click the `Create` button to create the Service Connector

![MySQL Service Connector, 5 of 5](media/05-create-service-connector-mysql.png)

## Create a Spring Boot microservice

Now that we've provisioned the Azure Spring Apps instance and configured the Service Connector, let's get the code for `weather-service` ready. The microservice that we create in this guide is [available here](weather-service/).

To create our microservice, we will invoke the Spring Initalizr service from the command line:

```bash
curl https://start.spring.io/starter.tgz -d type=maven-project -d dependencies=web,data-jpa,mysql,cloud-eureka,cloud-config-client -d baseDir=weather-service -d bootVersion=3.1.3 -d javaVersion=17 | tar -xzvf -
```

> We use the `Spring Web`, `Spring Data JPA`, `MySQL Driver`, `Eureka Discovery Client` and the `Config Client` components.

## Add Spring code to get the data from the database

Next to the `DemoApplication` class, create a `Weather` JPA entity:

```java
package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

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

import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherRepository weatherRepository;

    public WeatherController(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    @GetMapping("/city")
    public Optional<Weather> getWeatherForCity(@RequestParam("name") String cityName) {
        return weatherRepository.findById(cityName);
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
INSERT INTO `azure-spring-apps-training`.`weather` (`city`, `description`, `icon`) VALUES ('Paris, France', 'Very cloudy!', 'weather-fog');
INSERT INTO `azure-spring-apps-training`.`weather` (`city`, `description`, `icon`) VALUES ('London, UK', 'Quite cloudy', 'weather-pouring');
```

> The icons we are using are the ones from [https://materialdesignicons.com/](https://materialdesignicons.com/) - you can pick their other weather icons if you wish.

## Deploy the application

You can now build your "weather-service" project and send it to Azure Spring Apps:

```bash
cd weather-service
./mvnw clean package -DskipTests
az spring app deploy -n weather-service --artifact-path target/demo-0.0.1-SNAPSHOT.jar
cd ..
```

## Test the project in the cloud

- Go to "Apps" in your Azure Spring Apps instance.
  - Verify that `weather-service` has a `Registration status` which says `1/1`. This shows that it is correctly registered in the Spring Cloud Service Registry.
  - Select `weather-service` to have more information on the microservice.
- Copy/paste the "Test endpoint" that is provided.

You can now use cURL to test the `/weather/city` endpoint. For example, to test for `Paris, France` city, append to the end of the test endpoint: `/weather/city?name=Paris%2C%20France`.

```bash
curl "https://primary:31SifNyr649htxU3IEpYaLbxRz6Gy3xAk0aLDFM49hcwx9zcCEXvPEGkHSpzJzKv@judubois-4876.test.azuremicroservices.io/weather-service/default/weather/city?name=Paris%2C%20France"
```

Here is the response you should receive:

```json
{"city":"Paris, France","description":"Very cloudy!","icon":"weather-fog"}
```

If you need to check your code, the final project is available in the ["weather-service" folder](weather-service/).

---

⬅️ Previous guide: [06 - Build a reactive Spring Boot microservice using Cosmos DB](../06-build-a-reactive-spring-boot-microservice-using-cosmosdb/README.md)

➡️ Next guide: [08 - Build a Spring Cloud Gateway](../08-build-a-spring-cloud-gateway/README.md)
