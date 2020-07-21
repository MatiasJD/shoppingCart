package com.demo.ShoppingCartBackend.repository;

import com.demo.ShoppingCartBackend.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findByEmail (String email);

    @Query(value = "SELECT * from Client where vip = true",
    nativeQuery = true)
    List<Client> findByVipIsTrue();

    @Query(value = "SELECT * from Client where to_char(vip_start_date, 'yyyy-MM') = to_char(?1, 'yyyy-MM')",
            nativeQuery = true)
    List<Client> findByVipStartDate(LocalDate vipStartDate);

    @Query(value = "SELECT * from Client where to_char(vip_end_date, 'yyyy-MM') = to_char(?1, 'yyyy-MM')",
            nativeQuery = true)
    List<Client> findByVipEndDate(LocalDate vipEndDate);
}
