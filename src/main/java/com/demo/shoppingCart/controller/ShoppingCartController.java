package com.demo.shoppingCart.controller;

import com.demo.shoppingCart.domain.Client;
import com.demo.shoppingCart.service.ShoppingCartService;
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
public class ShoppingCartController {

    private ShoppingCartService shoppingCartService;
    private Logger logger;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
        this.logger = LoggerFactory.getLogger(ShoppingCartController.class);
    }

    @GetMapping(path = "/clients")
    public List<Client> getClients(){
        return shoppingCartService.getClients();
    }
}
