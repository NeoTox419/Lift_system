package com.saikat.liftsystem.controller;

import com.saikat.liftsystem.model.Elevator;
import com.saikat.liftsystem.model.ElevatorStatusDto;
import com.saikat.liftsystem.model.RequestAck;
import com.saikat.liftsystem.service.ElevatorService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/lifts")
public class ElevatorController {

    private final ElevatorService svc;

    public ElevatorController(ElevatorService svc) {
        this.svc = svc;
    }

    @GetMapping("/status")
    public ElevatorStatusDto status() {
        Elevator e = svc.get(1);
        if (e == null) {
            return new ElevatorStatusDto(1, 0, null, null, 0);
        }
        return new ElevatorStatusDto(
                e.getId(),
                e.getCurrentFloor(),
                e.getDirection(),
                e.getNextStop(),
                e.getPendingCount()
        );
    }

    @RequestMapping(value = "/request/{floor}", method = { RequestMethod.POST, RequestMethod.GET })
    public RequestAck request(@PathVariable int floor) {
        return svc.requestFloor(1, floor);
    }

    @RequestMapping(value = "/tick", method = { RequestMethod.POST, RequestMethod.GET })
    public Map<String, Object> tick() {
        boolean moved = svc.tickAll();
        Elevator e = svc.get(1);
        Map<String, Object> body = new HashMap<>();
        body.put("moved", moved);
        body.put("currentFloor", e != null ? e.getCurrentFloor() : null);
        body.put("direction", e != null ? e.getDirection() : null);
        body.put("nextStop", e != null ? e.getNextStop() : null);
        return body;
    }
}
