package com.demo.shoppingCart.service;

import com.demo.shoppingCart.domain.Client;
import com.demo.shoppingCart.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartService {

    private ClientRepository clientRepository;
    private Logger logger;

    @Autowired
    public ShoppingCartService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
        this.logger = LoggerFactory.getLogger(ShoppingCartService.class);
    }

    public List<Client> getClients(){
        return clientRepository.findAll();
    }
}
