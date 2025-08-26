package com.saikat.liftsystem.model;

import java.util.ArrayDeque;
import java.util.Deque;

public class Elevator {
    private final int id;
    private int currentFloor = 0;
    private ElevatorDirection direction = ElevatorDirection.IDLE;
    private final Deque<Integer> targets = new ArrayDeque<>();

    public Elevator(int id) { this.id = id; }
    public int getId() { return id; }
    public int getCurrentFloor() { return currentFloor; }
    public ElevatorDirection getDirection() { return direction; }
    public Deque<Integer> getTargets() { return targets; }

    public boolean stepOnce() {
        if (targets.isEmpty()) { direction = ElevatorDirection.IDLE; return false; }
        int before = currentFloor;
        int target = targets.peekFirst();
        if (currentFloor < target) { currentFloor++; direction = ElevatorDirection.UP; }
        else if (currentFloor > target) { currentFloor--; direction = ElevatorDirection.DOWN; }
        else { targets.pollFirst(); if (targets.isEmpty()) direction = ElevatorDirection.IDLE; }
        return currentFloor != before;
    }

    public void addTarget(int floor) { if (!targets.contains(floor)) targets.addLast(floor); }


}