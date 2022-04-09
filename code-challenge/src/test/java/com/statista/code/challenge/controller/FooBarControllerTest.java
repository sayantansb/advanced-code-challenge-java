package com.statista.code.challenge.controller;

import com.statista.code.challenge.domain.*;
import com.statista.code.challenge.exception.ObjectNotFoundException;
import com.statista.code.challenge.service.AppService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(FooBarController.class)
@Slf4j
@ActiveProfiles("test")
public class FooBarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppService appService;

    @Test
    void createBooking() throws Exception {

        when(appService.createBooking(any(Booking.class))).thenReturn(Integer.valueOf(100));
        mockMvc.perform(post("/bookingservice/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":null,\"department\":\"marketing\",\"price\":2345.45,\"email\":\"abc@xyz.com\",\"currency\":\"AUD\",\"subscription_start_date\":683124845099,\"description\":\"Booking for Test\"}"))
                .andExpect(
                        status().isCreated()
                )
                .andExpect(
                        content().string("100")
                );
    }

    @Test
    void updateBooking() throws Exception {

        when(appService.updateBooking(any(Booking.class),any(Integer.class))).thenReturn(true);
        mockMvc.perform(put("/bookingservice/bookings/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"department\":\"marketing\",\"price\":2345.45,\"email\":\"abc@xyz.com\",\"currency\":\"AUD\",\"subscription_start_date\":683124845099,\"description\":\"Booking for Test\"}"))
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.department", is("marketing"))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.price", is(Double.valueOf("2345.45")))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.email", is("abc@xyz.com"))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.currency", is("AUD"))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.subscription_start_date", is(Long.valueOf("683124845099")))
                )
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.description", is("Booking for Test"))
                );

    }

    @Test
    void getBookingById() throws Exception {
        Booking booking = new Booking();
        booking.setCurrency(CurrencyTypes.AUD);
        booking.setDepartment("marketing");
        booking.setDescription("Booking for Test");
        booking.setEmail("abc@xyz.com");
        booking.setPrice(BigDecimal.valueOf(2345.45));
        booking.setSubscriptionStartDate(683124845099L);
        when(appService.getBookingById(any(Integer.class))).thenReturn(booking);

        mockMvc.perform(get("/bookingservice/bookings/{bookingId}",Integer.valueOf(100))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.department", is("marketing")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price", is(2345.45)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", is("abc@xyz.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currency", is("AUD")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subscription_start_date", is(Long.valueOf("683124845099"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", is("Booking for Test")))
                .andReturn();

    }

    @Test
    void getBookingIdsForDepartmentId() throws Exception {
        when(appService.getBookingIdsForDepartment(any(String.class))).thenReturn(Arrays.asList(1,2,3,4));

        mockMvc.perform(get("/bookingservice/bookings/department/{department}","test_department")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string("[1,2,3,4]"))
                .andReturn();


    }

    @Test
    void getCurrenciesUsedInBookings() throws Exception {
        when(appService.getCurrencyTypesUsedinBooking()).thenReturn(new HashSet<>(Arrays.asList(CurrencyTypes.EUR,CurrencyTypes.USD,CurrencyTypes.SGD,CurrencyTypes.CHF)) );

        MvcResult mvcResult = mockMvc.perform(get("/bookingservice/bookings/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();
        Assert.assertTrue(mvcResult.getResponse().getContentAsString().contains("EUR"));
        Assert.assertTrue(mvcResult.getResponse().getContentAsString().contains("USD"));
        Assert.assertTrue(mvcResult.getResponse().getContentAsString().contains("CHF"));
        Assert.assertTrue(mvcResult.getResponse().getContentAsString().contains("SGD"));

    }

    @Test
    void getSumOfCurrencies() throws Exception {
        when(appService.getSumOfAmountByCurrency(any(CurrencyTypes.class))).thenReturn(Double.valueOf(123.45));
        mockMvc.perform(get("/bookingservice/sum/{currency}","EUR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string("123.45"))
                .andReturn();
    }

    @Test
    void getBusinessResultForDepartmentOfBooking() throws Exception {
    CustomerOnboarding customerOnboarding = new CustomerOnboarding();
    customerOnboarding.setName("test_name");
    customerOnboarding.setLeadId("arb_lead_id");
    customerOnboarding.setLeadType(LeadType.AUTOMATED);
    customerOnboarding.setLeadResult(LeadResult.CONVERTED);
    customerOnboarding.setOpportunityId("arb_opportunity_id");
    customerOnboarding.setOpportunityResult(OpportunityResult.CLOSED_WON);
    customerOnboarding.setContractStatus(ContractStatus.ACCEPTED);
    customerOnboarding.setContractType(ContractType.AUTOMATED);
    customerOnboarding.setContractId("arb_contract_id");
    customerOnboarding.setOnboardingType(OnboardingType.AUTOMATED);
    customerOnboarding.setOnboardingStatus(OnboardingStatus.COMPLETED);
    when(appService.getBusinessResultForDepartmentOfBooking(any(Integer.class))).thenReturn(customerOnboarding);
    mockMvc.perform(get("/bookingservice/bookings/dobusiness/{booking_id}",Integer.valueOf(1))
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("test_name")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.leadId", is("arb_lead_id")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.leadType", is("AUTOMATED")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.leadResult", is("CONVERTED")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.opportunityId", is("arb_opportunity_id")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.opportunityResult", is("CLOSED_WON")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.contractId", is("arb_contract_id")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.contractStatus", is("ACCEPTED")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.onboardingStatus", is("COMPLETED")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.onboardingType", is("AUTOMATED")))
            .andReturn();
    }

    @Test
    void createBooking_Null_Department() throws Exception {

        when(appService.createBooking(any(Booking.class))).thenReturn(Integer.valueOf(100));
        mockMvc.perform(post("/bookingservice/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":null,\"price\":2345.45,\"email\":\"abc@xyz.com\",\"currency\":\"AUD\",\"subscription_start_date\":683124845099,\"description\":\"Booking for Test\"}"))
                .andExpect(
                        status().isBadRequest()
                );
    }

    @Test
    void createBooking_invalid_currency() throws Exception {

        when(appService.createBooking(any(Booking.class))).thenReturn(Integer.valueOf(100));
        mockMvc.perform(post("/bookingservice/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":null,\"department\":\"marketing\",\"price\":2345.45,\"email\":\"abc@xyz.com\",\"currency\":\"CNY\",\"subscription_start_date\":683124845099,\"description\":\"Booking for Test\"}"))
                .andExpect(
                        status().isBadRequest()
                );
    }

    @Test
    void createBooking_invalid_email() throws Exception {

        when(appService.createBooking(any(Booking.class))).thenReturn(Integer.valueOf(100));
        mockMvc.perform(post("/bookingservice/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":null,\"department\":\"marketing\",\"price\":2345.45,\"email\":\"invalid\",\"currency\":\"SGD\",\"subscription_start_date\":683124845099,\"description\":\"Booking for Test\"}"))
                .andExpect(
                        status().isBadRequest()
                );
    }

    @Test
    void getBusinessResultForDepartmentOfBooking_null_department() throws Exception {
        when(appService.getBusinessResultForDepartmentOfBooking(any(Integer.class))).thenThrow(new ObjectNotFoundException("No department found in Booking","Department"));
        mockMvc.perform(get("/bookingservice/bookings/dobusiness/{booking_id}",Integer.valueOf(1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", is("NOT_FOUND")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", is("No department found in Booking")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]", is("No Object Found for type Department")))
                .andReturn();
    }

}