package com.cognizant.ems.dto;

import lombok.Getter;

// Class-based projection, populated via a JPQL constructor expression
// ("SELECT new com.cognizant.ems.dto.EmployeeSummaryDTO(...) FROM Employee e")
// in EmployeeRepository.findSummaryByDepartmentName().
@Getter
public class EmployeeSummaryDTO {

    private final Long id;
    private final String name;
    private final String email;

    public EmployeeSummaryDTO(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
