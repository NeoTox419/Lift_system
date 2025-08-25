package com.saikat.liftsystem.controller;

import com.saikat.liftsystem.service.ElevatorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ConfigController {
    private final ElevatorService svc;
    public ConfigController(ElevatorService svc) { this.svc = svc; }

    @GetMapping("/api/config")
    public Map<String, Object> config() {
        return Map.of("maxFloors", svc.getMaxFloors());
    }
}