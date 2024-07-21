package com.app.cart.dto;


import lombok.Data;

import java.util.List;

@Data
public class CartResponseDTO {
    UserDTO user;
    List<CartDTO> productList;
    Double cartPrice;

}
