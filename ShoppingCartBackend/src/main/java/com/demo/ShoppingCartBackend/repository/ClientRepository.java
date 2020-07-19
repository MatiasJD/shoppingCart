package com.demo.ShoppingCartBackend.repository;

import com.demo.ShoppingCartBackend.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

}
