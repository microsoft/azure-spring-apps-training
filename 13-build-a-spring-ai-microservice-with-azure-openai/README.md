# 13 - Build a Spring AI microservice with Azure OpenAI

## Prepare Azure OpenAI Service

First, define the following environment variables for use in creating the AI resources we'll use in this example:

```bash
export LOCATION="eastus"
export OPENAI_RESOURCE_NAME="<Azure-OpenAI-resource-name>"
```

Next, run the following command to create an Azure OpenAI resource in the the default resource group:

```bash
az cognitiveservices account create \
    -n ${OPENAI_RESOURCE_NAME} \
    -l ${LOCATION} \
    --kind OpenAI \
    --sku s0 \
    --custom-domain ${OPENAI_RESOURCE_NAME}   
```

To create the model deployments for `text-embedding-ada-002` and `gpt-35-turbo-16k` in your Azure OpenAI service, execute the following commands:

```bash
az cognitiveservices account deployment create \
    -n ${OPENAI_RESOURCE_NAME} \
    --deployment-name text-embedding-ada-002 \
    --model-name text-embedding-ada-002 \
    --model-version "2"  \
    --model-format OpenAI

az cognitiveservices account deployment create \
    -n ${OPENAI_RESOURCE_NAME} \
    --deployment-name gpt-35-turbo-16k \
    --model-name gpt-35-turbo-16k \
    --model-version "0613"  \
    --model-format OpenAI \
    --sku Standard \
    --capacity 120
```

You'll need to retrieve the endpoint and API key for your Azure OpenAI resource to proceed. To do so, follow the guidance in [this doc](https://learn.microsoft.com/en-us/azure/ai-services/openai/quickstart?pivots=programming-language-java).

## Create a Spring AI microservice

The microservice that we create in this guide is [available here](ai-weather-service/).

To create our microservice, we will invoke the Spring Initalizr service from the command line:

```bash
NOTE: For now, please use the project link above.
```

## Run locally

First, export the environment variables required by Spring AI:

```shell
export SPRING_AI_AZURE_OPENAI_API_KEY=<INSERT_KEY_HERE>
export SPRING_AI_AZURE_OPENAI_ENDPOINT=<INSERT_ENDPOINT_URL_HERE>
```

Next, start the service by running `./mvnw spring-boot:run`.

Finally, open `http://localhost:8080` in a browser to access the application.


## Deploy to Azure Container Apps

Use the following command to create the container app in your Azure Container Apps environment:

```bash
az containerapp create \
    --name ai-weather-service \
    --resource-group $AZ_RESOURCE_GROUP \
    --environment $AZ_CONTAINERAPP_ENV \
    --image <your-registry>/ai-weather-service:latest \
    --target-port 8080 \
    --ingress external \
    --cpu 2 \
    --memory 4Gi \
    --min-replicas 2 \
    --max-replicas 2 \
    --env-vars \
      SPRING_AI_AZURE_OPENAI_API_KEY=${SPRING_AI_AZURE_OPENAI_API_KEY} \
      SPRING_AI_AZURE_OPENAI_ENDPOINT=${SPRING_AI_AZURE_OPENAI_ENDPOINT}
```

Build the jar package using the command: `./mvnw clean package`.

Build and deploy the container image:

```bash
# Build and push the container image
az acr build \
    --registry <your-registry> \
    --image ai-weather-service:latest \
    --file Dockerfile \
    .

# Update the container app
az containerapp update \
    --name ai-weather-service \
    --resource-group $AZ_RESOURCE_GROUP \
    --image <your-registry>.azurecr.io/ai-weather-service:latest \
    --set-env-vars \
      SPRING_AI_AZURE_OPENAI_API_KEY=${SPRING_AI_AZURE_OPENAI_API_KEY} \
      SPRING_AI_AZURE_OPENAI_ENDPOINT=${SPRING_AI_AZURE_OPENAI_ENDPOINT}
```

## Test the project in the cloud

- Go to your Azure Container Apps environment in the Azure Portal.
  - Select the `ai-weather-service` container app to see more information about the microservice.
  - Navigate to the "Ingress" section to find the application URL.
- Click the URL to open it in another browser tab, then type `Basel` into the `Question` prompt box. Click the `Ask AI` button to see the results!
