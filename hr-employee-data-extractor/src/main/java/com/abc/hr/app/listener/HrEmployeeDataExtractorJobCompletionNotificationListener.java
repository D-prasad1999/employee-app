package com.abc.hr.app.listener;

import java.time.Duration;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;
import com.abc.hr.app.processor.EmployeeDataProcessor;

@Component
public class HrEmployeeDataExtractorJobCompletionNotificationListener implements JobExecutionListener {
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeDataProcessor.class);

	/** The start time. */
	private Instant startTime;
	

	/**
	 * Gets the duration in milli seconds.
	 *
	 * @param startTime the start time
	 * @param endTime the end time
	 * @return the duration in milli seconds
	 */
	private long getDurationInMilliSeconds(Instant startTime, Instant endTime) {
		Duration time = Duration.between(startTime, endTime);
		return time.toMillis();
	}

	@Override
	public void beforeJob(JobExecution jobExecution) {
		startTime = Instant.now();
		logger.info("StartTime " + startTime);
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		Instant endTime = Instant.now();
		logger.info("EndTime " + endTime);
		long timeTakenToComplete = getDurationInMilliSeconds(startTime, endTime);
		logger.info("TimeTakenToComeplete " + timeTakenToComplete);
		if (BatchStatus.COMPLETED == jobExecution.getStatus()) {
			logger.info("BatchStatusSuccess " + jobExecution.getStatus());
		
		} else if (BatchStatus.FAILED == jobExecution.getStatus()) {
			logger.info("BatchStatusFailure " + jobExecution.getStatus());
		}
	}
}
