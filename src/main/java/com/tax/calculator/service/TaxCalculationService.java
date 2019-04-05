package com.tax.calculator.service;

import com.tax.calculator.model.CalculationRequest;
import com.tax.calculator.model.TaxCalculation;
import com.tax.calculator.repository.TaxCalculationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class TaxCalculationService {

    @Value("${executor.pool.size}")
    private Integer pollSize;

    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    private TaxCalculationRepository taxCalculationRepository;

    @PostConstruct
    private void buildPool() {
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(pollSize);
    }

    public TaxCalculation calculate(CalculationRequest calculationRequest) {
        TaxCalculation taxCalculation = TaxCalculation.from(calculationRequest);
        threadPoolExecutor.submit(new CalculationWorker(taxCalculation));

        return taxCalculationRepository.save(taxCalculation);
    }

    public Integer getPoolSize() {
        return threadPoolExecutor.getPoolSize();
    }

    public TaxCalculation findByClientIdAndYear(String clientID, Integer year) {
        return taxCalculationRepository.findByClientIDAndYear(clientID, year);
    }

    private void completeTaxCalculation(TaxCalculation taxCalculation) {
        taxCalculation.setStatus(TaxCalculation.Status.PROCESSED);
        taxCalculationRepository.save(taxCalculation);
    }

    //stubbed calculation
    private BigDecimal realComplexCalculation(TaxCalculation taxCalculation) {
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return taxCalculation.getAnnualIncome().multiply(new BigDecimal("0.3"));
    }

    public class CalculationWorker implements Callable<TaxCalculation> {
        private final TaxCalculation taxCalculation;

        public CalculationWorker(TaxCalculation taxCalculation) {
            this.taxCalculation = taxCalculation;
        }

        @Override
        public TaxCalculation call() {
            BigDecimal tax = realComplexCalculation(taxCalculation);

            taxCalculation.setTax(tax);
            completeTaxCalculation(taxCalculation);
            return taxCalculation;
        }
    }
}
