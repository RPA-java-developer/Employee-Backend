package com.employes.controller;

import java.util.Optional;

import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.employes.model.Employee;
import com.employes.model.EmployeeResponse;
import com.employes.repository.EmployeeRepository;
import com.employes.util.EmployeeAnualSalary;
import com.employes.exceptions.ResourceNotFoundException;




@RestController
@RequestMapping("/api/v1/")
//@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {

	@Autowired
	EmployeeRepository employeeRepository;
	Optional<Employee> employee;
	EmployeeResponse employeeResponse;
	EmployeeAnualSalary employeeAnualSalary;
	
	
	public EmployeeController(EmployeeRepository employeeRepository, EmployeeAnualSalary employeeAnualSalary) {
		this.employeeRepository = employeeRepository;
		this.employeeAnualSalary = employeeAnualSalary;
	}
	
	
	// This method is used to list all employees
	@GetMapping("/employeesB")
	public String listAllEmployees() {	
		JSONObject respuestaJson = new JSONObject();
		respuestaJson.put("status", "success");
		respuestaJson.put("data", employeeRepository.findAll());
		respuestaJson.put("message", "Successfully! Record has been fetched.");
		System.out.println(respuestaJson);
		System.out.println(" ");
		
	    return respuestaJson.toString();		
	}
		
	/*
    @GetMapping(path = "/employee/{id}")
    public ResponseEntity<Employee> getEmployeeDetails(@PathVariable("id") Integer id) {
    	System.out.println("llega id: " + id);   	
		Employee employee = employeeRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException(">>>>>>  The employee with the ID: " + id + " does not exist:  <<<<<<"));
		return ResponseEntity.ok(employee);    	
    }
    */
    
    @GetMapping(path = "/employeeB/{id}")
    //public ResponseEntity<EmployeeResponse> getEmployeeDetails(@PathVariable("id") Integer id) {
    public String getEmployeeDetails(@PathVariable("id") Integer id) {
    	System.out.println("llega id: " + id);
    	
    	JSONObject respuestaJson = new JSONObject();
   
    	employee = Optional.of(new Employee());
    	employeeResponse = new EmployeeResponse();
    	
    	employee = employeeRepository.findById(id);
    	
    	if (employee.isPresent()) {
    		System.out.println("Employee present: "	+ employee.isPresent());
    		float anualSalary = employeeAnualSalary.calculateYearSalary(employee.get().getEmployee_salary());
    		employeeResponse.setId(employee.get().getId());
    		employeeResponse.setEmployee_name(employee.get().getEmployee_name());
    		employeeResponse.setEmployee_age(employee.get().getEmployee_age());
    		employeeResponse.setEmployee_salary(employee.get().getEmployee_salary());
    		employeeResponse.setProfile_image(employee.get().getProfile_image());
    		employeeResponse.setEmployee_anual_salary(anualSalary);
    		
            System.out.println(" ");
            System.out.println("employeeResponse: "	+ employeeResponse.getEmployee_anual_salary());    
            
            JSONArray jsArray = new JSONArray();
            jsArray.put(employeeResponse.getId());
            jsArray.put(employeeResponse.getEmployee_name());
            System.out.println(jsArray);    	
            
    		JSONObject employeeJson = new JSONObject();
    		employeeJson.put("id", employeeResponse.getId());
    		employeeJson.put("employee_name", employeeResponse.getEmployee_name());
    		employeeJson.put("employee_salary", employeeResponse.getEmployee_salary());
    		employeeJson.put("employee_age", employeeResponse.getEmployee_age());
    		employeeJson.put("profile_image", employeeResponse.getProfile_image());
    		employeeJson.put("employee_anual_salary", employeeResponse.getEmployee_anual_salary());   
    		
    		respuestaJson.put("status", "success");
    		respuestaJson.put("data", employeeJson);
    		respuestaJson.put("message", "Successfully! Record has been fetched.");
    		System.out.println(respuestaJson);    		
    	} else {
			respuestaJson.put("status", "failure");
			respuestaJson.put("data", "");
			respuestaJson.put("message", "Employee with ID:"+ id + " no found:" );
			System.out.println(respuestaJson);    		
    	}
	    System.out.println(" ");		
	    return respuestaJson.toString();    	   	
    }    

    
	// This method is used for find an Employee by ID 
	@GetMapping("/employeeB/")
	public String obtenerListadoPorDefecto(){
		System.out.println("No llega id: ");
			
		JSONObject respuestaJson = new JSONObject();
		respuestaJson.put("status", "success");
		respuestaJson.put("data", employeeRepository.findAll());
		respuestaJson.put("message", "Successfully! Record has been fetched.");
		System.out.println(respuestaJson);
		
	    System.out.println(" ");		
	    return respuestaJson.toString();		
	}  
	
	
	
	@GetMapping("/employees")
	private String listAllEmployeesExternalSource() {
		String result = "";
		String uri = "https://dummy.restapiexample.com/api/v1/employees";
		RestTemplate restTemplate = new RestTemplate();
		try {
			result = restTemplate.getForObject(uri, String.class);
        } catch (Exception handlerException) {
            System.out.println("Error: " + handlerException);
            result = "{429 Too Many Requests:}";
        }
		System.out.println("r >>>> " + result);	
		System.out.println("r >>>> " + result.getClass());	
		
		JSONArray listEmployeesYear = new JSONArray();
		JSONObject respuestaJson = new JSONObject();
		
		if (result.equals("{429 Too Many Requests:}")) {
			System.out.println("Entra al if 409");
			respuestaJson.put("status", "failure");
			respuestaJson.put("data", "");
			respuestaJson.put("message", "429 Too Many Requests:");
			System.out.println(respuestaJson);
		} else {		
			JSONObject jsonObject = new JSONObject(result);
			System.out.println(">>>> " + jsonObject.getClass());
			System.out.println(">>>> " + jsonObject.getString("status"));	
			System.out.println(">>>> " + jsonObject.getString("message"));
			JSONArray initialListEmployees = jsonObject.getJSONArray("data");

			System.out.println(">>>> " + initialListEmployees.length());
			
			JSONObject jsonObjectData = null;
 
			for (int k=0; k<initialListEmployees.length(); k++) {			
				
				System.out.println("for >>>> " + initialListEmployees.get(k));
				System.out.println("for >>>> " + initialListEmployees.get(k).getClass());
				jsonObjectData = new JSONObject(initialListEmployees.get(k).toString());
				
				System.out.println(" ");
				System.out.println("-----------------------------------------------------------");
				System.out.println("for >>>> " + jsonObjectData.get("id"));
				System.out.println("for >>>> " + jsonObjectData.get("employee_name"));
				System.out.println("for >>>> " + jsonObjectData.get("employee_salary"));

				Integer salary = new Integer((int)jsonObjectData.get("employee_salary"));
				float mouth_salary = salary.floatValue();
				float anualSalary = employeeAnualSalary.calculateYearSalary(mouth_salary);
				
				System.out.println("for anualSalary >>>>  " + anualSalary);
				JSONObject employeeWithYearJson = new JSONObject();		
				employeeWithYearJson.put("id", jsonObjectData.get("id"));
				employeeWithYearJson.put("employee_name", jsonObjectData.get("employee_name"));			
				employeeWithYearJson.put("employee_salary", jsonObjectData.get("employee_salary"));
				employeeWithYearJson.put("employee_age", jsonObjectData.get("employee_age"));
				employeeWithYearJson.put("profile_image", jsonObjectData.get("profile_image"));
				employeeWithYearJson.put("employee_anual_salary", anualSalary);											
				listEmployeesYear.put(employeeWithYearJson);
			
			}
			System.out.println("listEmployeesYear >>>>  " + listEmployeesYear.length());
			
			//JSONObject respuestaJson = new JSONObject();
			respuestaJson.put("status", "success");
			respuestaJson.put("data", listEmployeesYear);
			respuestaJson.put("message", "Successfully! Record has been fetched.");
			System.out.println(respuestaJson);
		    System.out.println(" ");
		}
	    return respuestaJson.toString();  		
	}	
	
	
	@GetMapping("/employee/{id}")
	private String findEmployeesExternalSource(@PathVariable("id") Integer id) {
		System.out.println("llega un id: " + id);
		String result = "";
		String uri = "https://dummy.restapiexample.com/api/v1/employee/"+id;
		RestTemplate restTemplate = new RestTemplate();
		try {
			result = restTemplate.getForObject(uri, String.class);
        } catch (Exception handlerException) {
            System.out.println("Error: " + handlerException);
            result = "{429 Too Many Requests:}";
        }
		System.out.println("r >>>> " + result);	
		System.out.println("r >>>> " + result.getClass());	

		JSONObject respuestaJson = new JSONObject();	
		
		boolean isFoundNull = result.indexOf("null") !=-1? true: false;
		System.out.println(isFoundNull);
		if (isFoundNull) {
			respuestaJson.put("status", "failure");
			respuestaJson.put("data", "");
			respuestaJson.put("message", "Employee with ID:"+ id + " no found:" );
			System.out.println(respuestaJson);						
		} else {
			
			if (result.equals("{429 Too Many Requests:}")) {
				System.out.println("Entra al if 409");		
				respuestaJson.put("status", "failure");
				respuestaJson.put("data", "");
				respuestaJson.put("message", "429 Too Many Requests:");
				System.out.println(respuestaJson);
			} else {
				
				JSONObject jsonObject = new JSONObject(result);			
				System.out.println(">>>> " + jsonObject.getString("status"));	
				System.out.println(">>>> " + jsonObject.getString("message"));
					
				System.out.println(" ");
				System.out.println("-----------------------------------------------------------");
				JSONObject jsonObjectData = jsonObject.getJSONObject("data");
				System.out.println("solo >>> " + jsonObjectData.get("id"));
				System.out.println("solo >>> " + jsonObjectData.get("employee_name"));
				System.out.println("solo >>> " + jsonObjectData.get("employee_salary"));

				Integer salary = new Integer((int)jsonObjectData.get("employee_salary"));
				float mouth_salary = salary.floatValue();
				float anualSalary = employeeAnualSalary.calculateYearSalary(mouth_salary);		
				System.out.println("for anualSalary >>>>  " + anualSalary);
								
				JSONObject employeeWithYearJson = new JSONObject();		
				employeeWithYearJson.put("id", jsonObjectData.get("id"));
				employeeWithYearJson.put("employee_name", jsonObjectData.get("employee_name"));			
				employeeWithYearJson.put("employee_salary", jsonObjectData.get("employee_salary"));
				employeeWithYearJson.put("employee_age", jsonObjectData.get("employee_age"));
				employeeWithYearJson.put("profile_image", jsonObjectData.get("profile_image"));
				employeeWithYearJson.put("employee_anual_salary", anualSalary);											

				respuestaJson.put("status", "success");
				respuestaJson.put("data", employeeWithYearJson);
				respuestaJson.put("message", "Successfully! Record has been fetched.");									
			} 			
		}
		
	
		

			
	    System.out.println(" ");		
	    return respuestaJson.toString();  		
	}	
	
	
	
}
