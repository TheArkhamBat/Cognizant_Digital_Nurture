package com.cognizant.springlearn.service;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cognizant.springlearn.Employee;
import com.cognizant.springlearn.dao.EmployeeDao;
import com.cognizant.springlearn.service.exception.EmployeeNotFoundException;

@Service
public class EmployeeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private EmployeeDao employeeDao;

    @Transactional
    public ArrayList<Employee> getAllEmployees() {
        LOGGER.info("START");
        ArrayList<Employee> employees = employeeDao.getAllEmployees();
        LOGGER.info("END");
        return employees;
    }

    public void updateEmployee(Employee employee) throws EmployeeNotFoundException {
        LOGGER.info("START");
        employeeDao.updateEmployee(employee);
        LOGGER.info("END");
    }

    public void deleteEmployee(Long id) throws EmployeeNotFoundException {
        LOGGER.info("START");
        employeeDao.deleteEmployee(id);
        LOGGER.info("END");
    }
}
