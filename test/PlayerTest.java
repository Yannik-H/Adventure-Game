import static org.junit.Assert.assertEquals;

import dungeon.Treasure;
import org.junit.Test;
import player.Player;
import player.PlayerImpl;


/**
 * A JUnit test for {@link player.Player}.
 */
public class PlayerTest {

  @Test
  public void testConstructor() {
    Player player = new PlayerImpl();
    assertEquals("X: -1, Y: -1", player.locationToString());
    assertEquals("[]", player.collectionToString());
  }

  @Test
  public void testAddTreasure() {
    Player player = new PlayerImpl();
    assertEquals("[]", player.collectionToString());
    player.collectTreasure(Treasure.DIAMOND);
    player.collectTreasure(Treasure.SAPPHIRE);
    player.collectTreasure(Treasure.RUBY);
    player.collectTreasure(Treasure.RUBY);
    assertEquals("[Diamond, Sapphire, Ruby, Ruby]", player.collectionToString());
  }

}