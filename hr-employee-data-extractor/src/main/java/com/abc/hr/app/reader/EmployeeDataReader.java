package com.abc.hr.app.reader;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import static com.abc.hr.app.constants.HrEmployeeDataExtractorConstants.*;
import com.abc.hr.app.constants.HrEmployeeDataExtractorConstants;
import com.abc.hr.app.entity.Employee;
import com.abc.hr.app.exception.HrEmployeeDataExtractorException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

@Component
public class EmployeeDataReader implements ItemReader<Employee>{
	
	 private static final Logger LOGGER = LogManager.getLogger(EmployeeDataReader.class);

	    @Autowired
	    private JdbcTemplate jdbcTemplate;

	    private int currentIndex = 0;
	    private List<Employee> records;

	    @Override
	    public Employee read() throws Exception {
	        if (records == null) {
	        try {
	                LOGGER.info("Fetching employee records with query: {}", QUERY);
	                Instant start = Instant.now();

	                records = jdbcTemplate.query(QUERY, new BeanPropertyRowMapper<>(Employee.class));

	                long duration = Duration.between(start, Instant.now()).toMillis();
	                LOGGER.info("Employee records fetched. Duration: {} ms; Size: {}", duration, records.size());

	                //Logically, records.size() will never be negative. If there are no records, it returns 0—not less than 0—so this condition will never execute.
	                if (records.size()< 0) {
	                    LOGGER.warn("{} - {}. SQL: {}", HrEmployeeDataExtractorConstants.ERR_DB_002_CODE,
	                            HrEmployeeDataExtractorConstants.ERR_DB_002_MSG, QUERY);
	                    return null;
	                }
	            } catch (Exception e) {
	                String errorMsg = String.format("Error while fetching employee records. Query: %s", QUERY);
	                LOGGER.error(errorMsg, e);
	                throw new HrEmployeeDataExtractorException(errorMsg, e);
	            }
	        }

	        if (currentIndex < records.size()) {
	            return records.get(currentIndex++);
	        }

	        return null;
	    }
}
