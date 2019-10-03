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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for Inky Ghost functioning.
 */
class InkyTest {
    private GhostMapParser parser;
    private PacManSprites pacManSprites;

    /**
     * Setting up the GhostMapParser and Sprites to be used by the tests.
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
     * When Pacman does not exist, Inky should not output a move.
     */
    @Test
    public void noPacman() {
        //Create map
        List<String> map = Arrays.asList(
            "#######",
            "# I B #",
            "#######");
        Level level = parser.parseMap(map);

        //Let ghost make move
        Inky ghost = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        assertNotNull(ghost);
        Optional<Direction> direction = ghost.nextAiMove();

        //Verify
        assertFalse(direction.isPresent());
    }

    /**
     * When Blinky does not exist, Inky should not output a move.
     */
    @Test
    public void noBlinky() {
        //Create map
        List<String> map = Arrays.asList(
            "#######",
            "# I P #",
            "#######");
        Level level = parser.parseMap(map);

        //Let ghost make move
        Inky ghost = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        assertNotNull(ghost);
        Optional<Direction> direction = ghost.nextAiMove();

        //Verify
        assertFalse(direction.isPresent());
    }

    /**
     * When there is no route to Pacman, Inky should not output a move.
     */
    @Test
    public void notPossibleToDouble() {
        //Create map
        List<String> map = Arrays.asList(
            "#######",
            "# I   #",
            "#     #",
            "#     #",
            "#  P  #",
            "#  B  #",
            "#######");
        Level level = parser.parseMap(map);

        //Create player on map
        PlayerFactory playerFactory = new PlayerFactory(pacManSprites);
        Player player = playerFactory.createPacMan();
        player.setDirection(Direction.EAST);
        level.registerPlayer(player);

        //Let ghost make move
        Inky ghost = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        assertNotNull(ghost);
        Optional<Direction> direction = ghost.nextAiMove();

        //Verify
        assertFalse(direction.isPresent());
    }


    /**
     * When Inky is on the other side of pacman as blinky, it should draw a line between the tile
     * two places to the right of Blinky and Pacman, and multiply that by two. In this case, it
     * should go to the tile to the right of Inky. This test validates this behaviour.
     */
    @Test
    public void otherSide() {
        //Create map
        List<String> map = Arrays.asList(
            "#######",
            "# I   #",
            "#     #",
            "#     #",
            "#     #",
            "#BP   #",
            "#######");
        Level level = parser.parseMap(map);

        //Create player on map
        PlayerFactory playerFactory = new PlayerFactory(pacManSprites);
        Player player = playerFactory.createPacMan();
        player.setDirection(Direction.NORTH);
        level.registerPlayer(player);

        //Let ghost make move
        Inky ghost = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        assertNotNull(ghost);
        Optional<Direction> direction = ghost.nextAiMove();

        //Verify
        assertTrue(direction.isPresent());
        assertEquals(Direction.EAST, direction.get());
    }

    /**
     * When Pac-Man is moving or facing up,
     * the spot Inky uses to draw the line is two squares above
     * and left of Pac-Man.
     */
    @Test
    public void inkyBug() {
        //Create map
        List<String> map = Arrays.asList(
            "#####",
            "#   #",
            "#I  #",
            "#   #",
            "#   #",
            "#   #",
            "#  P#",
            "#  B#",
            "#####");
        Level level = parser.parseMap(map);

        //Create player on the Map
        PlayerFactory playerFactory = new PlayerFactory(pacManSprites);
        Player player = playerFactory.createPacMan();
        player.setDirection(Direction.NORTH);
        level.registerPlayer(player);

        //Inky makes the move
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        assertNotNull(inky);
        Optional<Direction> inkyDirection = inky.nextAiMove();

        //Verification
        assertTrue(inkyDirection.isPresent());
        assertEquals(Direction.NORTH, inkyDirection.get());

    }

    /**
     * When Inky is alongside Blinky he should be following Blinky.
     */
    @Test
    public void alongBlinky() {
        //Create map
        List<String> map = Arrays.asList(
            "#####",
            "#I  #",
            "#   #",
            "#B  #",
            "#P  #",
            "#   #",
            "#   #",
            "#   #",
            "#   #",
            "#   #",
            "#####");
        Level level = parser.parseMap(map);
        //Create player on the Map
        PlayerFactory playerFactory = new PlayerFactory(pacManSprites);
        Player player = playerFactory.createPacMan();
        player.setDirection(Direction.SOUTH);
        level.registerPlayer(player);

        //Inky makes the move
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        assertNotNull(inky);
        Optional<Direction> inkyDirection = inky.nextAiMove();

        //Verification
        assertTrue(inkyDirection.isPresent());
        assertEquals(Direction.SOUTH, inkyDirection.get());
    }
}
