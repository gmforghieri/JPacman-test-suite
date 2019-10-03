package nl.tudelft.jpacman.level;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.InvocationTargetException;

/**
 * DefaultPlayerInteractionMap test, the implementation of the test itself,
 * is in the CollisionMapTest.
 */
class DefaultPlayerInteractionMapTest extends CollisionMapTest {

    /**
     * Dependency injection.
     */
    DefaultPlayerInteractionMapTest() {
        super(DefaultPlayerInteractionMap.class);
    }

    /**
     * @param firstEntity first entity to collide.
     * @param secondEntity second entity to collide.
     * @param shouldPlayerAlive result whether player is alive from the collision.
     * @param shouldPointGained result whether player score has increased after the collision.
     * @throws InvocationTargetException exception.
     * @throws NoSuchMethodException exception.
     * @throws InstantiationException exception.
     * @throws IllegalAccessException exception.
     */
    @ParameterizedTest
    @CsvSource({
        "player, player, true, false",
        "player, ghost, false, false",
        "player, pellet, true, true",
        "ghost, player, false, false",
        "ghost, ghost, true, false",
        "ghost, pellet, true, false",
        "pellet, player, true, true",
        "pellet, ghost, true, false",
        "pellet, pellet, true, false"
    })
    public void testPlayerCollisions(String firstEntity, String secondEntity,
                                     boolean shouldPlayerAlive, boolean shouldPointGained)
            throws InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {
        actuallyTestCollisions(firstEntity, secondEntity, shouldPlayerAlive, shouldPointGained);
    }
}
