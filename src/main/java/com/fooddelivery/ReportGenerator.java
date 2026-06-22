package com.fooddelivery;

import java.util.List;

@FunctionalInterface
public interface ReportGenerator<T>{
    public void generate(List<T> data);
}
class CustomerReport implements ReportGenerator<Customer>{
    @Override
    public void generate(List<Customer> data){
        System.out.println("\n----------Customer report----------\n");
        data.forEach(System.out::println);
    }
}
class RestaurantReport implements ReportGenerator<Restaurant>{
    @Override
    public void generate(List<Restaurant> data){
        System.out.println("\n----------Restaurant report----------\n");
        data.forEach(System.out::println);
    }
}
class OrderReport implements ReportGenerator<Order>{
    @Override
    public void generate(List<Order> data){
        System.out.println("\n----------Order report----------\n");
        data.forEach(System.out::println);
    }
}

