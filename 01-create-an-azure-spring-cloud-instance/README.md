# 01 - Create an Azure Spring Apps instance

__This guide is part of the [Azure Spring Apps training](../README.md)__

In this section, we'll create an Azure Spring Apps instance using Azure CLI. While there are other ways of creating Azure resources, Azure CLI is the quickest and simplest method.

---

## Verify Azure Subscription

Ensure your Azure CLI is logged into your Azure subscription.

>💡 If using Windows, you can run those commands in Git Bash, WSL 2.0 or another cloud shell.

```bash
az login # Sign into an azure account
az account show # See the currently signed-in account.
```

Ensure your default subscription is the one you intend to use for this lab, and if not - set the subscription via 
```az account set --subscription <SUBSCRIPTION_ID>```

## Create an Azure Spring Apps instance

In this section, we will create our Azure Spring Apps instance using Azure CLI.

First, you will need to come up with a name for your Azure Spring Apps instance.

- __The name must be unique among all Azure Spring Apps instances across all of Azure__. Consider using your username as part of the name.
- The name can contain only lowercase letters, numbers and hyphens. The first character must be a letter. The last character must be a letter or number. The value must be between 4 and 32 characters long.

To limit typing, set the variable `AZ_RESOURCE_GROUP` to the name of the resource group created in the previous section. Set the variable `AZ_SPRING_APPS_NAME` to the name of the Azure Spring Apps instance to be created:

>🛑 Be sure to substitute your own values for `AZ_RESOURCE_GROUP` and `AZ_SPRING_APPS_NAME` as described above. __`AZ_SPRING_APPS_NAME` must be globally unique, use lowercase letters and should not have special characters.__

```bash
export AZ_RESOURCE_GROUP=spring-apps-lab
export AZ_SPRING_APPS_NAME=azure-spring-apps-lab
```

To use the most developer focussed option of Azure Spring Apps, the Dedicated Consumption Plan, we create a container environment and the workload profile.

```bash
export AZ_CONTAINER_APP_ENV=$AZ_SPRING_APPS_NAME'-env'

az containerapp env create \
    --resource-group $AZ_RESOURCE_GROUP \
    --name $AZ_CONTAINER_APP_ENV \
    --enable-workload-profiles

az containerapp env workload-profile set \
    --resource-group $AZ_RESOURCE_GROUP \
    --name $AZ_CONTAINER_APP_ENV \
    --workload-profile-name my-wlp \
    --workload-profile-type D4 \
    --min-nodes 1 \
    --max-nodes 2

export MANAGED_ENV_RESOURCE_ID=$(az containerapp env show \
    --resource-group $AZ_RESOURCE_GROUP \
    --name $AZ_CONTAINER_APP_ENV \
    --query id \
    --output tsv)
```

With these prerequists set, we can now create the Azure Spring Apps instance.

```bash
az spring create \
    --resource-group $AZ_RESOURCE_GROUP \
    --name $AZ_SPRING_APPS_NAME \
    --managed-environment $MANAGED_ENV_RESOURCE_ID \
    --sku StandardGen2
```

For the remainder of this workshop, we will be running Azure CLI commands referencing the same resource group and Azure Spring Apps instance. So let's set them as defaults, so we don't have to specify them again:

```bash
az configure --defaults group=$AZ_RESOURCE_GROUP
az configure --defaults spring=$AZ_SPRING_APPS_NAME
```

---

⬅️ Previous guide: [00 - Set Up Your Environment](../00-setup-your-environment/README.md)

➡️ Next guide: [02 - Build a simple Spring Boot microservice](../02-build-a-simple-spring-boot-microservice/README.md)
