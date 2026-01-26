# 11 - Configure CI/CD

__This guide is part of the [Azure Container Apps training](../README.md)__

In this section, we will use GitHub Actions to implement continuous deployment to Azure Container Apps. For simplicity, we will not implement blue-green deployments in this section, but don't hesitate to come back and add blue-green deployments after completing the remainder of the tutorial.

---

Our microservices and gateway are easy to deploy manually, but it is of course better to automate all those tasks! We are going to use [GitHub actions](https://github.com/features/actions) as a Continuous Integration / Continuous Deployment platform (or CI/CD for short). This configuration is rather simple, so it should be trivial to port it to another CI/CD platform.

We are going to automate the deployment of the `weather-service` microservice that was developed in [07 - Build a Spring Boot microservice using MySQL](../07-build-a-spring-boot-microservice-using-mysql/README.md). It is exactly the same configuration that would need to be done for the `city-service` microservice and the gateway, so if you want to automate them too, you can just copy/paste what is being done in the current guide.

## Configure GitHub

[Create a new GitHub repository](https://github.com/new) and commit the code from the `weather-service` microservice into that repository:

> 🛑 Make sure you substitute the Git URL from your own github repository (make sure you use the HTTPS URL, not the SSH URL). This should be a different repository than the one you used to store configuration in section 4. If a login dialog appears, log in with your regular GitHub credentials.

```bash
cd weather-service
git init
git add .
git commit -m 'Initial commit'
git remote add origin <GIT HTTPS URL HERE>
git push origin main
cd ..
```

You now need to allow access from your GitHub workflow to your Azure Container Apps environment. Open up a terminal and type the following command, replacing `$AZ_RESOURCE_GROUP` with the name of your resource group.

🛑 Make sure you assign the name of your resource group to the variable `AZ_RESOURCE_GROUP` or substitute the value for it in the commands below.

```bash
# Prevents a Git bash issue. Not necessary outside of Windows:
export MSYS_NO_PATHCONV=1

# Get the ARM resource ID of the resource group
export RESOURCE_ID=$(az group show --name $AZ_RESOURCE_GROUP --query id -o tsv)

# Create a service principal with a Contributor role to the resource group.
export SPNAME=sp-containerapp-$AZ_RESOURCE_GROUP
az ad sp create-for-rbac --name $SPNAME --role contributor --scopes $RESOURCE_ID --sdk-auth
```

This should output a JSON text, that you need to copy.

Then, in your GitHub project, select `Settings > Secrets` and add a new secret called `AZURE_CREDENTIALS`. Paste the JSON text you just copied into that secret.

## Create a GitHub Action

Inside the `weather-service` directory, create a new directory called `.github/workflows` and add a file called `azure-container-apps.yml` in it. This file is a GitHub workflow and will use the secret we just configured above to deploy the application to your Azure Container Apps environment.

In that file, copy/paste the following content, performing the indicated substitutions:

>🛑 You must substitute the name of your Container Apps environment for `<AZ_CONTAINERAPP_ENV>`, resource group for `<AZ_RESOURCE_GROUP>`, and container registry for `<REGISTRY_NAME>` in the YAML below.

```yaml
name: Build and deploy to Azure Container Apps

on: [push]

env:
  REGISTRY_NAME: <REGISTRY_NAME>
  IMAGE_NAME: weather-service
  RESOURCE_GROUP: <AZ_RESOURCE_GROUP>
  CONTAINERAPP_ENV: <AZ_CONTAINERAPP_ENV>
  CONTAINERAPP_NAME: weather-service

jobs:
  build-and-deploy:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2
    - name: Set up Java
      uses: actions/setup-java@v2
      with:
        distribution: 'temurin'
        java-version: 17
        check-latest: false
        cache: 'maven'
    - name: Build with Maven
      run: mvn package -DskipTests
    - name: Login to Azure
      uses: azure/login@v1
      with:
        creds: ${{ secrets.AZURE_CREDENTIALS }}
    - name: Build and push container image
      run: |
        az acr build \
          --registry ${{ env.REGISTRY_NAME }} \
          --image ${{ env.IMAGE_NAME }}:${{ github.sha }} \
          --image ${{ env.IMAGE_NAME }}:latest \
          --file Dockerfile \
          .
    - name: Deploy to Container Apps
      run: |
        az containerapp update \
          --name ${{ env.CONTAINERAPP_NAME }} \
          --resource-group ${{ env.RESOURCE_GROUP }} \
          --image ${{ env.REGISTRY_NAME }}.azurecr.io/${{ env.IMAGE_NAME }}:${{ github.sha }}

```

This workflow does the following:

- It sets up the JDK
- It compiles and packages the application using Maven
- It authenticates to Azure using the credentials we just configured
- It builds a container image and pushes it to Azure Container Registry
- It deploys the container image to your Azure Container Apps environment

This workflow is configured to be triggered whenever code is pushed to the repository.
There are many other [events that trigger GitHub actions](https://help.github.com/en/articles/events-that-trigger-workflows). You could, for example, deploy each time a new tag is created on the project.

## Test the GitHub Action

You can now commit and push the `azure-container-apps.yml` file we just created.

Going to the `Actions` tab of your  GitHub project, you should see that your project is automatically built and deployed to your Azure Container Apps environment:

![GitHub workflow](media/01-github-workflow.png)

Congratulations! Each time you `git push` your code, your microservice is now automatically deployed to production.

---

⬅️ Previous guide:  [10 - Blue/Green deployment](../10-blue-green-deployment/README.md)

➡️ Next guide: [12 - Making Microservices Talk To Each Other](../12-making-microservices-talk-to-each-other/README.md)