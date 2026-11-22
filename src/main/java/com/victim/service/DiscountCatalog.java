package com.victim.service;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DiscountCatalog {

    private final Map<String, Discount> byCode;

    public DiscountCatalog() {
        this.byCode = load();
    }

    private static Map<String, Discount> load() {
        List<Discount> discounts = List.of(
            new Discount("SAVE10", new BigDecimal("0.90")),
            new Discount("SAVE15", new BigDecimal("0.85")),
            new Discount("SAVE10", new BigDecimal("0.75")),
            new Discount("SAVE20", new BigDecimal("0.80"))
        );
        return discounts.stream().collect(Collectors.toMap(Discount::code, Function.identity()));
    }

    public BigDecimal apply(BigDecimal amount, String code) {
        if (code == null || code.isBlank()) {
            return amount;
        }
        Discount discount = byCode.get(code);
        return discount == null ? amount : amount.multiply(discount.multiplier());
    }

    public record Discount(String code, BigDecimal multiplier) {}
}
