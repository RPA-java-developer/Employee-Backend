package com.employes.service;

import java.util.List;

import com.employes.model.Employee;

public interface IEmployeeService {
	
	List<Employee> findAllWithYearSalary(String name);

}
