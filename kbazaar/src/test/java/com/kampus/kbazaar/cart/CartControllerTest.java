package com.kampus.kbazaar.cart;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kampus.kbazaar.security.JwtAuthFilter;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(SpringExtension.class)
@WebMvcTest(
        controllers = CartController.class,
        excludeFilters =
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = JwtAuthFilter.class))
public class CartControllerTest {

    @Autowired private MockMvc mockMvc;

    @InjectMocks private CartController cartController;

    @MockBean private CartService cartService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }

    @Test
    @DisplayName("should return all carts")
    public void shouldReturnAllCarts() throws Exception {
        when(cartService.getAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/v1/carts").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(cartService, times(1)).getAll();
    }

    @Test
    @DisplayName("When add product promotion: FIXEDAMOUNT2 it should return status 200 with message: Promotion code FIXEDAMOUNT2 update successfully")
    public  void addProductPromotion()throws  Exception{
        String username = "TechNinja";
        String code = "FIXEDAMOUNT2";
        String productSku = "STATIONERY-STAPLER-SWINGLINE,STATIONERY-PENCIL-FABER-CASTELL";

        mockMvc.perform(post("/api/v1/carts/${username}/promotions", username))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Promotion code "+ code + " update successfully"));
    }
}
