# 01 - Create a cluster

__This guide is part of the [Azure Spring Cloud training](../README.md)__

Basics on creating a cluster and configuring the CLI to work efficiently.

---

## Install the CLI and authenticate

Install the [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli/?WT.mc_id=azurespringcloud-github-judubois) and log in to your account:

```bash
az login
```

Configure the CLI to use Azure subscription you want to use for this training:

```bash
# List all subscriptions
az account list -o table

# Set active subscription
az account set --subscription <target subscription ID>
```

## Install the Azure Spring Cloud CLI extension

__This is temporary, and will not be necessary when the service is released__

```bash
az extension add -y --source https://azureclitemp.blob.core.windows.net/spring-cloud/spring_cloud-0.1.0-py2.py3-none-any.whl
```

## Create an Azure Spring Cloud instance

__This is temporary, and will not be necessary when the service is released__

- Azure Spring Cloud is currently in private preview, [fill this form to request an access](https://aka.ms/AzureSpringCloudInterest).
- Once you have access, [click here](https://portal.azure.com/?WT.mc_id=azurespringcloud-github-judubois&microsoft_azure_marketplace_ItemHideKey=AppPlatformExtension#blade/Microsoft_Azure_Marketplace/MarketplaceOffersBlade/selectedMenuItemId/home/searchQuery/spring) to access the cluster creation page.

![Cluster creation](media/01-create-azure-spring-cloud.png)

- Click on "Create"
- Select your subscription, resource group name, name of the service and location
- Once everything is validated, the cluster can be created

![Cluster configuration](media/02-creation-details.png)

Creating the cluster will take a few minutes.

## Configure the CLI to use that cluster

Using the cluster's resource group and name by default will save you a lot of typing later:

```bash
az configure --defaults group=<resource group name>
az configure --defaults spring-cloud=<service instance name>
```

---

➡️ Next guide: [02 - Build a simple Spring Boot microservice](../02-build-a-simple-spring-boot-microservice/README.md)
