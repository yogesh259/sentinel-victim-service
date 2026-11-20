package com.victim.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final List<String> orders = new ArrayList<>();

    @GetMapping
    public List<String> listOrders() {
        return orders;
    }

    @PostMapping
    public String createOrder(@RequestBody String orderJson) {
        orders.add(orderJson);
        return "{\"status\": \"created\"}";
    }
}
