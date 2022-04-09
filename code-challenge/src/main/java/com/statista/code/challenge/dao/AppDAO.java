package com.statista.code.challenge.dao;

import com.statista.code.challenge.database.MockDB;
import com.statista.code.challenge.domain.Booking;
import com.statista.code.challenge.domain.CurrencyTypes;
import com.statista.code.challenge.exception.ObjectNotFoundException;
import com.statista.code.challenge.exception.ObjectNotPersistedException;
import com.statista.code.challenge.exception.ObjectNotUpdatedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * This class mimics a DAO class as it updates the object states in mock database (MockDB)
 * It supports only the persistence or query operations to cater to limited set of APIs in FooBarController
 * The classes/objects are designed to support retrieval from object graph
 */

@Component
@Slf4j
public class AppDAO {
    private Map<String, AtomicInteger> idGeneratorMap = new HashMap<>();

    @PostConstruct
    public void postConstruct() {
        idGeneratorMap.put("booking",new AtomicInteger(MockDB.bookingMap.size()));
    }

    public Map<String, AtomicInteger> getIdGeneratorMap() {
        return idGeneratorMap;
    }

    public Integer createBooking(Booking booking){
        try{
            booking.setId(this.generateNextID("booking"));
            insertBookingInDB(booking);
            updateBookingIdListForDepartment(booking.getDepartment(),booking.getId());
            updatePricesByCurrencyMap(booking.getCurrency(),booking.getPrice(),booking.getId());
            return booking.getId();
        }catch (Exception ex){
            log.error("Exception in persisting booking : ",ex);
            throw new ObjectNotPersistedException("Booking not created","Booking");
        }
    }

    public boolean updateBooking(Booking booking,Integer Id){
        Booking existingBooking = MockDB.bookingMap.get(Id);
        if(existingBooking == null){
            throw new ObjectNotFoundException("Booking not found for bookingId : ".concat(String.valueOf(Id)),"Booking");
        }
        try{
            BeanUtils.copyProperties(booking,existingBooking);
            existingBooking.setId(Id);
            removeBookingIdListForDepartment(Id);
            removePriceByCurrencyMap(Id);
            updateBookingInDB(existingBooking,Id);
            updateBookingIdListForDepartment(existingBooking.getDepartment(),existingBooking.getId());
            updatePricesByCurrencyMap(existingBooking.getCurrency(),existingBooking.getPrice(),Id);
            return true;
        }catch (Exception ex){
            log.error("Exception in updating booking : ",ex);
            throw new ObjectNotUpdatedException("Booking not created","Booking");
        }

    }

    public Booking getBookingById(Integer bookingId){
        Booking booking = MockDB.bookingMap.get(bookingId);
        if(booking == null){
            throw new ObjectNotFoundException("Booking not found with Id : ".concat(String.valueOf(bookingId)),"Booking");
        }
        return booking;
    }

    public List<Integer> getBookingIdsForDepartment(String departmentId){
        if(!MockDB.bookingIdListByDepartmentIdMap.containsKey(departmentId)){
            throw new ObjectNotFoundException("No Booking with department ".concat(departmentId).concat(" exists!"),"Department");
        }
        return MockDB.bookingIdListByDepartmentIdMap.get(departmentId);
    }

    public Set<CurrencyTypes> getCurrencyTypesUsedinBooking(){
        return MockDB.pricesForBookingsByCurrencyMap.keySet();
    }

    public Double getSumForCurrency(CurrencyTypes currencyTypes){
        if(!MockDB.pricesForBookingsByCurrencyMap.containsKey(currencyTypes)){
            throw new ObjectNotFoundException("No booking found for the given currency ".concat(currencyTypes.name()),"currency");
        }
        return BigDecimal.valueOf(MockDB.pricesForBookingsByCurrencyMap.get(currencyTypes).values().stream().collect(Collectors.summingDouble(BigDecimal::doubleValue))).setScale(3, RoundingMode.HALF_UP).doubleValue();
    }


    private Integer generateNextID(String objectType){
        switch (objectType){
            case "booking" :
                return idGeneratorMap.get("booking").incrementAndGet();
            default :
                return null;
        }
    }

    private void insertBookingInDB(Booking booking){
        MockDB.bookingMap.put(booking.getId(),booking);
    }

    private void updateBookingInDB(Booking booking, Integer bookingId){
        if(!MockDB.bookingMap.containsKey(bookingId)){
            throw new ObjectNotUpdatedException("Booking with Id ".concat(String.valueOf(bookingId)).concat(" does not exist"),"Booking");
        }
        MockDB.bookingMap.put(bookingId,booking);
    }

     private void updateBookingIdListForDepartment(String departmentId,Integer bookingId){
        if(MockDB.bookingIdListByDepartmentIdMap.containsKey(departmentId)){
            MockDB.bookingIdListByDepartmentIdMap.get(departmentId).add(bookingId);
        }else{
            List<Integer> bookingIdList = new ArrayList<>();
            bookingIdList.add(bookingId);
            MockDB.bookingIdListByDepartmentIdMap.put(departmentId,bookingIdList);
        }
     }

    private void removeBookingIdListForDepartment(Integer bookingId){
        for(Map.Entry<String,List<Integer>> entry : MockDB.bookingIdListByDepartmentIdMap.entrySet()){
            if(entry.getValue().contains(bookingId)){
                entry.getValue().remove(bookingId);
            }
        }
    }

    private void updatePricesByCurrencyMap(CurrencyTypes currencyTypes, BigDecimal price,Integer bookingId){
        if(MockDB.pricesForBookingsByCurrencyMap.containsKey(currencyTypes)){
            MockDB.pricesForBookingsByCurrencyMap.get(currencyTypes).put(bookingId,price);
        }else{
          Map<Integer,BigDecimal> pricesMap = new HashMap<>();
          pricesMap.put(bookingId,price);
          MockDB.pricesForBookingsByCurrencyMap.put(currencyTypes,pricesMap);
        }
    }

    private void removePriceByCurrencyMap(Integer bookingId){
        for(Map.Entry<CurrencyTypes,Map<Integer,BigDecimal>> entry : MockDB.pricesForBookingsByCurrencyMap.entrySet()){
            if(entry.getValue().containsKey(bookingId)){
                entry.getValue().remove(bookingId);
            }
        }
    }

}
