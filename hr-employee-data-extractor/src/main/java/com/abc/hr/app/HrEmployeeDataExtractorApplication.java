package com.abc.hr.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HrEmployeeDataExtractorApplication  {

    private static final Logger logger = LoggerFactory.getLogger(HrEmployeeDataExtractorApplication.class);

	    public static void main(String[] args) {
	        logger.debug("Before starting application");
	        startBatch(HrEmployeeDataExtractorApplication.class, args);
	    }

	    public static void startBatch(Class<?> configurationClass, String[] args) {
	        try {
	            SpringApplication.run(configurationClass, args);
	        } catch (Exception e) {
	            logger.error("Unexpected error while running Spring application: " + e.getMessage(), e);
	        }
	    }

	    public void nonStaticMethodForSonar() {
	        logger.debug("Log the message");
	    }
	

	}

