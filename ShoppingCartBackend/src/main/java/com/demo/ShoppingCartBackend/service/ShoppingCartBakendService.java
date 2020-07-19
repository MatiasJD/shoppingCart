package com.demo.ShoppingCartBackend.service;

import com.demo.ShoppingCartBackend.domain.Client;
import com.demo.ShoppingCartBackend.domain.Product;
import com.demo.ShoppingCartBackend.repository.CartRepository;
import com.demo.ShoppingCartBackend.repository.ClientRepository;
import com.demo.ShoppingCartBackend.repository.EspecialDateRepository;
import com.demo.ShoppingCartBackend.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingCartBakendService {

    private ClientRepository clientRepository;
    private ProductRepository productRepository;
    private CartRepository cartRepository;
    private EspecialDateRepository especialDateRepository;
    private Logger logger;

    @Autowired
    public ShoppingCartBakendService(ClientRepository clientRepository, ProductRepository productRepository, CartRepository cartRepository, EspecialDateRepository especialDateRepository) {
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.especialDateRepository = especialDateRepository;

        this.logger = LoggerFactory.getLogger(ShoppingCartBakendService.class);
    }

    public List<Client> getClients(){
        logger.info("buscando todos los clientes");
        return clientRepository.findAll();
    }

    public List<Client> addClients() {
        List<Client> clientList = new ArrayList<>();
        clientList.add(new Client("test1@test.com", "1234", "test", "1", true, LocalDate.now(), null));
        clientList.add(new Client("prueba2@test.com", "1234", "prueba", "2", false, null, null));
        clientList.add(new Client("nuevo3@test.com", "1234", "nuevo", "3", false, null, null));
        logger.info("guardando clientes");
        return clientRepository.saveAll(new ArrayList<>(clientList));
    }

    public List<Product> getProducts(){
        logger.info("buscando todos los productos");
        return productRepository.findAll();
    }

    public List<Product> addProducts() {
        List<Product> productList = new ArrayList<>();
        productList.add(new Product("Libre C++", new BigDecimal("300")));
        productList.add(new Product("Libro Java", new BigDecimal("300")));
        productList.add(new Product("Arquitectura de Software", new BigDecimal("500")));
        logger.info("guardando productos");
        return productRepository.saveAll(new ArrayList<>(productList));
    }
}
