package com.jc.spring.jpa.criteria.service;

import com.jc.spring.jpa.criteria.NotImplementedException;
import com.jc.spring.jpa.criteria.model.Employee;
import com.jc.spring.jpa.criteria.repository.EmployeeRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public Employee create(Employee employee) {
        return employeeRepository.save(employee);
    }

    public List<Employee> find(String criteria) {
        throw new NotImplementedException("find functionality not implemented");
    }

}
