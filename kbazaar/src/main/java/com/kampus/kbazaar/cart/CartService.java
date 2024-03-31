package com.kampus.kbazaar.cart;

import com.kampus.kbazaar.product.Product;
import com.kampus.kbazaar.product.ProductRepository;
import com.kampus.kbazaar.promotion.Promotion;
import com.kampus.kbazaar.promotion.PromotionRepository;
import com.kampus.kbazaar.promotion.PromotionRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private CartRepository cartRepository;
    private CartItemRepository cartItemRepository;
    private ProductRepository productRepository;
    private PromotionRepository promotionRepository;

    @Value("${enabled.shipping.fee:false}")
    private boolean enableShippingFee;

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
                            getShippingFee(),
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
        List<CartItemResponse> cartItemResponse =
                cartItems.get().stream().map(CartItem::toResponse).toList();

        BigDecimal shippingFee = getShippingFee();

        cart = updateCart(cart, cartItems.get(), shippingFee);
        cartRepository.save(cart);

        CartResponse cartResponse =
                new CartResponse(
                        cart.getUsername(),
                        cartItemResponse,
                        cart.getDiscount(),
                        cart.getTotalDiscount(),
                        cart.getSubtotal(),
                        shippingFee,
                        cart.getGrandTotal());
        return cartResponse;
    }

    public BigDecimal getShippingFee() {
        if (enableShippingFee) {
            return new BigDecimal(25);
        }
        return new BigDecimal(0);
    }

    public Cart updateCart(Cart cart, List<CartItem> cartItems, BigDecimal shippingFee) {
        cart.setSubtotal(
                cartItems.stream()
                        .map(CartItem::getPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));

        cart.setTotalDiscount(
                cartItems.stream()
                        .map(CartItem::getDiscount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .add(cart.getDiscount()));

        cart.setGrandTotal(cart.getSubtotal().subtract(cart.getTotalDiscount()).add(shippingFee));

        return cart;
    }

    public void addProductPromotion(String userName, PromotionRequest promotionRequest) {

        Optional<Promotion> promotion =
                promotionRepository.findByCode(promotionRequest.promotionCode());
        Optional<Product> product = productRepository.findBySku(promotionRequest.productSku());

        List<CartItem> cartItemsListByUser =
                cartItemRepository.findByUsername(userName).orElse(new ArrayList<>()).stream()
                        .toList();

        for (CartItem cartItem : cartItemsListByUser) {
            if (promotionRequest.productSku().contains(cartItem.getSku())) {
                cartItem.setDiscount(promotion.get().getDiscountAmount());
                cartItem.setPromotionCodes(promotion.get().getCode());
                cartItemRepository.save(cartItem);
            }
        }

        List<CartItem> cartItemsListAfterDiscount =
                cartItemRepository.findByUsername(userName).orElse(new ArrayList<>()).stream()
                        .toList();

        Optional<Cart> cartOptional = cartRepository.findByUsername(userName);
        Cart cart = cartOptional.get();
        BigDecimal totalDiscount = new BigDecimal("0.00");

        for (CartItem ci : cartItemsListAfterDiscount) {
            totalDiscount = totalDiscount.add(ci.getDiscount());
        }

        cart.setTotalDiscount(totalDiscount);
        cartRepository.save(cart);
    }
}
