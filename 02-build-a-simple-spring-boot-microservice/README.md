# 02 - Build a simple Spring Boot microservice

__This guide is part of the [Azure Spring Apps training](../README.md)__

In this section, we'll build a simple Spring boot microservice and deploy it to Azure Spring Apps. This will give us a starting point for adding Spring Cloud technologies in later sections.

---

## Create a simple Spring Boot microservice

The microservice that we create in this guide is [available here](simple-microservice/).

A typical way to create Spring Boot applications is to use the Spring Initializr at  [https://start.spring.io/](https://start.spring.io/). Feel free to explore it outside this training. **For the purposes of this training, we will only invoke the Spring Initializr site via the `curl` command**.

>💡 __Note:__ All subsequent commands in this workshop should be run from the same directory, except where otherwise indicated via `cd` commands.

In an __empty__ directory execute the curl command line below:

```bash
curl https://start.spring.io/starter.tgz -d type=maven-project -d dependencies=web -d baseDir=simple-microservice -d bootVersion=3.3.2 -d javaVersion=17 | tar -xzvf -
```

> We force the Spring Boot version to be 3.3.2, and keep default settings that use the `com.example.demo` package.

## Add a new Spring MVC Controller

In the `simple-microservice/src/main/java/com/example/demo` directory, create a
new file  called `HelloController.java` next to `DemoApplication.java` file with
the following content:

```java
package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Azure Spring Apps\n";
    }
}
```

The final project is available in the ["simple-microservice" folder](simple-microservice/).

## Test the project locally

Run the project:

```bash
cd simple-microservice
./mvnw spring-boot:run &
cd ..
```

Requesting the `/hello` endpoint should return the "Hello from Azure Spring Apps" message.

```bash
curl http://127.0.0.1:8080/hello
```

Finally, kill running app:

```bash
kill %1
```

## Create and deploy the application on Azure Spring Apps

This section shows how to create an app instance and then deploy your code to it.

In order to create the app instance graphically, you can use [the Azure portal](https://portal.azure.com/?WT.mc_id=java-asa-judubois):

- Look for your Azure Spring Apps instance in your resource group
- Click on the "Apps" link under "Settings" on the navigation sidebar.
- Click on "Create App" link at the top of the Apps page.
- Create a new application named "simple-microservice", using the Java 17 environment.

![Create application](media/00-create-application.png)

- Click on "Create".

Alternatively, you can use the command line to create the app instance, which is easier:

```bash
az spring app create -n simple-microservice --runtime-version Java_17
```

You can now build your "simple-microservice" project and deploy it to Azure Spring Apps:

```bash
cd simple-microservice
./mvnw clean package
az spring app deploy -n simple-microservice --artifact-path target/demo-0.0.1-SNAPSHOT.jar
cd ..
```

This creates a jar file on your local disk and uploads it to the app instance you created in the preceding step.  The `az` command will output a result in JSON.  You don't need to pay attention to this output right now, but in the future, you will find it useful for diagnostic and testing purposes.

## Test the project in the cloud

Go to [the Azure portal](https://portal.azure.com/?WT.mc_id=azurespringcloud-github-judubois):

- Look for your Azure Spring Apps instance in your resource group
- Click "Apps" in the "Settings" section of the navigation pane and select "simple-microservice"
- Find the "Test endpoint" in the "Essentials" section.
![Test endpoint](media/01-test-endpoint.png)
- This will give you something like:
  `https://primary:BBQM6nsYnmmdQREXQINityNx63kWUbjsP7SIvqKhOcWDfP6HJTqg27klMLaSfpTB@rwo1106f.test.azuremicroservices.io/simple-microservice/default/`
  >💡 Note the text between `https://` and `@`.  These are the basic authentication credentials, without which you will not be authorized to access the service.
- Append `hello` to the URL.  Failure to do this will result in a "404 not found".

You can now use cURL again to test the `/hello` endpoint, this time served by Azure Spring Apps.  For example.

```bash
curl https://primary:...simple-microservice/default/hello
```

If successful, you should see the message: `Hello from Azure Spring Apps`.

## Conclusion

Congratulations, you have deployed your first Spring Boot microservice to Azure Spring Apps!

If you need to check your code, the final project is available in the ["simple-microservice" folder](simple-microservice/).

Here is the final script to build and deploy everything that was done in this guide:

```
curl https://start.spring.io/starter.tgz -d type=maven-project -d dependencies=web -d baseDir=simple-microservice -d bootVersion=3.3.2 -d javaVersion=17 | tar -xzvf -
cd simple-microservice
cat > HelloController.java << EOF
package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Azure Spring Apps";
    }
}
EOF
mv HelloController.java src/main/java/com/example/demo/HelloController.java
az spring app create -n simple-microservice --runtime-version Java_17
./mvnw clean package
az spring app deploy -n simple-microservice --artifact-path target/demo-0.0.1-SNAPSHOT.jar
cd ..
```

---

⬅️ Previous guide: [01 - Create an Azure Spring Apps instance](../01-create-an-azure-spring-cloud-instance/README.md)

➡️ Next guide: [03 - Configure monitoring](../03-configure-monitoring/README.md)
