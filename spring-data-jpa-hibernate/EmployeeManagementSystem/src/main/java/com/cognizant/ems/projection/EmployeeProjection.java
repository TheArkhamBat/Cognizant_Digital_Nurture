package com.cognizant.ems.projection;

// Interface-based projection: Spring Data generates a proxy at runtime that
// only fetches the id/name/email columns instead of the whole Employee entity.
public interface EmployeeProjection {

    Long getId();

    String getName();

    String getEmail();
}
