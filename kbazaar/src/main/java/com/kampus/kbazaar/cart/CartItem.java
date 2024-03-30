package com.kampus.kbazaar.cart;

import com.kampus.kbazaar.product.ProductResponse;
import jakarta.persistence.*;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "cart_item")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "sku")
    private String sku;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "quanttity")
    private int quanttity;

    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = "promotion_codes")
    private String promotionCodes;

    public CartItemResponse toResponse() {
        return new CartItemResponse(this.id,this.username,this.sku,this.name,this.price,this.quanttity,this.discount,this.promotionCodes);
    }
}
