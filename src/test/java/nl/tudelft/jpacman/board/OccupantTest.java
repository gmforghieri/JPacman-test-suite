package nl.tudelft.jpacman.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test suite to confirm that {@link Unit}s correctly (de)occupy squares.
 *
 * @author Jeroen Roosen 
 *
 */
class OccupantTest {

    /**
     * The unit under test.
     */
    private Unit unit;

    /**
     * Resets the unit under test.
     */
    @BeforeEach
    void setUp() {
        unit = new BasicUnit();
    }

    /**
     * Asserts that a unit has no square to start with.
     */
    @Test
    void noStartSquare() {
        //Assert that unit does not have square
        assertFalse(unit.hasSquare());
        //If the unit is not assigned to a square, it should throw AssertionError
        assertThrows(AssertionError.class, () -> unit.getSquare());
    }

    /**
     * Tests that the unit indeed has the target square as its base after
     * occupation.
     */
    @Test
    void testOccupy() {
        //Create a new square for the unit to occupy
        Square square = new BasicSquare();
        unit.occupy(square);

        //Verify that the unit is on the square
        assertSame(square, unit.getSquare());

        //Verify that square is occupied by unit
        assertTrue(square.getOccupants().contains(unit));
    }

    /**
     * Test that the unit indeed has the target square as its base after
     * double occupation.
     */
    @Test
    void testReoccupy() {
        //Assign unit to a square once
        Square square1 = new BasicSquare();
        unit.occupy(square1);

        //Assign unit to a square for the second time;
        Square square2 = new BasicSquare();
        unit.occupy(square2);

        //Verify that first square is empty
        assertEquals(0, square1.getOccupants().size());

        //Verify that unit is on second square
        assertSame(square2, unit.getSquare());

        //Verify that second square is occupied by unit
        assertEquals(Collections.singletonList(unit), square2.getOccupants());
    }
}
