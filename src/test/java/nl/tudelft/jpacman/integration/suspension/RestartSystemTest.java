package nl.tudelft.jpacman.integration.suspension;

import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.game.Game;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Scenario S4.2: Restart the game.
 * Given the game is suspended;
 * When  the player hits the "Start" button;
 * Then  the game is resumed.
 */
public class RestartSystemTest {

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
     * Test that starts the game, stops it, and starts it again.
     */
    @Test
    public void restartSystemTest() {
        //Setup
        launcher.launch();
        Game game = launcher.getGame();
        game.start();
        game.stop();
        assertThat(game.isInProgress()).isFalse();

        //Execute
        game.start();

        //Assert
        assertThat(game.isInProgress()).isTrue();
    }

}
