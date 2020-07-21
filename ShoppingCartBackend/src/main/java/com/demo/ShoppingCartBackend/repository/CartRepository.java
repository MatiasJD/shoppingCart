package com.demo.ShoppingCartBackend.repository;

import com.demo.ShoppingCartBackend.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query(value = "SELECT c.* FROM Cart c, Client cl WHERE cl.email = ?1 AND cl.id = c.client_id AND to_char(c.created_date, 'yyyy-MM-dd') = to_char(?2, 'yyyy-MM-dd') AND updated_date IS NULL",
            nativeQuery = true)
    Cart findByMailAndCreatedDate(String email, LocalDate createdDate);

    @Modifying
    @Query(value = "UPDATE FROM Cart SET updated_date = ?2 WHERE id = ?1",
            nativeQuery = true)
    boolean updateUpdatedDate(Long id, LocalDateTime date);

    @Modifying
    @Query(value = "DELETE FROM Cart WHERE updated_date IS NULL AND created_date < ?1",
    nativeQuery = true)
    @Transactional
    void deleteCartsNotFinished(LocalDate createdDate);

    @Modifying
    @Query(value = "DELETE FROM CART_PRODUCT WHERE cart_id in (SELECT id FROM Cart WHERE updated_date IS NULL AND created_date < ?1)",
            nativeQuery = true)
    @Transactional
    void deleteCartsProduct(LocalDate createdDate);

    @Query(value = "SELECT * FROM Cart WHERE id = (SELECT cart_id FROM CART_PRODUCT WHERE id = ?1)",
    nativeQuery = true)
    Cart findCartByCartProducts(Long id);

}
