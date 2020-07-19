package com.demo.shoppingCart.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
public class Client extends User {

    private boolean vip;
    @Column(name = "vip_start_date")
    private LocalDate vipStartDate;
    @Column(name = "vip_end_date")
    private LocalDate vipEndDate;

    public Client(String email, String password, String nombre, String apellido, boolean vip, LocalDate vipStartDate, LocalDate vipEndDate) {
        super(email, password, nombre, apellido);
        this.vip = vip;
        this.vipStartDate = vipStartDate;
        this.vipEndDate = vipEndDate;
    }

    public Client() {
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public LocalDate getVipStartDate() {
        return vipStartDate;
    }

    public void setVipStartDate(LocalDate vipStartDate) {
        this.vipStartDate = vipStartDate;
    }

    public LocalDate getVipEndDate() {
        return vipEndDate;
    }

    public void setVipEndDate(LocalDate vipEndDate) {
        this.vipEndDate = vipEndDate;
    }
}
