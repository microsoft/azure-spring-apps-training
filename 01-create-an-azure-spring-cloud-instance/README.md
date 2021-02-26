# Exercise 1 - Create an Azure Spring Cloud instance

In this section, we'll create an Azure Spring Cloud instance using Azure CLI. While there are other ways of creating Azure resources, Azure CLI is the quickest and simplest method.

---
### Task 1: Verify Azure Subscription

1. Ensure your Azure CLI is logged into your Azure subscription.

>💡 Make sure you enter these commands and all others that follow in **Git Bash**. **Do not use WSL, CloudShell, or any other shell.**

```bash
az login # Sign into an azure account
az account show # See the currently signed-in account.
```

Ensure your default subscription is the one you intend to use for this lab, and if not - set the subscription via 
```az account set --subscription <SUBSCRIPTION_ID>```

### Task 2: Create an Azure Spring Cloud instance

1. In this section, we will create our Azure Spring Cloud instance using Azure CLI.

2. First, you will need to come up with a name for your Azure Spring Cloud instance.

- __The name must be unique among all Azure Spring Cloud instances across all of Azure__. Consider using **azure-spring-cloud-lab-DID** where **DID** is the **DeploymentID** (Unique Id) which can be found from the **Environment Details** page.

To limit typing, set the variable `AZ_RESOURCE_GROUP` to the name of the resource group **spring-cloud-workshop-DID**. And set the variable `AZ_SPRING_CLOUD_NAME` to **azure-spring-cloud-lab-DID**

>🛑 Be sure to substitute your DID in `AZ_RESOURCE_GROUP` and `AZ_SPRING_CLOUD_NAME`, where **DID** is the **DeploymentID** (Unique Id) which can be found from the **Environment Details** page.

```bash
AZ_RESOURCE_GROUP=spring-cloud-workshop-DID
AZ_SPRING_CLOUD_NAME=azure-spring-cloud-lab-DID
```

With these variables set, we can now create the Azure Spring Cloud instance. To enable the Java in-process monitoring agent, we add the `enable-java-agent` flag.

```bash
az spring-cloud create \
    -g "$AZ_RESOURCE_GROUP" \
    -n "$AZ_SPRING_CLOUD_NAME" \
    --enable-java-agent \
    --sku standard
```

For the remainder of this workshop, we will be running Azure CLI commands referencing the same resource group and Azure Spring Cloud instance. So let's set them as defaults, so we don't have to specify them again:

```bash
az configure --defaults group=$AZ_RESOURCE_GROUP
az configure --defaults spring-cloud=$AZ_SPRING_CLOUD_NAME
```

---
