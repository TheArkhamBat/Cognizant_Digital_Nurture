package com.cognizant.ems.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

// The PRIMARY datasource (used by JPA/Hibernate for Employee/Department) is
// auto-configured by Spring Boot from the plain spring.datasource.* properties
// -- that part needs no code at all.
//
// This class adds a completely independent SECOND datasource
// (app.datasource.secondary.*) to demonstrate managing multiple data sources
// in one application, accessed directly via JdbcTemplate rather than JPA.
@Configuration
public class SecondaryDataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "app.datasource.secondary")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public JdbcTemplate secondaryJdbcTemplate(DataSource secondaryDataSource) {
        return new JdbcTemplate(secondaryDataSource);
    }
}
