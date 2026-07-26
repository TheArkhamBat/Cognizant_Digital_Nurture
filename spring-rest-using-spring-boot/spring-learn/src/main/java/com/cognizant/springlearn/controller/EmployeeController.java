package com.cognizant.springlearn.controller;

import java.util.ArrayList;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.springlearn.Employee;
import com.cognizant.springlearn.service.EmployeeService;
import com.cognizant.springlearn.service.exception.EmployeeNotFoundException;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ArrayList<Employee> getAllEmployees() {
        LOGGER.info("START");
        ArrayList<Employee> employees = employeeService.getAllEmployees();
        LOGGER.info("END");
        return employees;
    }

    @PutMapping
    public void updateEmployee(@RequestBody @Valid Employee employee) throws EmployeeNotFoundException {
        LOGGER.info("START");
        employeeService.updateEmployee(employee);
        LOGGER.info("END");
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) throws EmployeeNotFoundException {
        LOGGER.info("START");
        employeeService.deleteEmployee(id);
        LOGGER.info("END");
    }
}
