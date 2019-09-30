# Azure Spring Cloud training

You will find here a full workshop on Azure Spring Cloud, including guides and demos.

This is done by [Julien Dubois](https://twitter.com/juliendubois) and available for free to everyone, under the [Apache 2 license](LICENSE.txt).

## What you should expect

This is not the official documentation, but an opinionated training.

It is made to by hands-on, and will use the command line extensively. The idea is to get coding very quickly and play with the platform, from a simple demo to far more complex examples.

After completing all the guides, you should have a fairly good understanding of everything that Azure Spring Cloud offers.

## Prerequisites

- Java 11 or above
- The [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli/?WT.mc_id=azurespringcloud-github-judubois)
- A [GitHub account](https://github.com)

## [01 - Create a cluster](01-create-a-cluster/README.md)

Basics on creating a cluster and configuring the CLI to work efficiently.

## [02 - Build a simple Spring Boot microservice](02-build-a-simple-spring-boot-microservice/README.md)

Build the simplest possible Spring Boot microservice, made with [https://start.spring.io/](https://start.spring.io/).

## [03 - Configure application logs](03-configure-application-logs/README.md)

Access Spring Boot applications logs to understand common issues.

## [04 - Configure a Spring Cloud Config server](04-configure-a-spring-cloud-config-server/README.md)

Configure a [Spring Cloud Config Server](https://cloud.spring.io/spring-cloud-config), that will be entirely managed and supported by Azure Spring Cloud, to be used by Spring Boot microservices.

## [05 - Build a Spring Boot microservice using Spring Cloud features](05-build-a-spring-boot-microservice-using-spring-cloud-features/README.md)

Build a Spring Boot microservice that is cloud-enabled: it uses a discovery server ([Eureka](https://github.com/Netflix/eureka)) and a [Spring Cloud Config Server](https://cloud.spring.io/spring-cloud-config) which are both managed and supported by Azure Spring Cloud.

## [06 - Build a reactive Spring Boot microservice using Cosmos DB](06-build-a-reactive-spring-boot-microservice-using-cosmosdb/README.md)

Build a reactive Spring Boot microservice, that uses the [Spring reactive stack](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html) and is binded to a [Cosmos DB database](https://docs.microsoft.com/en-us/azure/cosmos-db/?WT.mc_id=azurespringcloud-github-judubois) in order to access a globally-distributed database without optimum performance.

## [07 - Build a Spring Boot microservice using MySQL](07-build-a-spring-boot-microservice-using-mysql/README.md)

Build a classical Spring Boot application that uses JPA to acess a [MySQL database managed by Azure](https://docs.microsoft.com/en-us/azure/mysql/?WT.mc_id=azurespringcloud-github-judubois).

## [08 - Build a Spring Cloud Gateway](08-build-a-spring-cloud-gateway/README.md)

Build a [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway) to route HTTP requests to the correct Spring Boot microservices.

## [09 - Putting it all together, a complete microservice stack](09-putting-it-all-together-a-complete-microservice-stack/README.md)

Use a front-end to access graphically our complete microservice stack. Monitor our services with Azure Spring Cloud's distributed tracing mechanism, and scale our services depending on our needs.

---

## Legal Notices

Microsoft and any contributors grant you a license to the Microsoft documentation and other content
in this repository under the [Creative Commons Attribution 4.0 International Public License](https://creativecommons.org/licenses/by/4.0/legalcode),
see the [LICENSE](LICENSE) file, and grant you a license to any code in the repository under the [MIT License](https://opensource.org/licenses/MIT), see the
[LICENSE-CODE](LICENSE-CODE) file.

Microsoft, Windows, Microsoft Azure and/or other Microsoft products and services referenced in the documentation
may be either trademarks or registered trademarks of Microsoft in the United States and/or other countries.
The licenses for this project do not grant you rights to use any Microsoft names, logos, or trademarks.
Microsoft's general trademark guidelines can be found at http://go.microsoft.com/fwlink/?LinkID=254653.

Privacy information can be found at https://privacy.microsoft.com/en-us/

Microsoft and any contributors reserve all other rights, whether under their respective copyrights, patents,
or trademarks, whether by implication, estoppel or otherwise.
