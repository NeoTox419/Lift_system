package com.saikat.liftsystem.controller;

import com.saikat.liftsystem.model.Elevator;
import com.saikat.liftsystem.service.ElevatorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/lifts")
public class DebugController {

    private final ElevatorService svc;

    public DebugController(ElevatorService svc) {
        this.svc = svc;
    }

    @GetMapping("/debug")
    public Map<String, Object> debug() {
        Elevator e = svc.get(1);
        Map<String, Object> body = new HashMap<>();
        if (e == null) return body;
        body.put("id", e.getId());
        body.put("currentFloor", e.getCurrentFloor());
        body.put("direction", e.getDirection());
        body.put("upStops", new ArrayList<>(e.getUpStops()));
        body.put("downStops", new ArrayList<>(e.getDownStops()));
        body.put("nextStop", e.getNextStop());
        body.put("pendingStops", e.getPendingCount());
        return body;
    }
}
