package com.abc.hr.app.entity;

public class EmployeeDetails {
	 
    private Long empId;
    private String name;
    private String department;
    private double salary;
    private double bonus;
    private double totalPaybleSalary;
    private String country;
    private String region;
	public Long getEmpId() {
		return empId;
	}
	public void setEmpId(Long empId) {
		this.empId = empId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public double getSalary() {
		return salary;
	}
	public void setSalary(double salary) {
		this.salary = salary;
	}
	public double getTotalPaybleSalary() {
		return totalPaybleSalary;
	}
	public void setTotalPaybleSalary(double totalPaybleSalary) {
		this.totalPaybleSalary = totalPaybleSalary;
	}
	public double getBonus() {
		return bonus;
	}
	public void setBonus(double bonus) {
		this.bonus = bonus;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
}