# 01 - Create an Azure Container Apps environment

__This guide is part of the [Azure Container Apps training](../README.md)__

In this section, we'll create an Azure Container Apps environment using Azure CLI. While there are other ways of creating Azure resources, Azure CLI is the quickest and simplest method.

---

## Verify Azure Subscription

Ensure your Azure CLI is logged into your Azure subscription.

>💡 If using Windows, make sure you enter these commands and all others that follow in Git Bash. **Do not use WSL, CloudShell, or any other shell.**

```bash
az login # Sign into an azure account
az account show # See the currently signed-in account.
```

Ensure your default subscription is the one you intend to use for this lab, and if not - set the subscription via 
```az account set --subscription <SUBSCRIPTION_ID>```

## Create an Azure Container Apps environment

In this section, we will create our Azure Container Apps environment using Azure CLI.

First, you will need to come up with a name for your Azure Container Apps environment.

- __The name must be unique within your resource group__.
- The name can contain only lowercase letters, numbers and hyphens. The first character must be a letter. The last character must be a letter or number. The value must be between 2 and 32 characters long.

To limit typing, set the variable `AZ_RESOURCE_GROUP` to the name of the resource group created in the previous section. Set the variable `AZ_CONTAINERAPP_ENV` to the name of the Azure Container Apps environment to be created:

>🛑 Be sure to substitute your own values for `AZ_RESOURCE_GROUP` and `AZ_CONTAINERAPP_ENV` as described above.

```bash
export AZ_RESOURCE_GROUP=spring-apps-lab
export AZ_CONTAINERAPP_ENV=aca-env-lab
export AZ_LOCATION=eastus
```

With these variables set, we can now create the Azure Container Apps environment.

```bash
az containerapp env create \
    --name "$AZ_CONTAINERAPP_ENV" \
    --resource-group "$AZ_RESOURCE_GROUP" \
    --location "$AZ_LOCATION"
```

For the remainder of this workshop, we will be running Azure CLI commands referencing the same resource group and Azure Container Apps environment. So let's set them as defaults, so we don't have to specify them again:

```bash
az configure --defaults group=$AZ_RESOURCE_GROUP
az configure --defaults location=$AZ_LOCATION
```

---

⬅️ Previous guide: [00 - Set Up Your Environment](../00-setup-your-environment/README.md)

➡️ Next guide: [02 - Build a simple Spring Boot microservice](../02-build-a-simple-spring-boot-microservice/README.md)
