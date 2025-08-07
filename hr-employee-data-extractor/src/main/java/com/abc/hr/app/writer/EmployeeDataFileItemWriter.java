package com.abc.hr.app.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import com.abc.hr.app.entity.EmployeeDetails;
import com.abc.hr.app.exception.HrEmployeeDataExtractorException;

 @Component
 public class EmployeeDataFileItemWriter implements ItemWriter<EmployeeDetails> {
 
    private static final Logger logger = LoggerFactory.getLogger(EmployeeDataFileItemWriter.class);
    private final String outputFilePath;
    private boolean headerWritten = false;
 
    public EmployeeDataFileItemWriter() {
    	logger.info("Initializing EmployeeFileItemWriter...");
 
        // Generate file name with timestamp
        LocalDateTime now = LocalDateTime.now();
        String formattedDate = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(now);
        String fileName = "employees_" + formattedDate + ".csv";
 
        // Set output file path
        outputFilePath = "src/main/resources/" + fileName;
    }
 
    @Override
    public void write(List<? extends EmployeeDetails> items) {
    logger.info("Writing {} records to file", items.size());
     
        File file = new File(outputFilePath);
        boolean append = file.exists();
     
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file, append), StandardCharsets.UTF_8))) {
     
            // Write header if needed
            if (!headerWritten && !append) {
                writer.write("Id,Name,Salary,Age,Country,Region");
                writer.newLine();
                headerWritten = true;
            }
     
            // Write each employee detail
            for (EmployeeDetails emp : items) {
                String line = String.join(",",
                        String.valueOf(emp.getId()),
                        emp.getName(),
                        String.valueOf(emp.getSalary()),
                        String.valueOf(emp.getAge()),
                        emp.getCountry(),
                        emp.getRegion());
     
                writer.write(line);
                writer.newLine();
            }
     
            logger.info("Finished writing records to {}", outputFilePath);
     
        } catch (Exception e) {
            logger.error("Error occurred while writing records to file: {}", outputFilePath, e);
            throw new HrEmployeeDataExtractorException("Failed to write employee records", e);
        }
    }
 }
