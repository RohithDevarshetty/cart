package com.app.cart.mapper;

import com.app.cart.dto.CartItemDTO;
import com.app.cart.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ProductMapper.class})
public interface CartItemMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "product.id", target = "productId")
    CartItemDTO toDto(CartItem cartItem);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "productId", target = "product.id")
    CartItem toEntity(CartItemDTO cartItemDTO);
}