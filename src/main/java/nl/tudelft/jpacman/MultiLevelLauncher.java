package nl.tudelft.jpacman;

import nl.tudelft.jpacman.game.Game;
import nl.tudelft.jpacman.game.GameFactory;
import nl.tudelft.jpacman.game.MultiLevelGame;
import nl.tudelft.jpacman.level.Level;

import java.io.IOException;

/**
 * Creates and launches the JPacMan UI.
 * 
 * @author Jeroen Roosen
 */
@SuppressWarnings("PMD.TooManyMethods")
public class MultiLevelLauncher extends Launcher {
    private MultiLevelGame multiGame;
    private String fileName1 = "/level1.txt";
    private String fileName2 = "/level2.txt";
    private String fileName3 = "/level3.txt";
    private String fileName4 = "/level4.txt";

    @Override
    public MultiLevelGame getGame() {
        return multiGame;
    }

    @Override
    public Game makeGame() {
        GameFactory gf = getGameFactory();
        //Parse maps
        Level[] levels;
        try {
            levels = new Level[]{
                getMapParser().parseMap(fileName1),
                getMapParser().parseMap(fileName2),
                getMapParser().parseMap(fileName3),
                getMapParser().parseMap(fileName4)
            };
        } catch (IOException e) {
            throw new PacmanConfigurationException(
                "Unable to create levels", e);
        }

        multiGame = gf.createMultiLevelGame(levels, loadPointCalculator());
        return multiGame;
    }

    /**
     * Set the name of the file containing this level's map.
     *
     * @param fileName1 Map to be used for level 1.
     * @param fileName2 Map to be used for level 2.
     * @param fileName3 Map to be used for level 3.
     * @param fileName4 Map to be used for level 4.
     * @return Level corresponding to the given map.
     */
    public Launcher withMapFiles(String fileName1, String fileName2, String fileName3,
                                 String fileName4) {
        this.fileName1 = fileName1;
        this.fileName2 = fileName2;
        this.fileName3 = fileName3;
        this.fileName4 = fileName4;
        return this;
    }

    /**
     * Main execution method for the Launcher.
     *
     * @param args
     *            The command line arguments - which are ignored.
     * @throws IOException
     *             When a resource could not be read.
     */
    public static void main(String[] args) throws IOException {
        new MultiLevelLauncher().launch();
    }
}
