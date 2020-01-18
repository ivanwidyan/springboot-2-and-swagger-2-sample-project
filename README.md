# Spring Boot 2 and Swagger 2 Sample Project

This is an example Spring boot 2 with REST, JPA, and Swagger 2. The swagger is handled by
[Springfox](http://springfox.io) which is a library for enabling swagger.

## Running

To build the jar:

```
mvn clean package
```

This command produces a jar file in `target` folder.

To run the jar:

```
java -jar target/springboot-sample-project-0.0.1-SNAPSHOT.jar
```

## Endpoints

These are the list of APIs available in this sample application.

|Method | 	Url		| 	Description |
|-------| ------- | ----------- |
|GET| /api/members | Get all members |
|GET| /api/member |	Get member by id |
|POST| /api/member | Create member |
|PUT| /api/member| Update member |
|DELETE| /api/member| Delete member |

### Screenshot

![Swagger2 Screenshot](https://user-images.githubusercontent.com/12959761/72666581-6849a500-3a46-11ea-934a-515ff7310bbf.png)