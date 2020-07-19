package com.demo.ShoppingCartBackend.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Especial_Date")
public class EspecialDate {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private LocalDate date;

    public EspecialDate(LocalDate date) {
        this.date = date;
    }

    public EspecialDate() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
