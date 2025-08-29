package com.saikat.liftsystem.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ElevatorTest {

    @Test
    void upSequence_2_4_6() {
        Elevator e = new Elevator(1);
        // start at 0, add stops
        e.addStopLook(2);
        e.addStopLook(4);
        e.addStopLook(6);

        // Tick our way up
        // Move to 1
        assertTrue(e.stepOnceLook());
        assertEquals(1, e.getCurrentFloor());
        // Move to 2 (arrive, next tick will clear)
        assertTrue(e.stepOnceLook());
        assertEquals(2, e.getCurrentFloor());
        // Clear stop at 2 (no move this tick)
        assertFalse(e.stepOnceLook());
        // Move to 3
        assertTrue(e.stepOnceLook());
        // Move to 4 (arrive)
        assertTrue(e.stepOnceLook());
        assertEquals(4, e.getCurrentFloor());
        // Clear stop at 4
        assertFalse(e.stepOnceLook());
        // Move to 5
        assertTrue(e.stepOnceLook());
        // Move to 6 (arrive)
        assertTrue(e.stepOnceLook());
        assertEquals(6, e.getCurrentFloor());
        // Clear stop at 6; no more pending -> IDLE
        assertFalse(e.stepOnceLook());
        assertEquals(ElevatorDirection.IDLE, e.getDirection());
    }

    @Test
    void inPathPickup_3_to_7_then_5() {
        Elevator e = new Elevator(1);
        // Start at 0, request 7
        e.addStopLook(7);

        // Move up toward 7 for a few ticks so we’re “en route”
        e.stepOnceLook(); // to 1
        e.stepOnceLook(); // to 2
        e.stepOnceLook(); // to 3

        // New request in path (5)
        e.addStopLook(5);

        // Keep going: should visit 5 before 7
        e.stepOnceLook(); // to 4
        e.stepOnceLook(); // to 5
        assertEquals(5, e.getCurrentFloor());
        // Clear stop at 5 (no move)
        e.stepOnceLook();
        // Continue to 6 and 7
        e.stepOnceLook(); // 6
        e.stepOnceLook(); // 7
        assertEquals(7, e.getCurrentFloor());
    }

    @Test
    void reverseDirection_afterFinishingDown_thenGoUp() {
        Elevator e = new Elevator(1);
        // Move up first so we can add a lower stop and then higher
        e.addStopLook(4);
        // move to 4
        for (int i=0;i<5;i++) e.stepOnceLook(); // arrive and clear
        e.stepOnceLook(); // clear 4

        // Now request 1 (down), then while going down request 5 (up)
        e.addStopLook(1);
        // start going down
        e.stepOnceLook(); // 3
        e.stepOnceLook(); // 2
        e.stepOnceLook(); // 1
        assertEquals(1, e.getCurrentFloor());
        e.stepOnceLook(); // clear 1 (no move), should reverse to UP if needed

        e.addStopLook(5);
        // go up to 5
        e.stepOnceLook(); // 2
        e.stepOnceLook(); // 3
        e.stepOnceLook(); // 4
        e.stepOnceLook(); // 5
        assertEquals(5, e.getCurrentFloor());
    }

    @Test
    void nextStop_prefersClosestInDirection() {
        Elevator e = new Elevator(1);
        e.addStopLook(9);
        e.addStopLook(6);
        e.addStopLook(8);
        // Start moving up: nextStop should be 6 first
        assertEquals(6, e.getNextStop());
    }
}
