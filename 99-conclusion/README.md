# Conclusion

__This guide is part of the [Azure Spring Cloud training](../README.md)__

---

## Cleaning up

Unless you plan to perform additional tasks with the Azure resources from the workshop (such as tutorials referenced below), it is important to destroy the resources that we created for it to avoid the cost of keeping them provisioned.

The easiest way to do this is to delete the entire resource group.

>🛑Substitute the name of your resource group for `$RESOURCE_GROUP_NAME` below:

```bash
az group delete -g "$RESOURCE_GROUP_NAME" --yes --no-wait
```

## Additional Resources

As an addendum to this workshop, consider taking the [tutorial on using alerts and action groups with Azure Spring Cloud](https://docs.microsoft.com/azure/spring-cloud/spring-cloud-tutorial-alerts-action-groups/?WT.mc_id=azurespringcloud-github-judubois) to detect and respond to abberant conditions.

Have a look through the [Azure Spring Cloud documentation](https://docs.microsoft.com/azure/spring-cloud/?WT.mc_id=azurespringcloud-github-judubois) for more quickstarts, tutorials, and reference materials.

---

⬅️ Previous guide:  [12 - Making Microservices Talk To Each Other](../12-making-microservices-talk-to-each-other/README.md)
