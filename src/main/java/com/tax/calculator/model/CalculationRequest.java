package com.tax.calculator.model;

import java.math.BigDecimal;
import java.util.Objects;

public class CalculationRequest {
    private String clientID;
    private Integer year;
    private BigDecimal annualIncome;

    public CalculationRequest(String clientID, Integer year, BigDecimal annualIncome) {
        this.clientID = clientID;
        this.year = year;
        this.annualIncome = annualIncome;
    }

    public CalculationRequest() {
    }

    public void setAnnualIncome(BigDecimal annualIncome) {
        this.annualIncome = annualIncome;
    }

    public BigDecimal getAnnualIncome() {
        return annualIncome;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalculationRequest that = (CalculationRequest) o;
        return Objects.equals(clientID, that.clientID) &&
                Objects.equals(year, that.year) &&
                Objects.equals(annualIncome, that.annualIncome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientID, year, annualIncome);
    }
}
