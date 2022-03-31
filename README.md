# Spring Boot + OAS Code Generation Demo

## How to use it?

Register in [Amadeus for Developers](https://developers.amadeus.com) to get your `AMADEUS_CLIENT_ID` & `AMADEUS_CLIENT_SECRET`

Export the values:

```
export AMADEUS_CLIENT_ID=YOUR_CLIENT_ID
export AMADEUS_CLIENT_SECRET=YOUR_CLIENT_SECRET
```

## How to run in local?

```
mvn clean verify
mvn clean spring-boot:run
mvn clean package
java -jar ./target/spring-boot-amadeus-oas-code-generator-demo-0.1.0-SNAPSHOT.jar
curl 'http://localhost:8080/api/v1/check-in-links'
```

## How to run as a Docker Container?

```
mvn spring-boot:build-image
docker run -p 8080:8080 \
    -e AMADEUS_CLIENT_ID=$AMADEUS_CLIENT_ID \
    -e AMADEUS_CLIENT_SECRET=$AMADEUS_CLIENT_SECRET \
    -t docker.io/library/spring-boot-amadeus-oas-code-generator-demo:0.1.0-SNAPSHOT
```

```
docker-machine ip xxx
```

With that IP, you can execute curl without any issue:

```
curl 'http://IP_FROM_CONTAINER:8080/api/v1/check-in-links'
```

## Others commands

```
mvn prettier:write
mvn versions:display-dependency-updates
```

# References

- https://git-scm.com/book/en/v2/Git-Tools-Submodules
- https://github.com/amadeus4dev/amadeus-open-api-specification
- https://developers.amadeus.com/
- https://github.com/OpenAPITools/openapi-style-validator/blob/master/lib/src/main/java/org/openapitools/openapistylevalidator/ValidatorParameters.java