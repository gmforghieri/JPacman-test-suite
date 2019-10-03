package nl.tudelft.jpacman.game;

import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Player;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Abstract Game integration test class from which,
 * the SingleLevel and MultiLevel classes extend.
 */
abstract class AbstractGameIntegrationTest {
    //CHECKSTYLE:OFF
    protected Game game;
    protected Player player;

    //CHECKSTYLE:ON
    /**
     * @return specified in inherited methods.
     */
    public abstract Game getGame();

    /**
     * Setup.
     */
    @BeforeEach
    public void before() {
        game = spy(getGame());
        player = game.getPlayers().get(0);
    }

    /**
     *
     * @param start the starting state of the application
     * @param path the event happening of the application
     * @param end the ending state after the event of the application
     */
    @SuppressWarnings("MethodLength")
    public void gameParamTest(State start, Action path, State end) {
        //Arrange
        switch (start) {
            case READY_TO_START:
                break;
            case PLAYING:
                game.start();
                break;
            case GAME_WON:
                game.start();
                game.move(player, Direction.SOUTH);
                verify(game).levelWon();
                break;
            case GAME_LOST:
                game.start();
                game.move(player, Direction.EAST);
                verify(game).levelLost();
                break;
            case GAME_PAUSED:
                game.start();
                game.stop();
                break;
            default:
                break;
        }

        //Act
        switch (path) {
            case START_BUTTON:
                game.start();
                break;
            case STOP_BUTTON:
                game.stop();
                break;
            case PELLETS_CONSUMED:
                game.move(player, Direction.SOUTH);
                break;
            case COLLISION_GHOST:
                game.move(player, Direction.EAST);
                break;
            default:
                break;
        }

        //Assert
        switch (end) {
            case READY_TO_START:
                assertFalse(game.isInProgress());
                assertTrue(player.isAlive());
                assertNotEquals(0, game.getLevel().remainingPellets());
                break;
            case PLAYING:
                assertTrue(game.isInProgress());
                assertTrue(player.isAlive());
                assertNotEquals(0, game.getLevel().remainingPellets());
                break;
            case GAME_WON:
                assertFalse(game.isInProgress());
                assertTrue(player.isAlive());
                assertEquals(0, game.getLevel().remainingPellets());
                verify(game).levelWon();
                break;
            case GAME_LOST:
                assertFalse(game.isInProgress());
                assertFalse(player.isAlive());
                assertNotEquals(0, game.getLevel().remainingPellets());
                verify(game).levelLost();
                break;
            case GAME_PAUSED:
                assertFalse(game.isInProgress());
                assertTrue(player.isAlive());
                assertNotEquals(0, game.getLevel().remainingPellets());
                break;
            default:
                break;
        }
    }
}

/**
 * Enums for the states of the state machine.
 */
enum State {
    READY_TO_START, PLAYING, GAME_WON, GAME_LOST, GAME_PAUSED
}

/**
 * Enums for the events of the state machine.
 */
enum Action {
    START_BUTTON, STOP_BUTTON, PELLETS_CONSUMED, COLLISION_GHOST
}
