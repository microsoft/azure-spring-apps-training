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


## Deploy to Azure Spring Apps

Use the following command to specify the app name on Azure Spring Apps and to allocate the required resources:

```bash
az spring app create \
    --name ai-weather-service \
    --cpu 2 \
    --memory 4Gi \
    --min-replicas 2 \
    --max-replicas 2 \
    --assign-endpoint true
```

Build the jar package using the command: `./mvnw clean package`.

Use the following command to deploy the *.jar* file for the app:

```bash
az spring app deploy \
    --name ai-weather-service \
    --artifact-path target/demo-0.0.1-SNAPSHOT.jar \
    --env SPRING_AI_AZURE_OPENAI_API_KEY=${SPRING_AI_AZURE_OPENAI_API_KEY} SPRING_AI_AZURE_OPENAI_ENDPOINT=${SPRING_AI_AZURE_OPENAI_ENDPOINT} \
    --runtime-version Java_17
```

## Test the project in the cloud

- Go to "Apps" in your Azure Spring Apps instance.
  - Verify that `ai-weather-service` has a `Registration status` which says `1/1`. This shows that it is correctly registered in Spring Cloud Service Registry.
  - Select `ai-weather-service` to see more information about the microservice.
- Click the "URL" that is provided to open it in another browser tab, then type `Basel` into the `Question` prompt box. Click the `Ask AI` button to see the results!
