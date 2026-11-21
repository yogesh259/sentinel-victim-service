package com.victim.customer;

import java.math.BigDecimal;

public enum Tier {
    STANDARD("1.00"),
    SILVER("0.95"),
    GOLD("0.90");

    private final BigDecimal multiplier;

    Tier(String multiplier) {
        this.multiplier = new BigDecimal(multiplier);
    }

    public BigDecimal multiplier() {
        return multiplier;
    }
}
