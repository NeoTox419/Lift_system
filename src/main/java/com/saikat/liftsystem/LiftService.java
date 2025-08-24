package com.saikat.liftsystem;

import org.springframework.stereotype.Service;

@Service
public class LiftService {

    private Integer numberOfFloors;
    private Lift lift = new Lift(0);

    public String setFloors(int count){
        if (count < 2) return "Number of floors must be >= 2";
        this.numberOfFloors = count;
        System.out.println("[CONFIG] Number of floors set to: " + numberOfFloors);
        return "Floors set to " + numberOfFloors;
    }

    public String getConfig(){
        return numberOfFloors == null ? "Floors not set" : "Floors="+numberOfFloors;
    }

    //=====  Case 1: single request example =====
    //Scenario: someone requests the lift to come to floor 'targetFloor'
    public void runCase1() {
        ensureConfigured();

        int targetFloor = Math.min(numberOfFloors - 1, 5); //demo: go to floor 5 (or top-1 if <=5 floors)
        System.out.println("\n=== CASE 1: Request lift to floor " + targetFloor + "===");
        System.out.println("Lift starting at floor " + lift.getCurretFloor() + ", direction " + lift.getDirection());

        if (lift.getCurretFloor() == targetFloor) {
            System.out.println("Lift already at requested floor" + targetFloor + ". Doors Opening.");
            lift.setDirection(Direction.IDLE);
            return;
        }

        if (lift.getCurretFloor() < targetFloor){
            lift.setDirection(Direction.UP);
            while (lift.getCurretFloor() < targetFloor){
                lift.setCurretFloor(lift.getCurretFloor()+1);
                System.out.println("Moving Up → floor " + lift.getCurretFloor());
            }
        } else {
            lift.setDirection(Direction.DOWN);
            while (lift.getCurretFloor() > targetFloor){
                lift.setCurretFloor(lift.getCurretFloor()-1);
                System.out.println("Moving DOWN → floor " + lift.getCurretFloor());
            }
        }

        lift.setDirection(Direction.IDLE);
        System.out.println("Arrived at floor "+ targetFloor + ". Doors Opening. Direction → " + lift.getDirection());
    }

    private void ensureConfigured(){
        if(numberOfFloors == null){
            throw new IllegalStateException("Number of floors not set. Call /config/floors/{count} first.");
        }
    }
}
