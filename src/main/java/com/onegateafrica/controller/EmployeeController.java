package com.onegateafrica.controller;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.onegateafrica.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.onegateafrica.entity.Employee;
import com.onegateafrica.repository.EmployeeRepository;
@RestController
public class EmployeeController  implements CommandLineRunner{
	private final EmployeeService employeeService;




	@Override
		public void run(String... args) throws Exception {
		}
		
		@Autowired
		public EmployeeController (EmployeeService employeeService){
			this.employeeService=employeeService;
		}
		
		@PostMapping(value="/add")
		public Employee save(@RequestBody Employee employee) {
			return employeeService.saveEmployee(employee);
		}
		
		public static String uploadDirectory = System.getProperty("user.dir")+"/uploads";
		@PostMapping("/")
		  public String UploadPage(Employee employee) {
			  return "uploadview";
		  }
		@PostMapping("/upload")
		  public String upload(Employee employee,@RequestParam("files") MultipartFile[] files) {
			  StringBuilder fileNames = new StringBuilder();
			  for (MultipartFile file : files) {
				  Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
				  fileNames.append(file.getOriginalFilename()+" ");
				  try {
					Files.write(fileNameAndPath, file.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
			  }
			  return "uploadstatusview";
		  }
		
		
}
