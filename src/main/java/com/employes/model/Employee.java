package com.employes.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")	
	private Integer id;
    @Column(name = "employee_name")
	private String employee_name;
    @Column(name = "employee_salary")
	private Float employee_salary;
    @Column(name = "employee_age")
	private Integer employee_age;	
    @Column(name = "profile_image")
	private String profile_image;	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmployee_name() {
		return employee_name;
	}

	public void setEmployee_name(String employee_name) {
		this.employee_name = employee_name;
	}

	public Float getEmployee_salary() {
		return employee_salary;
	}

	public void setEmployee_salary(Float employee_salary) {
		this.employee_salary = employee_salary;
	}

	public Integer getEmployee_age() {
		return employee_age;
	}

	public void setEmployee_age(Integer employee_age) {
		this.employee_age = employee_age;
	}

	public String getProfile_image() {
		return profile_image;
	}

	public void setProfile_image(String profile_image) {
		this.profile_image = profile_image;
	}
	
}
