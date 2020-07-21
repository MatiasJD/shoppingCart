package com.demo.ShoppingCartBackend.domain;

public class Wrapper {

    private Cart cart;
    private Product product;
    private int cant;

    public Wrapper(Cart cart, Product product, int cant) {
        this.cart = cart;
        this.product = product;
        this.cant = cant;
    }

    public Wrapper() {
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getCant() {
        return cant;
    }

    public void setCant(int cant) {
        this.cant = cant;
    }
}
