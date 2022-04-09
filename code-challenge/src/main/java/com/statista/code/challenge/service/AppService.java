package com.statista.code.challenge.service;

import com.statista.code.challenge.dao.AppDAO;
import com.statista.code.challenge.domain.*;
import com.statista.code.challenge.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppService {

    private final AppDAO appDAO;
    private final DepartmentFactory departmentFactory;

    @Transactional
    public Integer createBooking(Booking booking){
        log.info("Creating booking entry for booking : {}",booking);
        Integer bookingId = appDAO.createBooking(booking);
        log.info("After Creating booking entry, bookingId : {}",bookingId);
        return bookingId;
    }

    @Transactional
    public boolean updateBooking(Booking booking, Integer bookingId){
        log.info("Updating booking entry for bookingId : {}",bookingId);
        boolean result = appDAO.updateBooking(booking,bookingId);
        log.info("After Updating booking entry for bookingId {}, result = {}",bookingId,result);
        return result;
    }

    public Booking getBookingById(Integer bookingId){
        return appDAO.getBookingById(bookingId);
    }

    public List<Integer> getBookingIdsForDepartment(String department){
        log.info("Before fetching List of bookingId for department : {}",department);
        List<Integer>  bookingIdList = appDAO.getBookingIdsForDepartment(department);
        log.info("After fetching List of bookingId for department : {}",department);
        return bookingIdList;
    }
    public Set<CurrencyTypes> getCurrencyTypesUsedinBooking(){
        return appDAO.getCurrencyTypesUsedinBooking();
    }

    public Double getSumOfAmountByCurrency(CurrencyTypes currencyTypes){
        Double dSumOfCurrency = appDAO.getSumForCurrency(currencyTypes);
        log.info("In getSumOfAmountByCurrency, dSumOfCurrency : {}",dSumOfCurrency);
        return dSumOfCurrency;
    }

    @Transactional
    public CustomerOnboarding getBusinessResultForDepartmentOfBooking(Integer bookingId){
        Booking booking = appDAO.getBookingById(bookingId);
        if(booking==null){
            throw new ObjectNotFoundException("No Booking found for id = ".concat(String.valueOf(bookingId)),"Booking");
        }
        if(StringUtils.isBlank(booking.getDepartment())){
            throw new ObjectNotFoundException("No department found for booking of bookingId = ".concat(String.valueOf(bookingId)),"Booking");
        }
        Department department = departmentFactory.create(booking.getDepartment());
        CustomerOnboarding customerOnboarding = department.doBusiness();
        log.info("In getBusinessResultForDepartmentofBooking, customerOnboarding : {}",customerOnboarding);

        return customerOnboarding;
    }

}
