package com.tax.calculator.api;

import com.google.gson.Gson;
import com.tax.calculator.model.CalculationRequest;
import com.tax.calculator.model.TaxCalculation;
import com.tax.calculator.service.TaxCalculationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaxCalculationApiTest {

    private static final String API_TAX_CALCULATION = "/tax/calculation";

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private TaxCalculationService taxCalculationService;

    private TaxCalculation taxCalculation;
    private final String clientID = "200101004";
    private final Integer year = 2018;

    @Before
    public void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();

        taxCalculation = new TaxCalculation();
        taxCalculation.setClientID(clientID);
        taxCalculation.setStatus(TaxCalculation.Status.POSTED);
        taxCalculation.setYear(year);
    }

    @Test
    public void shouldRequestTaxCalculation() throws Exception {
        final String clientID = "200101004";
        Integer year = 2018;
        CalculationRequest calculationRequest = new CalculationRequest();
        calculationRequest.setClientID(clientID);
        calculationRequest.setYear(year);
        calculationRequest.setAnnualIncome(new BigDecimal("1000"));

        final String json = new Gson().toJson(calculationRequest);

        when(taxCalculationService.calculate(calculationRequest)).thenReturn(taxCalculation);
        MockHttpServletRequestBuilder post = post(API_TAX_CALCULATION).contentType(MediaType.APPLICATION_JSON).content(json);

        mvc.perform(post).andExpect(status().isOk()).andExpect(jsonPath("$.clientID", is(clientID)));

        verify(taxCalculationService, times(1)).calculate(calculationRequest);
    }

    @Test
    public void shouldCheckStatusOfRequest() throws Exception {
        MockHttpServletRequestBuilder getOperation = get(API_TAX_CALCULATION).param("clientID", clientID).param("year", year.toString());

        when(taxCalculationService.findByClientIdAndYear(clientID, year)).thenReturn(taxCalculation);

        mvc.perform(getOperation).andExpect(status().isOk()).
                andExpect(jsonPath("$.year", is(year))).
                andExpect(jsonPath("$.clientID", is(clientID)));

    }
}
