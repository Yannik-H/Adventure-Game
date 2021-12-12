import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import dungeon.Dungeon;
import dungeon.DungeonImpl;
import dungeon.ModelFacade;
import location.Direction;
import location.LocationNode;
import monster.Monster;
import org.junit.Before;
import org.junit.Test;
import player.Player;
import player.PlayerImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A JUnit test for {@link DungeonImpl}.
 */
public class DungeonImplTest {

  Dungeon unwrapDungeon;
  Dungeon wrapDungeon;

  @Before
  public void setUp() {
    wrapDungeon = new DungeonImpl(3, 4, 2, 20, true, 10);
    unwrapDungeon = new DungeonImpl(3, 4, 3, 20, false, 10);
  }

  @Test
  public void testConstructor() {
    assertEquals(
            "                     \n"
                    + " (0,0)--(1,0)  (2,0) \n"
                    + "          |      |   \n"
                    + "          |      |   \n"
                    + " (0,1)  (1,1)--(2,1) \n"
                    + "   |      |      |   \n"
                    + "   |      |      |   \n"
                    + " (0,2)--(1,2)--(2,2) \n"
                    + "   |      |      |   \n"
                    + "   |      |      |   \n"
                    + " (0,3)--(1,3)--(2,3) \n"
                    + "                     \n", unwrapDungeon.gridToString());
    assertEquals(
            "   |             |   \n"
                    + " (0,0)  (1,0)  (2,0) \n"
                    + "          |      |   \n"
                    + "          |      |   \n"
                    + "-(0,1)--(1,1)--(2,1)-\n"
                    + "   |             |   \n"
                    + "   |             |   \n"
                    + " (0,2)  (1,2)  (2,2) \n"
                    + "   |      |          \n"
                    + "   |      |          \n"
                    + "-(0,3)  (1,3)--(2,3)-\n"
                    + "   |             |   \n", wrapDungeon.gridToString());
  }

