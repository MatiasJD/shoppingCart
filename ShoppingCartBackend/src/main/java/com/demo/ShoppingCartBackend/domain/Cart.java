package com.demo.ShoppingCartBackend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Cart {

    public enum CartType{
        VIP, ESPECIALDATE, COMMON
    }

    @Id
    @GeneratedValue
    private Long id;
    private BigDecimal total;
    @Column(name = "total_with_discount")
    private BigDecimal totalWithDiscount;
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false, updatable = false)
    @JsonIgnore
    private Client client;
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "cart", orphanRemoval = true)
    private List<CartProduct> cartProducts;
    @Enumerated(EnumType.STRING)
    @Column(name = "cart_type", nullable = false, updatable = false)
    private CartType cartType;

    public Cart(BigDecimal total, BigDecimal totalWithDiscount, LocalDateTime createdDate, LocalDateTime updatedDate, Client client, List<CartProduct> cartProducts, CartType cartType) {
        this.total = total;
        this.totalWithDiscount = totalWithDiscount;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.client = client;
        this.cartProducts = cartProducts;
        this.cartType = cartType;
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

    public BigDecimal getTotalWithDiscount() {
        return totalWithDiscount;
    }

    public void setTotalWithDiscount(BigDecimal totalWithDiscount) {
        this.totalWithDiscount = totalWithDiscount;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
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

    public CartType getCartType() {
        return cartType;
    }

    public void setCartType(CartType cartType) {
        this.cartType = cartType;
    }
}
