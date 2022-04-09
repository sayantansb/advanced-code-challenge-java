package com.statista.code.challenge.database;

import com.statista.code.challenge.domain.Booking;
import com.statista.code.challenge.domain.CurrencyTypes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DBData implements Serializable {
    public Map<Integer, Booking> bookingMap;
    public Map<String, List<Integer>> bookingIdListByDepartmentIdMap;
    public Map<CurrencyTypes,Map<Integer, BigDecimal>> pricesForBookingsByCurrencyMap;
}
