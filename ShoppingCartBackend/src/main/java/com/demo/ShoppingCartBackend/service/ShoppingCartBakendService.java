package com.demo.ShoppingCartBackend.service;

import com.demo.ShoppingCartBackend.domain.*;
import com.demo.ShoppingCartBackend.repository.*;
import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.util.resources.sr.LocaleNames_sr_Latn;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShoppingCartBakendService {

    private ClientRepository clientRepository;
    private ProductRepository productRepository;
    private CartRepository cartRepository;
    private EspecialDateRepository especialDateRepository;
    private CartProductRepository cartProductRepository;
    private Logger logger;

    @Autowired
    public ShoppingCartBakendService(ClientRepository clientRepository, ProductRepository productRepository, CartRepository cartRepository, EspecialDateRepository especialDateRepository, CartProductRepository cartProductRepository) {
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.especialDateRepository = especialDateRepository;
        this.cartProductRepository = cartProductRepository;

        this.logger = LoggerFactory.getLogger(ShoppingCartBakendService.class);
    }

    public Client getClientByEmail(String email){
        return clientRepository.findByEmail(email);
    }

    public List<Client> getClients(){
        logger.info("buscando todos los clientes");
        return clientRepository.findAll();
    }

    public List<Client> getClientsVip(){
        logger.info("buscando clientes que son vip");
        return clientRepository.findByVipIsTrue();
    }

    public List<Client> getClientsVipStartDate(LocalDate vipStartDate){
        logger.info("buscando clientes que comenzaron a ser vip el año y mes: " + vipStartDate.toString());
        return clientRepository.findByVipStartDate(vipStartDate);
    }

    public List<Client> getClientsVipEndDate(LocalDate vipEndDate){
        logger.info("buscando clientes que dejaron de ser vip el año y mes: " + vipEndDate.toString());
        return clientRepository.findByVipEndDate(vipEndDate);
    }

    public List<Product> getProducts(){
        logger.info("buscando todos los productos");
        return productRepository.findAll();
    }

    public Cart getCartByClient(String email) throws Exception {
        Cart cart = cartRepository.findByMailAndCreatedDate(email, LocalDate.now());
        if( cart != null){
            return cart;
        } else {
            Client client = clientRepository.findByEmail(email);

            if (client != null){
                cart = new Cart(null,null, LocalDateTime.now(), null, client, null, null);

                if (client.isVip()){
                    cart.setCartType(Cart.CartType.VIP);

                } else{
                    EspecialDate especialDate = especialDateRepository.findByDate(LocalDate.now());

                    if (especialDate != null){
                        cart.setCartType(Cart.CartType.ESPECIALDATE);

                    } else {
                        cart.setCartType(Cart.CartType.COMMON);
                    }
                }
            } else {
                throw new Exception("No se encontró ningun cliente con email: " + email);
            }
            return cartRepository.saveAndFlush(cart);
        }
    }

    @Transactional
    public Cart saveCartWithProduct(Wrapper wrapper){
        Product product = wrapper.getProduct();
        Cart cart = wrapper.getCart();
        int cant = wrapper.getCant();

        CartProduct cartProduct = new CartProduct();
        cartProduct.setSubtotal(new BigDecimal(cant).multiply(product.getPrice()));
        cartProduct.setCant(cant);
        cartProduct.setProduct(product);
        cartProduct.setCart(cart);

        cartProduct = cartProductRepository.saveAndFlush(cartProduct);

        if (cart.getTotal() == null){
            cart.setTotal(cartProduct.getSubtotal());
        } else {
            cart.setTotal(cart.getTotal().add(cartProduct.getSubtotal()));
        }

        if (cart.getCartProducts() != null){
            cart.getCartProducts().add(cartProduct);
        } else {
            List<CartProduct> cartProductList = new ArrayList<>();
            cartProductList.add(cartProduct);
            cart.setCartProducts(cartProductList);
        }

        return cartRepository.saveAndFlush(cart);
    }

    @Transactional
    public Cart updateCart(Cart cart){
        BigDecimal total = new BigDecimal(0);
        int cantidadItems = 0;
        for (CartProduct cartProduct: cart.getCartProducts()){
            cantidadItems += cartProduct.getCant();
            BigDecimal subtotal = new BigDecimal(cartProduct.getCant()).multiply(cartProduct.getProduct().getPrice());
            cartProduct.setSubtotal(subtotal);
            total.add(subtotal);
        }
        applyDiscount(cart, cantidadItems, total);

        cart.setCartProducts(cartProductRepository.saveAll(cart.getCartProducts()));
        return cartRepository.saveAndFlush(cart);
    }

    public void applyDiscount(Cart cart, int cantidadItems, BigDecimal total){
        if(cantidadItems > 5 && cantidadItems <= 10){
            cart.setTotal(total.multiply(new BigDecimal(0.9)));
        } else {
            if(cart.getCartType().equals(Cart.CartType.VIP)){
                total = total.subtract(new BigDecimal(700));
            } else if(cart.getCartType().equals(Cart.CartType.ESPECIALDATE)){
                total = total.subtract(new BigDecimal(500));
            } else {
                total = total.subtract(new BigDecimal(200));
            }
            total = total.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : total;
            cart.setTotal(total);
        }
    }

    @Transactional
    public Cart deleteCardProduct(CartProduct cartProduct){
        Cart cart = cartRepository.findCartByCartProducts(cartProduct.getId());
        cartProductRepository.delete(cartProduct);
        cart.getCartProducts().forEach(elements -> {
            if(elements.getId() == cartProduct.getId())
                cart.getCartProducts().remove(elements);
        });

        BigDecimal total = new BigDecimal(0);
        int cantidadItems = 0;
        for (CartProduct element: cart.getCartProducts()){
            cantidadItems += cartProduct.getCant();
            total.add(new BigDecimal(cartProduct.getCant()).multiply(cartProduct.getProduct().getPrice()));
        }
        applyDiscount(cart, cantidadItems, total);
        return cartRepository.saveAndFlush(cart);
    }

    @Transactional
    public boolean deleteCart(Cart cart) {
        cartProductRepository.deleteAll(cart.getCartProducts());
        cartRepository.delete(cart);
        return true;
    }

    public boolean finishBuy(Long id){
        return cartRepository.updateUpdatedDate(id, LocalDateTime.now());
    }

    @Transactional
    public void deleteCartsNotFinished(LocalDate date){
        cartRepository.deleteCartsProduct(date);
        cartRepository.deleteCartsNotFinished(date);
    }

    public List<Client> addClients() {
        List<Client> clientList = new ArrayList<>();
        clientList.add(new Client("test1@test.com", "1234", "test", "1", true, LocalDate.now(), null));
        clientList.add(new Client("prueba2@test.com", "1234", "prueba", "2", true, LocalDate.of(2020,6,20), null));
        clientList.add(new Client("nuevo3@test.com", "1234", "nuevo3", "3", true, LocalDate.of(2020,6,20), null));
        clientList.add(new Client("nuevo4@test.com", "1234", "nuevo4", "4", false, null, LocalDate.now()));
        clientList.add(new Client("nuevo5@test.com", "1234", "nuevo5", "5", false, null, LocalDate.of(2020,8,20)));
        clientList.add(new Client("nuevo6@test.com", "1234", "nuevo6", "6", false, null, LocalDate.of(2020,8,20)));
        logger.info("guardando clientes");
        return clientRepository.saveAll(clientList);
    }

    public List<Product> addProducts() {
        List<Product> productList = new ArrayList<>();
        productList.add(new Product("Libre C++", new BigDecimal("300")));
        productList.add(new Product("Libro Java", new BigDecimal("300")));
        productList.add(new Product("Arquitectura de Software", new BigDecimal("500")));
        logger.info("guardando productos");
        return productRepository.saveAll(productList);
    }

    public List<Cart> addCarts() {
        this.addClients();
        this.addProducts();
        Client client = new Client();
        client.setId(1L);
        Cart cart = new Cart();
        cart.setCartType(Cart.CartType.VIP);
        cart.setCreatedDate(LocalDateTime.now());
        cart.setClient(client);

        Client client2 = new Client();
        client2.setId(2L);
        Cart cart2 = new Cart();
        cart2.setCartType(Cart.CartType.VIP);
        cart2.setCreatedDate(LocalDateTime.now());
        cart2.setClient(client2);

        List<Cart> cartList = new ArrayList<>();
        cartList.add(cart);
        cartList.add(cart2);
        logger.info("guardando carts");
        return cartRepository.saveAll(cartList);
    }
}
