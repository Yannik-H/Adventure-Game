import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import controller.DungeonConsoleController;
import controller.DungeonController;
import dungeon.MockModelFacade;
import dungeon.ModelFacade;

import org.junit.Test;

import java.io.StringReader;

/**
 * A JUnit test for {@link controller.DungeonController}.
 */
public class DungeonCommandTest {

  @Test
  public void testCreateDungeon() {
    // Totally correct input
    Readable input = new StringReader("3 4 20 y 5 2 Q");
    Appendable output = new StringBuilder();
    DungeonController dungeonController = new DungeonConsoleController(input, output);
    MockModelFacade test = new MockModelFacade();
    dungeonController.playGame(test);
    assertEquals(3, test.getLength());
    assertEquals(4, test.getWidth());
    assertEquals(20, test.getPercentage());
    assertTrue(test.isWrap());
    assertEquals(5, test.getInterconnectivity());
    assertEquals(10, test.getRandomSeed());
    assertEquals(2, test.getMonster());
  }

  @Test
  public void testWrongInput() {
    // Wrong input but generate the dungeon finally.
    Readable input = new StringReader("3 q 4 c 20 c y u 5 g 2 Q");
    Appendable output = new StringBuilder();
    DungeonController dungeonController = new DungeonConsoleController(input, output);
    MockModelFacade test = new MockModelFacade();
    dungeonController.playGame(test);
    assertEquals(3, test.getLength());
    assertEquals(4, test.getWidth());
    assertEquals(20, test.getPercentage());
    assertTrue(test.isWrap());
    assertEquals(5, test.getInterconnectivity());
    assertEquals(10, test.getRandomSeed());
    assertEquals(2, test.getMonster());
  }

  @Test
  public void testFailedCreatingDungeon() {
    // Can not create the dungeon with given input.
    Readable input = new StringReader("3 4 140 y 5 2 Q");
    Appendable output = new StringBuilder();
    DungeonController dungeonController = new DungeonConsoleController(input, output);
    MockModelFacade test = new MockModelFacade();
    dungeonController.playGame(test);
    String[] log = output.toString().split("\n");
    assertEquals(6, log.length);
    assertEquals("Given parameters are illegal, input again", log[5]);
  }

  @Test
  public void testPickUp() {
    // Pick up nothing or something
    Readable input = new StringReader("3 4 20 n 3 2 C P M W P Q");
    Appendable output = new StringBuilder();
    DungeonController dungeonController = new DungeonConsoleController(input, output);
    ModelFacade test = new ModelFacade();
    dungeonController.playGame(test);
    String[] log1 = output.toString().split("\n");
    assertEquals(42, log1.length);
    assertEquals("Move, Pickup, or Shoot (M-P-S)?You pick up 1 arrows ", log1[35]);
    assertEquals("Move, Pickup, or Shoot (M-P-S)?Nothing for picking up.", log1[25]);
  }

  @Test
  public void testMove() {
    // Safe and eaten
    Readable input = new StringReader("3 4 20 n 3 2 C M W Q");
    Appendable output = new StringBuilder();
    DungeonController dungeonController = new DungeonConsoleController(input, output);
    ModelFacade test = new ModelFacade();
    dungeonController.playGame(test);

    String[] logs = output.toString().split("\n");
    assertEquals(31, logs.length);
    assertEquals("Move, Pickup, or Shoot (M-P-S)?Where to?", logs[25]);
    assertEquals("You are in a cave", logs[26]);
    assertEquals("You find 1 arrows ", logs[27]);
    assertEquals("Doors lead to the [East, West, North]", logs[28]);
  }

  @Test
  public void testInvalidMove() {
    // Safe and eaten
    Readable input = new StringReader("3 4 20 y 3 2 C M W Q");
    Appendable output = new StringBuilder();
    DungeonController dungeonController = new DungeonConsoleController(input, output);
    ModelFacade test = new ModelFacade();
    dungeonController.playGame(test);

    String[] logs = output.toString().split("\n");
    assertEquals(31, logs.length);
    assertEquals("Move, Pickup, or Shoot (M-P-S)?Where to?"
            + "There is no door leads to West", logs[25]);

  }

  @Test
  public void testShoot() {
    // Out of arrow or shoot on nothing or shoot on a monster.
    Readable input = new StringReader("3 4 20 n 3 2 C M N S 1 N S 2 N Q");
    Appendable output = new StringBuilder();
    DungeonController dungeonController = new DungeonConsoleController(input, output);
    ModelFacade test = new ModelFacade();
    dungeonController.playGame(test);

    String[] logs = output.toString().split("\n");
    assertEquals(46, logs.length);
    assertEquals("Move, Pickup, or Shoot (M-P-S)?"
            + "No. of caves (1-5)?Where to?You shoot an arrow into the darkness", logs[31]);
    assertEquals("Move, Pickup, or Shoot (M-P-S)?"
            + "No. of caves (1-5)?Where to?You hear a great howl in the distance", logs[38]);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullModel() {
    Readable input = new StringReader("3 4 20 y 3 2 C M W Q");
    Appendable output = new StringBuilder();
    DungeonController dungeonController = new DungeonConsoleController(input, output);
    dungeonController.playGame(null);
  }

  @Test
  public void testGameOver() {
    // Eaten or reach at the goal
    Readable input = new StringReader("3 4 20 n 3 2 C P M W P M W M N M N M S M E M E M N S 1 "
            + "N S 1 N M N M S M W S 2 N S 2 N M N M W");
    Appendable output = new StringBuilder();
    DungeonController dungeonController = new DungeonConsoleController(input, output);
    ModelFacade test = new ModelFacade();
    dungeonController.playGame(test);

    String[] logs = output.toString().split("\n");
    String out = output.toString();
    assertEquals(126, logs.length);
    assertEquals("Congratulations! You reach at the goal point!", logs[125]);

    Readable input1 = new StringReader("3 4 20 n 3 2 C M W P M E M N M N P M N");
    Appendable output1 = new StringBuilder();
    DungeonController dungeonController1 = new DungeonConsoleController(input1, output1);
    ModelFacade test1 = new ModelFacade();
    dungeonController1.playGame(test1);

    String[] logs1 = output1.toString().split("\n");
    assertEquals(62, logs1.length);
    assertEquals("Chomp, chomp, chomp, you are eaten by an Otyugh!", logs1[60]);
  }


}