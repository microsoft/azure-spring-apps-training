---
page_type: sample
languages:
- java
---

# Azure Spring Cloud training

You will find here a full workshop on Azure Spring Cloud, including guides and demos.

This lab is based on a public workshop created by [Julien Dubois](https://twitter.com/juliendubois) and available for free to everyone, under the [MIT license](LICENSE.txt).

## What you should expect

This is not the official documentation but an opinionated training.

It is a hands-on training, and it will use the command line extensively. The idea is to get coding very quickly and play with the platform, from a simple demo to far more complex examples.

After completing all the guides, you should have a fairly good understanding of everything that Azure Spring Cloud offers.

## Symbols

>🛑 -  __Manual Modification Required__. When this symbol appears in front of one or more commands, you will need to modify the commands as indicated prior to running them.

>🚧 - __Preview-specific__. This symbol indicates steps that are only necessary while Azure Spring Cloud is in preview.

>💡 - __Frustration Avoidance Tip__. These will help you avoid potential pitfalls.

## [00 - Prerequisites and Setup](00-setup-your-environment/README.md)

Prerequisites and environment setup.

## [01 - Create an Azure Spring Cloud cluster](01-create-an-azure-spring-cloud-instance/README.md)

Basics on creating a cluster and configuring the CLI to work efficiently.

## [02 - Build a simple Spring Boot microservice](02-build-a-simple-spring-boot-microservice/README.md)

Build the simplest possible Spring Boot microservice using the Spring Initializer.

## [03 - Configure application logs](03-configure-monitoring/README.md)

Access Spring Boot applications logs to understand common issues.

## [04 - Configure a Spring Cloud Config server](04-configure-a-spring-cloud-config-server/README.md)

Configure a [Spring Cloud Config Server](https://cloud.spring.io/spring-cloud-config), that will be entirely managed and supported by Azure Spring Cloud, to be used by Spring Boot microservices.

## [05 - Build a Spring Boot microservice using Spring Cloud features](05-build-a-spring-boot-microservice-using-spring-cloud-features/README.md)

Build a Spring Boot microservice that is cloud-enabled: it uses a Spring Cloud Service Registry and a [Spring Cloud Config Server](https://cloud.spring.io/spring-cloud-config) which are both managed and supported by Azure Spring Cloud.

## [06 - Build a reactive Spring Boot microservice using Cosmos DB](06-build-a-reactive-spring-boot-microservice-using-cosmosdb/README.md)

Build a reactive Spring Boot microservice, that uses the [Spring reactive stack](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html) and is bound to a [Cosmos DB database](https://docs.microsoft.com/en-us/azure/cosmos-db/?WT.mc_id=azurespringcloud-github-judubois) in order to access a globally-distributed database with optimum performance.

## [07 - Build a Spring Boot microservice using MySQL](07-build-a-spring-boot-microservice-using-mysql/README.md)

Build a classical Spring Boot application that uses JPA to access a [MySQL database managed by Azure](https://docs.microsoft.com/en-us/azure/mysql/?WT.mc_id=azurespringcloud-github-judubois).

## [08 - Build a Spring Cloud Gateway](08-build-a-spring-cloud-gateway/README.md)

Build a [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway) to route HTTP requests to the correct Spring Boot microservices.

## [09 - Putting it all together, a complete microservice stack](09-putting-it-all-together-a-complete-microservice-stack/README.md)

Use a front-end to access graphically our complete microservice stack. Monitor our services with Azure Spring Cloud's distributed tracing mechanism and scale our services depending on our needs.

## [10 - Blue/Green deployment](10-blue-green-deployment/README.md)

Deploy new versions of applications in a staging environment and switch between staging and production with Azure Spring Cloud.

## [11 - Configure CI/CD](11-configure-ci-cd/README.md)

Configure a Continuous Integration / Continuous Deployment platform using GitHub Actions, so our Spring Boot microservices are automatically deployed.

## [12 - Making Microservices Talk to Each Other](12-making-microservices-talk-to-each-other/README.md)

Creating a microservice that talks to other microservices.

## [Conclusion](99-conclusion/README.md)

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
