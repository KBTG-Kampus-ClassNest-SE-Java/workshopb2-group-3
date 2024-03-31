package com.kampus.kbazaar.cart;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "cart_item")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "sku", unique = true)
    private String sku;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = "promotion_codes")
    private String promotionCodes;

    public CartItemResponse toResponse() {
        return new CartItemResponse(
                this.id,
                this.username,
                this.sku,
                this.name,
                this.price,
                this.quantity,
                this.discount,
                this.promotionCodes);
    }
}
