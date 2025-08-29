package com.saikat.liftsystem.jobs;

import com.saikat.liftsystem.model.Elevator;
import com.saikat.liftsystem.model.ElevatorDirection;
import com.saikat.liftsystem.service.ElevatorService;
import com.saikat.liftsystem.util.Ansi;
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

        Elevator elev = svc.get(1);
        if (elev == null) return;

        String dirIcon;
        ElevatorDirection dir = elev.getDirection();
        if (dir == ElevatorDirection.UP) {
            dirIcon = Ansi.up("▲ UP");
        } else if (dir == ElevatorDirection.DOWN) {
            dirIcon = Ansi.down("▼ DOWN");
        } else {
            dirIcon = Ansi.idle("■ IDLE");
        }

        log.info("""
                {}
                {}
                Current floor : {}
                Direction     : {}
                Next stop     : {}
                Pending stops : {}
                {}
                """,
                Ansi.info("\n=========== LIFT STATUS (AUTO TICK) ==========="),
                "",
                elev.getCurrentFloor(),
                dirIcon,
                elev.getNextStop(),
                elev.getPendingCount(),
                Ansi.info("===============================================")
        );
    }
}
