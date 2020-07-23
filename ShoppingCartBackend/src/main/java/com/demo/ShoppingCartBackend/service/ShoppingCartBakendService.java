package com.demo.ShoppingCartBackend.service;

import com.demo.ShoppingCartBackend.domain.*;
import com.demo.ShoppingCartBackend.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public Client getClientByEmail(String email) throws Exception {
        Client client = clientRepository.findByEmail(email);
        if (client == null){
            throw new Exception("Email incorrecto");
        }
        return client;
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
    public Cart saveCartWithProduct(Wrapper wrapper) throws Exception {
        Product product = wrapper.getProduct();
        Cart cart = wrapper.getCart();
        if( !exist(cart) ){
            throw new Exception("No existe ningún carrito o fue eliminado por terminar el día en que se comenzó la compra");
        }
        int cant = wrapper.getCant();

        CartProduct cartProduct = new CartProduct();
        cartProduct.setSubtotal(new BigDecimal(cant).multiply(product.getPrice()));
        cartProduct.setCant(cant);
        cartProduct.setProduct(product);
        cartProduct.setCart(cart);
//        cartProduct = cartProductRepository.saveAndFlush(cartProduct);

        if (cart.getCartProducts() != null){
            cart.getCartProducts().add(cartProduct);
        } else {
            List<CartProduct> cartProductList = new ArrayList<>();
            cartProductList.add(cartProduct);
            cart.setCartProducts(cartProductList);
        }

        applyDiscount(cart);
        return cartRepository.saveAndFlush(cart);
    }

    @Transactional
    public Cart updateCart(Cart cart) throws Exception {
        if( !exist(cart) ){
            throw new Exception("No existe ningún carrito o fue eliminado por terminar el día en que se comenzó la compra");
        }
        updateCartProducts(cart);

        applyDiscount(cart);
        return cartRepository.saveAndFlush(cart);
    }

    public void applyDiscount(Cart cart){
        BigDecimal total = getTotal(cart.getCartProducts());
        int cantidadItems = getCantitdadItems(cart.getCartProducts());
        BigDecimal totalWithDiscount = new BigDecimal(total.toString());
        if(cantidadItems > 5 ){
            if(cantidadItems <= 10){
                totalWithDiscount = total.multiply(new BigDecimal("0.9"));
            } else {
                if(cart.getCartType().equals(Cart.CartType.VIP)){
                    totalWithDiscount = total.subtract(new BigDecimal(700));
                } else if(cart.getCartType().equals(Cart.CartType.ESPECIALDATE)){
                    totalWithDiscount = total.subtract(new BigDecimal(500));
                } else {
                    totalWithDiscount = total.subtract(new BigDecimal(200));
                }
            }
            cart.setTotalWithDiscount(
                    totalWithDiscount.compareTo(BigDecimal.ZERO) <= 0 ? BigDecimal.ZERO : totalWithDiscount
            );
        } else {
            cart.setTotalWithDiscount(total);
        }
        cart.setTotal(total);

    }

    private BigDecimal getTotal(List<CartProduct> cartProductList) {
        BigDecimal total = new BigDecimal(0);
        for (CartProduct item: cartProductList){
            total = total.add(item.getSubtotal());
        }
        return total;
    }

    private int getCantitdadItems(List<CartProduct> cartProductList){
        int cantidadItems = 0;
        for (CartProduct item: cartProductList){
            cantidadItems += item.getCant();
        }
        return cantidadItems;
    }

    private void updateCartProducts(Cart cart) {
        for (CartProduct item: cart.getCartProducts()){
            BigDecimal subtotal = new BigDecimal(item.getCant()).multiply(item.getProduct().getPrice());
            item.setSubtotal(subtotal);
        }
    }

    @Transactional
    public Cart deleteCardProduct(Long id) throws Exception {
        Cart cart = cartRepository.findCartByCartProducts(id);
        if(cart == null){
            throw new Exception("No existe ningún carrito o fue eliminado por terminar el día en que se comenzó la compra");
        }
//        cartProductRepository.deleteById(id);
        for(CartProduct cartProduct : cart.getCartProducts()) {
            if (cartProduct.getId().compareTo(id) == 0){
                cart.getCartProducts().remove(cartProduct);
                break;
            }
        }
        applyDiscount(cart);
        return cartRepository.saveAndFlush(cart);
    }

    @Transactional
    public Cart deleteCart(Long id) throws Exception {
        Cart cart = new Cart();
        cart.setId(id);
        if( !exist(cart) ){
            throw new Exception("No existe ningún carrito o fue eliminado por terminar el día en que se comenzó la compra");
        }
        cart = cartRepository.getOne(id);
//        cartProductRepository.deleteAll(cart.getCartProducts());
        cart.getCartProducts().clear();
        return cart;
    }

    @Transactional
    public boolean finishBuy(Long id) throws Exception{
        Cart cart = new Cart();
        cart.setId(id);
        if( !exist(cart) ){
            throw new Exception("No existe ningún carrito o fue eliminado por terminar el día en que se comenzó la compra");
        }
        cart = cartRepository.getOne(id);
        cart.setUpdatedDate(LocalDateTime.now());
        cartRepository.saveAndFlush(cart);
        verificarVip(id);
        return true;
    }

    @Transactional
    public void verificarVip(Long id){
        Client client = clientRepository.findByCartId(id);
        if(!client.isVip()){
            client = clientRepository.superaTotalEnElMes(client.getId(), new BigDecimal("10000"), LocalDate.now());
            if(client != null){
                client.setVip(true);
                client.setVipEndDate(null);
                client.setVipStartDate(LocalDate.now());
                clientRepository.saveAndFlush(client);
            }
        }
    }

    @Transactional
    public void deleteCartsNotFinished(LocalDate date){
        cartRepository.deleteCartsProduct(date);
        cartRepository.deleteCartsNotFinished(date);
    }

    public void changeVip(LocalDate date){
        List<Client> clientList = clientRepository.getClientesQueNoRealizaronCompras(date);
        clientList.forEach(client -> {
            if(client.isVip()){
                client.setVipEndDate(LocalDate.now());
            }
            client.setVip(false);
        });
        clientRepository.saveAll(clientList);
    }

    public boolean exist(Cart cart) throws Exception{
        if (cart.getId()!=null){
            if(cartRepository.getOne(cart.getId()) != null){
                return true;
            } else {
                throw new Exception("No existe ningún carrito o fue eliminado por terminar el día");
            }
        }
        return false;
    }

    public boolean addClients() {
        List<Client> clientList = new ArrayList<>();
        clientList.add(new Client("test@test.com", "1234", "test", "1", true, LocalDate.now(), null));
        clientList.add(new Client("prueba@test.com", "1234", "prueba", "2", true, LocalDate.of(2020,6,20), null));
        clientList.add(new Client("nuevo@test.com", "1234", "nuevo", "3", true, LocalDate.of(2020,6,20), null));
        clientList.add(new Client("nuevo1@test.com", "1234", "nuevo1", "4", false, null, LocalDate.now()));
        clientList.add(new Client("nuevo2@test.com", "1234", "nuevo2", "5", false, null, LocalDate.of(2020,8,20)));
        clientList.add(new Client("nuevo3@test.com", "1234", "nuevo3", "6", false, null, LocalDate.of(2020,8,20)));

        logger.info("guardando clientes");
        clientRepository.saveAll(clientList);
        return true;
    }

    public boolean addProducts() {
        List<Product> productList = new ArrayList<>();
        productList.add(new Product("Libre C++", new BigDecimal("300")));
        productList.add(new Product("Libro Java", new BigDecimal("300")));
        productList.add(new Product("Arquitectura de Software", new BigDecimal("500")));
        productList.add(new Product("Arquitectura", new BigDecimal("500")));
        productList.add(new Product("Notebook", new BigDecimal("70000")));
        productList.add(new Product("Teclado mecanico", new BigDecimal("6000")));
        productList.add(new Product("Auriculares", new BigDecimal("2000")));
        productList.add(new Product("Mouse", new BigDecimal("3000")));
        productList.add(new Product("Mouse pad", new BigDecimal("100")));
        productList.add(new Product("Libro JavaScript", new BigDecimal("900")));
        productList.add(new Product("Silla de Oficina", new BigDecimal("10000")));

        logger.info("guardando productos");
        productRepository.saveAll(productList);
        return true;
    }

    public boolean addEspecialDate(){
        List<EspecialDate> especialDateList = new ArrayList<>();
        especialDateList.add(new EspecialDate(LocalDate.of(2020,7,30)));
        especialDateList.add(new EspecialDate(LocalDate.of(2020,7,23)));
        especialDateList.add(new EspecialDate(LocalDate.of(2020,8,1)));
        especialDateList.add(new EspecialDate(LocalDate.of(2020,7,25)));
        especialDateList.add(new EspecialDate(LocalDate.of(2020,8,3)));
        especialDateList.add(new EspecialDate(LocalDate.of(2020,7,28)));

        logger.info("guardando especial dates");
        especialDateRepository.saveAll(especialDateList);
        return true;
    }

    public boolean addData() {
        addClients();
        addProducts();
        addEspecialDate();
        return true;
    }
}
