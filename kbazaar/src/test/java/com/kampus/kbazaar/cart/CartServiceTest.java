package com.kampus.kbazaar.cart;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.kampus.kbazaar.product.Product;
import com.kampus.kbazaar.product.ProductRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CartServiceTest {

    @Mock private CartRepository cartRepository;

    @Mock private CartItemRepository cartItemRepository;

    @Mock private ProductRepository productRepository;

    @InjectMocks private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("should be able to add product to cart")
    void addProduct_ShouldBeReturnCartResponse() {

        CartRequest request = new CartRequest("MOBILE-GOOGLE-PIXEL-12313", 1);
        String username = "user1";

        Product product = new Product();
        product.setSku("MOBILE-GOOGLE-PIXEL-12313");
        product.setPrice(new BigDecimal(1000));
        product.setName("Google Pixel");
        product.setQuantity(10);
        when(productRepository.findBySku(request.productSku())).thenReturn(Optional.of(product));

        Cart cart = new Cart();
        cart.setUsername(username);
        cart.setSubtotal(new BigDecimal(1000));
        cart.setGrandTotal(new BigDecimal(1000));
        when(cartRepository.findByUsername(username)).thenReturn(Optional.of(cart));

        List<CartItem> cartItems = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItem.setSku("MOBILE-GOOGLE-PIXEL-12313");
        cartItem.setName("Google Pixel");
        cartItem.setPrice(new BigDecimal(1000));
        cartItem.setQuantity(1);

        cartItems.add(cartItem);
        when(cartItemRepository.findByUsername(username)).thenReturn(Optional.of(cartItems));

        CartResponse result = cartService.addProduct(request, username);

        assertEquals("MOBILE-GOOGLE-PIXEL-12313", result.items().get(0).sku());
    }

    @Test
    @DisplayName("should be update cart")
    void updateCart_ShouldBeReturnCartResponse() {
        Cart cart = new Cart();
        cart.setUsername("username");
        cart.setDiscount(new BigDecimal("5"));
        cart.setTotalDiscount(new BigDecimal("0"));
        cart.setSubtotal(new BigDecimal("0"));
        cart.setGrandTotal(new BigDecimal("0"));

        List<CartItem> cartItems = new ArrayList<>();
        CartItem cartItem1 = new CartItem();
        cartItem1.setSku("MOBILE-GOOGLE-PIXEL-12313");
        cartItem1.setName("Google Pixel");
        cartItem1.setPrice(new BigDecimal("1000"));
        //        cartItem1.setDiscount(new BigDecimal("10"));
        cartItem1.setQuantity(1);

        CartItem cartItem2 = new CartItem();
        cartItem2.setSku("SUMSUNG-GALAXY");
        cartItem2.setName("Samsung Galaxy");
        cartItem2.setPrice(new BigDecimal("28000.25"));
        cartItem2.setDiscount(new BigDecimal("10"));
        cartItem2.setQuantity(1);

        cartItems.add(cartItem1);
        cartItems.add(cartItem2);

        Cart result = cartService.updateCart(cart, cartItems);
        assertEquals(new BigDecimal("29000.25"), result.getSubtotal());
        assertEquals(new BigDecimal("15"), result.getTotalDiscount());
        assertEquals(new BigDecimal("28985.25"), result.getGrandTotal());
    }
}
