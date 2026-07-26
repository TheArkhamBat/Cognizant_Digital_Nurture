package com.cognizant.ems.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.ems.dto.EmployeeSummaryDTO;
import com.cognizant.ems.entity.Employee;
import com.cognizant.ems.projection.EmployeeProjection;
import com.cognizant.ems.projection.EmployeeSpelProjection;
import com.cognizant.ems.service.EmployeeService;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // ---- Exercise 4: basic CRUD ---------------------------------------------
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee,
            @RequestParam Long departmentId) {
        return ResponseEntity.ok(employeeService.createEmployee(employee, departmentId));
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        return employeeService.updateEmployee(id, employee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    // ---- Exercise 5: derived query / @Query / named query methods ----------
    @GetMapping("/by-department")
    public List<Employee> findByDepartment(@RequestParam String departmentName) {
        return employeeService.findByDepartmentName(departmentName);
    }

    @GetMapping("/ordered")
    public List<Employee> findAllOrdered() {
        return employeeService.findAllOrderedByName();
    }

    // ---- Exercise 6: pagination + sorting combined --------------------------
    @GetMapping("/search")
    public Page<Employee> search(
            @RequestParam(defaultValue = "") String departmentName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort sort = "desc".equalsIgnoreCase(direction) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return employeeService.searchByDepartment(departmentName, pageable);
    }

    // ---- Exercise 8: projections ---------------------------------------------
    @GetMapping("/projections/interface")
    public List<EmployeeProjection> getInterfaceProjection(@RequestParam String departmentName) {
        return employeeService.getProjectedByDepartment(departmentName);
    }

    @GetMapping("/projections/spel")
    public List<EmployeeSpelProjection> getSpelProjection(@RequestParam String departmentName) {
        return employeeService.getSpelProjectionByDepartment(departmentName);
    }

    @GetMapping("/projections/dto")
    public List<EmployeeSummaryDTO> getDtoProjection(@RequestParam String departmentName) {
        return employeeService.getSummaryByDepartment(departmentName);
    }

    // ---- Exercise 10: Hibernate batch insert ---------------------------------
    @PostMapping("/batch")
    public ResponseEntity<String> batchInsert(@RequestBody List<Employee> employees,
            @RequestParam Long departmentId) {
        employeeService.batchInsertEmployees(employees, departmentId);
        return ResponseEntity.ok(employees.size() + " employees inserted in batch.");
    }
}
