package com.abc.hr.app.processor;

import java.io.StringReader;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import com.abc.hr.app.entity.Employee;
import com.abc.hr.app.entity.EmployeeDetails;
import com.abc.hr.app.exception.HrEmployeeDataExtractorException;
import com.abc.hr.app.service.EmployeeDataService;

/*
 * This process class will process the raw data and convert it into EmployeeDetails object.
 * It will also calculate the salary and total payable salary.
 * It will also update the country code and region based on the country.
 * If the employee is inactive, it will skip processing and log a warning.
 * After processing, it will return the EmployeeDetails object.
  * * */
public class EmployeeDataProcessor implements ItemProcessor<Map<String, Object>,EmployeeDetails> {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeDataProcessor.class);

	@Autowired
	private EmployeeDataService employeeDataService;
	
	@Override
	public EmployeeDetails process(Map<String, Object> raw) throws Exception {
	        
		 Employee employee=convertFromXmlToEmployee(raw);
		 if(employee!= null && !employee.getStatus().equalsIgnoreCase("Inactive")) {   
	     EmployeeDetails employeeDetails = new EmployeeDetails();
	     employeeDetails.setEmpId(employee.getEmpId()); 
	     employeeDetails.setName(employee.getName());
	     employeeDetails.setDepartment(employee.getDepartment());
	     employeeDetails.setCountry(employee.getCountry());
	     employeeDetails.setBonus(employee.getBonus());
	     
	     // Calculate salary 
	     double salary = employee.getWorkingDaysPerMonth() * employee.getWorkingHoursPerDay() * employee.getHourlyRate();
	     employeeDetails.setSalary(salary);
	 
	     //Calculate total payable salary
	     employeeDetails.setTotalPaybleSalary(salary + employee.getBonus());
	     
	     // Update country code
	     employeeDetails.setCountry(employee.getCountry().toUpperCase());
	     
	     // Update region based on country
	     String region =updateRegionCode(employee); 
	     employeeDetails.setRegion(region);
	     	            return employeeDetails;
		 }
		 else {
			// Skip processing for inactive employees
			 logger.warn("Employee is inactive or null, skipping processing for employee: {}", employee.getEmpId());
			 return null; 
		 }
	}


	public Employee convertFromXmlToEmployee(Map<String, Object> raw) throws HrEmployeeDataExtractorException {
		try {
       JAXBContext context = JAXBContext.newInstance(Employee.class);
       Unmarshaller unmarshaller = context.createUnmarshaller();

       // XML is stored under a key like "payload"
       String encodedXml = raw.get("payload").toString();

       // Decode HTML entities like &lt; to <
       String decodedXml = StringEscapeUtils.unescapeHtml4(encodedXml);

       StringReader reader = new StringReader(decodedXml);
       return (Employee) unmarshaller.unmarshal(reader);
		} catch (Exception e) {
       logger.error("Error converting XML to Employee object", e);
       throw new HrEmployeeDataExtractorException("Failed to convert XML to Employee object", e);
		}
	}
	
	public String updateRegionCode (Employee employee) throws Exception{
		String region=employeeDataService.getRegionByCountry(employee.getCountry());

		if(!region.isEmpty()&&region.equalsIgnoreCase("Asia")) {
			
			return region;
		}else {
			return "Others";
		}
	}
}
