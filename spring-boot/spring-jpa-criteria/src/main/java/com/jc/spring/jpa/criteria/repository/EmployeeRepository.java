package com.jc.spring.jpa.criteria.repository;

import com.jc.spring.jpa.criteria.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
