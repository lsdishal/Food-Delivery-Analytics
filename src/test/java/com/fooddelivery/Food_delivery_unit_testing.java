package com.fooddelivery;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class Food_delivery_unit_testing {

    @Test
    public void testValidation() {
        List<Customer> cus = Arrays.asList(
                new Customer(1L, "Deepthi", "Chennai", LocalDate.parse("2024-01-10")),
                new Customer(2L, "Dishal", "bengaluru", LocalDate.parse("2025-01-15"))
        );
        List<Restaurant> restaurants = Arrays.asList(
                new Restaurant(1L, "Dominos", "cuddalore", 4.2),
                new Restaurant(2L, "KFC", "bengaluru", 4.5)
        );

        // Valid orders
        List<Order> orders = Arrays.asList(
                new Order("O1", 1L, 1L, LocalDateTime.now().minusMinutes(10), 500, "placed", Arrays.asList("Pizza")),
                new Order("O2", 2L, 2L, LocalDateTime.now().minusMinutes(5), 300, "placed", Arrays.asList("Burger"))
        );
        assertTrue(Food_delivery.validation(cus, restaurants, orders));
    }

    @Test
    public void testInvalidRestaurant() {
        List<Customer> cus = Arrays.asList(new Customer(1L, "Deepthi", "Chennai",LocalDate.parse("2024-01-10")));
        List<Restaurant> restaurants = Arrays.asList(new Restaurant(1L, "Dominos", "cuddalore", 4.2));
        List<Order> orders = Arrays.asList(new Order("O1",1L,999L,LocalDateTime.now().minusMinutes(10),500,"placed",Arrays.asList("Pizza")));
        assertFalse(Food_delivery.validation(cus, restaurants, orders));
    }

    @Test
    public void testInvalidCustomer() {

        List<Customer> cus = Arrays.asList(new Customer(1L, "Deepthi", "Chennai",LocalDate.parse("2024-01-10")));
        List<Restaurant> restaurants = Arrays.asList(new Restaurant(1L, "Dominos", "cuddalore", 4.2));
        List<Order> orders = Arrays.asList(new Order("O1",999L,1L,LocalDateTime.now().minusMinutes(10),500,"placed",Arrays.asList("Pizza")));
        assertFalse(Food_delivery.validation(cus,restaurants,orders));
    }
    @Test
    public void testNegativeAmount() {

    List<Customer> cus = Arrays.asList(new Customer(1L,"Deepthi","Chennai",LocalDate.parse("2024-01-10")));

    List<Restaurant> restaurants = Arrays.asList(new Restaurant(1L,"Dominos","cuddalore",4.2));

    List<Order> orders = Arrays.asList(new Order("O1",1L,1L,LocalDateTime.now().minusMinutes(5),-500,"placed",Arrays.asList("Pizza")));

    assertFalse(Food_delivery.validation(cus,restaurants,orders));
    }
    
@Test
public void testDuplicateOrder() {

    List<Customer> cus = Arrays.asList(new Customer(1L,"Deepthi","Chennai",LocalDate.parse("2024-01-10")));

    List<Restaurant> restaurants = Arrays.asList(new Restaurant(1L,"Dominos","cuddalore",4.2));

    List<Order> orders = Arrays.asList(new Order("O1",1L,1L,LocalDateTime.now(),500,"placed",Arrays.asList("Pizza")),
                                    new Order("O2",1L,1L,LocalDateTime.now().plusMinutes(1),500,"placed",Arrays.asList("Pizza")));

    assertFalse(Food_delivery.validation(cus,restaurants,orders));
}
@Test
public void testAverageSpendCustomer() {

    List<Order> orders = Arrays.asList(new Order("O1",1L,1L,LocalDateTime.now(),500,"placed",Arrays.asList("Pizza")),
    new Order("O2",1L,1L,LocalDateTime.now(),1000,"placed",Arrays.asList("Burger")));

    Map<Long,Double> result =Food_delivery.avgspendCustomers(orders);
    System.out.println(result);
    assertEquals(750.0,result.get(1L));
}
@Test
void testTop10Customers() {

    List<Customer> customers =Arrays.asList(new Customer(1L,"Deepthi","Chennai",LocalDate.now()));

    List<Order> orders = Arrays.asList(new Order("O1",1L,1L,LocalDateTime.now(),1000,"PLACED",Arrays.asList("Pizza")));

    List<Customer> result = Food_delivery.top10(orders,customers);

    assertEquals(1,result.size());
}}
