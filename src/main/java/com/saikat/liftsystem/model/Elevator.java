    package com.saikat.liftsystem.model;

    import java.util.*;

    public class Elevator {
        private final int id;
        private int currentFloor = 0;
        private ElevatorDirection direction = ElevatorDirection.IDLE;
        private final Deque<Integer> targets = new ArrayDeque<>();

        private final NavigableSet<Integer> upStops = new TreeSet<>();
        private final NavigableSet<Integer> downStops = new TreeSet<>(Comparator.reverseOrder());

        public Elevator(int id) { this.id = id; }
        public int getId() { return id; }
        public int getCurrentFloor() { return currentFloor; }
        public ElevatorDirection getDirection() { return direction; }
        public Deque<Integer> getTargets() { return targets; }

        public NavigableSet<Integer> getUpStops() { return upStops; }
        public NavigableSet<Integer> getDownStops() { return downStops; }

        public Integer getNextStop() {
            return targets.peekFirst();
        }

        public int getPendingCount() {
            return targets.size(); // tomorrow we'll return upStops.size()+downStops.size()
        }

        public boolean hasTarget(int floor) {
            return targets.contains(floor);
        }

        public void addTarget(int floor) {
            if (!targets.contains(floor)) targets.addLast(floor);
            // we ALSO mirror into LOOK sets now so data builds up for tomorrow's switch
            // (not used for movement yet)
            if (floor > currentFloor) upStops.add(floor);
            else if (floor < currentFloor) downStops.add(floor);
            // equal -> current-floor; service layer handles no-op
        }

        public boolean stepOnce() {
            if (targets.isEmpty()) { direction = ElevatorDirection.IDLE; return false; }
            int before = currentFloor;
            int target = targets.peekFirst();
            if (currentFloor < target) { currentFloor++; direction = ElevatorDirection.UP; }
            else if (currentFloor > target) {
                currentFloor--; direction = ElevatorDirection.DOWN;
            }
            else {
                targets.pollFirst();
                if (targets.isEmpty())
                    direction = ElevatorDirection.IDLE;
            }
            return currentFloor != before;
        }
    }