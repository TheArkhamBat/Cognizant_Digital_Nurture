package com.cognizant.ems.service;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {

    private final JdbcTemplate secondaryJdbcTemplate;

    public AuditLogService(@Qualifier("secondaryJdbcTemplate") JdbcTemplate secondaryJdbcTemplate) {
        this.secondaryJdbcTemplate = secondaryJdbcTemplate;
    }

    @PostConstruct
    public void init() {
        secondaryJdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS audit_log ("
                        + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                        + "message VARCHAR(255), "
                        + "logged_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
    }

    public void log(String message) {
        secondaryJdbcTemplate.update("INSERT INTO audit_log (message) VALUES (?)", message);
    }

    public List<Map<String, Object>> getAllLogs() {
        return secondaryJdbcTemplate.queryForList("SELECT * FROM audit_log ORDER BY id DESC");
    }
}
