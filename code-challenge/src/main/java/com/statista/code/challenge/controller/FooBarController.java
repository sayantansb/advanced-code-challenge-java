package com.statista.code.challenge.controller;

import com.statista.code.challenge.domain.Booking;
import com.statista.code.challenge.domain.CurrencyTypes;
import com.statista.code.challenge.domain.CustomerOnboarding;
import com.statista.code.challenge.service.AppService;
import com.statista.code.challenge.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

@Controller
@RequestMapping("/bookingservice")
@RequiredArgsConstructor
@Slf4j
public class FooBarController {


    private final AppService appService;

    private JavaMailSender emailSender;

    @Autowired
    private EmailService emailService;

    @PostConstruct
    public void init(){
        emailSender = mock(JavaMailSender.class);
        doNothing().when(emailSender).send(any(SimpleMailMessage.class));
        ReflectionTestUtils.setField(emailService, "emailSender", emailSender);
    }



    @PostMapping("/bookings")
    public ResponseEntity<Integer> createBooking(@RequestBody @Valid Booking booking) {
        Integer bookingId = appService.createBooking(booking);
        log.info("Id of booking created : {}",bookingId);
        return new ResponseEntity<Integer>(bookingId, HttpStatus.CREATED);
    }
    @PutMapping("/bookings/{bookingId}")
    public ResponseEntity<Booking> updateBooking(@PathVariable("bookingId")Integer bookingId, @RequestBody @Valid Booking booking) {
        appService.updateBooking(booking,bookingId);
        return new ResponseEntity<Booking>(booking, HttpStatus.OK);
    }
    @GetMapping("/bookings/{bookingId}")
    public ResponseEntity<Booking> getBookingById(@PathVariable("bookingId")Integer bookingId) {
        Booking booking = appService.getBookingById(bookingId);
        log.info("booking retrieved : {}",booking);
        return new ResponseEntity<Booking>(booking, HttpStatus.OK);
    }


    @GetMapping("/bookings/department/{department}")
    public ResponseEntity<List<Integer>> getBookingIdsForDepartmentId(@PathVariable("department")String department) {
        List<Integer> bookingIds = appService.getBookingIdsForDepartment(department);
        return new ResponseEntity<List<Integer>>(bookingIds, HttpStatus.OK);
    }

    @GetMapping("/bookings/currencies")
    public ResponseEntity<Set<CurrencyTypes>> getCurrenciesUsedInBookings() {
        Set<CurrencyTypes> currencyTypes = appService.getCurrencyTypesUsedinBooking();
        return new ResponseEntity<Set<CurrencyTypes>>(currencyTypes, HttpStatus.OK);
    }

    @GetMapping("/sum/{currency}")
    public ResponseEntity<Double> getSumOfCurrencies(@PathVariable("currency")CurrencyTypes currencyTypes) {
        Double sum = appService.getSumOfAmountByCurrency(currencyTypes);
        return new ResponseEntity<Double>(sum, HttpStatus.OK);
    }

    @GetMapping("/bookings/dobusiness/{booking_id}")
    public ResponseEntity<CustomerOnboarding> getBusinessResultForDepartmentOfBooking(@PathVariable("booking_id")Integer bookingId)  {
        CustomerOnboarding customerOnboarding = appService.getBusinessResultForDepartmentOfBooking(bookingId);
        emailService.sendSimpleMessage("test@statista.de","Onboarding Confirmation","Congratulations, You have been onboarded!!");
        return new ResponseEntity<CustomerOnboarding>(customerOnboarding, HttpStatus.OK);
    }

}