  @Test(expected =  IllegalArgumentException.class)
  public void testIllegalDungeonSize() {
    new DungeonImpl(3, 3, 0, 0, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalWrapInterconnecty() {
    new DungeonImpl(3, 4, 25, 0, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalInterconnecty() {
    new DungeonImpl(3, 4, 20, 0, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testINegativeInput() {
    new DungeonImpl(-3, 4, 0, 0, false);
    new DungeonImpl(3, -4, 0, 0, false);
    new DungeonImpl(3, 4, -1, 0, false);
    new DungeonImpl(3, 4, 0, -2, false);
  }

  @Test
  public void testAddTreasure() {
    wrapDungeon.initializeGame();
    Set<Integer> indexSet = new HashSet<>();
    indexSet.add(2);
    indexSet.add(5);
    indexSet.add(6);
    indexSet.add(8);
    indexSet.add(9);
    LocationNode[][] grid = wrapDungeon.getGrid();
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[0].length; j++) {
        if (grid[i][j].getTreasure().size() == 0) {
          assertFalse(indexSet.contains(grid[i][j].getIndex()));
        } else {
          assertTrue(indexSet.contains(grid[i][j].getIndex()));
        }
      }
    }
  }

  @Test
  public void testAddMonster() {
    ModelFacade facade = new ModelFacade();
    facade.createDungeon(3, 4, 3, 20, false, 10);
    facade.addMonster(2);
    LocationNode[][] grid = facade.getGrid();
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[0].length; j++) {
        if (grid[i][j].getIndex() == 1 | grid[i][j].getIndex() == 3) {
          assertEquals(1, grid[i][j].getLiveMonsters().size());
        } else {
          assertEquals(0, grid[i][j].getLiveMonsters().size());
        }
      }
    }
  }

  @Test
  public void testMonsterAtGoal() {
    ModelFacade facade = new ModelFacade();
    facade.createDungeon(3, 4, 3, 20, false, 10);
    facade.addMonster(2);
    LocationNode[][] grid = facade.getGrid();
    assertEquals(1, grid[0][0].getLiveMonsters().size());
  }

  @Test
  public void testNoMonsterAtStart() {
    for (int i = 0; i < 100; i++) {

      ModelFacade facade = new ModelFacade();
      facade.createDungeon(3, 4, 3, 20, false, i);
      facade.addMonster(3);
      Player tmp = new PlayerImpl();
      facade.addPlayer(tmp);
      assertEquals(0,
              facade.getGrid()[tmp.getCurrenY()][tmp.getCurrenX()].getLiveMonsters().size());
    }

  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalAddMonster() {
    ModelFacade facade = new ModelFacade();
    facade.createDungeon(3, 4, 3, 20, false, 10);
    facade.addMonster(0);
    facade.addMonster(-1);
  }

  @Test
  public void testAddArrow() {
    unwrapDungeon.initializeGame();
    LocationNode[][] grid = unwrapDungeon.getGrid();
    double arrowLocation = 0;
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[0].length; j++) {
        if (grid[i][j].getNArrows() > 0) {
          arrowLocation += 1;
        }
      }
    }
    assertTrue((arrowLocation / 12) * 100 >= 20);
  }

  @Test
  public void testPickUpArrow() {
    Dungeon dungeon = new DungeonImpl(3, 4, 2, 40, true, 10);
    dungeon.initializeGame();
    Player player = new PlayerImpl();
    dungeon.addPlayer(player);
    assertEquals(3, player.getnArrows());
    dungeon.pickUpArrows();
    assertEquals(4, player.getnArrows());
  }

  @Test
  public void testInitializeGame() {
    unwrapDungeon.initializeGame();
    Player player1 = new PlayerImpl();
    unwrapDungeon.addPlayer(player1);

    assertEquals(2, player1.getCurrenX());
    assertEquals(3, player1.getCurrenY());

    wrapDungeon.initializeGame();
    Player player2 = new PlayerImpl();
    wrapDungeon.addPlayer(player2);
    assertEquals(0, player2.getCurrenX());
    assertEquals(0, player2.getCurrenY());
  }

  @Test
  public void testMove() {
    Player mockPlayer = new PlayerImpl();
    ModelFacade facade = new ModelFacade();
    facade.createDungeon(3, 4, 3, 20, false, 10);
    facade.addMonster(2);
    facade.addPlayer(mockPlayer);
    assertEquals(2, mockPlayer.getCurrenX());
    assertEquals(3, mockPlayer.getCurrenY());
    facade.movePlayer(Direction.WEST);
    assertEquals(1, mockPlayer.getCurrenX());
    assertEquals(3, mockPlayer.getCurrenY());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveIllegalDirection() {
    Player mockPlayer = new PlayerImpl();
    unwrapDungeon.initializeGame();
    unwrapDungeon.addPlayer(mockPlayer);
    unwrapDungeon.move(Direction.EAST);
  }

  @Test
  public void testIsGameOver() {
    Player mockPlayer = new PlayerImpl();
    ModelFacade facade = new ModelFacade();
    facade.createDungeon(3, 4, 3, 20, false, 10);
    facade.addMonster(2);
    facade.addPlayer(mockPlayer);
    facade.movePlayer(Direction.NORTH);
    facade.movePlayer(Direction.NORTH);
    facade.movePlayer(Direction.WEST);
    facade.shootArrow(Direction.NORTH, 2);
    facade.shootArrow(Direction.NORTH, 2);
    facade.movePlayer(Direction.NORTH);
    facade.movePlayer(Direction.WEST);
    assertTrue(facade.isGameOver());
    assertTrue(facade.reachGoal());
  }

  @Test
  public void testEaten() {
    Player mockPlayer = new PlayerImpl();
    ModelFacade facade = new ModelFacade();
    facade.createDungeon(3, 4, 3, 20, false, 10);
    facade.addMonster(2);
    facade.addPlayer(mockPlayer);
    assertFalse(mockPlayer.isEaten());
    assertFalse(facade.isGameOver());
    facade.movePlayer(Direction.NORTH);
    facade.movePlayer(Direction.NORTH);
    facade.movePlayer(Direction.NORTH);
    assertTrue(mockPlayer.isEaten());
    assertTrue(facade.isGameOver());

    ModelFacade facade2 = new ModelFacade();
    facade2.createDungeon(3, 4, 3, 20, false, 10);
    facade2.addMonster(2);
    double eatCount = 0.0;
    Player mockPlayer2;
    mockPlayer2 = new PlayerImpl();
    facade2.addPlayer(mockPlayer2);
    facade2.movePlayer(Direction.NORTH);
    facade2.movePlayer(Direction.NORTH);
    facade2.shootArrow(Direction.NORTH, 1);
    facade2.movePlayer(Direction.NORTH);
    if (mockPlayer2.isEaten()) {
      eatCount += 1;
    }
    for (int i = 0; i < 100000; i++) {
      mockPlayer2 = new PlayerImpl();
      facade2.addPlayer(mockPlayer2);
      facade2.movePlayer(Direction.NORTH);
      facade2.movePlayer(Direction.NORTH);
      facade2.movePlayer(Direction.NORTH);
      if (mockPlayer2.isEaten()) {
        eatCount += 1;
      }
    }

    assertTrue(Math.abs((eatCount / 100000) - 0.5) < 0.01);

  }

  @Test(expected = IllegalStateException.class)
  public void testMoveAfterGameOver() {
    Player mockPlayer = new PlayerImpl();
    unwrapDungeon.initializeGame();
    unwrapDungeon.addPlayer(mockPlayer);
    assertFalse(unwrapDungeon.isGameOver());
    unwrapDungeon.move(Direction.NORTH);
    assertFalse(unwrapDungeon.isGameOver());
    unwrapDungeon.move(Direction.NORTH);
    assertFalse(unwrapDungeon.isGameOver());
    unwrapDungeon.move(Direction.WEST);
    assertFalse(unwrapDungeon.isGameOver());
    unwrapDungeon.move(Direction.NORTH);
    assertFalse(unwrapDungeon.isGameOver());
    unwrapDungeon.move(Direction.WEST);
    assertTrue(unwrapDungeon.isGameOver());
    unwrapDungeon.move(Direction.NORTH);
  }



  @Test
  public void testDescription() {
    Player mockPlayer = new PlayerImpl();
    ModelFacade facade = new ModelFacade();
    facade.createDungeon(3, 4, 3, 20, false, 10);
    facade.addMonster(2);
    facade.addPlayer(mockPlayer);
    facade.movePlayer(Direction.NORTH);
    assertEquals("You smell something terrible nearby\n"
            + "You are in a cave\n"
            + "You find [Sapphire]\n"
            + "Doors lead to the [South, West, North]\n", facade.describePlayerLocation());
    facade.movePlayer(Direction.NORTH);

    assertEquals("You smell something even more terrible nearby\n"
            + "You are in a cave\n"
            + "You find [Diamond]\n"
            + "Doors lead to the [South, West, North]\n", facade.describePlayerLocation());
  }

  @Test
  public void testShootArrow() {
    Player mockPlayer = new PlayerImpl();
    ModelFacade facade = new ModelFacade();
    facade.createDungeon(3, 4, 3, 20, false, 10);
    facade.addMonster(2);
    facade.addPlayer(mockPlayer);
    assertEquals(3, mockPlayer.getnArrows());
    assertFalse(facade.shootArrow(Direction.WEST, 2));
    assertEquals(2, mockPlayer.getnArrows());
    facade.movePlayer(Direction.NORTH);
    assertTrue(facade.shootArrow(Direction.NORTH, 2));
    LocationNode[][] grid = facade.getGrid();
    List<Monster> monsters = grid[0][2].getLiveMonsters();
    assertEquals(1, monsters.get(0).getHitTimes());
    assertEquals(1, mockPlayer.getnArrows());
  }

  @Test
  public void testCrookedArrow() {
    Player mockPlayer = new PlayerImpl();
    ModelFacade facade = new ModelFacade();
    facade.createDungeon(3, 4, 3, 20, false, 10);
    facade.addMonster(2);
    facade.addPlayer(mockPlayer);
    assertEquals(2, mockPlayer.getCurrenX());
    assertEquals(3, mockPlayer.getCurrenY());
    LocationNode[][] original = facade.getGrid();
    assertEquals(1, original[0][0].getLiveMonsters().size());
    assertEquals(1, original[0][2].getLiveMonsters().size());
    facade.movePlayer(Direction.NORTH);
    facade.movePlayer(Direction.NORTH);
    facade.movePlayer(Direction.WEST);
    facade.shootArrow(Direction.NORTH, 2);
    facade.shootArrow(Direction.NORTH, 2);
    LocationNode[][] after = facade.getGrid();
    assertEquals(0, after[0][0].getLiveMonsters().size());
  }

  @Test(expected = IllegalStateException.class)
  public void testShootNoArrow() {
    Player mockPlayer = new PlayerImpl();
    ModelFacade facade = new ModelFacade();
    facade.createDungeon(3, 4, 3, 20, false, 10);
    facade.addPlayer(mockPlayer);
    facade.shootArrow(Direction.WEST,1);
    facade.shootArrow(Direction.WEST,1);
    facade.shootArrow(Direction.WEST,1);
    facade.shootArrow(Direction.WEST,1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIZeroSteps() {
    Player mockPlayer = new PlayerImpl();
    ModelFacade facade = new ModelFacade();
    facade.createDungeon(3, 4, 3, 20, false, 10);
    facade.addMonster(2);
    facade.addPlayer(mockPlayer);
    facade.shootArrow(Direction.WEST, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeSteps() {
    Player mockPlayer = new PlayerImpl();
    ModelFacade facade = new ModelFacade();
    facade.createDungeon(3, 4, 3, 20, false, 10);
    facade.addMonster(2);
    facade.addPlayer(mockPlayer);
    facade.shootArrow(Direction.WEST, -1);
  }


}