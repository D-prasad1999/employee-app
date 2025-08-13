package com.abc.hr.app.processor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import com.abc.hr.app.entity.Employee;
import com.abc.hr.app.entity.EmployeeDetails;
import com.abc.hr.app.service.EmployeeDataService;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;
 
@RunWith(MockitoJUnitRunner.class)
public class EmployeeDataProcessorUTest {
 
    @Mock
    private EmployeeDataService employeeDataService;
 
    @Spy
    @InjectMocks
    private EmployeeDataProcessor employeeProcessor;
 
    
    @Test
    public void testProcessor_Success() throws Exception {
        Employee mockEmployee = new Employee();
        mockEmployee.setEmpId(1L);
        mockEmployee.setName("John");
        mockEmployee.setDepartment("IT");
        mockEmployee.setCountry("india");
        mockEmployee.setBonus(5000.0);
        mockEmployee.setWorkingDaysPerMonth(20);
        mockEmployee.setWorkingHoursPerDay(8);
        mockEmployee.setHourlyRate(100.0);
        mockEmployee.setStatus("Active");
 
        doReturn(mockEmployee).when(employeeProcessor).convertFromXmlToEmployee(anyMap());
        when(employeeDataService.getRegionByCountry("india")).thenReturn("Asia");
 
        Map<String, Object> raw = new HashMap<>();
        raw.put("payload", "<employee/>");
 
        EmployeeDetails result = employeeProcessor.process(raw);
 
        assertNotNull(result);
        assertEquals("John", result.getName());
        assertEquals("IT", result.getDepartment());
        assertEquals("INDIA", result.getCountry());
        assertEquals("Asia", result.getRegion());
 
    }
}