package com.demo.ShoppingCartBackend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class ScheduledTasks {

    @Autowired
    private ShoppingCartBakendService shoppingCartBakendService;
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleTaskWithFixedRate() {
        shoppingCartBakendService.deleteCartsNotFinished(LocalDate.now().plusDays(1));
        logger.info("Deleting shoppingCart not finiched");
    }
}
