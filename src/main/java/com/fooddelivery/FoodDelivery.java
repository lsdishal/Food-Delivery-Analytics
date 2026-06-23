package com.fooddelivery;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FoodDelivery {

    public static void main(String[] args) {
        List<Customer> cus = Arrays.asList(
                new Customer(1L, "Deepthi", "Chennai", LocalDate.parse("2024-01-10")),
                new Customer(2L, "Dishal", "bengaluru", LocalDate.parse("2025-01-15")),
                new Customer(3L, "Tharun", "cuddalore", LocalDate.parse("2023-05-20"))
        );
        List<Restaurant> restaurants = Arrays.asList(
                new Restaurant(1L, "Dominos", "cuddalore", 4.2),
                new Restaurant(2L, "KFC", "bengaluru", 4.5),
                new Restaurant(3L, "Aiwo", "chennai", 4.8)
        );

        List<Order> orders = Arrays.asList(
                new Order("01", 2l, 3l, LocalDateTime.now().minusMinutes(10), 1212, "placed", Arrays.asList("Sandwich", "Juice")),
                new Order("O2", 1L, 2l, LocalDateTime.of(2025, 12, 18, 2, 1), 500, "PLACED", Arrays.asList("Pizza")),
                new Order("O3", 1L, 3l, LocalDateTime.of(2026, 5, 30, 14, 1), 1000, "PLACED", Arrays.asList("burger")),
                new Order("O4", 1L, 2l, LocalDateTime.of(2026, 3, 24, 10, 1), 300, "PLACED", Arrays.asList("biriyani")),
                new Order("O5", 2L, 1l, LocalDateTime.of(2026, 3, 30, 20, 1), 1000, "PLACED", Arrays.asList("biriyani")),
                new Order("O6", 2L, 1l, LocalDateTime.of(2026, 6, 2, 20, 1), 1000, "PLACED", Arrays.asList("mutton chukka"))
        );

        //Module 1-5
        Completable(orders, restaurants, cus);
        //module 6
        SearchEngine(orders, restaurants, cus);
        //module 7
        GeneralReportingFramework(cus, restaurants, orders);

        //module8
        DateAndTime(orders);

        //module 9
        System.out.println("---------------------------------------------------------------");
        System.out.println("\nModule 9: In-memory cache for orders\n");
        System.out.println("---------------------------------------------------------------");

        orders.forEach(FoodDelivery::ProcessOrder);

        System.out.println("Processing again: ");
        orders.forEach(FoodDelivery::ProcessOrder);

        //Bonus challenge
        RecommendationEngine(1L, orders, restaurants);
    }

    //module 1(validation check)-----------------------------
    public static boolean Validation(List<Customer> cus, List<Restaurant> restaurants, List<Order> orders) {
        System.out.println("---------------------------------------------------------------");
        System.out.println("\nModule 1: Validation check\n");
        System.out.println("---------------------------------------------------------------");
        Predicate<Order> dupli = (order) -> orders.stream()
                .filter(o -> !o.getOrder_id().equals(order.getOrder_id()))
                .anyMatch(o -> o.getCustomer_id().equals(order.getCustomer_id())
                && o.getRestaurant_id().equals(order.getRestaurant_id())
                && o.getOrderAmount() == order.getOrderAmount()
                && Math.abs(
                        Duration.between(
                                o.getOrderTime(),
                                order.getOrderTime()
                        ).toMinutes()
                ) <= 2
                );
        Predicate<Order> notDuplicate = (order) -> !dupli.test(order);

        Predicate<Order> validate_amt = (order) -> {
            return order.getOrderAmount() > 0;
        };
        Predicate<Order> validate_emptyList = (order) -> {
            return !order.getItems().isEmpty();
        };
        Predicate<Order> validTime = order -> !order.getOrderTime()
                .isAfter(LocalDateTime.now());

        Predicate<Order> validRestaurant = (order) -> restaurants.stream()
                .anyMatch(r -> r.getRestaurant_id()
                .equals(order.getRestaurant_id()));

        Predicate<Order> validCustomer = (order) -> cus.stream()
                .anyMatch(c -> c.getCustomer_id()
                .equals(order.getCustomer_id()));

        Predicate<Order> validator = validate_amt.and(validate_emptyList)
                .and(validTime)
                .and(validRestaurant)
                .and(validCustomer);

        for (Order CurrentOrder : orders) {
            if (validator.test(CurrentOrder) && notDuplicate.test(CurrentOrder)) {
                //continue;
            } else {
                return false;
            }
        }
        return true;
    }

    //Module 2(Customer analytics)-------
    public static void Reports(List<Order> orders, List<Customer> cus) {
        Map<Long, Long> fre = FrequencyOfCustomer(orders);
        System.out.println("---------------------------------------------------------------");
        System.out.println("\nModule 2: Customer Analytics\n");
        System.out.println("---------------------------------------------------------------");
        System.out.println("Customer Order frequency: ");
        System.out.println(fre);

        Map<Long, Double> avg = AvgSpendCustomer(orders);
        System.out.println("\nAverage spend per customer: ");
        avg.forEach((id, amt) -> System.out.println("id ->" + id + " amt : " + amt));

        List<Customer> c = CustomersWithMoreThan20Orders(orders, cus);
        System.out.println("\nCustomers with more than 20 orders: ");
        System.out.println(c);

        List<Customer> c1 = Top10(orders, cus);
        System.out.println("\nTop 10 customers: ");
        c1.forEach(o -> System.out.println(o));
        System.out.println("---------------------------------------------------------------");
    }

    public static Map<Long, Long> FrequencyOfCustomer(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(Order::getCustomer_id,
                        Collectors.counting()));
    }

    public static Map<Long, Double> AvgSpendCustomer(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(Order::getCustomer_id,
                        Collectors.averagingDouble(Order::getOrderAmount)));

    }

    public static List<Customer> Top10(List<Order> orders, List<Customer> customers) {
        List<Long> topIds = orders.stream()
                .collect(Collectors.groupingBy(Order::getCustomer_id, Collectors.summingDouble(Order::getOrderAmount)))
                .entrySet()
                .stream()
                .sorted(
                        Map.Entry.<Long, Double>comparingByValue()
                                .reversed()
                )
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return customers.stream()
                .filter(c -> topIds.contains(c.getCustomer_id()))
                .collect(Collectors.toList());
    }

    public static List<Customer> CustomersWithMoreThan20Orders(List<Order> orders, List<Customer> customers) {

        Map<Long, Long> frequency = FrequencyOfCustomer(orders);

        return customers.stream()
                .filter(c -> frequency.getOrDefault(c.getCustomer_id(), 0L) > 20)
                .collect(Collectors.toList());
    }

    // Module 3( Restaurant Analytics )------------------------------------------------------
    public static void RestaurantReports(List<Order> orders, List<Customer> cus, List<Restaurant> restaurants) {
        System.out.println("---------------------------------------------------------------");
        System.out.println("\nModule 3: Restaurant Analytics\n");
        System.out.println("---------------------------------------------------------------");
        Map<Long, Double> avgorder = orders.stream()
                .collect(Collectors.groupingBy(
                        Order::getRestaurant_id,
                        Collectors.averagingDouble(Order::getOrderAmount)));
        System.out.println("Average order amount per restaurant : ");
        System.out.println(avgorder);

        List<Restaurant> rating = restaurants.stream()
                .filter(r -> r.getRating() > 4.5)
                .collect(Collectors.toList());
        System.out.println("\nRestaurants with rating greater than 4.5 : ");
        rating.forEach(o -> System.out.println(o));

        List<Long> top5 = orders.stream()
                .collect(Collectors.groupingBy(
                        Order::getRestaurant_id,
                        Collectors.summingDouble(Order::getOrderAmount)
                ))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        System.out.println("\nTop 5 Restaurants by Revenue: ");
        System.out.println(top5);

        List<Long> top5rest = orders.stream()
                .collect(Collectors.groupingBy(Order::getRestaurant_id, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        System.out.println("\nTop 5 Restaurants by number of orders : ");
        System.out.println(top5rest);

        Map<YearMonth, Double> revenueByMonth = orders.stream()
                .collect(Collectors.groupingBy(order -> YearMonth.from(order.getOrderTime()),
                        Collectors.summingDouble(Order::getOrderAmount)));
        System.out.println("\nRevenue generated each month : ");
        System.out.println(revenueByMonth);
        System.out.println("---------------------------------------------------------------");
    }

    //Module 4 (Stream processing challenge)
    public static void StreamProcessingChallenge(List<Order> orders, List<Restaurant> restaurants, List<Customer> customers) {
        System.out.println("---------------------------------------------------------------");
        System.out.println("\nModule 4: Stream processing challenge\n");
        System.out.println("---------------------------------------------------------------");

        String most_ordered = orders.stream()
                .flatMap(o -> o.getItems().stream())
                .collect(Collectors.groupingBy(item -> item, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No Items");

        System.out.println("Most ordered item : " + most_ordered);

        Long high_rev_res = orders.stream()
                .collect(Collectors.groupingBy(Order::getRestaurant_id, Collectors.summingDouble(Order::getOrderAmount)))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                .limit(1)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        System.out.println("Restaurant with highest revenue : " + high_rev_res);

        Long high_spen_cus = orders.stream()
                .collect(Collectors.groupingBy(Order::getCustomer_id, Collectors.summingDouble(Order::getOrderAmount)))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        System.out.println("Highest spending Customer : " + high_spen_cus);

        Map<YearMonth, Double> mon_rev = orders.stream()
                .collect(Collectors.groupingBy(order -> YearMonth.from(order.getOrderTime()), Collectors.summingDouble(Order::getOrderAmount)));

        System.out.println("Revenue generated each month : ");
        mon_rev.entrySet()
                .stream()
                .forEach(System.out::println);

        Map<String, Double> cityWiseRevenue = orders.stream()
                .collect(
                        Collectors.groupingBy(
                                order -> restaurants.stream()
                                        .filter(r
                                                -> r.getRestaurant_id()
                                                .equals(order.getRestaurant_id()))
                                        .findFirst()
                                        .get()
                                        .getCity(),
                                Collectors.summingDouble(
                                        Order::getOrderAmount
                                )
                        )
                );

        System.out.println("City wise revenue :" + cityWiseRevenue);

        Integer peakHour = orders.stream()
                .collect(Collectors.groupingBy(o -> o.getOrderTime().getHour(), Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        System.out.println("Peak Hour : " + peakHour);
        System.out.println("---------------------------------------------------------------");
    }

    //Module 5(real time order processing pipeline)----------
    public static void Completable(List<Order> orders, List<Restaurant> restaurants, List<Customer> cus) {

        CompletableFuture<List<Order>> orderfuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("Reading orders");
            return orders;
        });

        CompletableFuture<List<Order>> validatedorders = orderfuture.thenApply(orderList -> {

            if (!Validation(cus, restaurants, orderList)) {
                throw new RuntimeException("validation failed");
            } else {
                System.out.println("All the Orders are valid and none are duplicate");
            }
            return orderList;
        });
        CompletableFuture<Void> customerAnalytics = validatedorders.thenAccept(orderList -> {
            System.out.println("\nRunning Customer Analytics...");
            Reports(orderList, cus);
        });
        CompletableFuture<Void> restaurantAnalytics = validatedorders.thenAccept(order -> {
            System.out.println("\nRunning Restaurant Analytics...");
            RestaurantReports(order, cus, restaurants);
        });
        CompletableFuture<Void> streamAnalytics = validatedorders.thenAccept(order -> {
            System.out.println("\nRunning Stream Analytics...");
            StreamProcessingChallenge(order, restaurants, cus);
        });
        CompletableFuture<Void> all = CompletableFuture.allOf(customerAnalytics, restaurantAnalytics, streamAnalytics);
        all.thenRun(() -> {
            System.out.println("\nModule 5 Pipeline Completed Successfully");
        }).join();

        validatedorders = validatedorders.exceptionally(ex -> {
            System.out.println("\nValidation failed -Please try WITH DIFFERENT INPUTS");
            return List.of();
        });
    }

    //Module 6(search engine)
    public static void SearchEngine(List<Order> orders, List<Restaurant> restaurants, List<Customer> cus) {
        System.out.println("---------------------------------------------------------------");
        System.out.println("\nModule 6: Search Orders \n");
        System.out.println("---------------------------------------------------------------");

        Predicate<Order> searchcus = (order) -> order.getCustomer_id().equals(1l);
        Predicate<Order> searchres = (order) -> order.getRestaurant_id().equals(2l);
        Predicate<Order> amtrange = (order) -> order.getOrderAmount() >= 200 && order.getOrderAmount() <= 1000;
        Predicate<Order> status = (order) -> order.getStatus().equalsIgnoreCase("placed");

        Predicate<Order> finalfilter = searchcus
                .and(searchres)
                .and(status)
                .and(amtrange);
        ApplyFilter(orders, finalfilter);

        Predicate<Order> finalfilter1 = searchcus
                .and(amtrange);
        ApplyFilter(orders, finalfilter1);

    }

    public static void ApplyFilter(List<Order> orders, Predicate<Order> filter) {
        System.out.println("\nresults according to given filter: ");
        orders.stream()
                .filter(filter)
                .forEach(System.out::println);

    }

    //module 7(Generic reporting framework)
    public static void GeneralReportingFramework(List<Customer> customers, List<Restaurant> restaurants, List<Order> orders) {
        System.out.println("---------------------------------------------------------------");
        System.out.println("\nModule 7: Generic reporting framework\n");
        System.out.println("---------------------------------------------------------------");

        ReportGenerator<Customer> customerReport = new CustomerReport();
        ReportGenerator<Restaurant> restaurantReport = new RestaurantReport();
        ReportGenerator<Order> orderReport = new OrderReport();

        customerReport.generate(customers);
        restaurantReport.generate(restaurants);
        orderReport.generate(orders);
    }

    //Module 8(Date and Time analytics)
    public static void DateAndTime(List<Order> orders) {
        System.out.println("---------------------------------------------------------------");
        System.out.println("\nModule 8: Date and Time Analytics\n");
        System.out.println("---------------------------------------------------------------");
        System.out.println("---------Orders Today-----------");
        orders.stream()
                .filter(order -> order.getOrderTime().toLocalDate().equals(LocalDate.now()))
                .forEach(System.out::println);

        System.out.println("---------Orders Orders this Month-----------");
        orders.stream()
                .filter(order -> YearMonth.from(order.getOrderTime()).equals(YearMonth.now()))
                .forEach(order -> {
                    System.out.println("order id :" + order.getOrder_id());
                });

        System.out.println("---------Revenue last 12 Months----------");
        LocalDate last12 = LocalDate.now().minusMonths(12);
        orders.stream()
                .filter(order -> !order.getOrderTime().toLocalDate().isBefore(last12))
                .collect(Collectors.groupingBy(order -> order.getOrderTime().getMonth(), Collectors.summingDouble(Order::getOrderAmount)))
                .forEach((month, amount) -> {
                    System.out.println(month + " --->" + amount);
                });
        System.out.println("---------Average Daily Orders---------");
        double avgDaily = orders.stream()
                .collect(Collectors.groupingBy(order -> order.getOrderTime().toLocalDate(), Collectors.counting()))
                .values()
                .stream()
                .mapToDouble(Long::doubleValue)
                .average()
                .orElse(0.0);
        System.out.println("Average Daily Orders :" + avgDaily);

        System.out.println("---------Peak revenue Day ---------");
        LocalDate peakDay = orders.stream()
                .collect(Collectors.groupingBy(order -> order.getOrderTime().toLocalDate(),
                        Collectors.summingDouble(Order::getOrderAmount)))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        System.out.println("Peak revenue Day :" + peakDay.getDayOfWeek());

    }
    //Module 9(In-memory cache)
    private static final ConcurrentHashMap<String, LocalDateTime> cache = new ConcurrentHashMap<>();

    public static void ProcessOrder(Order order) {

        Optional<LocalDateTime> cachedTime = Optional.ofNullable(cache.get(order.getOrder_id()));

        if (cachedTime.isPresent()) {

            long minutes = Duration.between(cachedTime.get(), LocalDateTime.now()).toMinutes();
            if (minutes < 30) {
                System.out.println("Order " + order.getOrder_id() + " already processed. Skipping...");
                return;
            }
        }
        System.out.println("Processing Order : " + order.getOrder_id());
        cache.put(order.getOrder_id(), LocalDateTime.now());
    }

    //bonus challenge
    public static Restaurant RecommendByPreviousOrders(Long customerId, List<Order> orders, List<Restaurant> restaurants) {

        Long restaurantId = orders.stream()
                .filter(o -> o.getCustomer_id().equals(customerId))
                .collect(Collectors.groupingBy(Order::getRestaurant_id, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        return restaurants.stream()
                .filter(r -> r.getRestaurant_id().equals(restaurantId))
                .findFirst()
                .orElse(null);
    }

    public static String MostOrderedCuisine(Long customerId, List<Order> orders) {

        return orders.stream()
                .filter(o -> o.getCustomer_id().equals(customerId))
                .flatMap(o -> o.getItems().stream())
                .collect(Collectors.groupingBy(item -> item, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public static Restaurant HighestRatedRestaurant(List<Restaurant> restaurants) {

        return restaurants.stream()
                .sorted(Comparator.comparingDouble(Restaurant::getRating).reversed())
                .findFirst()
                .orElse(null);
    }

    public static void RecommendationEngine(Long customerId, List<Order> orders, List<Restaurant> restaurants) {

        Restaurant previousOrderRecommendation = RecommendByPreviousOrders(customerId, orders, restaurants);

        String favouriteCuisine = MostOrderedCuisine(customerId, orders);

        Restaurant highestRated = HighestRatedRestaurant(restaurants);

        System.out.println("\n========== RECOMMENDATION ENGINE ==========\n");

        System.out.println("Recommended Restaurant (Previous Orders): " + previousOrderRecommendation);

        System.out.println("Most Ordered Cuisine : " + favouriteCuisine);

        System.out.println("Highest Rated Restaurant : " + highestRated);
    }
}
