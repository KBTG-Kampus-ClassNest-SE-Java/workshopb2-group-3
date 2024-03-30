package com.kampus.kbazaar.cart;

import com.kampus.kbazaar.product.Product;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(String username, List<CartItemResponse> items, BigDecimal discount, BigDecimal totalDiscount,BigDecimal subTotal,BigDecimal grandTotal ) {}
