package com.saikat.liftsystem.jobs;

import com.saikat.liftsystem.model.Elevator;
import com.saikat.liftsystem.service.ElevatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AutoMoveJob {
    private static final Logger log = LoggerFactory.getLogger(AutoMoveJob.class);
    private final ElevatorService svc;

    public AutoMoveJob(ElevatorService svc) {
        this.svc = svc;
    }

    // runs every 5 seconds after app starts
    @Scheduled(initialDelay = 5000, fixedRate = 5000)
    public void autoMove() {
        if (!svc.isAutoEnabled()) {
            return; // pause mode
        }
        svc.tickAll();

        Elevator e = svc.get(1);
        if(e!=null){
            log.info("[AUTO] floor{} dir={} queue{}",
                      e.getCurrentFloor(), e.getDirection(), e.getTargets());
        }
    }
}