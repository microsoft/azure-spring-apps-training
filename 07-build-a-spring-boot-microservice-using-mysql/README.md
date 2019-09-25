# 07 - Build a Spring Boot microservice using MySQL

__This guide is part of the [Azure Spring Cloud training](../README.md)__

Build a classical Spring Boot application that uses JPA to acess a [MySQL database managed by Azure](https://docs.microsoft.com/en-us/azure/mysql/?WT.mc_id=azurespringcloud-github-judubois).

---

## Create a MySQL Server instance

- Go to the [the Azure portal](https://portal.azure.com/?WT.mc_id=azurespringcloud-github-judubois) and look for "Azure Database for MySQL servers" in the search box.
- Create a new database
  - Write down the password in a safe place
  - You can choose the low-budget "Basic" database

![Create MySQL database](media/01-create-mysql.png)

- Once the databas is created, select it and go to "Connection security"
  - Enable "Allow access to Azure services"
  - Add your current IP to the firewall rules, so you can access it from your machine

> If you need to know your current external IP, you can use https://www.whatismyip.com/

![Configure firewall](media/02-firewall.png)

- Connect to your database using your favorite database explorer tool
  - For example [MySQL Workbench](https://www.mysql.com/fr/products/workbench/)
  - The admin login name is available in the "overview" section of your database, it is in the form of `username@database`
- Create a new schema named `azure-spring-cloud-training`

# Create a Spring Boot microservice

The microservice that we create in this guide is [available here](spring-boot-mysql/).

To create our microservice, we will use [https://start.spring.io/](https://start.spring.io/) with the command line:

```
curl https://start.spring.io/starter.tgz -d dependencies=web,data-jpa,mysql,cloud-eureka,cloud-config-client -d baseDir=spring-boot-mysql | tar -xzvf -
```

> We use the `Spring Web`, `Spring Data JPA`, `MySQL Driver`, `Eureka Discovery Client` and the `Config Client` components.

## Add a "cloud" Maven profile

*To deploy to Azure Spring Cloud, we add a "cloud" Maven profile like in the guide [05 - Build a Spring Boot microservice using Spring Cloud features](../05-build-a-spring-boot-microservice-using-spring-cloud-features/README.md)*

At the end of the application's `pom.xml` file (just before the closing `</project>` XML node), add the following code:

```xml
	<profiles>
		<profile>
			<id>cloud</id>
			<repositories>
				<repository>
					<id>nexus-snapshots</id>
					<url>https://oss.sonatype.org/content/repositories/snapshots/</url>
					<snapshots>
						<enabled>true</enabled>
					</snapshots>
				</repository>
			</repositories>
			<dependencies>
				<dependency>
					<groupId>com.microsoft.azure</groupId>
					<artifactId>spring-cloud-starter-azure-spring-cloud-client</artifactId>
					<version>2.1.0-SNAPSHOT</version>
				</dependency>
			</dependencies>
		</profile>
	</profiles>
```



---

⬅️ Previous guide: [06 - Build a reactive Spring Boot microservice using Cosmos DB](../06-build-a-reactive-spring-boot-microservice-using-cosmosdb/README.md)

➡️ Next guide: [08 - Build a Spring Boot gateway](../08-build-a-spring-boot-gateway/README.md)
