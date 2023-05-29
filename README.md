# CapMO
## Running the application

The project is a standard Maven project. To run it from the command line,
type `mvnw` (Windows), or `./mvnw` (Mac & Linux), then open
http://localhost:8080 in your browser.

You can also import the project to your IDE of choice as you would with any
Maven project. Read more on [how to import Vaadin projects to different IDEs](https://vaadin.com/docs/latest/guide/step-by-step/importing) (Eclipse, IntelliJ IDEA, NetBeans, and VS Code).

## Deploying to Production

To create a production build, call `mvnw clean package -Pproduction` (Windows),
or `./mvnw clean package -Pproduction` (Mac & Linux).
This will build a JAR file with all the dependencies and front-end resources,
ready to be deployed. The file can be found in the `target` folder after the build completes.

Once the JAR file is built, you can run it using
`java -jar target/capmo-1.0-SNAPSHOT.jar`

## Available views
http://localhost:8080/login
http://localhost:8080/register
http://localhost:8080/logout

### User views
http://localhost:8080/home
http://localhost:8080/profile
http://localhost:8080/profiles/{username}
http://localhost:8080/search
http://localhost:8080/notifications

### Admin views
http://localhost:8080/admin
http://localhost:8080/posts
