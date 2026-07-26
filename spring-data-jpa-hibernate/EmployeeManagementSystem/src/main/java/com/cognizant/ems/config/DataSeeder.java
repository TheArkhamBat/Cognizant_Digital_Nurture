package com.cognizant.ems.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.cognizant.ems.entity.Department;
import com.cognizant.ems.entity.Employee;
import com.cognizant.ems.repository.DepartmentRepository;
import com.cognizant.ems.repository.EmployeeRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public DataSeeder(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void run(String... args) {
        if (departmentRepository.count() > 0) {
            return;
        }

        Department engineering = departmentRepository.save(Department.builder().name("Engineering").build());
        Department hr = departmentRepository.save(Department.builder().name("Human Resources").build());

        employeeRepository.save(Employee.builder()
                .name("Aditya Sharma").email("aditya.sharma@ems.com").department(engineering).build());
        employeeRepository.save(Employee.builder()
                .name("Meera Nair").email("meera.nair@ems.com").department(engineering).build());
        employeeRepository.save(Employee.builder()
                .name("Rahul Verma").email("rahul.verma@ems.com").department(hr).build());
        employeeRepository.save(Employee.builder()
                .name("Priya Iyer").email("priya.iyer@ems.com").department(hr).build());
    }
}
