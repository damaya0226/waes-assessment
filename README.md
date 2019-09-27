# WAES Assessment

The goal of this assignment is to show your coding skills and what you value in software
engineering. We value new ideas so next to the original requirement feel free to
improve/add/extend.

We evaluate the assignment depending on your role (Developer/Tester) and your level of
seniority.

## Requirements

- Provide 2 http endpoints that accepts JSON base64 encoded binary data on both
endpoints
    - <host>/v1/diff/<ID>/left and <host>/v1/diff/<ID>/right
- The provided data needs to be diff-ed and the results shall be available on a third end
point
    - <host>/v1/diff/<ID>
- The results shall provide the following info in JSON format
    - If equal return that
    - If not of equal size just return that
    - If of same size provide insight in where the diffs are, actual diffs are not needed.
          - So mainly offsets + length in the data
- Make assumptions in the implementation explicit, choices are good but need to be
communicated

## Installation with Maven

To install and run the project maven wrapper can be use by running
> ./mvnw spring-boot:run

## Installation with Docker

This project was dockerized and you can run it as a container by using:
> docker build -t damaya0226/waes-assessment .

And then run it with:
> docker run -p 8080:8080 damaya0226/waes-assessment

## API Reference

This project uses swagger for the API documentation, to access docs go to [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html). It consist on 3 endpoints as the requirement said:

Insert left part example:
> curl -X POST "http://localhost:8080/v1/diff/f693bb60-6330-4f95-9fc7-bcd02eb4a976/left" -H "accept: application/json;charset=UTF-8" -H "Content-Type: application/octet-stream" -d "eyJrZXkiOiJ2YWx1ZSJ9"

Insert right part example:
> curl -X POST "http://localhost:8080/v1/diff/f693bb60-6330-4f95-9fc7-bcd02eb4a976/right" -H "accept: application/json;charset=UTF-8" -H "Content-Type: application/octet-stream" -d "eyJrZXkiOiJ2YWx1ZSJ9"

Evaluating the differences example:
> curl -X GET "http://localhost:8080/v1/diff/f693bb60-6330-4f95-9fc7-bcd02eb4a976" -H "accept: application/json;charset=UTF-8"

Result when equal:

```
{
  "result": "EQUAL"
}
```

Result with different sizes:

```
{
  "result": "DIFFERENT_SIZE"
}
```

Result same size different values:

```
{
  "result": "NOT_EQUAL",
  "detail": {
    "differences": [
      {
        "index": 2,
        "length": 1
      },
      {
        "index": 8,
        "length": 2
      },
      {
        "index": 11,
        "length": 2
      }
    ]
  }
}
```

## Tests and Static Analysis

Unit tests and integration tests were written. To run the tests the next command can be executed:
> ./mvnw clean test

### Jacoco

It was used to ensure that code was cover by the tests. With next results in coverage:

- Instructions: 98%
- Branches:    100%
- Complexity:   97%
- Line:         97%
- Methods:      96%
- Classes:     100%

To rerun this report:
> ./mvnw clean verify

### Sonar

It was used to ensure that the quality of the code was good. And the results were **0 Bugs** and **0 Code Smells**.

## Logging

[Logback] (https://logback.qos.ch/) was used as it is the default logging library for spring boot. A log file is written and rotated when it achieves 50MB. I conserve 3 files.

## Monitoring

[Actuator] (https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-endpoints.html) was used to monitor the microservice. Only "loggers,health,env,info,logfile,metrics" endpoints were exposed as they are the ones that make more sense for this project.


## Tech/framework used
- [Spring] (https://spring.io/)
- [Swagger] (https://swagger.io/)
- [H2] (https://www.h2database.com/html/main.html)
- [Actuator] (https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-endpoints.html)
- [AssertJ] (https://joel-costigliola.github.io/assertj/)
- [Mockito] (https://site.mockito.org/)

## Future Improvements

- Support different Relational Databases and also NoSQL if needed.
- Clean all comparison operations if the history is not needed.
- Continuous integration and continuous delivery.