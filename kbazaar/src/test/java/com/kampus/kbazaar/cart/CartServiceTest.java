package com.kampus.kbazaar.cart;

import com.kampus.kbazaar.promotion.CartPromotionRequest;
import com.kampus.kbazaar.promotion.CartPromotionResponse;
import com.kampus.kbazaar.promotion.PromotionRequest;
import com.kampus.kbazaar.promotion.PromotionResponse;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;
    @InjectMocks
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("When use product promotion code it should update promotion code")
    void addProductPromotion(){
        String username = "CodeMaster";
        String code = "FIXEDAMOUNT2";
        String productSku = "STATIONERY-STAPLER-SWINGLINE,STATIONERY-PENCIL-FABER-CASTELL";

        CartItem mockCartItem = new CartItem();
        mockCartItem.setId(1L);
        mockCartItem.setUsername(username);
        mockCartItem.setName("Pencils");
        mockCartItem.setPrice(BigDecimal.valueOf(10.25));
        mockCartItem.setQuantity(1);
        mockCartItem.setDiscount(BigDecimal.valueOf(2.00));
        mockCartItem.setPromotionCodes(code);
        mockCartItem.setSku(productSku);

        Cart mockCart = new Cart();
        mockCart.setId(1L);
        mockCart.setUsername(username);
        mockCart.setDiscount(BigDecimal.valueOf(2.00));
        mockCart.setTotalDiscount(BigDecimal.valueOf(0.00));
        mockCart.setPromotionCodes("");
        mockCart.setSubtotal(BigDecimal.valueOf(10.25));
        mockCart.setGrandTotal(BigDecimal.valueOf(8.25));

        PromotionRequest requestDto = new PromotionRequest(code,productSku);
        ResponseEntity<PromotionResponse> responseEntity = cartService.addProductPromotion(username, requestDto);

        when(cartItemRepository.findCartItemByUsernameAndSku(username,productSku)).thenReturn(Optional.of(mockCartItem));
        when(cartRepository.findByUsername(username)).thenReturn(Optional.of(mockCart));
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
//        assertEquals("Promotion code "+ code + " update successfully", responseEntity.getBody().getMessage());
    }

    @Test
    @DisplayName("When use cart promotion code it should update GrandTotal, TotalDiscount and PromotionCodes")
    void addCartPromotion() throws BadRequestException {
        String username = "CodeMaster";
        String code = "FIXEDAMOUNT10";

        Cart mockCart = new Cart();
        mockCart.setId(1L);
        mockCart.setUsername(username);
        mockCart.setDiscount(BigDecimal.valueOf(2.00));
        mockCart.setTotalDiscount(BigDecimal.valueOf(0.825));
        mockCart.setPromotionCodes(code);
        mockCart.setSubtotal(BigDecimal.valueOf(10.25));
        mockCart.setGrandTotal(BigDecimal.valueOf(7.425));

        CartPromotionRequest requestDto = new CartPromotionRequest(code);
        ResponseEntity<CartPromotionResponse> responseEntity = cartService.addCartPromotion(username, requestDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Promotion code "+ code + " update successfully", responseEntity.getBody().getMessage());
    }
}

