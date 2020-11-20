package com.jc.spring.jpa.criteria.controller;

import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

import com.fasterxml.jackson.databind.JsonNode;
import com.jc.spring.jpa.criteria.model.Employee;
import com.jc.spring.jpa.criteria.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity create(@RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.create(employee));
    }

    @PostMapping("/filter")
    public ResponseEntity find(@RequestBody JsonNode filter) {
        return ResponseEntity.status(NOT_IMPLEMENTED).build();
    }
}
