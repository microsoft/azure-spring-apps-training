# 13 - Bulid a Spring-AI microservice with Azure OpenAI

## Prepare Azure OpenAI Service

1. Use the following commands to define variables:

   ```bash
   LOCATION="eastus"
   OPENAI_RESOURCE_NAME="<Azure-OpenAI-resource-name>"
   ```

1. Run the following command to create an Azure OpenAI resource in the the resource group.

   ```bash
   az cognitiveservices account create \
      -n ${OPENAI_RESOURCE_NAME} \
      -l ${LOCATION} \
      --kind OpenAI \
      --sku s0 \
      --custom-domain ${OPENAI_RESOURCE_NAME}   
   ```

1. Create the model deployments for `text-embedding-ada-002` and `gpt-35-turbo-16k` in your Azure OpenAI service.
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

1. Retrieve the endpoint and API key of your Azure OpenAI resource, following [this doc](https://learn.microsoft.com/en-us/azure/ai-services/openai/quickstart?pivots=programming-language-java).


## Run in local

1. Export the environment variables required by the `spring-ai`:
    ```shell
    export SPRING_AI_AZURE_OPENAI_API_KEY=<INSERT_KEY_HERE>
    export SPRING_AI_AZURE_OPENAI_ENDPOINT=<INSERT_ENDPOINT_URL_HERE>
    ```

1. Start the service by running `mvn spring-boot:run`.

1. Open the browser at `http://localhost:8080`


## Deploy to Azure Spring Apps
1. Use the following command to specify the app name on Azure Spring Apps and to allocate required resources:

    ```bash
    az spring app create \
        --name ai-weather-service \
        --cpu 2 \
        --memory 4Gi \
        --min-replicas 2 \
        --max-replicas 2 \
        --assign-endpoint true
    ```

1. Build the jar package by `mvn clean package`.

1. Use the following command to deploy the *.jar* file for the app:

    ```bash
    az spring app deploy \
        --name ai-weather-service \
        --artifact-path target/demo-0.0.1-SNAPSHOT.jar \
        --env SPRING_AI_AZURE_OPENAI_API_KEY=${SPRING_AI_AZURE_OPENAI_API_KEY} SPRING_AI_AZURE_OPENAI_ENDPOINT=${SPRING_AI_AZURE_OPENAI_ENDPOINT} \
        --runtime-version Java_17
    ```