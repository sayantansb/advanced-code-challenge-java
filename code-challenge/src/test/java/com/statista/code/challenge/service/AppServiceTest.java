package com.statista.code.challenge.service;

import com.statista.code.challenge.App;
import com.statista.code.challenge.dao.AppDAO;
import com.statista.code.challenge.domain.*;
import junit.framework.TestCase;
import org.javamoney.moneta.Money;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { App.class})
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class AppServiceTest extends TestCase {

    @Mock
    private AppDAO appDAO;

    @Mock
    private DepartmentFactory departmentFactory;

    @InjectMocks
    private AppService appService;

    private Booking booking;

    @Before
    public void createTestBooking(){
        booking = new Booking();
        //booking.setCurrency(CurrencyTypes.AUD);
        Money usd = Money.of(29.95, "USD");
        booking.setMonetaryAmount(usd);
        booking.setDepartment("marketing");
        booking.setDescription("Booking for Test");
        booking.setEmail("abc@xyz.com");
   //     booking.setMonetaryAmount(BigDecimal.valueOf(2345.45));
        booking.setSubscriptionStartDate(683124845099L);
    }

    @Test
    public void testCreateBooking() {
        when(appDAO.createBooking(any(Booking.class))).thenReturn(1);
        Integer bookingId = appService.createBooking(booking);
        assertEquals(Integer.valueOf(1),bookingId);
    }

    @Test
    public void testUpdateBooking() {
        when(appDAO.createBooking(any(Booking.class))).thenReturn(1);
        when(appDAO.updateBooking(any(Booking.class),any(Integer.class))).thenReturn(true);
        Integer bookingId = appService.createBooking(booking);
        booking.setId(bookingId);
     //   booking.setMonetaryAmount(BigDecimal.valueOf(121L));
     //   booking.setCurrency(CurrencyTypes.CHF);
        Money usd = Money.of(121, "CHF");
        booking.setMonetaryAmount(usd);
        appService.updateBooking(booking,bookingId);
        assertEquals(Integer.valueOf(1),booking.getId());
        assertEquals(CurrencyTypes.CHF.name(),booking.getMonetaryAmount().getCurrency().getCurrencyCode());
        assertEquals(121L,booking.getMonetaryAmount().getNumber().longValue());
    }

    @Test
    public void testGetBookingById() {
        when(appDAO.getBookingById(any(Integer.class))).thenReturn(booking);
        Booking bookingById = appService.getBookingById(Integer.valueOf(1));
        Booking originalBooking = new Booking(booking);
        Assert.assertEquals(0,bookingById.compareTo(originalBooking));

    }

    @Test
    public void testGetBookingIdsForDepartment() {
        when(appDAO.getBookingIdsForDepartment(any(String.class))).thenReturn(Arrays.asList(1,2));
        List<Integer> bookingIds = appService.getBookingIdsForDepartment("any_department");
        assertEquals(2,bookingIds.size());
        assertTrue(bookingIds.contains(1));
        assertTrue(bookingIds.contains(2));
    }

    @Test
    public void testGetCurrencyTypesUsedinBooking() {
        when(appDAO.getCurrencyTypesUsedinBooking()).thenReturn(new HashSet<>(Arrays.asList(CurrencyTypes.EUR,CurrencyTypes.USD,CurrencyTypes.SGD,CurrencyTypes.CHF)));
        Set<CurrencyTypes> currencyTypesSet = appService.getCurrencyTypesUsedinBooking();
        assertEquals(4,currencyTypesSet.size());
        assertTrue(currencyTypesSet.contains(CurrencyTypes.EUR));
        assertTrue(currencyTypesSet.contains(CurrencyTypes.USD));
        assertTrue(currencyTypesSet.contains(CurrencyTypes.SGD));
        assertTrue(currencyTypesSet.contains(CurrencyTypes.CHF));
    }

    @Test
    public void testGetSumOfAmountByCurrency() {

        when(appDAO.getSumForCurrency(any(CurrencyTypes.class))).thenReturn(Double.valueOf(1234));
        Double sum = appService.getSumOfAmountByCurrency(CurrencyTypes.AUD);
        assertEquals(Double.valueOf(1234),sum);

    }

    @Test
    public void getBusinessResultForDepartmentOfBooking() {

        Department department = new AutomatedSalesDepartment();
        department.setName("arbitrary_name");
        department.setOpportunityId("arb_opportunity_id");
        department.setOpportunityResult(OpportunityResult.CLOSED_WON);
        department.setLeadId("arbitrary_lead_id");
        department.setLeadType(LeadType.AUTOMATED);
        department.setLeadResult(LeadResult.CONVERTED);
        department.setContractId("arb_contract_id");
        department.setContractType(ContractType.AUTOMATED);
        department.setContractStatus(ContractStatus.ACCEPTED);
        department.setOnboardingType(OnboardingType.AUTOMATED);
        department.setOnboardingStatus(OnboardingStatus.COMPLETED);

        when(appDAO.getBookingById(any(Integer.class))).thenReturn(booking);
        when(departmentFactory.create(any(String.class))).thenReturn(department);
        CustomerOnboarding businessResultForDepartmentOfBooking = appService.getBusinessResultForDepartmentOfBooking(Integer.valueOf(1));
        assertNotNull(businessResultForDepartmentOfBooking);
        assertNotNull(businessResultForDepartmentOfBooking.getName());
        assertNotNull(businessResultForDepartmentOfBooking.getLeadId());
        assertNotNull(businessResultForDepartmentOfBooking.getLeadType());
        assertNotNull(businessResultForDepartmentOfBooking.getLeadResult());
        assertNotNull(businessResultForDepartmentOfBooking.getOpportunityId());
        assertNotNull(businessResultForDepartmentOfBooking.getOpportunityResult());
        assertNotNull(businessResultForDepartmentOfBooking.getContractId());
        assertNotNull(businessResultForDepartmentOfBooking.getContractType());
        assertNotNull(businessResultForDepartmentOfBooking.getContractStatus());
        assertNotNull(businessResultForDepartmentOfBooking.getOnboardingType());
        assertNotNull(businessResultForDepartmentOfBooking.getOnboardingStatus());

    }
}