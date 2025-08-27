package com.saikat.liftsystem.model;

public class RequestAck {
    private final boolean accepted;
    private final String reason;
    private final int floor;

    public RequestAck(boolean accepted, String reason, int floor) {
        this.accepted = accepted;
        this.reason = reason;
        this.floor = floor;
    }

    public boolean isAccepted() { return accepted; }
    public String getReason() { return reason; }
    public int getFloor() { return floor; }

    public static RequestAck ok(int floor) { return new RequestAck(true, "ok", floor); }
    public static RequestAck alreadyHere(int floor) { return new RequestAck(false, "already-here", floor); }
    public static RequestAck duplicate(int floor) { return new RequestAck(false, "duplicate", floor); }
    public static RequestAck outOfRange(int floor) { return new RequestAck(false, "out-of-range", floor); }
}
