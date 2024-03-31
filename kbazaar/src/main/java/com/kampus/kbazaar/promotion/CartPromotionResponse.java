package com.kampus.kbazaar.promotion;

public class CartPromotionResponse {
    private final String message;

    public CartPromotionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
