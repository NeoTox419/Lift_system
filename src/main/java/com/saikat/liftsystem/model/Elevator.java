    package com.saikat.liftsystem.model;

    import java.util.*;

    public class Elevator {
        private final int id;
        private int currentFloor = 0;
        private ElevatorDirection direction = ElevatorDirection.IDLE;

        private final NavigableSet<Integer> upStops = new TreeSet<>();
        private final NavigableSet<Integer> downStops = new TreeSet<>();

        public Elevator(int id) { this.id = id; }

        public int getId() { return id; }
        public int getCurrentFloor() { return currentFloor; }
        public ElevatorDirection getDirection() { return direction; }

        public NavigableSet<Integer> getUpStops() { return upStops; }
        public NavigableSet<Integer> getDownStops() { return downStops; }

        public void addStopLook(int floor) {
            if (floor > currentFloor) {
                upStops.add(floor);
            } else if (floor < currentFloor) {
                downStops.add(floor);
            }
            // equal (== currentFloor) is handled as "already-here" by service; we don't enqueue here.
        }

        public boolean hasStop(int floor) {
            return upStops.contains(floor) || downStops.contains(floor);
        }

        // Total pending stops
        public int getPendingCount() {
            return upStops.size() + downStops.size();
        }

        // Compute the next stop based on current direction (used by /status and logs). */
        public Integer getNextStop() {
            switch (direction) {
                case UP: {
                    Integer nextUp = upStops.ceiling(currentFloor + 1); // strictly above current
                    if (nextUp != null) return nextUp;
                    // Would reverse next, but for "nextStop" we can preview the head in the other set:
                    Integer nextDown = downStops.floor(currentFloor - 1);
                    return nextDown;
                }
                case DOWN: {
                    Integer nextDown = downStops.floor(currentFloor - 1); // strictly below current
                    if (nextDown != null) return nextDown;
                    Integer nextUp = upStops.ceiling(currentFloor + 1);
                    return nextUp;
                }
                case IDLE:
                default: {
                    // Choose the closest among nearest candidates on each side
                    Integer up = upStops.ceiling(currentFloor + 1);
                    Integer down = downStops.floor(currentFloor - 1);
                    if (up == null && down == null) return null;
                    if (up != null && down == null) return up;
                    if (up == null) return down;
                    int distUp = Math.abs(up - currentFloor);
                    int distDown = Math.abs(currentFloor - down);
                    if (distUp < distDown) return up;
                    if (distDown < distUp) return down;

                    return up;
                }
            }
        }

        /** One simulation step using LOOK. Returns true if we moved one floor this tick. */
        public boolean stepOnceLook() {
            // If no pending requests, go idle.
            if (upStops.isEmpty() && downStops.isEmpty()) {
                direction = ElevatorDirection.IDLE;
                return false;
            }

            // If IDLE, pick a direction toward the closest pending stop.
            if (direction == ElevatorDirection.IDLE) {
                Integer up = upStops.ceiling(currentFloor + 1);
                Integer down = downStops.floor(currentFloor - 1);
                if (up == null && down == null) {
                    // If sets contain only stops equal to current (shouldn't happen), clear and idle.
                    direction = ElevatorDirection.IDLE;
                    return false;
                }
                if (up != null && down == null) direction = ElevatorDirection.UP;
                else if (up == null) direction = ElevatorDirection.DOWN;
                else {
                    int distUp = Math.abs(up - currentFloor);
                    int distDown = Math.abs(currentFloor - down);
                    direction = (distUp <= distDown) ? ElevatorDirection.UP : ElevatorDirection.DOWN;
                }
            }

            // If we're exactly at a stop for current direction, "open doors" (remove it) and don't move.
            if (direction == ElevatorDirection.UP) {
                if (upStops.contains(currentFloor)) {
                    upStops.remove(currentFloor);
                    if (upStops.isEmpty() && !downStops.isEmpty()) {
                        direction = ElevatorDirection.DOWN; // reverse next tick
                    } else if (upStops.isEmpty()) {
                        direction = ElevatorDirection.IDLE;
                    }
                    return false;
                }
                Integer nextUp = upStops.ceiling(currentFloor + 1);
                if (nextUp != null) {
                    currentFloor++; // move one floor up toward nextUp
                    return true;
                } else {
                    // No more up stops → reverse if needed (no movement this tick)
                    if (!downStops.isEmpty()) {
                        direction = ElevatorDirection.DOWN;
                    } else {
                        direction = ElevatorDirection.IDLE;
                    }
                    return false;
                }
            } else { // DOWN
                if (downStops.contains(currentFloor)) {
                    downStops.remove(currentFloor);
                    if (downStops.isEmpty() && !upStops.isEmpty()) {
                        direction = ElevatorDirection.UP;
                    } else if (downStops.isEmpty()) {
                        direction = ElevatorDirection.IDLE;
                    }
                    return false;
                }
                Integer nextDown = downStops.floor(currentFloor - 1);
                if (nextDown != null) {
                    currentFloor--; // move one floor down toward nextDown
                    return true;
                } else {
                    if (!upStops.isEmpty()) {
                        direction = ElevatorDirection.UP;
                    } else {
                        direction = ElevatorDirection.IDLE;
                    }
                    return false;
                }
            }
        }
    }