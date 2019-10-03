package nl.tudelft.jpacman.game;

import nl.tudelft.jpacman.MultiLevelLauncher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for the multilevel game implementation we have created.
 * The only additional test cases compared the SinglePlayerGame is
 * the winning the game scenario, where the game can continue by pressing
 * the start button again.
 */
public class MultiLevelGameIntegrationTest extends AbstractGameIntegrationTest {

    @Override
    public Game getGame() {
        MultiLevelLauncher launcher = new MultiLevelLauncher();
        launcher.withMapFiles("/gametest.txt", "/gametest.txt",
            "/gametest.txt", "/gametest.txt").launch();
        return launcher.getGame();
    }

    /**
     * Parameterized test for all paths and sneak paths
     * derived from the state transition table.
     * @param start the starting state of the application
     * @param path the event happening of the application
     * @param end the ending state after the event of the application
     */
    @ParameterizedTest
    @CsvSource({
        "READY_TO_START, START_BUTTON, PLAYING",
        "READY_TO_START, STOP_BUTTON, READY_TO_START",
        "READY_TO_START, PELLETS_CONSUMED, READY_TO_START",
        "READY_TO_START, COLLISION_GHOST, READY_TO_START",
        "PLAYING, START_BUTTON, PLAYING",
        "PLAYING, STOP_BUTTON, GAME_PAUSED",
        "PLAYING, PELLETS_CONSUMED, GAME_WON",
        "PLAYING, COLLISION_GHOST, GAME_LOST",
        "GAME_WON, START_BUTTON, PLAYING",
        "GAME_WON, STOP_BUTTON, GAME_WON",
        "GAME_WON, PELLETS_CONSUMED, GAME_WON",
        "GAME_WON, COLLISION_GHOST, GAME_WON",
        "GAME_LOST, START_BUTTON, GAME_LOST",
        "GAME_LOST, STOP_BUTTON, GAME_LOST",
        "GAME_LOST, PELLETS_CONSUMED, GAME_LOST",
        "GAME_LOST, COLLISION_GHOST, GAME_LOST",
        "GAME_PAUSED, START_BUTTON, PLAYING",
        "GAME_PAUSED, STOP_BUTTON, GAME_PAUSED",
        "GAME_PAUSED, PELLETS_CONSUMED, GAME_PAUSED",
        "GAME_PAUSED, COLLISION_GHOST, GAME_PAUSED"
    })
    @Override
    public void gameParamTest(State start, Action path, State end) {
        super.gameParamTest(start, path, end);
    }

    /**
     * Separate test scenario for the modified multi level
     * game, testing that multiple levels are possible to be played.
     * We have decided to create a separate method in order
     * to avoid refactoring the method for the parameterized test cases.
     */
    @Test
    @SuppressWarnings("PMD")
    public void winFourTimesTest() {
        //Session 1
        game.start();
        assertTrue(game.isInProgress());
        game.levelWon();
        assertFalse(game.isInProgress());

        //Session 2
        game.start();
        assertTrue(game.isInProgress());
        game.levelWon();
        assertFalse(game.isInProgress());

        //Session 3
        game.start();
        assertTrue(game.isInProgress());
        game.levelWon();
        assertFalse(game.isInProgress());

        //Session 4
        game.start();
        assertTrue(game.isInProgress());
        game.levelWon();
        assertFalse(game.isInProgress());

        //Session 5
        game.start();
        assertFalse(game.isInProgress());
    }
}
