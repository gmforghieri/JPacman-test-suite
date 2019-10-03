package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.points.PointCalculator;

import java.lang.reflect.InvocationTargetException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.times;

/**
 *
 */
public class CollisionMapTest {

    private Class<? extends CollisionMap> tested;

    /**
     * @param tested the class being tested.
     */
    public CollisionMapTest(Class<? extends CollisionMap> tested) {
        this.tested = tested;
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
    public void actuallyTestCollisions(String firstEntity,
                                       String secondEntity, boolean shouldPlayerAlive,
                                       boolean shouldPointGained) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        //Create units
        Unit unit1 = getUnitFromString(firstEntity);
        Unit unit2 = getUnitFromString(secondEntity);
        //Mock point calculator
        PointCalculator pc = mock(PointCalculator.class);
        //Call player collisions
        CollisionMap map = tested.getConstructor(PointCalculator.class).newInstance(pc);
        map.collide(unit1, unit2);

        //Verify
        verifyPlayer(shouldPlayerAlive, unit1, unit2, pc);
        verifyPellet(shouldPointGained, unit1, unit2, pc);

        //Verify no more interactions
        verifyNoMoreInteractions(pc);
    }

    private void verifyPlayer(boolean shouldPlayerAlive, Unit unit1, Unit unit2,
                              PointCalculator pc) {
        //Verify player alive
        int expectAlive;
        if (shouldPlayerAlive) {
            expectAlive = 0;
        } else {
            expectAlive = 1;
        }
        if (unit1 instanceof Player) {
            Player player = (Player) unit1;
            verify(player, times(expectAlive)).setAlive(eq(false));
            verify(player, times(expectAlive)).setKiller(any());
            verifyNoMoreInteractions(player);
        }
        if (unit2 instanceof Player) {
            Player player = (Player) unit2;
            verify(player, times(expectAlive)).setAlive(eq(false));
            verify(player, times(expectAlive)).setKiller(any());
            verifyNoMoreInteractions(player);
        }
        //Verify pointcalculator interactions
        verify(pc, times(expectAlive)).collidedWithAGhost(any(), any());
    }

    private void verifyPellet(boolean shouldPointGained, Unit unit1, Unit unit2,
                              PointCalculator pc) {
        //Verify point gained
        int expectPoint;
        if (shouldPointGained) {
            expectPoint = 1;
        } else {
            expectPoint = 0;
        }
        if (unit1 instanceof Pellet) {
            Pellet pellet = (Pellet) unit1;
            verify(pellet, times(expectPoint)).leaveSquare();
            verifyNoMoreInteractions(pellet);
        }
        if (unit2 instanceof Pellet) {
            Pellet pellet = (Pellet) unit2;
            verify(pellet, times(expectPoint)).leaveSquare();
            verifyNoMoreInteractions(pellet);
        }
        //Verify pointcalculator interactions
        verify(pc, times(expectPoint)).consumedAPellet(any(), any());
    }

    private Unit getUnitFromString(String unit) {
        switch (unit) {
            case "player":
                return mock(Player.class);
            case "pellet":
                return mock(Pellet.class);
            case "ghost":
                return mock(Ghost.class);
            default:
                throw new IllegalArgumentException("Invalid unit.");
        }
    }
}
