# 04a - Using Azure Repos for Configuration

__This guide is part of the [Azure Container Apps training](../README.md)__

Configure a [Spring Cloud Config Server](https://cloud.spring.io/spring-cloud-config), deployed as a container app, to be used by Spring Boot microservices. 

---

## Create a Git repository for storing the application configuration

1. Create a [new Azure Repos repository](https://docs.microsoft.com/azure/devops/repos/git/create-new-repo?view=azure-devops). You can use a new project or an existing project, as long as your permissions on the project are sufficient to create, read, write, and generate credentials to repositories. The project should not be public.

1. In the new private repository, add a new `application.yml` file which will store configuration data for all our microservices.

    Typically, each Spring Boot application includes such a file within the application binaries to contain application settings. A Spring Cloud Configuration Server allow such settings to be stored at a single location and served from a single source.
    
    For the moment, our `application.yml` will just store a message to check if the configuration is successful:
    
    ```yml
    application:
        message: Configured by Azure Container Apps
    ```
    
    Commit and push the new file.

## Obtain Repository URL and Credentials

Spring Cloud Config Server can access Git repositories that are public, secured by SSH, or secured using HTTP basic authentication. We will use that last option, as it is easier to create and manage with Azure Repos.

1. In the Azure Repos portal for your project, click the "Clone" button:

    ![Clone Button](media/04-clone-button.png)

1. Copy the clone URL from the textbox. This URL will typically be in the form:

    ```Text
        https://<organization name>@dev.azure.com/<organization name>/<project name>/_git/<repository name>
    ```

    Remove everything after `https://` and before `dev.azure.com`, including the `@`. The resulting URL should be in the form:

    ```Text
        https://dev.azure.com/<organization name>/<project name>/_git/<repository name>
    ```

    Save this URL for use in the next section.

1. Click "Generate Git Credentials". A username and password will appear. Save these for use in the next section.


## Deploy Spring Cloud Config Server to Azure Container Apps

Now deploy the Spring Cloud Config Server as a container app, configured with your Azure Repos credentials:

```bash
# Set your Azure DevOps organization, project, and repository details
export AZDO_ORG_URL=https://dev.azure.com/<organization name>/<project name>/_git/<repository name>
export AZDO_USERNAME=<username from previous section>
export AZDO_PASSWORD=<password from previous section>

az containerapp create \
    --name config-server \
    --resource-group $AZ_RESOURCE_GROUP \
    --environment $AZ_CONTAINERAPP_ENV \
    --image hyness/spring-cloud-config-server:latest \
    --target-port 8888 \
    --ingress internal \
    --env-vars \
        SPRING_CLOUD_CONFIG_SERVER_GIT_URI=$AZDO_ORG_URL \
        SPRING_CLOUD_CONFIG_SERVER_GIT_DEFAULT_LABEL=main \
        SPRING_CLOUD_CONFIG_SERVER_GIT_USERNAME=$AZDO_USERNAME \
        SPRING_CLOUD_CONFIG_SERVER_GIT_PASSWORD=$AZDO_PASSWORD \
    --min-replicas 1 \
    --max-replicas 1
```

## Review

We have now created a private configuration repository and deployed a Spring Cloud Config Server as a container app with the configuration files from this repository.

In the next section, we will create an application that consumes this configuration, specifically the custom message we defined in `application.yml`.

---

⬅️ Previous guide: [03 - Configure application logs](../03-configure-monitoring/README.md)

⬅ GitHub version of this guide: [# 04 - Configure a Spring Cloud Config server](Readme.md)

➡️ Next guide: [05 - Build a Spring Boot microservice using Spring Cloud features](../05-build-a-spring-boot-microservice-using-spring-cloud-features/README.md)

---
