package com.victim.customer;

import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CustomerRepository {

    private final Map<String, Tier> cache = new ConcurrentHashMap<>();

    public CustomerRepository() {
        cache.put("cust_1001", Tier.GOLD);
        cache.put("cust_1002", Tier.SILVER);
    }

    public Tier findTier(String customerId) {
        return cache.get(customerId);
    }
}
