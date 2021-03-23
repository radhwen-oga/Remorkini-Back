package com.opengateafrica.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.opengateafrica.entities.Employee;

public interface EmployeeRepository extends MongoRepository<Employee, Long> {

}
