package com.abc.hr.app.reader;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import static com.abc.hr.app.constants.HrEmployeeDataExtractorConstants.*;
import com.abc.hr.app.constants.HrEmployeeDataExtractorConstants;
import com.abc.hr.app.exception.HrEmployeeDataExtractorException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * This reader fetches records from DB
 * If no records are found, it logs a warning and returns null.
 * If an error occurs during database access, it throws a custom exception
 */
@Component
public class EmployeeDataReader implements ItemReader<Map<String, Object>>{
	
	 private static final Logger LOGGER = LogManager.getLogger(EmployeeDataReader.class);

	    @Autowired
	    private JdbcTemplate jdbcTemplate;

	    private int currentIndex = 0;
	    private List<Map<String, Object>> records;

	    @Override
	    public Map<String, Object> read() throws Exception {
	        if (records == null) {
	        try {
	                LOGGER.info("Fetching employee records with query: {}", QUERY);
	                Instant start = Instant.now();

	                records =  jdbcTemplate.queryForList(QUERY);

	                long duration = Duration.between(start, Instant.now()).toMillis();
	                LOGGER.info("Employee records fetched. Duration: {} ms; Size: {}", duration, records.size());

	                if (records.isEmpty()) {
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
