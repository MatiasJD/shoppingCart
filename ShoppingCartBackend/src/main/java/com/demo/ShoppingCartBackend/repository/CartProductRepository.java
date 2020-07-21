package com.demo.ShoppingCartBackend.repository;

import com.demo.ShoppingCartBackend.domain.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
}
