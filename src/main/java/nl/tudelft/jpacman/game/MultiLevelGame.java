package nl.tudelft.jpacman.game;

import com.google.common.collect.ImmutableList;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.points.PointCalculator;

import java.util.Arrays;
import java.util.List;

/**
 * A basic implementation of a Pac-Man game.
 *
 * @author Jeroen Roosen 
 */
public class MultiLevelGame extends Game {

    /**
     * The player of this game.
     */
    private final Player player;

    /**
     * The level of this game.
     */
    private final Level[] levels;
    private int counter = 0;

    private boolean isGameOver = false;

    private final int levelCount = 4;

    /**
     * Create a new single player game for the provided level and player.
     *
     * @param player
     *            The player.
     * @param levels
     *            The level.
     * @param pointCalculator
     *            The way to calculate points upon collisions.
     */
    protected MultiLevelGame(Player player, Level[] levels, PointCalculator pointCalculator) {
        super(pointCalculator);

        assert player != null;
        assert levels != null;
        assert levels.length == levelCount;

        this.player = player;
        this.levels = Arrays.copyOf(levels, levels.length);
        levels[0].registerPlayer(player);
    }

    @Override
    public List<Player> getPlayers() {
        return ImmutableList.of(player);
    }

    @Override
    public Level getLevel() {
        return levels[Math.min(counter, levelCount - 1)];
    }

    @Override
    public void start() {
        //If in progress, don't do anything
        if (!isGameOver) {
            super.start();
            return;
        }
        //If counter >= 4, don't do anything
        if (counter >= levelCount - 1) {
            return;
        }
        //Start level
        counter++;
        getLevel().registerPlayer(player);
        isGameOver = false;
        super.start();
    }

    @Override
    public void levelWon() {
        stop();
        isGameOver = true;
    }

    @Override
    public void levelLost() {
        stop();
        counter = Integer.MAX_VALUE;
        isGameOver = true;
    }

}
