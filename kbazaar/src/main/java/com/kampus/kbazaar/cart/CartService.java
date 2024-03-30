package com.kampus.kbazaar.cart;

import com.kampus.kbazaar.exceptions.NotFoundException;
import com.kampus.kbazaar.product.Product;
import com.kampus.kbazaar.product.ProductRepository;
import com.kampus.kbazaar.product.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private CartRepository cartRepository;
    private CartItemRepository cartItemRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public List<CartResponse> getAll() {

        List<CartResponse> cartResponseList = new ArrayList<>();
        for (Cart cart : cartRepository.findAll()) {
            List<CartItemResponse> cartItems = cartItemRepository.findByUsername(cart.getUsername()).stream().map(CartItem::toResponse).toList();
            CartResponse cartResponse = new CartResponse(cart.getUsername(), cartItems, cart.getDiscount(), cart.getTotalDiscount(), cart.getSubtotal(), cart.getGrandTotal());
            cartResponseList.add(cartResponse);
        }
        return cartResponseList;
    }
}
