package com.tax.calculator.api;

import com.tax.calculator.model.CalculationRequest;
import com.tax.calculator.model.TaxCalculation;
import com.tax.calculator.service.TaxCalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/tax/calculation")
public class TaxCalculationApi {

    @Autowired
    private TaxCalculationService taxCalculationService;

    @RequestMapping(method = RequestMethod.GET)
    public TaxCalculation getTaxCalculation(@RequestParam String clientID, @RequestParam Integer year) {
        return taxCalculationService.findByClientIdAndYear(clientID, year);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public TaxCalculation requestTaxCalculation(@RequestBody CalculationRequest calculationRequest) {
        return taxCalculationService.calculate(calculationRequest);
    }
}
