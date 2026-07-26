package com.cognizant.ems.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cognizant.ems.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    // Derived query methods (Exercise 3)
    Optional<Department> findByName(String name);

    List<Department> findByNameContainingIgnoreCase(String keyword);
}
