package com.hrapp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Paystub {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float baseSalary;
    private float bonuses;
    private float deductions;

    private float netTotal;
    private Date creationDate;

    @DateTimeFormat(pattern = "yyyy-MM")
    private Date payPeriod;

    private Long employeeId;

    public Paystub() {}

    public Paystub(float baseSalary, float bonuses, float deductions, Date payPeriod) {
            this.baseSalary = baseSalary;
            this.bonuses = bonuses;
            this.deductions = deductions;
            this.payPeriod = payPeriod;
        }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public float getNetTotal() { return netTotal; }
    public void setNetTotal(float netTotal) { this.netTotal = netTotal; }

    public Date getCreationDate() { return creationDate; }
    public void setCreationDate(Date creationDate){ this.creationDate = creationDate; }

    public Date getPayPeriod() { return payPeriod; }
    public void setPayPeriod(Date payPeriod) { this.payPeriod = payPeriod; }

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId){ this.employeeId = employeeId; }

    public float getBaseSalary() { return baseSalary; }
    public void setBaseSalary(float baseSalary) { this.baseSalary = baseSalary; }

    public float getBonuses() { return bonuses; }
    public void setBonuses(float bonuses) { this.bonuses = bonuses; }

    public float getDeductions() { return deductions; }
    public void setDeductions(float deductions) { this.deductions = deductions; }

}
