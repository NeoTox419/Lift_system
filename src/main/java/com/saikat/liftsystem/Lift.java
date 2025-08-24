package com.saikat.liftsystem;

public class Lift {
    private int curretFloor;
    private Direction direction;

    public Lift(int startFloor){
        this.curretFloor = startFloor;
        this.direction = Direction.IDLE;
    }

    public int getCurretFloor() { return  curretFloor; }
    public Direction getDirection() { return direction; }

    public void setCurretFloor(int curretFloor) { this.curretFloor = curretFloor; }
    public void setDirection(Direction direction) { this.direction = direction; }
}
