package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.LevelFactory;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.level.PlayerFactory;
import nl.tudelft.jpacman.points.DefaultPointCalculator;
import nl.tudelft.jpacman.points.PointCalculator;
import nl.tudelft.jpacman.sprite.PacManSprites;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the Clyde class.
 */
class ClydeTest {
    private GhostMapParser parser;
    private PacManSprites pacManSprites;

    /**
     * Prepares the GhostMapParser and the PacManSprites, to be used by the tests.
     */
    @BeforeEach
    public void prepareGhostMapParser() {
        pacManSprites = new PacManSprites();
        BoardFactory boardFactory = new BoardFactory(pacManSprites);
        GhostFactory ghostFactory = new GhostFactory(pacManSprites);
        PointCalculator pointCalculator = new DefaultPointCalculator();
        LevelFactory levelFactory = new LevelFactory(pacManSprites, ghostFactory, pointCalculator);
        parser = new GhostMapParser(levelFactory, boardFactory, ghostFactory);
    }

    /**
     * The player and Clyde are 8 cells apart, so clyde should be going towards the player.
     * This test creates a simple map where this is true, and verifies this behaviour.
     */
    @Test
    public void goTowards() {
        //Create map
        List<String> map = Arrays.asList(
            "############",
            "#P        C#",
            "############");
        Level level = parser.parseMap(map);

        //Create player on map
        PlayerFactory playerFactory = new PlayerFactory(pacManSprites);
        Player player = playerFactory.createPacMan();
        player.setDirection(Direction.EAST);
        level.registerPlayer(player);

        //Let ghost make move
        Clyde ghost = Navigation.findUnitInBoard(Clyde.class, level.getBoard());
        assertNotNull(ghost);
        Optional<Direction> direction = ghost.nextAiMove();

        //Verify
        assertTrue(direction.isPresent());
        assertEquals(Direction.WEST, direction.get());
    }

    /**
     * The player is not on the board, so Clyde shouldn't do anything.
     * This test creates a simple map where this is true, and verifies this behaviour.
     */
    @Test
    public void noPacman() {
        //Create map
        List<String> map = Arrays.asList(
            "#####",
            "#   #",
            "# C #",
            "#   #",
            "#####");
        Level level = parser.parseMap(map);

        //Let ghost make move
        Clyde ghost = Navigation.findUnitInBoard(Clyde.class, level.getBoard());
        assertNotNull(ghost);
        Optional<Direction> direction = ghost.nextAiMove();

        //Verify
        assertFalse(direction.isPresent());
    }

    /**
     * Tests that Clyde tries to get away from Pacman if Clyde is close enough.
     */
    @Test
    public void goAway() {
        //Create map
        List<String> map = Arrays.asList(
            "###########",
            "#P       C#",
            "###########");
        Level level = parser.parseMap(map);

        //Create player on the Map
        PlayerFactory playerFactory = new PlayerFactory(pacManSprites);
        Player player = playerFactory.createPacMan();
        player.setDirection(Direction.EAST);
        level.registerPlayer(player);

        //Ghost makes the move
        Clyde ghost = Navigation.findUnitInBoard(Clyde.class, level.getBoard());
        assertNotNull(ghost);
        Optional<Direction> direction = ghost.nextAiMove();

        //Verification of Clyde running away
        assertTrue(direction.isPresent());
        assertEquals(Direction.EAST, direction.get());

    }

    /**
     *
     */
    @Test
    public void noPathToPacman() {
        //Create map
        List<String> map = Arrays.asList(
            "###########",
            "#P#      C#",
            "###########");
        Level level = parser.parseMap(map);

        //Create player on the Map
        PlayerFactory playerFactory = new PlayerFactory(pacManSprites);
        Player player = playerFactory.createPacMan();
        player.setDirection(Direction.EAST);
        level.registerPlayer(player);

        //Ghost makes the move
        Clyde ghost = Navigation.findUnitInBoard(Clyde.class, level.getBoard());
        assertNotNull(ghost);
        Optional<Direction> direction = ghost.nextAiMove();

        //Verification of Clyde running away
        assertFalse(direction.isPresent());
    }

}
