package com.abc.hr.app.exception;

public class HrEmployeeDataExtractorException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public HrEmployeeDataExtractorException(String message) {
		super(message);
	}

	public HrEmployeeDataExtractorException(String message, Throwable cause) {
		super(message, cause);
	}
}
