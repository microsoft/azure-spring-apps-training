# 01 - Create an Azure Spring Cloud instance

__This guide is part of the [Azure Spring Cloud training](../README.md)__

Basics on creating an instance and configuring the CLI to work efficiently.

---

## Verify Azure Subscription

Ensure your Azure CLI is logged into your Azure subscription.

>💡If you intend to use the Docker container as recommended, do this inside the container bash session.

```bash
az login # Sign into an azure account
az account show # See the currently signed-in account.
```

Ensure your default subscription is the one you intend to use for this lab, and if not - set the subscription via 
```az account set --subscription <SUBSCRIPTION_ID>```

## Create an Azure Spring Cloud instance

In this section, we will create our Azure Spring Cloud instance using Azure CLI.

First, you will need to come up with a name for your Azure Spring Cloud instance.

- __The name must be unique among all Azure Spring Cloud instances across all of Azure__. Consider using your username as part of the name.
- The name can contain only lowercase letters, numbers and hyphens. The first character must be a letter. The last character must be a letter or number. The value must be between 4 and 32 characters long.

To limit typing, set the variable `RESOURCE_GROUP_NAME` to the name of the resource group created in the previous section. Set the variable `SPRING_CLOUD_NAME` to the name of the Azure Spring Cloud instance to be created:

>🛑Be sure to substitute your own values for `RESOURCE_GROUP_NAME` and `SPRING_CLOUD_NAME` as described above. __`SPRING_CLOUD_NAME` must be globally unique.__

```bash
RESOURCE_GROUP_NAME=spring-cloud-lab
SPRING_CLOUD_NAME=azure-spring-cloud-lab
```

With these variables set, we can now create the Azure Spring Cloud instance:

```bash
az spring-cloud create \
    -g "$RESOURCE_GROUP_NAME" \
    -n "$SPRING_CLOUD_NAME"
```

For the remainder of this workshop, we will be running Azure CLI commands referencing the same resource group and Azure Spring Cloud instance. So let's set them as defaults, so we don't have to specify them again:

```bash
az configure --defaults group=$RESOURCE_GROUP_NAME
az configure --defaults spring-cloud=$SPRING_CLOUD_NAME
```

---

⬅️ Previous guide: [00 - Set Up Your Environment](../00-setup-your-environment/README.md)

➡️ Next guide: [02 - Build a simple Spring Boot microservice](../02-build-a-simple-spring-boot-microservice/README.md)
