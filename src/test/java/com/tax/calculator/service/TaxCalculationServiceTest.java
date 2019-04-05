package com.tax.calculator.service;

import com.tax.calculator.model.CalculationRequest;
import com.tax.calculator.model.TaxCalculation;
import com.tax.calculator.repository.TaxCalculationRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TaxCalculationServiceTest {

    @InjectMocks
    private TaxCalculationService taxCalculationService = new TaxCalculationService();

    @Mock
    private TaxCalculationRepository taxCalculationRepository;

    @Mock
    private ThreadPoolExecutor threadPoolExecutor;

    @Test
    public void shouldAddToQueueReceivedCalculationRequest() {
        taxCalculationService.calculate(new CalculationRequest());
        taxCalculationService.calculate(new CalculationRequest());

        Assertions.assertThat(taxCalculationService.getPoolSize()).isEqualTo(2);
    }

    @Test
    public void shouldCreateInitialCalculationInformation() {
        final CalculationRequest calculationRequest = new CalculationRequest();
        calculationRequest.setAnnualIncome(new BigDecimal("200"));
        calculationRequest.setClientID("04311558320");

        when(taxCalculationRepository.save(any(TaxCalculation.class))).thenAnswer(i -> i.getArguments()[0]);

        TaxCalculation taxCalculation = taxCalculationService.calculate(calculationRequest);

        Assertions.assertThat(taxCalculation).isNotNull();
        Assertions.assertThat(taxCalculation.getClientID()).isEqualTo(calculationRequest.getClientID());
        Assertions.assertThat(taxCalculation.getStatus()).isEqualTo(TaxCalculation.Status.POSTED);
        Assertions.assertThat(taxCalculation.getAnnualIncome()).isEqualTo(calculationRequest.getAnnualIncome());
    }

    @Test
    public void shouldHandleWorkerTaskCompletion() throws ExecutionException, InterruptedException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        final CalculationRequest calculationRequest = new CalculationRequest();
        calculationRequest.setAnnualIncome(new BigDecimal("100"));
        calculationRequest.setClientID("04311558320");
        ArgumentCaptor<TaxCalculationService.CalculationWorker> captor = ArgumentCaptor.forClass(TaxCalculationService.CalculationWorker.class);


        taxCalculationService.calculate(calculationRequest);

        verify(threadPoolExecutor).submit(captor.capture());

        Future<TaxCalculation> future = executor.submit(captor.getValue());
        TaxCalculation taxCalculation = future.get();

        final BigDecimal thirdPercent = new BigDecimal("30.0");
        Assertions.assertThat(taxCalculation).isNotNull();
        Assertions.assertThat(taxCalculation.getTax()).isEqualTo(thirdPercent);
        Assertions.assertThat(taxCalculation.getClientID()).isEqualTo(calculationRequest.getClientID());
        Assertions.assertThat(taxCalculation.getStatus()).isEqualTo(TaxCalculation.Status.PROCESSED);
    }
}
