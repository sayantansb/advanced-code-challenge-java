package com.statista.code.challenge.database;

import com.statista.code.challenge.domain.Booking;
import com.statista.code.challenge.domain.CurrencyTypes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MockDB implements Serializable {

    public static final Map<Integer, Booking> bookingMap = new HashMap<>();
    public static final Map<String, List<Integer>> bookingIdListByDepartmentIdMap = new HashMap<>();
    public static final Map<CurrencyTypes,Map<Integer,BigDecimal>> pricesForBookingsByCurrencyMap = new HashMap<>();


}
