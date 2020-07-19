package com.demo.shoppingCart.repository;

import com.demo.shoppingCart.domain.EspecialDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspecialDateRepository extends JpaRepository<EspecialDate, Long> {

}
