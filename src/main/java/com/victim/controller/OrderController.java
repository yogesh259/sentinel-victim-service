package com.victim.controller;

import com.victim.service.OrderService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Map<String, Object>> listOrders() {
        return orderService.listOrders();
    }

    @PostMapping
    public Map<String, Object> createOrder(@RequestBody Map<String, Object> body) {
        return orderService.createOrder(body);
    }
}
