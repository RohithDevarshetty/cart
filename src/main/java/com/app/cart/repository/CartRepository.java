package com.app.cart.repository;

import com.app.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    List<Cart> findAllByUserId(Long id);

    Optional<Cart> findByProductIdAndUserId(Long productId, Long userId);

    List<Cart> deleteByUserId(Long userId);

    void deleteByProductIdAndUserId(Long productId, Long userId);
}
