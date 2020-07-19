package com.demo.ShoppingCartBackend.repository;

import com.demo.ShoppingCartBackend.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

}
