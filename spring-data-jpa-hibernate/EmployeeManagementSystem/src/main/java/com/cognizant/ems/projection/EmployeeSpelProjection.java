package com.cognizant.ems.projection;

import org.springframework.beans.factory.annotation.Value;

// Open (SpEL-backed) projection: getDisplayLabel() is computed on the fly from
// the underlying Employee ("target") rather than mapped straight from a column.
public interface EmployeeSpelProjection {

    String getName();

    String getEmail();

    @Value("#{target.name + ' <' + target.email + '>'}")
    String getDisplayLabel();
}
