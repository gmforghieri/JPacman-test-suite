package nl.tudelft.jpacman.game;

import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.points.PointCalculator;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

/**
 * GameUnitTest which defines a SinglePlayerGame with mocked dependencies.
 */
class GameUnitTest {

    /**
     * Valid start of the game player is alive and pellets > 0.
     */
    @Test
    void startValid() {
        //Mock player, level
        Player player = mock(Player.class);
        Level level = mock(Level.class);
        PointCalculator calc = mock(PointCalculator.class);

        //Setup level
        when(level.isAnyPlayerAlive()).thenReturn(true);
        when(level.remainingPellets()).thenReturn(1);

        //Create game
        SinglePlayerGame singlePlayerGame = new SinglePlayerGame(player, level, calc);
        singlePlayerGame.start();

        //Verify start
        verify(level).start();
        verify(level).addObserver(any());
    }

    /**
     * Invalid start of the game because player is not dead.
     */
    @Test
    void startInvalidDeadPlayer() {
        //Mock player, level
        Player player = mock(Player.class);
        Level level = mock(Level.class);
        PointCalculator calc = mock(PointCalculator.class);

        //Setup level
        when(level.isAnyPlayerAlive()).thenReturn(false);
        when(level.remainingPellets()).thenReturn(1);

        //Create game
        SinglePlayerGame singlePlayerGame = new SinglePlayerGame(player, level, calc);
        singlePlayerGame.start();

        //Verify start
        verify(level, times(0)).start();
        verify(level, times(0)).addObserver(any());
    }

    /**
     * Invalid start of the game because the number of pellets is not > 0.
     */
    @Test
    void startInvalidNoPellets() {
        //Mock player, level
        Player player = mock(Player.class);
        Level level = mock(Level.class);
        PointCalculator calc = mock(PointCalculator.class);

        //Setup level
        when(level.isAnyPlayerAlive()).thenReturn(true);
        when(level.remainingPellets()).thenReturn(0);

        //Create game
        SinglePlayerGame singlePlayerGame = new SinglePlayerGame(player, level, calc);
        singlePlayerGame.start();

        //Verify start
        verify(level, times(0)).start();
        verify(level, times(0)).addObserver(any());
    }

    /**
     * Start in progress because of the progresslock.
     */
    @Test
    void startInProgress() {
        //Mock player, level
        Player player = mock(Player.class);
        Level level = mock(Level.class);
        PointCalculator calc = mock(PointCalculator.class);

        //Setup level
        when(level.isAnyPlayerAlive()).thenReturn(true);
        when(level.remainingPellets()).thenReturn(1);

        //Create game
        SinglePlayerGame singlePlayerGame = new SinglePlayerGame(player, level, calc);
        singlePlayerGame.start();
        singlePlayerGame.start();

        //Verify start
        verify(level, times(1)).start();
        verify(level, times(1)).addObserver(any());
    }
}
