package com.demo.ShoppingCartBackend.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Cart {

    @Id
    @GeneratedValue
    private Long id;
    private BigDecimal total;
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;
    private boolean finished;
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false, updatable = false)
    private Client client;
    @JsonManagedReference
    @Column(name = "cart_product_id", nullable = false, updatable = false)
    @OneToMany
    private List<CartProduct> cartProducts;

    public Cart(BigDecimal total, LocalDateTime createdDate, boolean finished, Client client, List<CartProduct> cartProducts) {
        this.total = total;
        this.createdDate = createdDate;
        this.finished = finished;
        this.client = client;
        this.cartProducts = cartProducts;
    }

    public Cart() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<CartProduct> getCartProducts() {
        return cartProducts;
    }

    public void setCartProducts(List<CartProduct> cartProducts) {
        this.cartProducts = cartProducts;
    }
}
