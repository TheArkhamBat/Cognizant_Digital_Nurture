package com.cognizant.ems.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cognizant.ems.dto.EmployeeSummaryDTO;
import com.cognizant.ems.entity.Employee;
import com.cognizant.ems.projection.EmployeeProjection;
import com.cognizant.ems.projection.EmployeeSpelProjection;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // ---- Exercise 3/5: derived query methods -------------------------------
    List<Employee> findByName(String name);

    List<Employee> findByEmailContainingIgnoreCase(String emailFragment);

    // This method name matches the @NamedQuery "Employee.findByDepartmentName"
    // defined on the Employee entity exactly, so Spring Data JPA uses that
    // named query instead of deriving one from the method signature.
    List<Employee> findByDepartmentName(String departmentName);

    // Likewise, matches @NamedQuery "Employee.findAllOrderedByName"
    List<Employee> findAllOrderedByName();

    // ---- Exercise 5: @Query -------------------------------------------------
    @Query("SELECT e FROM Employee e WHERE e.department.id = :departmentId")
    List<Employee> findEmployeesByDepartmentId(@Param("departmentId") Long departmentId);

    // ---- Exercise 6: pagination + sorting -----------------------------------
    Page<Employee> findByDepartmentNameContainingIgnoreCase(String departmentName, Pageable pageable);

    // ---- Exercise 8: projections --------------------------------------------
    List<EmployeeProjection> findProjectedByDepartmentName(String departmentName);

    List<EmployeeSpelProjection> findSpelProjectionByDepartmentName(String departmentName);

    @Query("SELECT new com.cognizant.ems.dto.EmployeeSummaryDTO(e.id, e.name, e.email) "
            + "FROM Employee e WHERE e.department.name = :departmentName")
    List<EmployeeSummaryDTO> findSummaryByDepartmentName(@Param("departmentName") String departmentName);
}
