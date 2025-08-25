package com.saikat.liftsystem.service;

import com.saikat.liftsystem.model.Elevator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ElevatorService {
    private final Map<Integer, Elevator> elevators = new HashMap<>();
    private final int maxFloors; // comes from the number entered at start

    public ElevatorService(@Value("${building.maxFloors}") int maxFloors) {
        this.maxFloors = maxFloors;
        elevators.put(1, new Elevator(1)); //one lift
    }

    public int getMaxFloors() { return maxFloors; }

    public Collection<Elevator> list() { return elevators.values(); }

    public Elevator get(int id) { return elevators.get(id); }

    public void requestFloor(int id, int floor) {
        if (floor < 0 || floor > maxFloors) {
            throw new IllegalArgumentException("Invalid floor " + floor + " (allowed: 0.." + maxFloors + ")");
        }
        Elevator e = elevators.get(id);
        if (e != null) e.addTarget(floor);
    }

    public void tickAll() { elevators.values().forEach(Elevator::stepOnce); }
}