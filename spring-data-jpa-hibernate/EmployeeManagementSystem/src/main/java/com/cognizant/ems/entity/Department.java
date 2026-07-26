package com.cognizant.ems.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "departments")
@DynamicUpdate // Exercise 10: Hibernate-specific annotation - only changed columns are
               // included in the generated UPDATE statement
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // JsonIgnoreProperties on the "department" side of each Employee breaks the
    // Department -> Employee -> Department -> ... serialization cycle.
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("department")
    @Builder.Default
    private List<Employee> employees = new ArrayList<>();
}
