package com.cognizant.ems.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.ems.service.AuditLogService;

// Talks to the SECOND datasource (auditdb) set up in SecondaryDataSourceConfig,
// completely independent of the primary JPA-managed datasource used by
// Employee/Department.
@RestController
@RequestMapping("/audit-logs")
public class AuditLogController {

    @Autowired
    private AuditLogService auditLogService;

    @PostMapping
    public String addLog(@RequestParam String message) {
        auditLogService.log(message);
        return "Logged: " + message;
    }

    @GetMapping
    public List<Map<String, Object>> getLogs() {
        return auditLogService.getAllLogs();
    }
}
