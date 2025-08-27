package com.saikat.liftsystem.service;

import com.saikat.liftsystem.model.Elevator;
import com.saikat.liftsystem.model.RequestAck;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ElevatorService {
    private final Map<Integer, Elevator> elevators = new HashMap<>();
    private final int maxFloors; // set at startup prompt
    private volatile boolean autoEnabled = true;

    public ElevatorService(@Value("${building.maxFloors}") int maxFloors) {
        this.maxFloors = maxFloors;
        elevators.put(1, new Elevator(1)); // single lift
    }

    public boolean isAutoEnabled() { return autoEnabled; }
    public void setAutoEnabled(boolean enabled) { this.autoEnabled = enabled; }

    public int getMaxFloors() { return maxFloors; }
    public Collection<Elevator> list() { return elevators.values(); }
    public Elevator get(int id) { return elevators.get(id); }

    public synchronized RequestAck requestFloor(int id, int floor) {
        if (floor < 0 || floor > maxFloors) {
            // Converted to 400 JSON by ApiErrors
            throw new IllegalArgumentException("Invalid floor " + floor + " (allowed: 0.." + maxFloors + ")");
        }
        Elevator e = elevators.get(id);
        if (e == null) {
            throw new IllegalArgumentException("Elevator " + id + " not found");
        }

        if (e.getCurrentFloor() == floor) {
            return RequestAck.alreadyHere(floor);
        }
        if (e.hasTarget(floor)) {
            return RequestAck.duplicate(floor);
        }

        e.addTarget(floor);
        return RequestAck.ok(floor);
    }

    public synchronized boolean tickAll() {
        Elevator e = elevators.get(1);
        return e != null && e.stepOnce();
    }
}
