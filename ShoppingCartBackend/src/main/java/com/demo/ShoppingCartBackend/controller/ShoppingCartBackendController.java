package com.demo.ShoppingCartBackend.controller;

import com.demo.ShoppingCartBackend.domain.*;
import com.demo.ShoppingCartBackend.service.ShoppingCartBakendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/shoppingCart/rest")
public class ShoppingCartBackendController {

    private ShoppingCartBakendService shoppingCartBakendService;
    private Logger logger;

    @Autowired
    public ShoppingCartBackendController(ShoppingCartBakendService shoppingCartBakendService) {
        this.shoppingCartBakendService = shoppingCartBakendService;
        this.logger = LoggerFactory.getLogger(ShoppingCartBackendController.class);
    }

    @GetMapping(path = "/products")
    public List<Product> getProducts(){
        return shoppingCartBakendService.getProducts();
    }

    @GetMapping(path = "/clients")
    public List<Client> getClients(){
        return shoppingCartBakendService.getClients();
    }

    @GetMapping(path = "/clientsVip")
    public List<Client> getClientsVip(){
        return shoppingCartBakendService.getClientsVip();
    }

    @GetMapping(path = "/clientsVipStartDate")
    public List<Client> getClientsVipStartDate(@RequestParam("date")
                                                   @DateTimeFormat(pattern = "yyyy-dd-MM") LocalDate date){
        return shoppingCartBakendService.getClientsVipStartDate(date);
    }

    @GetMapping(path = "/clientsVipEndDate")
    public List<Client> getClientsVipEndDate(@RequestParam("date")
                                                 @DateTimeFormat(pattern = "yyyy-dd-MM") LocalDate date){
        return shoppingCartBakendService.getClientsVipEndDate(date);
    }

    @GetMapping(path = "/getUser/{email}")
    public Client getUser(@PathVariable("email") String email) throws Exception{
        return shoppingCartBakendService.getClientByEmail(email);
    }

    @GetMapping(path = "/getCart/{email}")
    public Cart getCart(@PathVariable("email") String email) throws Exception{
        return shoppingCartBakendService.getCartByClient(email);
    }

    @PostMapping(path = "/saveCart")
    public Cart saveProductToCart(@RequestBody Wrapper wrapper) throws Exception{
        return shoppingCartBakendService.saveCartWithProduct(wrapper);
    }

    @PostMapping(path = "/updateCart")
    public Cart updateCart(@RequestBody Cart cart) throws Exception{
        return shoppingCartBakendService.updateCart(cart);
    }

    @DeleteMapping(path = "/deleteCart/{id}")
    public Cart deleteCart(@PathVariable("id") Long id) throws Exception{
        return shoppingCartBakendService.deleteCart(id);
    }

    @DeleteMapping(path = "/deleteCartProduct/{id}")
    public Cart deleteCartProduct(@PathVariable("id") Long id) throws Exception{
        return shoppingCartBakendService.deleteCardProduct(id);
    }

    @GetMapping(path = "/finishBuy/{id}")
    public boolean finishBuy(@PathVariable("id") Long id) throws Exception{
        return shoppingCartBakendService.finishBuy(id);
    }

    @GetMapping(path = "/addData")
    public boolean addData() throws Exception{
        return shoppingCartBakendService.addData();
    }
}
