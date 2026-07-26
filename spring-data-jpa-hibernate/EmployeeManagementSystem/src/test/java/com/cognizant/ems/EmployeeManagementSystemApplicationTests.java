package com.cognizant.ems;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cognizant.ems.entity.Department;
import com.cognizant.ems.entity.Employee;
import com.cognizant.ems.repository.DepartmentRepository;
import com.cognizant.ems.repository.EmployeeRepository;

@SpringBootTest
class EmployeeManagementSystemApplicationTests {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void contextLoads() {
        assertNotNull(departmentRepository);
        assertNotNull(employeeRepository);
    }

    @Test
    void dataSeederPopulatesDepartmentsAndEmployees() {
        assertTrue(departmentRepository.count() > 0);
        assertTrue(employeeRepository.count() > 0);
    }

    @Test
    void savingEmployeeSetsAuditFields() {
        Department department = departmentRepository.findByName("Engineering").orElseThrow();

        Employee employee = Employee.builder()
                .name("Test User")
                .email("test.user@ems.com")
                .department(department)
                .build();

        Employee saved = employeeRepository.save(employee);

        assertNotNull(saved.getCreatedDate());
        assertNotNull(saved.getCreatedBy());
    }
}
