package com.victim.customer;

import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public class CustomerRepository {

    private final Map<String, Tier> tiers = Map.of(
        "cust_1001", Tier.GOLD,
        "cust_1002", Tier.SILVER
    );

    public Tier findTier(String customerId) {
        return tiers.getOrDefault(customerId, Tier.STANDARD);
    }
}
