package com.kampus.kbazaar.cart;

import com.kampus.kbazaar.product.ProductResponse;
import com.kampus.kbazaar.product.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private final ProductService productService;
    private CartRepository cartRepository;
    private CartItemRepository cartItemRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductService productService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
    }

    public List<CartResponse> getAll() {

        List<CartResponse> cartResponseList = new ArrayList<>();
        for (Cart cart : cartRepository.findAll()) {
            List<CartItemResponse> cartItems = cartItemRepository.findByUsername(cart.getUsername()).orElse(new ArrayList<>()).stream().map(CartItem::toResponse).collect(Collectors.toList());
            CartResponse cartResponse = new CartResponse(cart.getUsername(), cartItems, cart.getDiscount(), cart.getTotalDiscount(), cart.getSubtotal(), cart.getGrandTotal());
            cartResponseList.add(cartResponse);
        }
        return cartResponseList;
    }

    public CartResponse addProduct(CartRequest request){
        ProductResponse productResponse = productService.getBySku(request.productSku());
        return null;
    }

}
