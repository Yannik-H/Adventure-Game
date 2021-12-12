package dungeon;


import location.Direction;
import location.LocationNode;
import player.Player;

/**
 * This class work as a facade connecting model and controller in MCV design pattern.
 * It has a dungeon as its private field. This is a read-write model facade
 */
public class ModelFacade implements Facade {

  private DungeonImpl dungeon;
  private Player player;

  @Override
  public void createDungeon(int length, int width, int interconnectivity,
                            int percentage, boolean wrap) throws IllegalArgumentException {
    this.dungeon = new DungeonImpl(length, width, interconnectivity, percentage, wrap);
    this.dungeon.initializeGame();
    player = null;
  }

  @Override
  public void createDungeon(int length, int width, int interconnectivity, int percentage,
                            boolean wrap, int randomSeed)throws IllegalArgumentException {
    this.dungeon = new DungeonImpl(length, width, interconnectivity, percentage, wrap, randomSeed);
    this.dungeon.initializeGame();
  }

  @Override
  public void addPlayer(Player player) {
    dungeon.addPlayer(player);
    this.player = player;
  }

  @Override
  public String describePlayerLocation() {
    return dungeon.printGameState();
  }

  @Override
  public void movePlayer(Direction direction)
          throws IllegalArgumentException, IllegalStateException {
    if (player == null) {
      throw new IllegalStateException("Please add a player first");
    }
    dungeon.move(direction);
  }

  @Override
  public String showDungeon() {
    System.out.println(String.format("Start:(%d, %d); Goal:(%d, %d)",
            dungeon.getStartX(), dungeon.getStartY(), dungeon.getGoalX(), dungeon.getGoalY()));
    return dungeon.gridToString();
  }

  @Override
  public String printGameState() throws IllegalStateException {
    if (player == null) {
      throw new IllegalStateException("Please add a player first");
    }
    return dungeon.printGameState();
  }

  @Override
  public String printGameOverState() throws IllegalStateException {
    if (player == null) {
      throw new IllegalStateException("Please add a player first");
    }
    if (dungeon.getPlayer().isEaten()) {
      return "Chomp, chomp, chomp, you are eaten by an Otyugh!\n"
              + "Better luck next time";
    } else {
      return "Congratulations! You reach at the goal point!";
    }
  }

  @Override
  public void addMonster(int nMonster) {
    dungeon.addMonsters(nMonster);
  }

  @Override
  public LocationNode[][] getGrid() {
    if (dungeon == null) {
      return null;
    }
    return dungeon.getGrid();
  }

  @Override
  public boolean shootArrow(Direction direction, int steps)
          throws IllegalArgumentException,IllegalStateException {
    if (player == null) {
      throw new IllegalStateException("Please add a player first");
    }
    return  dungeon.shootArrow(direction, steps);
  }

  @Override
  public boolean isGameOver() throws IllegalStateException {
    if (player == null) {
      throw new IllegalStateException("Please add a player first");
    }
    return dungeon.isGameOver();
  }

  @Override
  public boolean reachGoal() throws IllegalStateException {
    if (player == null) {
      throw new IllegalStateException("Please add a player first");
    }
    return dungeon.reachGoal();
  }

  @Override
  public boolean isOutOfArrow() throws IllegalStateException {
    if (player == null) {
      throw new IllegalStateException("Please add a player first");
    }
    return dungeon.getPlayer().getnArrows() == 0;
  }

  @Override
  public String pickUpAllStuff() throws IllegalStateException {
    LocationNode[][] grid = dungeon.getGrid();
    String content = grid[player.getCurrenY()][player.getCurrenX()].contentToString();
    dungeon.pickUpArrows();
    dungeon.pickUpTreasures();
    if (content.equals("")) {
      return "Nothing for picking up.";
    }
    return "You pick up " + content;
  }

  @Override
  public void resetDungeon() {
    dungeon.resetGrid();
  }

  @Override
  public int getPlayerX() {
    if (player == null) {
      throw new IllegalStateException();
    }
    return player.getCurrenX();
  }

  @Override
  public int getPlayerY() {
    if (player == null) {
      throw new IllegalStateException();
    }
    return player.getCurrenY();
  }

  @Override
  public String positionSmell(int x, int y) {
    return dungeon.monsterSmell(x, y);
  }
}