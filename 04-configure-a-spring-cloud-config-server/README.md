# 04 - Configure a Spring Cloud Config server

__This guide is part of the [Azure Spring Cloud training](../README.md)__

A key feature of cloud-native applications is *externalized configuration* - the ability to store, manage, and version configuration separately from the application code. In this section, we'll configure a [Spring Cloud Config Server](https://cloud.spring.io/spring-cloud-config) to enable this functionality. In the next section, you'll see how Spring Cloud Config can inject configuration from a Git repository into your application.

---

> 💡 If your organization uses Azure Repos as your source code repository, see [Using Azure Repos for Azure Spring Cloud configuration](AzureReposForConfig.md)

> ⏱ If you want to skip the step of creating a private repository, you can use this public repository instead: [https://github.com/Azure-Samples/spring-cloud-sample-public-config.git](https://github.com/Azure-Samples/spring-cloud-sample-public-config.git). __Storing configuration in a public repository is not recommended in real-world deployments.__ We offer this public repository only as a shortcut for this workshop, for example if you don't have a GitHub account. 
>
> To use this shortcut:
>  - Go to [the Azure portal](https://portal.azure.com/?WT.mc_id=azurespringcloud-github-judubois).
>  - Go to the overview page of your Azure Spring Cloud server and select "Config server" in the menu
>  - Set the repository URL: `https://github.com/Azure-Samples/spring-cloud-sample-public-config.git`
>  - Click on "Validate" and wait for the operation to succeed
>  - Click on "Apply" and wait for the operation to succeed
>  
>  We have enabled Azure Spring Cloud to create a configuration server with the configuration files from this repository. You can now proceed to the next guide: 
>  ➡ [05 - Build a Spring Boot microservice using Spring Cloud features](../05-build-a-spring-boot-microservice-using-spring-cloud-features/README.md)

## Create a Git repository for storing the application configuration

On your [GitHub account](https://github.com), create a new **private** repository where the Spring Boot configurations will be stored.

In the new private GitHub repository, add a new `application.yml` file which will store configuration data for all our microservices.

Typically, each Spring Boot application includes such a file within the application binaries to contain application settings. A Spring Cloud Configuration Server allow such settings to be stored at a single location and served from a single source.

For the moment, our `application.yml` will just store a message to check if the configuration is successful:

```yaml
application:
    message: Configured by Azure Spring Cloud
```

Commit and push the new file.

## Create a GitHub personal token

Azure Spring Cloud can access Git repositories that are public, secured by SSH, or secured using HTTP basic authentication. We will use that last option, as it is easier to create and manage with GitHub.

Follow the [GitHub guide to create a personal token](https://help.github.com/en/articles/creating-a-personal-access-token-for-the-command-line) and save your token. When asked to select scopes, check off the entire "repo" section (as shown below) and nothing else.

![GitHub personal access token](media/01-github-personal-access-token.png)

Once the token is generated, leave that tab open until the end of this section.

## Configure Azure Spring Cloud to access the Git repository

- Go to [the Azure portal](https://portal.azure.com/?WT.mc_id=azurespringcloud-github-judubois).
- Go to the overview page of your Azure Spring Cloud server and select "Config server" in the menu
- Configure the repository we previously created:
  - Add the repository URL, for example `https://github.com/Azure-Samples/spring-cloud-sample-public-config.git`

     >💡 Make sure you include the `.git` ending in the URL.

  - Click on `Authentication` and select `HTTP Basic`
  - The __username__ is your GitHub login name
  - The __password__ is the personal token we created in the previous section
- Click on "Validate" and wait for the operation to succeed  
- Click on "Apply" and wait for the operation to succeed

![Spring Cloud config server](media/02-config-server.png)

## Review

We have now created a private configuration repository. We have enabled Azure Spring Cloud to create a configuration server with the configuration files from this repository.

In the next section, we will create an application that consumes this configuration, specifically the custom message we defined in `application.yml`.

---

⬅️ Previous guide: [03 - Configure application logs](../03-configure-monitoring/README.md)

➡️ Next guide: [05 - Build a Spring Boot microservice using Spring Cloud features](../05-build-a-spring-boot-microservice-using-spring-cloud-features/README.md)
