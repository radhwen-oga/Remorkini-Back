package com.onegateafrica.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onegateafrica.entity.Employee;
import com.onegateafrica.repository.EmployeeRepository;


@Service
public class EmployeeService {
	
		@Autowired
		EmployeeRepository employeeRepository;
		public Employee saveEmployee(Employee employee) {
			 return employeeRepository.save(employee);
		}
		




}
