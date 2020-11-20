package com.jc.soda.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.jc.soda.document.Employee;
import com.jc.soda.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    @PostMapping
    public ResponseEntity<Employee> create(@RequestBody Employee employee) {
        return ResponseEntity.ok(employeeRepository.create(employee));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> find(@PathVariable("id") String id) {
        return employeeRepository.find(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<PagedData<Employee>> findAll(
        @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
        @RequestParam(value = "count", required = false, defaultValue = "20") Integer count) {
        return ResponseEntity.ok(employeeRepository.findAll(offset, count));
    }

    @PostMapping("/filter")
    public ResponseEntity<PagedData<Employee>> findAll(
        @RequestBody JsonNode jsonNode,
        @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
        @RequestParam(value = "count", required = false, defaultValue = "20") Integer count) {
        return ResponseEntity.ok(employeeRepository.findAll(jsonNode, offset, count));
    }

    @PutMapping
    public ResponseEntity update(@RequestBody Employee employee) {
        return ResponseEntity.ok(employeeRepository.updateEmployee(employee));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") String id) {
        return ResponseEntity.ok(employeeRepository.delete(id));
    }

}
