package com.onegateafrica.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.onegateafrica.entity.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository <Employee, Long> {
    static void saveEmployee(Employee employee) {
    }
}
