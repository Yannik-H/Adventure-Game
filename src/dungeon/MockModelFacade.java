package dungeon;

import player.Player;

/**
 * This class used to record the input from the controller.
 * It has same inputs as {@link ModelFacade}.
 */
public class MockModelFacade extends ModelFacade {

  private int length;
  private int width;
  private int interconnectivity;
  private int percentage;
  private boolean wrap;
  private int randomSeed;
  private int monster;

  /**
   * Create the dungeon with params.
   *
   * @param length            The length of the grid.
   * @param width             The width of the grid
   * @param interconnectivity How many interconnectivities should be considered
   * @param percentage        What percentages of caves should have treasures. Percentage should be
   *                          larger than 20
   * @param wrap              If true, wrap this dungeon, otherwise do not
   * @param randomSeed        The random seed
   */
  public void createDungeon(int length, int width, int interconnectivity, int percentage,
                            boolean wrap, int randomSeed) throws IllegalArgumentException {
    new DungeonImpl(length, width, interconnectivity, percentage, wrap, randomSeed);
    this.length = length;
    this.width = width;
    this.interconnectivity = interconnectivity;
    this.percentage = percentage;
    this.wrap = wrap;
    this.randomSeed = randomSeed;

  }

  public int getLength() {
    return length;
  }

  public int getWidth() {
    return width;
  }

  public int getInterconnectivity() {
    return interconnectivity;
  }

  public int getPercentage() {
    return percentage;
  }

  public boolean isWrap() {
    return wrap;
  }

  public int getRandomSeed() {
    return randomSeed;
  }

  public int getMonster() {
    return monster;
  }

  @Override
  public void addMonster(int nMonster) {
    this.monster = nMonster;
  }

  @Override
  public String showDungeon() {
    return "";
  }

  @Override
  public void addPlayer(Player player) {
    // Just to overwrite the original method to do nothing here.
    return;
  }

  @Override
  public boolean isGameOver() throws IllegalStateException {
    return false;
  }

  @Override
  public String printGameOverState() throws IllegalStateException {
    return "";
  }

  @Override
  public String printGameState() throws IllegalStateException {
    return "";
  }

  @Override
  public String pickUpAllStuff() throws IllegalStateException {
    return super.pickUpAllStuff();
  }
}
