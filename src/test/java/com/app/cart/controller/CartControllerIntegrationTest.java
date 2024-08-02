package com.app.cart.controller;

import com.app.cart.dto.CartRequestDTO;
import com.app.cart.repository.CartRepository;
import com.app.cart.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Transactional
public class CartControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;

    @BeforeEach
    void setUp() {
        cartRepository.deleteAll();
    }

    @Test
    void testAddProductItem() throws Exception {
        CartRequestDTO cartRequestDTO = new CartRequestDTO();
        cartRequestDTO.setUserId(1L);
        cartRequestDTO.setProductId(1L);
        cartRequestDTO.setQuantity(2);

        mockMvc.perform(post("/cart/add-item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.quantity").value(2));
    }

    @Test
    void testRefreshCart() throws Exception {
        CartRequestDTO cartRequestDTO = new CartRequestDTO();
        cartRequestDTO.setUserId(1L);
        cartRequestDTO.setProductId(1L);
        cartRequestDTO.setQuantity(2);

        cartService.addProducts(cartRequestDTO);

        mockMvc.perform(get("/cart/refresh/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateProductItem() throws Exception {
        CartRequestDTO cartRequestDTO = new CartRequestDTO();
        cartRequestDTO.setUserId(1L);
        cartRequestDTO.setProductId(1L);
        cartRequestDTO.setQuantity(2);

        cartService.addProducts(cartRequestDTO);

        cartRequestDTO.setQuantity(3);

        mockMvc.perform(put("/cart/update-item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(3));
    }

    @Test
    void testRemoveItem() throws Exception {
        CartRequestDTO cartRequestDTO = new CartRequestDTO();
        cartRequestDTO.setUserId(1L);
        cartRequestDTO.setProductId(1L);
        cartRequestDTO.setQuantity(2);

        cartService.addProducts(cartRequestDTO);

        mockMvc.perform(delete("/cart/remove-item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartRequestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testRemoveItem_NotFound() throws Exception {
        CartRequestDTO cartRequestDTO = new CartRequestDTO();
        cartRequestDTO.setUserId(1L);
        cartRequestDTO.setProductId(1L);

        mockMvc.perform(delete("/cart/remove-item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartRequestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(result -> result.getResolvedException().getMessage().contains("Product not found in cart"));
    }

    @Test
    void testClearCart() throws Exception {
        CartRequestDTO cartRequestDTO = new CartRequestDTO();
        cartRequestDTO.setUserId(1L);
        cartRequestDTO.setProductId(1L);
        cartRequestDTO.setQuantity(2);

        cartService.addProducts(cartRequestDTO);

        mockMvc.perform(delete("/cart/clear/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testCheckoutProducts() throws Exception {
        CartRequestDTO cartRequestDTO = new CartRequestDTO();
        cartRequestDTO.setUserId(1L);
        cartRequestDTO.setProductId(1L);
        cartRequestDTO.setQuantity(2);

        cartService.addProducts(cartRequestDTO);

        mockMvc.perform(post("/cart/checkout/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
