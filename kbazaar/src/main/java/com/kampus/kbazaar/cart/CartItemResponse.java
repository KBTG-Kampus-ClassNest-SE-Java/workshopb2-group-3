package com.kampus.kbazaar.cart;

import java.math.BigDecimal;

public record CartItemResponse(
        Long id,
        String username,
        String sku,
        String name,
        BigDecimal price,
        int quantity,
        BigDecimal discount,
        String promotionCode) {}
