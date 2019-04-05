package com.tax.calculator.repository;

import com.tax.calculator.model.TaxCalculation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxCalculationRepository extends JpaRepository<TaxCalculation, Long> {
    TaxCalculation findByClientIDAndYear(String clientID, Integer year);
}
