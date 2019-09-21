# Azure Spring Cloud training

You will find here a full workshop on Azure Spring Cloud, including guides and demos.

This is done by [Julien Dubois](https://twitter.com/juliendubois) and available for free for everyone.

## What you should expect

This is not the official documentation, but an opinionated training.

It is made to by hands-on, and will use the command line extensively. The idea is to get coding very quickly and play with the platform, from a simple demo to far more complex examples.

After completing all the guides, you should have a fairly good understanding of everything that Azure Spring Cloud offers.

## Prerequisites

- Java 11 or above
- The [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli/?WT.mc_id=azurespringcloud-github-judubois)

## 01 - Create a cluster

Basics on creating a cluster and configuring the CLI to work efficiently.

## 02 - Build a simple Spring Boot microservice

The simplest possible Spring Boot microservice, made with [https://start.spring.io/](https://start.spring.io/).

## 03 - Debug and monitor applications on Azure Spring Cloud

## 04 - Configure a Spring Cloud Config server

Configure a [Spring Cloud Config Server](https://cloud.spring.io/spring-cloud-config), that will be entirely managed and supported by Azure Spring Cloud, to be used by Spring Boot microservices.

## 05 - Build a Spring Boot microservice using Spring Cloud features

A simple Spring Boot microservice that is cloud-enabled: it uses a discovery server ([Eureka](https://github.com/Netflix/eureka)) and a [Spring Cloud Config Server](https://cloud.spring.io/spring-cloud-config) which are both managed and supported by Azure Spring Cloud.

## 06 - Build a reactive Spring Boot microservice using CosmosDB

A reactive Spring Boot application, that uses the [Spring reactive stack](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html) and is binded to a [CosmosDB database](https://docs.microsoft.com/en-us/azure/cosmos-db/?WT.mc_id=azurespringcloud-github-judubois) in order to access a globally-distributed database without optimum performance.

## 07 - Build a Spring Boot microservice using MySQL

A classical Spring Boot application that uses JPA to acess a [MySQL database managed by Azure](https://docs.microsoft.com/en-us/azure/mysql/?WT.mc_id=azurespringcloud-github-judubois).

## 08 - Build a Spring Boot gateway

## 09 - Putting it all together, a complete microservice stack


