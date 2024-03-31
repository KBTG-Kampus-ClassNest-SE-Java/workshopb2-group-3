package com.kampus.kbazaar.cart;

import com.kampus.kbazaar.product.Product;
import com.kampus.kbazaar.product.ProductRepository;
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

    public CartResponse addProduct(CartRequest request) {
        Optional<Product> productResponse = productRepository.findBySku(request.productSku());
        return null;
    }

    public ResponseEntity addProductPromotion(String userName, PromotionRequest promotionRequest) {
        Optional<Promotion> promotion =
                promotionRepository.findByCode(promotionRequest.promotionCode());
        Optional<Product> product = productRepository.findBySku(promotionRequest.productSku());
        //        System.out.println(product);

        return null;
    }
}
