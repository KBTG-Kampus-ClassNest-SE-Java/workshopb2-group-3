package com.kampus.kbazaar.cart;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CartController {

    private CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/carts")
    public List<CartResponse> getCart() {
        return cartService.getAll();
    }

    @PostMapping("/carts/{username}/items")
    public CartResponse addProduct(@RequestBody CartRequest cartRequest, @PathVariable("username") String username) {
        return cartService.addProduct(cartRequest, username);
    }

    @PostMapping("/cart/{username}/promotions")
    public ResponseEntity addPromotionToProduct(
            @PathVariable() String username, CartRequest cartRequest) {
        cartService.addProduct(cartRequest);
        return new ResponseEntity<>("System.", HttpStatus.OK);
    }
}
