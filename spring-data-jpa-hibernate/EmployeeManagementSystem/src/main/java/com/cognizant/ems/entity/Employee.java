package com.cognizant.ems.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Exercise 5: Named Queries. Because EmployeeRepository declares methods whose
// names exactly match "Employee.findByDepartmentName" and
// "Employee.findAllOrderedByName", Spring Data JPA uses these @NamedQuery
// definitions directly instead of deriving a query from the method name.
@NamedQueries({
        @NamedQuery(name = "Employee.findByDepartmentName",
                query = "SELECT e FROM Employee e WHERE e.department.name = :departmentName"),
        @NamedQuery(name = "Employee.findAllOrderedByName",
                query = "SELECT e FROM Employee e ORDER BY e.name ASC")
})
@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Exercise 10: Hibernate-specific @NaturalId - lets Hibernate look employees
    // up by a stable business key (email) via session.byNaturalId(), separate
    // from the generated surrogate primary key.
    @NaturalId
    @Column(unique = true)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @JsonIgnoreProperties("employees")
    private Department department;
}
