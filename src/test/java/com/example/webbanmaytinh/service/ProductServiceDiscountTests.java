package com.example.webbanmaytinh.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductServiceDiscountTests {

    @Test
    void calculateFinalPrice_returnsNull_whenPriceNull() {
        assertNull(ProductService.calculateFinalPrice(null, true, 10));
    }

    @Test
    void calculateFinalPrice_returnsOriginal_whenNotDiscounted() {
        BigDecimal price = new BigDecimal("100.00");
        assertEquals(new BigDecimal("100.00"), ProductService.calculateFinalPrice(price, false, 50));
    }

    @Test
    void calculateFinalPrice_returnsOriginal_whenDiscountPercentZeroOrNegative() {
        BigDecimal price = new BigDecimal("100.00");
        assertEquals(new BigDecimal("100.00"), ProductService.calculateFinalPrice(price, true, 0));
        assertEquals(new BigDecimal("100.00"), ProductService.calculateFinalPrice(price, true, -1));
    }

    @Test
    void calculateFinalPrice_appliesDiscount_withRounding() {
        BigDecimal price = new BigDecimal("999.99");
        assertEquals(new BigDecimal("849.99"), ProductService.calculateFinalPrice(price, true, 15));
    }

    @Test
    void calculateFinalPrice_throws_whenPriceNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> ProductService.calculateFinalPrice(new BigDecimal("-1.00"), true, 10));
    }

    @Test
    void calculateFinalPrice_throws_whenDiscountPercentGreaterThan100() {
        assertThrows(IllegalArgumentException.class,
                () -> ProductService.calculateFinalPrice(new BigDecimal("10.00"), true, 101));
    }
}

