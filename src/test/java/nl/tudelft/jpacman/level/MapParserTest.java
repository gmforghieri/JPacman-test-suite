package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.PacmanConfigurationException;
import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.npc.Ghost;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.anyList;

/**
 * MapParser Test. Apology for the some of the crowded methods,
 * it's because of the 30 line checkstyle rule.
 */
class MapParserTest {
    //These are required because of checkstyle issues.
    private static final int OUR_MAP_GROUND = 4;
    private static final int OUR_MAP_WALLS = 12;
    private static final int MAP_GROUND_FROM_FILE = 245;
    private static final int MAP_WALL_FROM_FILE = 238;
    private static final int GHOSTS_FROM_FILE = 4;
    private static final int PELLETS_FROM_FILE = 178;
    private static final char[][] TEST_BOARD = new char[][] {
        {'#', '#', '#', '#'},
        {'#', 'P', '.', '#'},
        {'#', 'G', ' ', '#'},
        {'#', '#', '#', '#'}};

    /**
     * Testing a board with the given(in the MapParser Documentation) valid characters.
     */
    @Test
    public void validBoard() {
        //Setup mocks
        LevelFactory levelFactory = Mockito.mock(LevelFactory.class);
        BoardFactory boardFactory = Mockito.mock(BoardFactory.class);
        //We first expect a createBoard call
        Board fakeBoard = mock(Board.class);
        when(boardFactory.createBoard(any())).thenReturn(fakeBoard);
        //Then a createLevel call
        Level fakeLevel = mock(Level.class);
        when(levelFactory.createLevel(any(), any(), any())).thenReturn(fakeLevel);
        //Set up board creator
        doReturn(mock(Square.class)).when(boardFactory).createGround();
        doReturn(mock(Square.class)).when(boardFactory).createWall();
        //Set up level creator
        when(levelFactory.createGhost()).thenReturn(mock(Ghost.class));
        when(levelFactory.createPellet()).thenReturn(mock(Pellet.class));
        MapParser parser = new MapParser(levelFactory, boardFactory);
        Level result = parser.parseMap(TEST_BOARD);
        verify(boardFactory, times(OUR_MAP_GROUND)).createGround();
        verify(boardFactory, times(OUR_MAP_WALLS)).createWall();
        //Verify correct entity calls
        verify(levelFactory, times(1)).createGhost();
        verify(levelFactory, times(1)).createPellet();
        //Verify behaviour
        verify(boardFactory).createBoard(any());
        verify(levelFactory).createLevel(eq(fakeBoard), anyList(), anyList());
        assertSame(fakeLevel, result);
    }

    /**
     * Invalid board because of invalid characters.
     */
    @Test
    public void invalidBoard() {
        //Setup mocks
        LevelFactory levelFactory = Mockito.mock(LevelFactory.class);
        BoardFactory boardFactory = Mockito.mock(BoardFactory.class);
        //We first expect a createBoard call
        Board fakeBoard = mock(Board.class);
        when(boardFactory.createBoard(any())).thenReturn(fakeBoard);
        //Then a createLevel call
        Level fakeLevel = mock(Level.class);
        when(levelFactory.createLevel(any(), any(), any())).thenReturn(fakeLevel);
        //Set up board creator
        doReturn(mock(Square.class)).when(boardFactory).createGround();
        doReturn(mock(Square.class)).when(boardFactory).createWall();
        //Set up level creator
        when(levelFactory.createGhost()).thenReturn(mock(Ghost.class));
        when(levelFactory.createPellet()).thenReturn(mock(Pellet.class));
        //Test method
        char[][] testBoard = new char[][] {
            {'#', '#', '#', '#'},
            {'#', 'P', '.', 'K'},
            {'#', 'G', ' ', '#'},
            {'#', '#', '#', '#'}
        };
        MapParser parser = new MapParser(levelFactory, boardFactory);

        assertThrows(PacmanConfigurationException.class,
            () -> parser.parseMap(testBoard));
    }



