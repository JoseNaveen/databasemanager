package com.joey.databasemanager.repository.sql;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.joey.databasemanager.classicmodelsbeans.Employee;
import com.joey.databasemanager.classicmodelsbeans.EmployeeView;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
	
	
	@Query("select e from Employee e where e.reportsTo.firstName=:firstName AND e.reportsTo.lastName=:lastName")
	List<Employee> findEmployeesByManager(@Param("firstName") String firstName,@Param("lastName") String lastName);
	
	@Query("select e from Employee e where e.reportsTo.employeeNumber=:managerId")
	List<Employee> findEmployeesByManagerId(@Param("managerId") Integer managerId);
	
	@Query("select e.firstName as firstName, e.lastName as lastName from Employee e where e.reportsTo.firstName=:firstName AND e.reportsTo.lastName=:lastName")
	List<EmployeeView> findEmployeesByManagerView(@Param("firstName") String firstName,@Param("lastName") String lastName);
	
	@Query("select e.firstName as firstName, e.lastName as lastName from Employee e where e.reportsTo.employeeNumber=:managerId")
	List<EmployeeView> findEmployeesByManagerIdView(@Param("managerId") Integer managerId);

}
