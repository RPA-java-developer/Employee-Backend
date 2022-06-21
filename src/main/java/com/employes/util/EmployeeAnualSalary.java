package com.employes.util;


import org.springframework.stereotype.Component;

@Component
public class EmployeeAnualSalary {
	
	public float calculateYearSalary(float monthSalary) {
		System.out.println("salario de entrada: "+ monthSalary);
		float yearSalary = monthSalary * 12;
		return yearSalary;
	}
	
}
