package com.app.cart.repository;

import com.app.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findAllByUserId(Long id);

    Optional<CartItem> findByProductIdAndUserId(Long productId, Long userId);
}
