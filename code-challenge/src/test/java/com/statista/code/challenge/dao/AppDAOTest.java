package com.statista.code.challenge.dao;

import com.statista.code.challenge.App;
import com.statista.code.challenge.database.MockDB;
import com.statista.code.challenge.domain.Booking;
import com.statista.code.challenge.domain.CurrencyTypes;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { App.class})
@ActiveProfiles("test")
public class AppDAOTest {

    @Autowired
    private AppDAO appDAO;

    private Booking booking;

    @Before
    public void createTestBooking(){
        booking = new Booking();
        booking.setCurrency(CurrencyTypes.AUD);
        booking.setDepartment("marketing");
        booking.setDescription("Booking for Test");
        booking.setEmail("abc@xyz.com");
        booking.setPrice(BigDecimal.valueOf(2345.45));
        booking.setSubscriptionStartDate(683124845099L);
    }

    @After
    public void resetState(){
        MockDB.bookingMap.clear();
        MockDB.pricesForBookingsByCurrencyMap.clear();
        MockDB.bookingIdListByDepartmentIdMap.clear();
    }

    @Test
    public void testCreateBooking() {
        appDAO.createBooking(booking);
        Assert.assertNotNull(booking.getId());
        Assert.assertEquals(1,MockDB.bookingIdListByDepartmentIdMap.get("marketing").size());
        Assert.assertEquals(1,MockDB.pricesForBookingsByCurrencyMap.get(CurrencyTypes.AUD).size());
    }

    @Test
    public void testUpdateBooking() {
        Integer bookingId = appDAO.createBooking(booking);
        Assert.assertEquals(1,MockDB.bookingIdListByDepartmentIdMap.get("marketing").size());
        Assert.assertEquals(1,MockDB.pricesForBookingsByCurrencyMap.get(CurrencyTypes.AUD).size());
        booking.setCurrency(CurrencyTypes.USD);
        booking.setDepartment("sales");
        appDAO.updateBooking(booking,bookingId);
        Assert.assertEquals(CurrencyTypes.USD,MockDB.bookingMap.get(bookingId).getCurrency());
        Assert.assertEquals("sales",MockDB.bookingMap.get(bookingId).getDepartment());
        Assert.assertEquals(0,MockDB.bookingIdListByDepartmentIdMap.get("marketing").size());
        Assert.assertEquals(0,MockDB.pricesForBookingsByCurrencyMap.get(CurrencyTypes.AUD).size());
        Assert.assertEquals(1,MockDB.bookingIdListByDepartmentIdMap.get("sales").size());
        Assert.assertEquals(1,MockDB.pricesForBookingsByCurrencyMap.get(CurrencyTypes.USD).size());

    }

    @Test
    public void testGetBookingById() {
        Integer bookingId = appDAO.createBooking(booking);
        Booking fetchedBooking = appDAO.getBookingById(bookingId);
        Booking originalBooking = new Booking(booking);
        Assert.assertEquals(0,fetchedBooking.compareTo(originalBooking));
    }

    @Test
    public void testGetBookingIdsForDepartment() {
        Integer bookingId = appDAO.createBooking(booking);
        List<Integer> bookingIds = appDAO.getBookingIdsForDepartment("marketing");
        Assert.assertEquals(1,bookingIds.size());
        Assert.assertEquals(bookingId,bookingIds.get(0));
    }

    @Test
    public void testGetCurrencyTypesUsedinBooking() {
        appDAO.createBooking(booking);
        Set<CurrencyTypes> typesUsedinBooking = appDAO.getCurrencyTypesUsedinBooking();
        Assert.assertEquals(1,typesUsedinBooking.size());
        Assert.assertTrue(typesUsedinBooking.contains(CurrencyTypes.AUD));
    }

    @Test
    public void testGetSumForCurrency() {
        appDAO.createBooking(booking);
        Booking anotherBooking = new Booking(booking);
        anotherBooking.setPrice(BigDecimal.valueOf(1234.45));
        appDAO.createBooking(anotherBooking);
        Double sumForCurrency = appDAO.getSumForCurrency(booking.getCurrency());
        Assert.assertEquals(Double.valueOf("3579.9"),sumForCurrency);
    }
}
