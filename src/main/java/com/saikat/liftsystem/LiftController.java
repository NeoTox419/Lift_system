package com.saikat.liftsystem;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LiftController {

    private final LiftService liftService;

    public LiftController(LiftService liftService){
        this.liftService = liftService;
    }

    @GetMapping("/ping")
    public String ping() {
        return "Lift System is Alive!";
    }

    //set floors once per run (this simulates  "runtime input"
    @GetMapping("/config/floors/{count}")
    public String setFloors(@PathVariable int count ){
        return liftService.setFloors(count);
    }

    @GetMapping("/config")
    public String getConfig(){
        return liftService.getConfig();
    }

    @GetMapping("/test/case1")
    public  String runCase1(){
        liftService.runCase1();
        return "Case 1 executed - check console logs for details.";
    }
}
