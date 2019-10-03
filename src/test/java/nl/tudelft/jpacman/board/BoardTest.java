package nl.tudelft.jpacman.board;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for checking board validity.
 */
class BoardTest {


    /**
     * Checking a simple 1x1 boards validity.
     */
    @Test
    void boardVerification() {

        //Create a 2D Square array with one element
        Square[][] field = new Square[1][1];
        //declared a new basic Square
        Square fieldSquare = new BasicSquare();
        //Set the values of testsquare to a basicSquare
        field[0][0] = fieldSquare;
        //Assign the testsquare to the board
        Board testBoard = new Board(field);
        //Verifying that the square is at the 0,0 position
        assertSame(fieldSquare, testBoard.squareAt(0, 0));

    }

    /**
     * Tests that the withinBorders function works correctly using boundary testing.
     * @param x The x to test
     * @param y The y to test
     * @param expected The expected result
     */
    @ParameterizedTest
    @CsvSource({
        "0, 1, true",
        "-1, 1, false",
        "3, 1, false",
        "2, 1, true",
        "1, 0, true",
        "1, -1, false",
        "1, 5, false",
        "1, 4, true"
    })
    void withinBorders(int x, int y, boolean expected) {
        //Constants for this test
        final int width = 3;
        final int height = 5;

        //Create field
        Square[][] field = new Square[width][height];
        for (int fx = 0; fx < width; fx++) {
            for (int fy = 0; fy < height; fy++) {
                field[fx][fy] = new BasicSquare();
            }
        }

        //Create board
        Board testBoard = new Board(field);

        //Test within boarders
        if (expected) {
            assertTrue(testBoard.withinBorders(x, y));
        } else {
            assertFalse(testBoard.withinBorders(x, y));
        }
    }
}
