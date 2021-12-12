import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import location.Direction;
import location.LocationNode;
import location.TunnelOrCave;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * A JUnit test for {@link LocationNode}.
 */
public class LocationNodeTest {

  private LocationNode locationNode;

  @Before
  public void setUp() {
    locationNode = new TunnelOrCave(1);
  }

  @Test
  public void testAddEntrance() {
    locationNode.addDirection(Direction.EAST);
    locationNode.addDirection(Direction.SOUTH);
    locationNode.addDirection(Direction.WEST);
    locationNode.addDirection(Direction.NORTH);
    Set<Direction> directionSet = new HashSet<>();
    directionSet.add(Direction.EAST);
    directionSet.add(Direction.SOUTH);
    directionSet.add(Direction.WEST);
    directionSet.add(Direction.NORTH);
    assertTrue(directionSet.equals(new HashSet<>(locationNode.getDirection())));
  }

  @Test
  public void testIsTunnel() {
    locationNode.addDirection(Direction.SOUTH);
    assertFalse(locationNode.isTunnel());
    locationNode.addDirection(Direction.NORTH);
    assertTrue(locationNode.isTunnel());
    locationNode.addDirection(Direction.EAST);
    assertFalse(locationNode.isTunnel());
    locationNode.addDirection(Direction.WEST);
    assertFalse(locationNode.isTunnel());
  }



}