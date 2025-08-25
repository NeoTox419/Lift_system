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

    @GetMapping
    public Collection<Elevator> list() { return svc.list(); }

    @GetMapping("/{id}/request")
    public Map<String, Object> request(@PathVariable int id, @RequestParam int floor) {
        svc.requestFloor(id,floor);
        return Map.of("lift",id, "queuedFloor", floor);
    }

    @GetMapping("/tick")
    public Map<String, String> tick(){
        svc.tickAll();
        return Map.of("moved", "ok");
    }
}
