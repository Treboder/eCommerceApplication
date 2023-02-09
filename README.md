[![Build Maven Project](https://github.com/Treboder/eCommerceApplication/actions/workflows/maven-build.yml/badge.svg)](https://github.com/Treboder/eCommerceApplication/actions/workflows/maven-build.yml)
[![Push image to Docker Hub](https://github.com/Treboder/eCommerceApplication/actions/workflows/docker-publish.yml/badge.svg)](https://github.com/Treboder/eCommerceApplication/actions/workflows/docker-publish.yml)
[![Push image to GitHub Container Registry](https://github.com/Treboder/eCommerceApplication/actions/workflows/ghcr-publish.yml/badge.svg)](https://github.com/Treboder/eCommerceApplication/actions/workflows/ghcr-publish.yml)

# eCommerce Application

This project demonstrates some security and DevOps skills as part of an Udacity Project. 
The web application provides proper authentication and authorization controls so users can only access their data, and that data can only be accessed in a secure way.
The project is written in Java using Spring Boot, Hibernate ORM, and the H2 database as in-memory database.
Unit tests ensure quality with at least 80% code coverage.

The project features:
* [Spring Boot JPA data management](https://spring.io/guides/gs/accessing-data-jpa/)
* [REST services with Spring Boot Web](https://spring.io/guides/tutorials/rest/)
* [JUnit Testing](https://junit.org/junit5/)
* [SLF4J Logging](https://www.slf4j.org/)
* [Containerization with Docker Hub image](https://hub.docker.com/r/treboder/ecommerceapplication/tags)
* [Containerization with Github Container Registry](https://github.com/Treboder/eCommerceApplication/pkgs/container/ecommerceapplication) 
* [CI via Github Actions](https://github.com/features/actions)

## Project Structure

Once the project is set up, you will see 5 packages:

* demo - this package contains the main method which runs the application

* model.persistence - this package contains the data models that Hibernate persists to H2. There are 4 models: Cart, for holding a User's items; Item , for defining new items; User, to hold user account information; and UserOrder, to hold information about submitted orders. Looking back at the application “demo” class, you'll see the `@EntityScan` annotation, telling Spring that this package contains our data models

* model.persistence.repositories - these contain a `JpaRepository` interface for each of our models. This allows Hibernate to connect them with our database so we can access data in the code, as well as define certain convenience methods. Look through them and see the methods that have been declared. Looking at the application “demo” class, you’ll see the `@EnableJpaRepositories` annotation, telling Spring that this package contains our data repositories.

* model.requests - this package contains the request models. The request models will be transformed by Jackson from JSON to these models as requests are made. Note the `Json` annotations, telling Jackson to include and ignore certain fields of the requests. You can also see these annotations on the models themselves.

* controllers - these contain the api endpoints for our app, 1 per model. Note they all have the `@RestController` annotation to allow Spring to understand that they are a part of a REST API

In resources, you'll see the application configuration that sets up our database and Hibernate, It also contains a data.sql file with a couple of items to populate the database with. Spring will run this file every time the application starts

## Getting Started

Once started, using a REST client, such as Postman, explore the APIs.

To create a new user for example, you would send a POST request to:
http://localhost:8080/api/user/create with an example body like 

```
{
    "username": "test"
}
```

and this would return
```
{
    "id" 1,
    "username": "test"
}
```

## Authentication and Authorization
We use authentication and authorization controls so users can only access their data, and that data can only be accessed in a secure way. We will do this using a combination of usernames and passwords for authentication, as well as JSON Web Tokens (JWT) to handle the authorization.

As stated prior, we implement a password based authentication scheme. To do this, we need to store the users' passwords in a secure way. This needs to be done with hashing, and it's this hash which should be stored. Additionally when viewing their user information, the user's hash should not be returned to them in the response.
We also add some requirements and validation, as well as a confirm field in the request, to make sure they didn't make a typo. 

The package Spring-boot-starter-security provides the authentication basics.
JWT does not ship as a part of spring security, so java-jwt dependency is added to the project. 
Spring Boot ships with an automatically configured security module that must be disabled, as we will be implementing our own. This is done in the Application class.

Once the automatically configured security module is disabled, authentication is done with 4 classes:
   * a subclass of `UsernamePasswordAuthenticationFilter` for taking the username and password from a login request and logging in. This, upon successful authentication, should hand back a valid JWT in the `Authorization` header
   * a subclass of `BasicAuthenticationFilter`. 
   * an implementation of the `UserDetailsService` interface. This should take a username and return a userdetails User instance with the user's username and hashed password.
   *  a subclass of `WebSecurityConfigurerAdapter`. This should attach your user details service implementation to Spring's `AuthenticationManager`. It also handles session management and what endpoints are secured. For us, we manage the session so session management should be disabled. Your filters should be added to the authentication chain and every endpoint but 1 should have security required. The one that should not is the one responsible for creating new users.

Once all this is setup, you can use Spring's default /login endpoint to login like so

```
POST /login 
{
    "username": "test",
    "password": "somepassword"
}
```

and that should, if those are valid credentials, return a 200 OK with an Authorization header which looks like "Bearer <data>" this "Bearer <data>" is a JWT and must be sent as a Authorization header for all other requests. If it's not present, endpoints should return 401 Unauthorized. If it's present and valid, the endpoints should function as normal.

## Featured Endpoints

Please refer to the `postman_query_collection.json` for a list of endpoints and how to use them.




