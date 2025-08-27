package com.saikat.liftsystem.model;

public class ElevatorStatusDto {
    private final int id;
    private final int currentFloor;
    private final ElevatorDirection direction;
    private final Integer nextStop;   // nullable if none
    private final int pendingStops;

    public ElevatorStatusDto(int id, int currentFloor, ElevatorDirection direction,
                             Integer nextStop, int pendingStops) {
        this.id = id;
        this.currentFloor = currentFloor;
        this.direction = direction;
        this.nextStop = nextStop;
        this.pendingStops = pendingStops;
    }

    public int getId() { return id; }
    public int getCurrentFloor() { return currentFloor; }
    public ElevatorDirection getDirection() { return direction; }
    public Integer getNextStop() { return nextStop; }
    public int getPendingStops() { return pendingStops; }
}