    /**
     * Reading from an external file with valid characters.
     * @throws IOException throws exception.
     */
    @Test
    public void readFromFileCorrect() throws IOException {
        //Setup mocks
        LevelFactory levelFactory = Mockito.mock(LevelFactory.class);
        BoardFactory boardFactory = Mockito.mock(BoardFactory.class);
        //We first expect a createBoard call
        Board fakeBoard = mock(Board.class);
        when(boardFactory.createBoard(any())).thenReturn(fakeBoard);
        //Then a createLevel call
        Level fakeLevel = mock(Level.class);
        when(levelFactory.createLevel(any(), any(), any())).thenReturn(fakeLevel);
        //Set up board creator
        doReturn(mock(Square.class)).when(boardFactory).createGround();
        doReturn(mock(Square.class)).when(boardFactory).createWall();
        //Set up level creator
        when(levelFactory.createGhost()).thenReturn(mock(Ghost.class));
        when(levelFactory.createPellet()).thenReturn(mock(Pellet.class));
        //Test method
        MapParser parser = new MapParser(levelFactory, boardFactory);
        Level result = parser.parseMap("/board.txt");
        //Verify correct tile calls
        verify(boardFactory, times(MAP_GROUND_FROM_FILE)).createGround();
        verify(boardFactory, times(MAP_WALL_FROM_FILE)).createWall();
        //Verify correct entity calls
        verify(levelFactory, times(GHOSTS_FROM_FILE)).createGhost();
        verify(levelFactory, times(PELLETS_FROM_FILE)).createPellet();
        //Verify behaviour
        verify(boardFactory).createBoard(any());
        verify(levelFactory).createLevel(eq(fakeBoard), anyList(), anyList());
        assertSame(fakeLevel, result); }

    /**
     * Test for reading in from a nonexistent file.
     */
    @Test
    public void readFromNonExistentFile() {
        //Setup mocks
        LevelFactory levelFactory = Mockito.mock(LevelFactory.class);
        BoardFactory boardFactory = Mockito.mock(BoardFactory.class);

        //Test method
        MapParser parser = new MapParser(levelFactory, boardFactory);
        assertThrows(PacmanConfigurationException.class,
            () -> parser.parseMap("/doesnotexist.txt"));
    }

    /**
     * @throws IOException throws exception.
     */
    @Test
    public void readFromIOErroringStream() throws IOException {
        //Setup mocks
        LevelFactory levelFactory = Mockito.mock(LevelFactory.class);
        BoardFactory boardFactory = Mockito.mock(BoardFactory.class);

        //Setup stream
        InputStream stream = mock(InputStream.class);
        when(stream.available()).thenReturn(1); //Will return null on read, this IOErroring

        //Test method
        MapParser parser = new MapParser(levelFactory, boardFactory);
        assertThrows(IOException.class,
            () -> parser.parseMap(stream));
    }

    /**
     * Input text cannot be null.
     */
    @Test
    public void checkMapFormatNull() {
        //Setup mocks
        LevelFactory levelFactory = Mockito.mock(LevelFactory.class);
        BoardFactory boardFactory = Mockito.mock(BoardFactory.class);

        //Test method
        List<String> testBoard = null;

        MapParser parser = new MapParser(levelFactory, boardFactory);

        assertThrows(PacmanConfigurationException.class, () -> parser.parseMap(testBoard));
    }

    /**
     * Input text must consist of at least 1 row.
     */
    @Test
    public void checkMapFormatEmpty() {
        //Setup mocks
        LevelFactory levelFactory = Mockito.mock(LevelFactory.class);
        BoardFactory boardFactory = Mockito.mock(BoardFactory.class);

        //Test method
        List<String> testBoard = new ArrayList<>();

        MapParser parser = new MapParser(levelFactory, boardFactory);

        assertThrows(PacmanConfigurationException.class, () -> parser.parseMap(testBoard));
    }

    /**
     * Input text lines cannot be empty.
     */
    @Test
    public void checkMapFormatInputLineEmpty() {
        //Setup mocks
        LevelFactory levelFactory = Mockito.mock(LevelFactory.class);
        BoardFactory boardFactory = Mockito.mock(BoardFactory.class);

        //Test method
        List<String> testBoard = new ArrayList<>();
        testBoard.add("");

        MapParser parser = new MapParser(levelFactory, boardFactory);

        assertThrows(PacmanConfigurationException.class, () -> parser.parseMap(testBoard));
    }

    /**
     * Input text lines are not of equal width.
     */
    @Test
    public void checkMapFormatInputLineEqualLength() {
        //Setup mocks
        LevelFactory levelFactory = Mockito.mock(LevelFactory.class);
        BoardFactory boardFactory = Mockito.mock(BoardFactory.class);

        //Test method
        List<String> testBoard = new ArrayList<>();
        testBoard.add("#");
        testBoard.add("##");

        MapParser parser = new MapParser(levelFactory, boardFactory);

        assertThrows(PacmanConfigurationException.class, () -> parser.parseMap(testBoard));
    }

}
