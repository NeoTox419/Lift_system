package com.saikat.liftsystem.controller;

import com.saikat.liftsystem.model.Elevator;
import com.saikat.liftsystem.model.ElevatorDirection;
import com.saikat.liftsystem.service.ElevatorService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/api/lifts")
public class ElevatorController {
    private final ElevatorService svc;
    public ElevatorController(ElevatorService svc) { this.svc = svc; };

    @GetMapping("/status")
    public Elevator getLift() {
        return svc.get(1);
    }

    @GetMapping("/request/{floor}")
    public Map<String, Object> requestByPath(@PathVariable int floor) {
        svc.requestFloor(1,floor);
        return Map.of("requestedFloor", floor);
    }

    @GetMapping("/request")
    public Map<String, Object> requestByQuery(@RequestParam(required = false) Integer floor) {
        if (floor == null) {
            return Map.of(
                    "error", "MISSING_FLOOR",
                    "usage", "/api/lifts/request/{floor} or /api/lifts/request?floor=NUMBER",
                    "range", "valid floors are 0.."+svc.getMaxFloors()
            );
        }
        svc.requestFloor(1, floor);
        return Map.of("requestedFloor", floor);
    }

    @GetMapping("/tick")
    public Map<String, Object> tick() {
        boolean moved = svc.tickAll();
        return Map.of("moved", moved);
    }
}
