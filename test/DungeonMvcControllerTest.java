import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import controller.DungeonMvcController;
import dungeon.MockModelFacade;
import dungeon.ModelFacade;
import location.Direction;
import org.junit.Before;
import org.junit.Test;

import location.LocationNode;
import viewer.MockView;


/**
 * A JUnit test for {@link controller.DungeonMvcController}.
 */
public class DungeonMvcControllerTest {

  private StringBuilder log;
  private MockModelFacade test;
  private DungeonMvcController dungeonController;

  @Before
  public void setUp() {
    log = new StringBuilder();
    test = new MockModelFacade();
    MockView mockView = new MockView(log);
    dungeonController = new DungeonMvcController(mockView);
    dungeonController.playGame(test);
  }

  @Test
  public void testDungeonSetting() {
    dungeonController.receiveParams("3", "4", "5", "20", "y", "2");
    assertEquals(3, test.getLength());
    assertEquals(4, test.getWidth());
    assertEquals(20, test.getPercentage());
    assertTrue(test.isWrap());
    assertEquals(5, test.getInterconnectivity());
    assertEquals(10, test.getRandomSeed());
    assertEquals(2, test.getMonster());
  }

  @Test
  public void testIllegalDungeonSetting() {
    dungeonController.receiveParams("3", "a", "5", "20", "y", "2");
    assertEquals("Invalid settings", log.toString());

  }

  @Test
  public void testWrongSizeDungeonSetting() {
    dungeonController.receiveParams("3", "3", "5", "20", "y", "2");
    assertEquals("Can not instantiate dungeon with given settings.", log.toString());
  }

  @Test
  public void testPickUp() {
    ModelFacade modelFacade = new ModelFacade();
    dungeonController.playGame(modelFacade);
    dungeonController.receiveParams("3", "4", "4", "20", "n", "2");
    dungeonController.pickPerformer();
    assertEquals("You pick up 1 arrows \n\n\n", log.toString());
  }

  @Test
  public void testMove() {
    ModelFacade modelFacade = new ModelFacade();
    dungeonController.playGame(modelFacade);
    dungeonController.receiveParams("3", "4", "3", "20", "y", "2");
    assertEquals(1, modelFacade.getPlayerX());
    assertEquals(3, modelFacade.getPlayerY());
    dungeonController.move(Direction.EAST);
    dungeonController.move(10);
    dungeonController.move(3);
    dungeonController.move(Direction.EAST);
    assertEquals(0, modelFacade.getPlayerX());
    assertEquals(3, modelFacade.getPlayerY());

    assertEquals("\n\nThere is no door leads to East\n\n", log.toString());
  }

  @Test
  public void testShoot() {
    ModelFacade modelFacade = new ModelFacade();
    dungeonController.playGame(modelFacade);
    dungeonController.receiveParams("3", "4", "3", "20", "y", "2");
    assertEquals(1, modelFacade.getPlayerX());
    assertEquals(3, modelFacade.getPlayerY());
    dungeonController.move(Direction.EAST);
    dungeonController.move(10);
    dungeonController.move(Direction.NORTH);
    dungeonController.move(Direction.NORTH);
    dungeonController.move(Direction.EAST);
    dungeonController.shootPerformer(Direction.NORTH, 1);
    dungeonController.shootPerformer(Direction.WEST, 1);
    dungeonController.shootPerformer(Direction.NORTH, 1);
    String[] test = log.toString().split("\n");
    assertEquals("You hear a great howl in the distance", test[5]);
    assertEquals("You shoot an arrow into the darkness", test[7]);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullView() {
    new DungeonMvcController(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullModel() {
    DungeonMvcController test = new DungeonMvcController(new MockView(null));
    test.playGame(null);
  }

  @Test
  public void testRestart() {
    ModelFacade modelFacade = new ModelFacade();
    dungeonController.playGame(modelFacade);
    dungeonController.receiveParams("3", "4", "3", "20", "y", "2");
    LocationNode[][] originalGrid = modelFacade.getGrid();
    assertEquals(1, originalGrid[3][2].copyNArrows());
    assertEquals(1, modelFacade.getPlayerX());
    assertEquals(3, modelFacade.getPlayerY());
    dungeonController.move(Direction.EAST);
    assertEquals(2, modelFacade.getPlayerX());
    assertEquals(3, modelFacade.getPlayerY());
    dungeonController.pickPerformer();
    LocationNode[][] changedGrid = modelFacade.getGrid();
    assertEquals(0, changedGrid[3][2].copyNArrows());
    dungeonController.reStartGame();
    LocationNode[][] recoverGrid = modelFacade.getGrid();
    assertEquals(1, recoverGrid[3][2].copyNArrows());
    assertEquals(1, modelFacade.getPlayerX());
    assertEquals(3, modelFacade.getPlayerY());
  }

  @Test(expected = IllegalStateException.class)
  public void testReset() {
    ModelFacade modelFacade = new ModelFacade();
    dungeonController.playGame(modelFacade);
    dungeonController.receiveParams("3", "4", "3", "20", "y", "2");
    assertNotNull(modelFacade.getGrid());
    dungeonController.resetPerformer();
    dungeonController.describePlayer();
  }

  @Test
  public void testDescribe() {
    ModelFacade modelFacade = new ModelFacade();
    dungeonController.playGame(modelFacade);
    dungeonController.receiveParams("3", "4", "3", "20", "y", "2");
    dungeonController.describePlayer();
    String[] test = log.toString().split("\n");
    assertEquals("You are in a tunnel", test[0]);
    assertEquals("Doors lead to the [East, North]", test[1]);
  }

}