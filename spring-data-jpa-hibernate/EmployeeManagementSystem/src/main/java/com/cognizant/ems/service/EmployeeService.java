package com.cognizant.ems.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cognizant.ems.dto.EmployeeSummaryDTO;
import com.cognizant.ems.entity.Department;
import com.cognizant.ems.entity.Employee;
import com.cognizant.ems.exception.DepartmentNotFoundException;
import com.cognizant.ems.exception.EmployeeNotFoundException;
import com.cognizant.ems.projection.EmployeeProjection;
import com.cognizant.ems.projection.EmployeeSpelProjection;
import com.cognizant.ems.repository.DepartmentRepository;
import com.cognizant.ems.repository.EmployeeRepository;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    public Employee createEmployee(Employee employee, Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));
        employee.setDepartment(department);
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee updated) {
        Employee existing = getEmployeeById(id);
        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        return employeeRepository.save(existing);
    }

    public void deleteEmployee(Long id) {
        Employee existing = getEmployeeById(id);
        employeeRepository.delete(existing);
    }

    public List<Employee> findByDepartmentName(String departmentName) {
        return employeeRepository.findByDepartmentName(departmentName);
    }

    public List<Employee> findAllOrderedByName() {
        return employeeRepository.findAllOrderedByName();
    }

    // Exercise 6: pagination + sorting combined in one search endpoint
    public Page<Employee> searchByDepartment(String departmentName, Pageable pageable) {
        return employeeRepository.findByDepartmentNameContainingIgnoreCase(departmentName, pageable);
    }

    // Exercise 8: projections
    public List<EmployeeProjection> getProjectedByDepartment(String departmentName) {
        return employeeRepository.findProjectedByDepartmentName(departmentName);
    }

    public List<EmployeeSpelProjection> getSpelProjectionByDepartment(String departmentName) {
        return employeeRepository.findSpelProjectionByDepartmentName(departmentName);
    }

    public List<EmployeeSummaryDTO> getSummaryByDepartment(String departmentName) {
        return employeeRepository.findSummaryByDepartmentName(departmentName);
    }

    // Exercise 10: Hibernate batch insert. spring.jpa.properties.hibernate.jdbc.batch_size
    // (application.properties) controls the actual JDBC batch size; periodically
    // flushing + clearing the EntityManager here keeps memory bounded for large inserts.
    @Transactional
    public void batchInsertEmployees(List<Employee> employees, Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));

        int batchSize = 20;
        for (int i = 0; i < employees.size(); i++) {
            Employee employee = employees.get(i);
            employee.setDepartment(department);
            entityManager.persist(employee);

            if (i > 0 && i % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
        entityManager.flush();
        entityManager.clear();
    }
}
