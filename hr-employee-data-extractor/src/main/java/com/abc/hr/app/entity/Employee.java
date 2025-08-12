package com.abc.hr.app.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Employee")
@XmlAccessorType(XmlAccessType.FIELD)
public class Employee {

    @XmlElement
    private Long empId;

    @XmlElement
    private String name;

    @XmlElement
    private int age;

    @XmlElement
    private String department;

    @XmlElement
    private int workingHoursPerDay;

    @XmlElement
    private int workingDaysPerMonth;

    @XmlElement
    private double hourlyRate;

    @XmlElement
    private double bonus;

    @XmlElement
    private String status;

    @XmlElement
    private String country;

    // Getters and setters
    public Long getEmpId() { return empId; }
    public void setEmpId(Long empId) { this.empId = empId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public int getWorkingHoursPerDay() { return workingHoursPerDay; }
    public void setWorkingHoursPerDay(int workingHoursPerDay) { this.workingHoursPerDay = workingHoursPerDay; }

    public int getWorkingDaysPerMonth() { return workingDaysPerMonth; }
    public void setWorkingDaysPerMonth(int workingDaysPerMonth) { this.workingDaysPerMonth = workingDaysPerMonth; }

    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }

    public double getBonus() { return bonus; }
    public void setBonus(double bonus) { this.bonus= bonus; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
}


