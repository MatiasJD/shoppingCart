package com.demo.ShoppingCartBackend.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Cart_Product")
public class CartProduct {

    @Id
    @GeneratedValue
    private Long id;
    private int cant;
    private BigDecimal subtotal;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false, updatable = false)
    private Product product;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "cart_id", nullable = false, updatable = false)
    private Cart cart;

    public CartProduct(int cant, BigDecimal subtotal, Product product, Cart cart) {
        this.cant = cant;
        this.subtotal = subtotal;
        this.product = product;
        this.cart = cart;
    }

    public CartProduct() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCant() {
        return cant;
    }

    public void setCant(int cant) {
        this.cant = cant;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
