# 04 - Configure a Spring Cloud Config server

__This guide is part of the [Azure Spring Cloud training](../README.md)__

Configure a [Spring Cloud Config Server](https://cloud.spring.io/spring-cloud-config), that will be entirely managed and supported by Azure Spring Cloud, to be used by Spring Boot microservices.

---

Two option are available to store your Spring Boot configuration:

- Use a public Git repository: this is the easiest option, as there is no security to configure
- Use a private Git repository: this is the most secured option, but it's a bit more complicated to set up

For both options, we will use an `application.yml` file which will store configuration data for all our microservices. For the moment, it will just store a message to check if the configuration is successful:

```
application:
    message: Configured by Spring Cloud Config Server
```

You can check this file in the following public GitHub repository:

[https://github.com/jdubois/spring-cloud-config-public](https://github.com/jdubois/spring-cloud-config-public)

## Configure a public Git repository

_Skip this step if you want to set up a private Git repository, which is explained in the next section_

We are going to use [https://github.com/jdubois/spring-cloud-config-public](https://github.com/jdubois/spring-cloud-config-public) directly, but you can create your own GitHub directory in which you you will add a custom `application.yml` if you want to do your own configuration.

- Go to the [the Azure portal](https://portal.azure.com/?WT.mc_id=azurespringcloud-github-judubois).
- Go to the overview page of your Azure Spring Cloud server, and select "Config server" in the menu
- Configure the Git repository:
  - Add the repository URL: `https://github.com/jdubois/spring-cloud-config-public.git`
  - Click on `Authentication` and select `Public`
- Click on "Apply" and wait for the operation to succeeed

You're done! You can skip the next section and go to the next guide: ➡️ [05 - Build a Spring Boot microservice using Spring Cloud features](../05-build-a-spring-boot-microservice-using-spring-cloud-features/README.md).

## Configure a private Git repository

_Ignore this step if you set up a public repository in the previous section_

On your [GitHub account](https://github.com), create a new **private** repository where the Spring Boot configurations will be stored.

In this repository, add a new `application.yml` file, as descibred above (you can copy the one from `https://github.com/jdubois/spring-cloud-config-public/blob/master/application.yml`).

We know need to create a GitHub personnal token to access this repository: follow the [GitHub guide to create a personal token](https://help.github.com/en/articles/creating-a-personal-access-token-for-the-command-line) and save your token.

![GitHub personnal access token](media/01-github-personal-access-token.png)

- Go to the [the Azure portal](https://portal.azure.com/?WT.mc_id=azurespringcloud-github-judubois).
- Go to the overview page of your Azure Spring Cloud server, and select "Config server" in the menu
- Configure the repository we previously created:
  - Add the repository URL, for example `https://github.com/jdubois/azure-spring-cloud-config.git`
  - Click on `Authentication` and select `HTTP Basic`
  - The username is your GitHub login name
  - The password is the personal token we created in the previous section
- Click on "Apply" and wait for the operation to succeeed

![Spring Cloud config server](media/02-config-server.png)

---

⬅️ Previous guide: [03 - Configure application logs](../03-configure-application-logs/README.md)

➡️ Next guide: [05 - Build a Spring Boot microservice using Spring Cloud features](../05-build-a-spring-boot-microservice-using-spring-cloud-features/README.md)
