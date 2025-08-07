package com.abc.hr.app.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import com.abc.hr.app.entity.Employee;
import com.abc.hr.app.entity.EmployeeDetails;
import com.abc.hr.app.exception.HrEmployeeDataExtractorException;
import com.abc.hr.app.service.EmployeeDataService;

public class EmployeeDataProcessor implements ItemProcessor<Employee,EmployeeDetails> {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeDataProcessor.class);

	@Autowired
	private EmployeeDataService employeeDataService;
	
	@Override
	public EmployeeDetails process(Employee emp) throws Exception {
	    int salary = emp.getSalary();

	    if (salary >= 20000) {
	        try {
	            EmployeeDetails employeeDetails = updateEmployeeDetails(emp);
	            logger.info("Processing employee with ID: {}", emp.getId());
	            return employeeDetails;
	        } catch (Exception e) {
	            logger.error("Error processing employee with ID: {}", emp.getId(), e);
	            throw new HrEmployeeDataExtractorException("Error occured while processing records", e); // propagate to caller
	        }
	    } else {
	        logger.debug("Skipping employee with ID: {}", emp.getId());
	        return null;
	    }
	}

	public EmployeeDetails updateEmployeeDetails (Employee employee) throws Exception{
		EmployeeDetails employeeDetails=new EmployeeDetails();
		String region=employeeDataService.getRegionByCountry(employee.getCountry());
		employeeDetails.setId(employee.getId());
		employeeDetails.setName(employee.getName());
		employeeDetails.setAge(employee.getAge());
		employeeDetails.setSalary(employee.getSalary());
		employeeDetails.setCountry(employee.getCountry());
		employeeDetails.setRegion(region);
		
		return employeeDetails;
	}
}
