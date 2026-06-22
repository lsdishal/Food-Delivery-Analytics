package com.fooddelivery;

import java.time.LocalDateTime;
import java.util.List;

public class Order{
    private String order_id;
    private Long customer_id;
    private Long restaurant_id;
    private LocalDateTime orderTime;
    private double orderAmount;
    private String status;
    private List<String> items;

    public Order(){

    }
    public Order(String order_id,Long customer_id,Long restaurant_id,LocalDateTime orderTime,double orderAmount,String status,List<String> items){
        this.order_id=order_id;
        this.customer_id=customer_id;
        this.restaurant_id=restaurant_id;
        this.orderTime=orderTime;
        this.orderAmount=orderAmount;
        this.status=status;
        this.items=items;

    }
    public void setOrder_id(String order_id){
        this.order_id=order_id;
    }
    public String getOrder_id(){
        return order_id;
    }
    public void setCustomer_id(Long customer_id){
        this.customer_id=customer_id;
    }
    public Long getCustomer_id(){
        return customer_id;
    }
    public void setRestaurant_id(Long restaurant_id){
        this.restaurant_id=restaurant_id;
    }
    public Long getRestaurant_id(){
        return restaurant_id;
    }
    public void setOrderTime(LocalDateTime orderTime){
        this.orderTime=orderTime;
    }
    public LocalDateTime getOrderTime(){
        return orderTime;
    }
    public void setOrderAmount(double orderAmount){
        this.orderAmount=orderAmount;
    }
    public double getOrderAmount(){
        return orderAmount;
    }
    public void setStatus(String status){
        this.status=status;
    }
    public String getStatus(){
        return status;
    }
    public void setItems(List<String> items){
        this.items=items;
    }
    public List<String> getItems(){
        return items;
    }
    @Override
    public String toString(){
        return "Order{order_id="+order_id+",customer_id="+customer_id+",restaurant_id="+restaurant_id+",orderTime="+orderTime+",orderAmount="+orderAmount+",status="+status+",items="+items+"}";
    }

}