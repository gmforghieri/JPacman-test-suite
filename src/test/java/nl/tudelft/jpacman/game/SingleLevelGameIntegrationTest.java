package nl.tudelft.jpacman.game;

import nl.tudelft.jpacman.Launcher;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Test class for the Single level game implementation we have created.
 */
public class SingleLevelGameIntegrationTest extends AbstractGameIntegrationTest {


    @Override
    public Game getGame() {
        Launcher launcher = new Launcher();
        launcher.withMapFile("/gametest.txt").launch();
        return launcher.getGame();
    }

    /**
     * Parameterized test for all paths and sneak paths
     * derived from the state transition table.
     * @param start the starting state
     * @param path the event happening
     * @param end the ending state after the event
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
        "GAME_WON, START_BUTTON, GAME_WON",
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
}
