# Getting Started


### Pre-requisites
* Java 17
* Gradle
* Postman


### Build the Project
`./gradlew build`


### Run the Application
`./gradlew bootRun`

Refer to the postman collection for the APIs.

Login API and Credentials:

`http://localhost:8080/api/auth/login`
```
{
    "username": "user",
    "password": "password"
}
```


### Run the Tests
`./gradlew test`


### H2 Console
1. Go to `http://localhost:8080/h2-console`
2. In the `JDBC URL` field, enter `jdbc:h2:mem:smartparkdb`
3. Username: `sa`
4. No password needed