package nl.tudelft.jpacman.integration.suspension;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.game.Game;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Scenario S4.1: Suspend the game.
 * Given the game has started;
 * When  the player clicks the "Stop" button;
 * Then  all moves from ghosts and the player are suspended.
 */
public class SuspendSystemTest {
    private Launcher launcher;

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
     * The test starts the game then stops is and
     * asserts that the game is not in progress.
     */
    @Test
    public void gameIsSuspended() {
        //Act
        launcher.launch();
        getGame().start();
        //Arrange
        getGame().stop();
        //Assert
        assertThat(getGame().isInProgress()).isFalse();
    }


    private Game getGame() {
        return launcher.getGame();
    }
}
