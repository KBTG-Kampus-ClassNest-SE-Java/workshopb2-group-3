package com.kampus.kbazaar.cart;

import jakarta.persistence.*;
import java.math.BigDecimal;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "discount")
    private BigDecimal discount = BigDecimal.ZERO;

    @Column(name = "total_discount")
    private BigDecimal totalDiscount = BigDecimal.ZERO;

    @Column(name = "promotion_codes")
    private String promotionCodes = "";

    @Description("precisely reflect its pre-discount status")
    @Column(name = "subtotal")
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Description("the final, all-inclusive amount to be paid.")
    @Column(name = "grand_total")
    private BigDecimal grandTotal = BigDecimal.ZERO;
}
