package com.opengateafrica.servecesImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opengateafrica.entities.Employee;
import com.opengateafrica.repositories.EmployeeRepository;
import com.opengateafrica.services.EmployeeService;


@Service
public class EmployeeImp implements EmployeeService {
	
		@Autowired
		EmployeeRepository employeeRepository;
		public void saveEmployee(Employee employee) {
			employeeRepository.save(employee);	
		}
		
		
		
		
		
}
