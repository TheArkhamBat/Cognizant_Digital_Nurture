# EmployeeManagementSystem

Single cumulative Spring Boot project covering all 10 exercises from
`Spring_Data_JPA_and_Hibernate.docx`.

| Exercise | What's implemented |
|---|---|
| 1 | Project setup: Spring Web, Spring Data JPA, H2, Lombok, `application.properties` |
| 2 | `Employee` / `Department` JPA entities, one-to-many relationship |
| 3 | `EmployeeRepository`, `DepartmentRepository` extending `JpaRepository`, derived query methods |
| 4 | Full CRUD via `EmployeeController` / `DepartmentController` |
| 5 | Derived queries, `@Query`, `@NamedQuery`/`@NamedQueries` on `Employee` |
| 6 | Pagination + sorting combined in `GET /employees/search` |
| 7 | Auditing: `Auditable` base class, `@EnableJpaAuditing`, `AuditorAwareImpl` |
| 8 | Interface-based, `@Value`(SpEL)-based, and class-based (DTO) projections |
| 9 | Second, independent datasource (`auditdb`) wired up manually and used via `JdbcTemplate` |
| 10 | Hibernate-specific annotations (`@DynamicUpdate`, `@NaturalId`), dialect/batch config, manual `EntityManager` batch insert |

## A few implementation notes

- **Port `8091`** -- chosen so this app doesn't collide with the `spring-learn`
  project (port `8090`) if you run both at once.
- **Named queries vs. derived queries**: `EmployeeRepository.findByDepartmentName(...)`
  and `findAllOrderedByName()` are deliberately named to exactly match the
  `@NamedQuery` names declared on the `Employee` entity
  (`Employee.findByDepartmentName`, `Employee.findAllOrderedByName`). Spring
  Data JPA checks for a matching named query *before* trying to derive one
  from the method name, so these two methods are served by the `@NamedQuery`
  JPQL, not by query derivation.
- **Bidirectional JSON**: `Department.employees` and `Employee.department`
  each carry `@JsonIgnoreProperties` on the *other* side to stop Jackson
  recursing forever when serializing a `Department` with its `Employee`s (or
  vice versa).
- **Lombok**: entities use `@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder`
  rather than `@Data`, since `@Data`'s generated `equals()`/`hashCode()`/`toString()`
  would also recurse through the bidirectional association.
- **Exercise 9's "second datasource"** is intentionally kept separate from
  JPA/Hibernate (no second `EntityManagerFactory`) and is accessed with a
  plain `JdbcTemplate` against an `audit_log` table -- this is the simplest
  way to *actually* prove two independent datasources are alive in the same
  app, without the extra ceremony of a second full JPA persistence unit.
- **Auditing** uses a fixed `"SYSTEM"` auditor since no authentication is
  configured in this project; swap `AuditorAwareImpl` for a
  `SecurityContextHolder`-based lookup if you wire in Spring Security later.

## Running it

```bash
cd EmployeeManagementSystem
mvn clean package
mvn spring-boot:run
```

H2 console: http://localhost:8091/h2-console (JDBC URL `jdbc:h2:mem:testdb`,
user `sa`, password `password`). The secondary `audit_log` table lives in a
separate in-memory DB, `jdbc:h2:mem:auditdb`.

On startup, `DataSeeder` creates 2 departments (Engineering, Human Resources)
and 4 employees, so every endpoint below has data to return immediately.

## Trying the endpoints

```bash
# Exercise 4: CRUD
curl -s http://localhost:8091/departments
curl -s http://localhost:8091/employees
curl -s -X POST -H 'Content-Type: application/json' \
  -d '{"name":"New Dept"}' http://localhost:8091/departments
curl -s -X POST -H 'Content-Type: application/json' \
  -d '{"name":"New Employee","email":"new.employee@ems.com"}' \
  "http://localhost:8091/employees?departmentId=1"
curl -s -X PUT -H 'Content-Type: application/json' \
  -d '{"name":"Updated Name","email":"updated@ems.com"}' http://localhost:8091/employees/1
curl -i -X DELETE http://localhost:8091/employees/4

# Exercise 5: derived / @Query / named queries
curl -s "http://localhost:8091/employees/by-department?departmentName=Engineering"
curl -s http://localhost:8091/employees/ordered

# Exercise 6: pagination + sorting
curl -s "http://localhost:8091/employees/search?departmentName=Eng&page=0&size=2&sortBy=name&direction=asc"

# Exercise 8: projections
curl -s "http://localhost:8091/employees/projections/interface?departmentName=Engineering"
curl -s "http://localhost:8091/employees/projections/spel?departmentName=Engineering"
curl -s "http://localhost:8091/employees/projections/dto?departmentName=Engineering"

# Exercise 9: second datasource
curl -s -X POST "http://localhost:8091/audit-logs?message=Employee+created"
curl -s http://localhost:8091/audit-logs

# Exercise 10: Hibernate batch insert
curl -s -X POST -H 'Content-Type: application/json' \
  -d '[{"name":"Batch One","email":"batch1@ems.com"},{"name":"Batch Two","email":"batch2@ems.com"}]' \
  "http://localhost:8091/employees/batch?departmentId=1"
```

## Running the tests

```bash
mvn clean test
```

Verifies the context loads, that `DataSeeder` populated demo data, and that
saving a new `Employee` populates the `@CreatedDate`/`@CreatedBy` audit fields.
