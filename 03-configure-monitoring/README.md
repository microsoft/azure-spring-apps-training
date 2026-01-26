# 03 - Configure application logs

__This guide is part of the [Azure Container Apps training](../README.md)__

Now that we have an application deployed, we'll configure Log Analytics, so that we can quickly search the application's logs if something goes wrong. We'll see how to take advantage of Log Analytics in a later section, but because it takes time for log entries to start coming in, we'll do the configuration steps here before moving on with the training.

---

## Configure log aggregation

There are actually three ways to access your application's logs: [Azure Storage](https://docs.microsoft.com/en-us/azure/storage/common/storage-introduction/?WT.mc_id=azurespringcloud-github-judubois), [Azure Events Hub](https://docs.microsoft.com/en-us/azure/event-hubs/?WT.mc_id=azurespringcloud-github-judubois), and [Log Analytics](https://docs.microsoft.com/en-us/azure/azure-monitor/log-query/get-started-portal/?WT.mc_id=azurespringcloud-github-judubois). We will focus here on Log Analytics as it's the most common one, and as it's integrated into Azure Container Apps.

[Log Analytics](https://docs.microsoft.com/en-us/azure/azure-monitor/log-query/get-started-portal/?WT.mc_id=azurespringcloud-github-judubois) is part of [Azure Monitor](https://azure.microsoft.com/en-us/services/monitor/?WT.mc_id=azurespringcloud-github-judubois), which is well-integrated into Azure Container Apps and which we will also use for metrics monitoring.

Having completed the setup in [Section 00](../00-setup-your-environment/README.md), you should have a Log Analytics workspace named `sclab-la-<unique string>` in your resource group for this workshop. 

Azure Container Apps automatically sends logs to Log Analytics when you create the environment. When you created the Container Apps environment in Section 01, it was automatically configured to use a Log Analytics workspace.

To verify the logs are flowing:

- Navigate to your Azure Container Apps environment in the Azure Portal
- Select "Logs" in the "Monitoring" section of the navigation pane
- You can now run queries against your container app logs using the Kusto query language

For example, to see recent logs from your simple-microservice:

```kusto
ContainerAppConsoleLogs_CL
| where ContainerAppName_s == "simple-microservice"
| order by TimeGenerated desc
| take 100
```

You can also view logs for individual container apps:

- Navigate to your container app (e.g., "simple-microservice") in the Azure Portal
- Select "Log stream" to see real-time logs
- Or select "Logs" for more advanced querying capabilities

⬅️ Previous guide: [02 - Build a simple Spring Boot microservice](../02-build-a-simple-spring-boot-microservice/README.md)

➡️ Next guide: [04 - Configure a Spring Cloud Config server](../04-configure-a-spring-cloud-config-server/README.md)
