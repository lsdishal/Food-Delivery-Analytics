package com.fooddelivery;

import java.time.LocalDate;

public class Customer{
    private Long customer_id;
    private String customer_name;
    private String city;
    private LocalDate registrationDate;
    public Customer(Long customer_id, String customer_name, String city, LocalDate registrationDate) {
        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.city = city;
        this.registrationDate = registrationDate;
    } 
    public Long getCustomer_id() {     
        return customer_id;
    }
    public String getCustomer_name() {     
        return customer_name;
    }
    public String getCity() {     
        return city;
    }
    public LocalDate getRegistrationDate() {     
        return registrationDate;
    }
    public void setCustomer_id(Long customer_id) {     
        this.customer_id = customer_id;
    }   
    public void setCustomer_name(String customer_name) {     
        this.customer_name = customer_name;
    }
    public void setCity(String city) {     
        this.city = city;
    }
    public void setRegistrationDate(LocalDate registrationDate) {     
        this.registrationDate = registrationDate;
    }
    @Override
    public String toString() {     
        return "Customer{" + "customer_id=" + customer_id + ", customer_name='" + customer_name + "'" + ", city='" + city + "'" + ", registrationDate=" + registrationDate + '}';
    }   
}