package com.statista.code.challenge.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.statista.code.challenge.validators.MonetaryVal;
import lombok.Data;

import javax.money.MonetaryAmount;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

@Data
public class Booking implements Comparable, Serializable {


    public Booking(){

    }
    public Booking(Booking other){
        this.department = other.department;
        this.email = other.email;
        this.monetaryAmount = other.monetaryAmount;
        this.subscriptionStartDate = other.subscriptionStartDate;
        this.description = other.description;
    }

    private Integer Id;

    @NotBlank(message = "department is mandatory")
    @JsonProperty("department")
    private String department ;

    @JsonProperty("email")
    @Email
    @NotBlank(message = "email is mandatory")
    private String email;


    @MonetaryVal
    private MonetaryAmount monetaryAmount;

    @JsonProperty("subscription_start_date")
    private Long subscriptionStartDate;

    @JsonProperty("description")
    private String description;


    @Override
    public int compareTo(Object o) {
        if (this == o) return 0;
        if (o == null || getClass() != o.getClass()) return 1;
        Booking booking = (Booking) o;
        if(getDepartment().equals(booking.getDepartment()) && this.getMonetaryAmount().equals(booking.getMonetaryAmount()) && getEmail().equals(booking.getEmail()) && this.getMonetaryAmount() == booking.getMonetaryAmount() && getSubscriptionStartDate().equals(booking.getSubscriptionStartDate()) && Objects.equals(getDescription(), booking.getDescription())) return 0;
        return 1;
    }
}
