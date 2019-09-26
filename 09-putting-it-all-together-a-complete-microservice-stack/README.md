# 09 - Putting it all together, a complete microservice stack

__This guide is part of the [Azure Spring Cloud training](../README.md)__

Use a front-end to access graphically our complete microservice stack. Monitor our services with Azure Spring Cloud's distributed tracing mechanism, and scale our services depending on our needs.

---

## Add a front-end to the microservices stack

We now have a complete microservices stack:

- A gateway based on Spring Cloud Gateway.
- A reactive `city-servivce` microservice, that stores its data on Cosmos DB.
- A `weather-service` microservice, that stores its data on MySQL

In order to finish this architecture, we need to add a front-end to it:

- We have already built a VueJS application, that is available in the ["weather-app" folder](weather-app/).
- This front-end could be hosted in Azure Spring Cloud, or at least under the same domain name.
- If you are familiar with NodeJS and Vue CLI, you can run this application locally by typing `npm install && vue ui`.

In order to simplify this part, which has little value for understanding Azure Spring Cloud, we have already this application running:

__[https://spring-training.azureedge.net/](https://spring-training.azureedge.net/)__

For your information, this website is hosted on Azure Storage and served through Azure CDN for optimum performance.

Go to [https://spring-training.azureedge.net/](https://spring-training.azureedge.net/), input your Spring Cloud Gateway's public URL in the text field, and click on "Go". You should see the following screen:

![VueJS front-end](media/01-vuejs-frontend.png)



---

⬅️ Previous guide: [08 - Build a Spring Cloud Gateway](../08-build-a-spring-cloud-gateway/README.md)
