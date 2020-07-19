package com.demo.ShoppingCartBackend.controller;

import com.demo.ShoppingCartBackend.domain.Client;
import com.demo.ShoppingCartBackend.domain.Product;
import com.demo.ShoppingCartBackend.service.ShoppingCartBakendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/shoppingCart/rest")
public class ShoppingCartBackendController {

    private ShoppingCartBakendService shoppingCartBakendService;
    private Logger logger;

    @Autowired
    public ShoppingCartBackendController(ShoppingCartBakendService shoppingCartBakendService) {
        this.shoppingCartBakendService = shoppingCartBakendService;
        this.logger = LoggerFactory.getLogger(ShoppingCartBackendController.class);
    }

    @GetMapping(path = "/clients")
    public List<Client> getClients(){
        return shoppingCartBakendService.getClients();
    }

    @GetMapping(path = "/addClients")
    public List<Client> addClients(){
        return shoppingCartBakendService.addClients();
    }

    @GetMapping(path = "/products")
    public List<Product> getProducts(){
        return shoppingCartBakendService.getProducts();
    }

    @GetMapping(path = "/addProducts")
    public List<Product> addProducts(){
        return shoppingCartBakendService.addProducts();
    }
}
