package nl.tudelft.jpacman.integration.movement;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.level.Pellet;
import nl.tudelft.jpacman.level.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for Story 2 Move the player.
 */
public class MovementSystemTest {

    private Launcher launcher;
    private static final int POINTS_EARNED = 10;

    /**
     * Start a launcher, which can display the user interface.
     */
    @BeforeEach
    public void before() {
        launcher = new Launcher();
    }

    /**
     * Close the user interface.
     */
    @AfterEach
    public void after() {
        launcher.dispose();
    }

    /**
     * Scenario S2.1: The player consumes
     * Given the game has started,
     *  and  my Pacman is next to a square containing a pellet;
     * When  I press an arrow key towards that square;
     * Then  my Pacman can move to that square,
     *  and  I earn the points for the pellet,
     *  and  the pellet disappears from that square.
     */
    @Test
    public void consumeTest() {
        //Arrange
        launcher.withMapFile("/movementtest.txt").launch();
        Game game = launcher.getGame();
        game.start();
        Player player = game.getPlayers().get(0);
        Square initialSquare = player.getSquare();
        Pellet pellet = (Pellet) initialSquare.getSquareAt(Direction.EAST).getOccupants().get(0);

        //Act
        game.move(player, Direction.EAST);

        //Assert
        assertSame(initialSquare.getSquareAt(Direction.EAST), player.getSquare());
        assertEquals(POINTS_EARNED, player.getScore());
        assertFalse(player.getSquare().getOccupants().contains(pellet));
        assertFalse(pellet.hasSquare());
    }

    /**
     * Scenario S2.2: The player moves on empty square
     * Given the game has started,
     *  and  my Pacman is next to an empty square;
     * When  I press an arrow key towards that square;
     * Then  my Pacman can move to that square
     *  and  my points remain the same.
     */
    @Test
    public void emptySquare() {
        //Arrange
        launcher.withMapFile("/movementtest.txt").launch();
        Game game = launcher.getGame();
        game.start();
        Player player = game.getPlayers().get(0);
        Square initialSquare = player.getSquare();

        //Act
        game.move(player, Direction.SOUTH);

        //Assert
        assertSame(initialSquare.getSquareAt(Direction.SOUTH), player.getSquare());
        assertEquals(0, player.getScore());
    }

    /**
     * Scenario S2.3: The move fails
     * Given the game has started,
     * and my Pacman is next to a cell containing a wall;
     * When  I press an arrow key towards that cell;
     * Then  the move is not conducted.
     */
    @Test
    public void blockedByWall() {
        //Arrange
        launcher.withMapFile("/movementtest.txt").launch();
        Game game = launcher.getGame();
        game.start();
        Player player = game.getPlayers().get(0);
        Square initialSquare = player.getSquare();

        //Act
        game.move(player, Direction.WEST);

        //Assert
        assertSame(initialSquare, player.getSquare());
        assertEquals(0, player.getScore());
    }

    /**
     * Scenario S2.4: The player dies
     * Given the game has started,
     *  and  my Pacman is next to a cell containing a ghost;
     * When  I press an arrow key towards that square;
     * Then  my Pacman dies,
     *  and  the game is over.
     */
    @Test
    public void playerDies() {
        //Arrange
        launcher.withMapFile("/movementtest2.txt").launch();
        Game game = launcher.getGame();
        game.start();
        Player player = game.getPlayers().get(0);

        //Act
        game.move(player, Direction.EAST);

        //Assert
        assertFalse(player.isAlive());
        assertFalse(game.isInProgress());
    }

    /**
     * Scenario S2.5: Player wins, extends S2.1
     * When  I have eaten the last pellet;
     * Then  I win the game.
     */
    @Test
    public void playerWins() {
        //Arrange
        launcher.withMapFile("/movementtest.txt").launch();
        Game game = launcher.getGame();
        game.start();
        Player player = game.getPlayers().get(0);

        //Act
        game.move(player, Direction.EAST);

        //Assert
        assertTrue(player.isAlive());
        assertFalse(game.isInProgress());
    }
}
