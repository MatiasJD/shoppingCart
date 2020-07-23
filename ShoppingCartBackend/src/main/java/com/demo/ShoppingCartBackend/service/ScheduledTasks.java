package com.demo.ShoppingCartBackend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class ScheduledTasks {

    @Autowired
    private ShoppingCartBakendService shoppingCartBakendService;
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    private static boolean needToRunStartupMethod = true;


    //se ejecuta todos los dias a las 00:00 a.m.
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleTaskWithCron() {
        shoppingCartBakendService.deleteCartsNotFinished(LocalDate.now());
        logger.info("Deleting shoppingCart not finiched");
    }

    //se ejecuta el 1ro de cada mes a las 00:00 a.m.
    @Scheduled(cron = "0 0 0 1 * ?")
    public void scheduleTaskWithCronMonth() {
        shoppingCartBakendService.changeVip(LocalDate.now().minus(1, ChronoUnit.MONTHS));
        logger.info("Updating vip clients");
    }

    //para verificar funcionamiento de la query, se ejecuta cada 5 segundos
//    @Scheduled(fixedRate = 5000)
//    public void scheduleTaskWithFixedRate() {
//        shoppingCartBakendService.deleteCartsNotFinished(LocalDate.now());
//        logger.info("Deleting shoppingCart not finiched");
//    }


    @Scheduled(fixedRate = 3600000)
    public void keepAlive() {
        //log "alive" every hour for sanity checks
        if (needToRunStartupMethod) {
            shoppingCartBakendService.addData();
            needToRunStartupMethod = false;
        }
    }
}
