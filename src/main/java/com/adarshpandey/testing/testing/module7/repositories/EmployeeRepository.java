package com.adarshpandey.testing.testing.module7.repositories;

import com.adarshpandey.testing.testing.module7.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByEmail(String email);
}