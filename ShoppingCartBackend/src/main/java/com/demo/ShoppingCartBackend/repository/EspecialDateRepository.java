package com.demo.ShoppingCartBackend.repository;

import com.demo.ShoppingCartBackend.domain.EspecialDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspecialDateRepository extends JpaRepository<EspecialDate, Long> {

}
