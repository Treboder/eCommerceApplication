# eCommerce Application

In this project, we demonstrate a simple Spring Boot App providing restful APIs secured with Java Web Tokens (JWT).  
Based on a forked udacity template project, we implement proper authentication and authorization controls so users can only access their data, and that data can only be accessed in a secure way. 

## Project Template Structure 
The template is written in Java using Spring Boot, Hibernate ORM, and the H2 database. 
H2 is an in memory database, so if you need to retry something, every application startup is a fresh copy.
Once the project is set up, you will see 5 packages:

* demo - this package contains the main method which runs the application

* model.persistence - this package contains the data models that Hibernate persists to H2. 
There are 4 models: Cart, for holding a User's items; Item , for defining new items; User, to hold user account information; and UserOrder, to hold information about submitted orders. 
Looking back at the application “demo” class, you'll see the `@EntityScan` annotation, telling Spring that this package contains our data models

* model.persistence.repositories - these contain a `JpaRepository` interface for each of our models. 
This allows Hibernate to connect them with our database so we can access data in the code, as well as define certain convenience methods. 
Look through them and see the methods that have been declared. 
Looking at the application “demo” class, you’ll see the `@EnableJpaRepositories` annotation, telling Spring that this package contains our data repositories.

* model.requests - this package contains the request models. 
The request models will be transformed by Jackson from JSON to these models as requests are made. 
Note the `Json` annotations, telling Jackson to include and ignore certain fields of the requests. 
You can also see these annotations on the models themselves.

* controllers - these contain the api endpoints for our app, 1 per model. Note they all have the `@RestController` annotation to allow Spring to understand that they are a part of a REST API

In resources, you'll see the application configuration that sets up our database and Hibernate, It also contains a data.sql file with a couple of items to populate the database with. 
Spring will run this file every time the application starts.

## Authentication and Authorization
We need to add proper authentication and authorization controls so users can only access their data, and that data can only be accessed in a secure way. 
We will do this using a combination of usernames and passwords for authentication, as well as JSON Web Tokens (JWT) to handle the authorization.
As stated prior, we implemented a password based authentication scheme here. 
To do this, we need to store the users' passwords in a secure way - This is done with hashing, and it's this hash which is stored in our H2 database via JPA.
Security protocols are based on the dependencies <Spring-boot-starter-security> and <java-jwt> for the java web token authorizing the user.
Spring Boot ships with an automatically configured security module that must be disabled, as we will be implementing our own. 
This must be done in the Application class.
Once that is disabled, we need to implement 4 classes:
   * a subclass of `UsernamePasswordAuthenticationFilter` for taking the username and password from a login request and logging in. This, upon successful authentication, should hand back a valid JWT in the `Authorization` header
   * a subclass of `BasicAuthenticationFilter`.
   * an implementation of the `UserDetailsService` interface. This should take a username and return a userdetails User instance with the user's username and hashed password.
   * a subclass of `WebSecurityConfigurerAdapter`. This should attach your user details service implementation to Spring's `AuthenticationManager`. It also handles session management and what endpoints are secured. For us, we manage the session so session management should be disabled. Your filters should be added to the authentication chain and every endpoint but 1 should have security required. The one that should not is the one responsible for creating new users.

## Endpoints Examples
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

Once all this is setup, you can use Spring's default /login endpoint to login like so

```
POST /login 
{
    "username": "test",
    "password": "somepassword"
}
```

and that should, if those are valid credentials, return a 200 OK with an Authorization header which looks like "Bearer <data>" this "Bearer <data>" is a JWT and must be sent as a Authorization header for all other rqeuests. 
If it's not present, endpoints should return 401 Unauthorized. 
If it's present and valid, the endpoints should function as normal.

Once you've created a user and logged in, try  to add items to cart (see the `ModifyCartRequest` class) and submit an order.

```
POST /api/cart/addToCart
{
    "username" : "user",
    "itemId" : 2,
    "quantity" : 3
}
```

In order to submit an order we would need to send a POST-request with the user name to http://localhost:8080/api/order/submit/user

A full list of a possible request can be found as [src/main/resources/postman_query_collection.json](src/main/resources/postman_query_collection.json)


