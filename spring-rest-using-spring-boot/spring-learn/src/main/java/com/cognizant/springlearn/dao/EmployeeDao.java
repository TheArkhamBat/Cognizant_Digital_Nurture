package com.cognizant.springlearn.dao;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Repository;

import com.cognizant.springlearn.Employee;
import com.cognizant.springlearn.service.exception.EmployeeNotFoundException;

@Repository
public class EmployeeDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeDao.class);

    private static ArrayList<Employee> EMPLOYEE_LIST;

    @SuppressWarnings("unchecked")
    public EmployeeDao() {
        LOGGER.info("START");
        ApplicationContext context = new ClassPathXmlApplicationContext("employee.xml");
        EMPLOYEE_LIST = (ArrayList<Employee>) context.getBean("employeeList", ArrayList.class);
        LOGGER.info("END");
    }

    public ArrayList<Employee> getAllEmployees() {
        LOGGER.info("START");
        LOGGER.info("END");
        return EMPLOYEE_LIST;
    }

    public void updateEmployee(Employee employee) throws EmployeeNotFoundException {
        LOGGER.info("START");

        boolean found = false;
        for (int i = 0; i < EMPLOYEE_LIST.size(); i++) {
            if (EMPLOYEE_LIST.get(i).getId().equals(employee.getId())) {
                EMPLOYEE_LIST.set(i, employee);
                found = true;
                break;
            }
        }

        if (!found) {
            throw new EmployeeNotFoundException();
        }

        LOGGER.info("END");
    }

    public void deleteEmployee(Long id) throws EmployeeNotFoundException {
        LOGGER.info("START");

        boolean removed = EMPLOYEE_LIST.removeIf(employee -> employee.getId().equals(id));
        if (!removed) {
            throw new EmployeeNotFoundException();
        }

        LOGGER.info("END");
    }
}
