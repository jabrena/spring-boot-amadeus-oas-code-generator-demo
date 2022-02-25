# API Notes

- [Authentication](spec/authentication/README.md)
- [Covid-19 and travel safety](spec/covid-19-and-travel-safety/README.md)

```
mvn clean verify
mvn clean spring-boot:run -Dspring-boot.run.arguments="--amadeus.clientid=YOUR_CLIENTID --amadeus.clientsecret=YOUR_SECRET"
```

# References

- https://developers.amadeus.com/
- https://github.com/OpenAPITools/openapi-style-validator/blob/master/lib/src/main/java/org/openapitools/openapistylevalidator/ValidatorParameters.java
