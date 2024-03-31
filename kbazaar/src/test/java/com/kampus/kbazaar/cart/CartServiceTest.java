package com.kampus.kbazaar.cart;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CartServiceTest {


    @Mock
    private CartRepository cartRepository;
    @InjectMocks
    private CartService cartService;

    @Test
    @DisplayName("When use product promotion code it should update promotion code")
    void addProductPromotion(){
        String username = "TechNinja";
        String code = "FIXEDAMOUNT2";
        String productSku = "STATIONERY-STAPLER-SWINGLINE,STATIONERY-PENCIL-FABER-CASTELL";

        AddProductPromotionRequestDto requestDto = new AddProductPromotionRequestDto(code,productSku);
        ResponseEntity<AddProductPromotionResponseDto> responseEntity = cartService.addProductPromotion(username, requestDto);


//        UserTicket mockUserTicket = new UserTicket();
//        mockUserTicket.setId(1);
//        mockUserTicket.setLottery(mockLottery);
//        mockUserTicket.setUserDetail(mockUserDetail);

        CartItem mockCartItem = new CartItem();
        mockCartItem.setId(1L);
        mockCartItem.setName(username);
        mockCartItem.setName("Pencils");
        mockCartItem.setPrice(BigDecimal.valueOf(10.25));
        mockCartItem.setQuantity(1);
        mockCartItem.setDiscount(BigDecimal.valueOf(0.00));
        mockCartItem.setPromotionCodes("");


        when(cartRepository.findByUsername(any())).thenReturn(Optional.of(mockCart));


        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Promotion code "+ code + " update successfully", responseEntity.getBody().getMessage());
    }


}
