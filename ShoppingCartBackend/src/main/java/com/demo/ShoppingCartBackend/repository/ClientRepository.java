package com.demo.ShoppingCartBackend.repository;

import com.demo.ShoppingCartBackend.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findByEmail (String email);

    @Query(value = "SELECT * from client where vip = true",
    nativeQuery = true)
    List<Client> findByVipIsTrue();

    @Query(value = "SELECT * from client where to_char(vip_start_date, 'yyyy-MM') = to_char(?1, 'yyyy-MM')",
            nativeQuery = true)
    List<Client> findByVipStartDate(LocalDate vipStartDate);

    @Query(value = "SELECT * from client where to_char(vip_end_date, 'yyyy-MM') = to_char(?1, 'yyyy-MM')",
            nativeQuery = true)
    List<Client> findByVipEndDate(LocalDate vipEndDate);

    @Query(value = "SELECT * FROM CLIENT WHERE ID = (SELECT CLIENT_ID FROM CART WHERE ID = ?1)", nativeQuery = true)
    Client findByCartId(Long id);

    @Query(value = "SELECT * FROM CLIENT WHERE ID = ?1 AND ?2 <= (SELECT SUM(TOTAL_WITH_DISCOUNT) FROM CART WHERE TO_CHAR(UPDATED_DATE, 'yyyy-MM') = TO_CHAR( ?3, 'yyyy-MM') AND CLIENT_ID = ?1)",
    nativeQuery = true)
    Client superaTotalEnElMes(Long id, BigDecimal totalASuperar, LocalDate date);

    @Query(value = "SELECT c.* FROM CLIENT c LEFT JOIN CART t ON t.CLIENT_ID = c.ID AND TO_CHAR(t.UPDATED_DATE, 'yyyy-MM') = TO_CHAR(?1, 'yyyy-MM') WHERE t.ID IS NULL"
            ,nativeQuery = true)
    List<Client> getClientesQueNoRealizaronCompras(LocalDate date);
}
