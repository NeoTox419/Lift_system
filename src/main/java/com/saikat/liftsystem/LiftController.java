package com.saikat.liftsystem;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LiftController {

    @GetMapping("/ping")
    public String ping() {
        return "Lift System is Alive!";
    }
}
