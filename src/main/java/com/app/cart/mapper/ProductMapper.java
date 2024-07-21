package com.app.cart.mapper;

import com.app.cart.dto.ProductDTO;
import com.app.cart.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "id", target = "id")
    ProductDTO toDto(Product product);

    @Mapping(source = "id", target = "id")
    Product toEntity(ProductDTO productDTO);

    default Long map(Product product) {
        return product == null ? null : product.getId();
    }

    default Product map(Long id) {
        if (id == null) {
            return null;
        }
        Product product = new Product();
        product.setId(id);
        return product;
    }
}
