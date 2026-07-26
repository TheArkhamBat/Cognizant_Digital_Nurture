# spring-learn

Single cumulative Spring Boot project covering all 5 hands-on documents:

1. `1__spring-rest-handson.docx`  - Spring Boot basics, XML bean config,
   SimpleDateFormat bean, Country bean + singleton/prototype scope, country
   list, logging.
2. `2__spring-rest-handson.docx`  - HelloController, CountryController (GET
   all / GET by code / not-found handling), MockMvc tests.
3. `3__spring-rest-handson.docx`  - Employee + Department static XML data,
   EmployeeDao/Service/Controller, DepartmentDao/Service/Controller.
4. `4__spring-rest-handson.docx`  - POST/PUT/DELETE, `@Valid` bean validation,
   global exception handling (`GlobalExceptionHandler`), REST naming
   conventions.
5. `5__JWT-handson.docx`          - Spring Security (HTTP Basic + in-memory
   users/roles), `/authenticate` endpoint issuing a JWT, `JwtAuthorizationFilter`
   validating the `Bearer` token on every other request.

## Notable adaptations (so the whole thing actually compiles/runs as one app)

- **Port**: the docs use `8083` in one early example and `8090` everywhere
  else (all the curl/JWT examples) -- this project standardizes on
  **`server.port=8090`**.
- **CountryController base URL**: consolidated to `/countries` (with
  `/countries/{code}` for a single country) per the naming convention doc 4
  explicitly asks you to apply, replacing the earlier one-off `/country`
  endpoint from doc 2's first draft.
- **Security is layered on last**: once `SecurityConfig` + `JwtAuthorizationFilter`
  are in place, *every* endpoint requires either HTTP Basic or a JWT Bearer
  token (`anyRequest().authenticated()`), including `/hello`, `/countries`,
  `/employees`, `/departments`. This is why the MockMvc tests attach
  `httpBasic("user", "pwd")` to every request.
- **JUnit 5**: Spring Boot 2.7's default test starter pulls in JUnit 5
  (Jupiter), so `SpringLearnApplicationTests` uses
  `org.junit.jupiter.api.Test` / `Assertions.assertNotNull` instead of the
  JUnit 4 imports shown in the doc -- behavior is identical.
- **Spring Security version**: built on Spring Boot 2.7.18 / Spring Security
  5.7.x so `WebSecurityConfigurerAdapter` (used throughout the JWT doc) is
  still available -- it was removed entirely in Spring Security 6 / Spring
  Boot 3.
- **CountryNotFoundException / EmployeeNotFoundException are checked
  exceptions** (`extends Exception`, not `RuntimeException`), matching the
  docs' explicit instruction to add a `throws` clause on the controller
  methods.
- Proxy flags in the doc's `mvn clean package -Dhttp.proxyHost=...` command
  are specific to being on Cognizant's internal network -- omitted here;
  add them back if you're building from inside that network.

## Running it

```bash
cd spring-learn
mvn clean package
mvn spring-boot:run
```

On startup, watch the console logs for `displayDate()`, `displayCountry()`,
`displayPrototypeScope()` and `displayCountries()` output (all via SLF4J,
never `System.out.println`, per the "important note" in doc 1).

## Trying the endpoints

All endpoints require authentication once Spring Security is active. Basic
auth users: `admin` / `pwd` (ROLE_ADMIN) and `user` / `pwd` (ROLE_USER).

```bash
# Without credentials -> 401 Unauthorized
curl -s http://localhost:8090/countries

# With Basic auth
curl -s -u user:pwd http://localhost:8090/hello
curl -s -u user:pwd http://localhost:8090/countries
curl -s -u user:pwd http://localhost:8090/countries/in
curl -i -u user:pwd http://localhost:8090/countries/az     # -> 404 Country not found

curl -s -u user:pwd http://localhost:8090/employees
curl -s -u user:pwd http://localhost:8090/departments

# Create a country (POST)
curl -i -u user:pwd -H 'Content-Type: application/json' \
  -X POST -d '{"code":"IN","name":"India"}' http://localhost:8090/countries

# Validation error (code must be exactly 2 characters)
curl -i -u user:pwd -H 'Content-Type: application/json' \
  -X POST -d '{"code":"I","name":"India"}' http://localhost:8090/countries

# Update / delete an employee
curl -i -u user:pwd -H 'Content-Type: application/json' -X PUT \
  -d '{"id":1,"name":"Aditya Sharma","salary":80000,"permanent":true,"dateOfBirth":"15/06/1990"}' \
  http://localhost:8090/employees

curl -i -u user:pwd -X DELETE http://localhost:8090/employees/4
```

### JWT flow

```bash
# 1. Exchange Basic credentials for a JWT
curl -s -u user:pwd http://localhost:8090/authenticate
# -> {"token":"eyJhbGciOiJIUzI1NiJ9....."}

# 2. Use the token as a Bearer header on any other request
curl -s -H "Authorization: Bearer <token-from-step-1>" http://localhost:8090/countries
```

## Running the tests

```bash
mvn clean test
```

`SpringLearnApplicationTests` verifies `CountryController` loads via context,
that `GET /countries/in` returns `{"code":"IN","name":"India"}`, that
`GET /countries/az` returns 404 with reason "Country not found", and that
`PUT /employees` with a non-existent id returns 404 with reason
"Employee not found".
