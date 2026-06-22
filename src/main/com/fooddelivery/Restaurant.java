package com.fooddelivery;

public class Restaurant{
    private Long restaurant_id;
    private String restaurant_name;
    private String city;
    private double rating;

    public Restaurant(){
        
    }
    public Restaurant(Long restaurant_id, String restaurant_name, String city, double rating) {
        this.restaurant_id = restaurant_id;
        this.restaurant_name = restaurant_name;
        this.city = city;
        this.rating = rating;
    }   
    public Long getRestaurant_id() {     
        return restaurant_id;    
    }   
    public String getRestaurant_name() {     
        return restaurant_name;    
    }   
    public String getCity() {     
        return city;    
    }   
    public double getRating() {     
        return rating;    
    }   
    public void setRestaurant_id(Long restaurant_id) {     
        this.restaurant_id = restaurant_id;    
    }   
    public void setRestaurant_name(String restaurant_name) {     
        this.restaurant_name = restaurant_name;    
    }   
    public void setCity(String city) {     
        this.city = city;    
    }   
    public void setRating(double rating) {     
        this.rating = rating;    
    }   
    @Override
    public String toString() {     
        return "Restaurant{" + "restaurant_id=" + restaurant_id + ", restaurant_name='" + restaurant_name + "'" + ", city='" + city + "'" + ", rating=" + rating + '}';
    }   
}