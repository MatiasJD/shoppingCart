package com.demo.ShoppingCartBackend.controller;

import com.demo.ShoppingCartBackend.domain.*;
import com.demo.ShoppingCartBackend.service.ShoppingCartBakendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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

    @GetMapping(path = "/clients")
    public List<Client> getClients(){
        return shoppingCartBakendService.getClients();
    }

    @GetMapping(path = "/products")
    public List<Product> getProducts(){
        return shoppingCartBakendService.getProducts();
    }

    @GetMapping(path = "/clientsVip")
    public List<Client> getClientsVip(){
        return shoppingCartBakendService.getClientsVip();
    }

    @GetMapping(path = "/clientsVipStartDate")
    public List<Client> getClientsVipStartDate(@RequestParam("date")
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        return shoppingCartBakendService.getClientsVipStartDate(date);
    }

    @GetMapping(path = "/clientsVipEndDate")
    public List<Client> getClientsVipEndDate(@RequestParam("date")
                                                 @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        return shoppingCartBakendService.getClientsVipEndDate(date);
    }

    @GetMapping(path = "/getCart/{email}")
    public Cart getCart(@PathVariable("email") String email) throws Exception{
        return shoppingCartBakendService.getCartByClient(email);
    }

    @GetMapping(path = "/getUser/{email}")
    public Client getUser(@PathVariable("email") String email) throws Exception{
        return shoppingCartBakendService.getClientByEmail(email);
    }

    @PostMapping(path = "/saveCart")
    public Cart saveProductToCart(@RequestBody Wrapper wrapper) throws Exception{
        return shoppingCartBakendService.saveCartWithProduct(wrapper);
    }

    @PostMapping(path = "/updateCart")
    public Cart updateCart(@RequestBody Cart cart) throws Exception{
        return shoppingCartBakendService.updateCart(cart);
    }

    @PostMapping(path = "/deleteCartProduct")
    public Cart deleteCartProduct(@RequestBody CartProduct cartProduct) throws Exception{
        return shoppingCartBakendService.deleteCardProduct(cartProduct);
    }

    @PostMapping(path = "/deleteCart")
    public boolean deleteCart(@RequestBody Cart cart) throws Exception{
        return shoppingCartBakendService.deleteCart(cart);
    }

    @GetMapping(path = "/finishBuy/{id}")
    public boolean finishBuy(@PathVariable("id") Long id) throws Exception{
        return shoppingCartBakendService.finishBuy(id);
    }

    @GetMapping(path = "/addCarts")
    public List<Cart> addCarts() throws Exception{
        return shoppingCartBakendService.addCarts();
    }

    @GetMapping(path = "/addClients")
    public List<Client> addClients(){
        return shoppingCartBakendService.addClients();
    }

    @GetMapping(path = "/addProducts")
    public List<Product> addProducts(){
        return shoppingCartBakendService.addProducts();
    }
}
