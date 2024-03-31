package com.kampus.kbazaar.cart;

import com.kampus.kbazaar.product.Product;
import com.kampus.kbazaar.product.ProductRepository;

import java.math.BigDecimal;
import com.kampus.kbazaar.promotion.Promotion;
import com.kampus.kbazaar.promotion.PromotionRepository;
import com.kampus.kbazaar.promotion.PromotionRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private CartRepository cartRepository;
    private CartItemRepository cartItemRepository;
    private ProductRepository productRepository;
    private PromotionRepository promotionRepository;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            PromotionRepository promotionRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.promotionRepository = promotionRepository;
    }

    public List<CartResponse> getAll() {

        List<CartResponse> cartResponseList = new ArrayList<>();
        for (Cart cart : cartRepository.findAll()) {
            List<CartItemResponse> cartItems =
                    cartItemRepository
                            .findByUsername(cart.getUsername())
                            .orElse(new ArrayList<>())
                            .stream()
                            .map(CartItem::toResponse)
                            .collect(Collectors.toList());
            CartResponse cartResponse =
                    new CartResponse(
                            cart.getUsername(),
                            cartItems,
                            cart.getDiscount(),
                            cart.getTotalDiscount(),
                            cart.getSubtotal(),
                            cart.getGrandTotal());
            cartResponseList.add(cartResponse);
        }
        return cartResponseList;
    }

    public CartResponse addProduct(CartRequest request, String username) {
        Optional<Product> product = productRepository.findBySku(request.productSku());
        if (product.isEmpty()) {
            throw new RuntimeException("Product not found");
        }
        Cart cart;
        Optional<Cart> cartOptional = cartRepository.findByUsername(username);
        if (cartOptional.isEmpty()) {
            cart = new Cart();
            cart.setUsername(username);
            cartRepository.save(cart);
            cartOptional = cartRepository.findByUsername(username);
        }

        cart = cartOptional.get();

        CartItem cartItemRequest = new CartItem();
        cartItemRequest.setUsername(username);
        cartItemRequest.setSku(request.productSku());
        cartItemRequest.setName(product.get().getName());
        cartItemRequest.setPrice(product.get().getPrice());
        cartItemRequest.setQuantity(request.quantity());

        cartItemRepository.save(cartItemRequest);

        Optional<List<CartItem>> cartItems = cartItemRepository.findByUsername(username);
        List<CartItemResponse> cartItemResponse = cartItems.get().stream().map(CartItem::toResponse).toList();

        cart = updateCart(cart, cartItems.get());
        cartRepository.save(cart);

        CartResponse cartResponse = new CartResponse(cart.getUsername(), cartItemResponse, cart.getDiscount(), cart.getTotalDiscount(), cart.getSubtotal(), cart.getGrandTotal());
        return cartResponse;
    }

    public Cart updateCart(Cart cart, List<CartItem> cartItems) {
        cart.setSubtotal(
                cartItems.stream()
                        .map(CartItem::getPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));

        cart.setTotalDiscount(cartItems.stream()
                .map(CartItem::getDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add).add(cart.getDiscount()));

        cart.setGrandTotal(cart.getSubtotal().subtract(cart.getTotalDiscount()));

        return cart;
    }

    public ResponseEntity addProductPromotion(String userName, PromotionRequest promotionRequest) {
        Optional<Promotion> promotion =
                promotionRepository.findByCode(promotionRequest.promotionCode());
        Optional<Product> product = productRepository.findBySku(promotionRequest.productSku());
        //        System.out.println(product);

        return null;
    }
}
