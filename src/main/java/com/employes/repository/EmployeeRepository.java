package com.employes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.employes.model.Employee;
import com.employes.model.EmployeeResponse;




@Repository
//public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {	
	
   // @Query("SELECT * FROM Employee")
    //List<Employee> findAll();

    
}
