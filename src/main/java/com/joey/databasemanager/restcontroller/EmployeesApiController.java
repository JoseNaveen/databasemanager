package com.joey.databasemanager.restcontroller;

import java.util.List;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.joey.databasemanager.classicmodelsbeans.Employee;
import com.joey.databasemanager.classicmodelsbeans.EmployeeView;
import com.joey.databasemanager.exceptions.EmployeeNotFoundException;
import com.joey.databasemanager.repository.sql.EmployeeRepository;

@RestController
public class EmployeesApiController {
	
	@Autowired
	private EmployeeRepository employeerepo;
	
	@GetMapping("/api/employees")
	public List<Employee> getAllEmployees() {
		
		return employeerepo.findAll();
	}
	
	@GetMapping("/api/employees/{id}")
	public Employee getEmployeeById(@PathVariable Integer id) {
		return employeerepo.findById(id).orElseThrow(()-> new EmployeeNotFoundException("employee not found"));
	}
	
	@GetMapping("/api/employees/findreportees")
	public List<Employee> getReportees(@RequestParam(value="firstName",required=false) String firstName, @RequestParam(value="lastName",required=false) String lastName, @RequestParam(value="managerId",required=false) Integer managerId) {
		List<Employee> list;
		if (firstName !=null && lastName!=null) {
		list = employeerepo.findEmployeesByManager(firstName, lastName);
		}
		else {
			list = employeerepo.findEmployeesByManagerId(managerId);
		}
		if (list.isEmpty()) {
			throw new EmployeeNotFoundException("no reportees found");
		}
		else {
			return list;
		}
	}
	
	@GetMapping("/api/employees/findreporteesview")
	public List<EmployeeView> getReporteesView(@RequestParam(value="firstName",required=false) String firstName, @RequestParam(value="lastName",required=false) String lastName, @RequestParam(value="managerId",required=false) Integer managerId) {
		List<EmployeeView> list;
		if (firstName !=null && lastName!=null) {
		list = employeerepo.findEmployeesByManagerView(firstName, lastName);
		}
		else {
			list = employeerepo.findEmployeesByManagerIdView(managerId);
		}
		if (list.isEmpty()) {
			throw new EmployeeNotFoundException("no reportees found");
		}
		else {
			return list;
		}
	}
}
