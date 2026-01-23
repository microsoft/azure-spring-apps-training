# Conclusion

__This guide is part of the [Azure Container Apps training](../README.md)__

---

## Cleaning up

Unless you plan to perform additional tasks with the Azure resources from the workshop (such as tutorials referenced below), it is important to destroy the resources that we created for it to avoid the cost of keeping them provisioned.

The easiest way to do this is to delete the entire resource group.

>🛑 Substitute the name of your resource group for `$AZ_RESOURCE_GROUP` below:

```bash
az group delete -g "$AZ_RESOURCE_GROUP" --yes --no-wait
```

## Additional Resources

As an addendum to this workshop, consider learning more about [Azure Container Apps monitoring and observability](https://learn.microsoft.com/azure/container-apps/observability) to detect and respond to abnormal conditions.

Also, check out the tutorial to [deploy Azure Container Apps in a virtual network](https://learn.microsoft.com/azure/container-apps/vnet-custom).

Have a look through the [Azure Container Apps documentation](https://learn.microsoft.com/azure/container-apps/) for more quickstarts, tutorials, and reference materials.

Find more about [monitoring Container Apps with Application Insights](https://learn.microsoft.com/azure/container-apps/application-insights) for distributed tracing and performance monitoring.

---

⬅️ Previous guide:  [12 - Making Microservices Talk To Each Other](../12-making-microservices-talk-to-each-other/README.md)
