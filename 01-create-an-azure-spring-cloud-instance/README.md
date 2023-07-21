# 01 - Create an Azure Spring Apps instance

__This guide is part of the [Azure Spring Apps training](../README.md)__

In this section, we'll create an Azure Spring Apps instance using Azure CLI. While there are other ways of creating Azure resources, Azure CLI is the quickest and simplest method.

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

With these variables set, we can now create the Azure Spring Apps instance.

```bash
az spring create \
    -g "$AZ_RESOURCE_GROUP" \
    -n "$AZ_SPRING_APPS_NAME" \
    --sku standard
```

For the remainder of this workshop, we will be running Azure CLI commands referencing the same resource group and Azure Spring Apps instance. So let's set them as defaults, so we don't have to specify them again:

```bash
az configure --defaults group=$AZ_RESOURCE_GROUP
az configure --defaults spring=$AZ_SPRING_APPS_NAME
```

---

⬅️ Previous guide: [00 - Set Up Your Environment](../00-setup-your-environment/README.md)

➡️ Next guide: [02 - Build a simple Spring Boot microservice](../02-build-a-simple-spring-boot-microservice/README.md)
