package com.tax.calculator.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class TaxCalculation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientID;
    private Integer year;
    private BigDecimal annualIncome;
    private Status status = Status.POSTED;
    private BigDecimal tax;

    public static TaxCalculation from(CalculationRequest calculationRequest) {
        TaxCalculation taxCalculation = new TaxCalculation();
        taxCalculation.setClientID(calculationRequest.getClientID());
        taxCalculation.setAnnualIncome(calculationRequest.getAnnualIncome());
        taxCalculation.setYear(calculationRequest.getYear());
        return taxCalculation;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(BigDecimal annualIncome) {
        this.annualIncome = annualIncome;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public enum Status {
        POSTED, PROCESSED
    }

}
