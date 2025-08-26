package com.saikat.liftsystem.controller;

import com.saikat.liftsystem.service.ElevatorService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auto")
public class AutoController {
    private final ElevatorService svc;

    public AutoController(ElevatorService svc) {
        this.svc = svc;
    }

    @GetMapping("/status")
    public Map<String, Object> status() {
        return Map.of("enabled", svc.isAutoEnabled());
    }

    @GetMapping("/pause")
    public Map<String, Object> pause() {
        svc.setAutoEnabled(false);
        return Map.of("enabled", false);
    }

    @GetMapping("/resume")
    public Map<String, Object> resume() {
        svc.setAutoEnabled(true);
        return Map.of("enabled", true);
    }
}