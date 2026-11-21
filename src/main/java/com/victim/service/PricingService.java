package com.victim.service;

import com.victim.customer.CustomerRepository;
import com.victim.customer.Tier;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PricingService {

    private final CustomerRepository customers;

    public PricingService(CustomerRepository customers) {
        this.customers = customers;
    }

    public BigDecimal priceFor(String customerId, BigDecimal amount) {
        Tier tier = customers.findTier(customerId);
        return amount.multiply(tier.multiplier()).setScale(2, RoundingMode.HALF_UP);
    }
}
