# 05 - Build a Spring Boot microservice using Spring Cloud features

__This guide is part of the [Azure Spring Cloud training](../README.md)__

Build a Spring Boot microservice that is cloud-enabled: it uses a discovery server ([Eureka](https://github.com/Netflix/eureka)) and a [Spring Cloud Config Server](https://cloud.spring.io/spring-cloud-config) which are both managed and supported by Azure Spring Cloud.

---

## What we are going to build

This guide builds upon the previous guides: we are going to build again a simple Spring Boot microservice like in [02 - Build a simple Spring Boot microservice](../02-build-a-simple-spring-boot-microservice/README.md), but this time it will use two major Spring Cloud features:

- It will be connected to a [Eureka server](https://github.com/Netflix/eureka) so it can discover other microservices, as well as being discovered itself!
- It will get its configuration from the Spring Cloud Config server that we configured in the previous guide, [04 - Configure a Spring Cloud Config server](../04-configure-a-spring-cloud-config-server/README.md)

For both features, it will just be a matter of adding an official Spring Boot starter, and Azure Spring Cloud will take care of everything else.



---

⬅️ Previous guide: [04 - Configure a Spring Cloud Config server](../04-configure-a-spring-cloud-config-server/README.md)

➡️ Next guide: [06 - Build a reactive Spring Boot microservice using CosmosDB](../06-build-a-reactive-spring-boot-microservice-using-cosmosdb/README.md)