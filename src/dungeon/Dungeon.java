package dungeon;

import location.Direction;
import location.LocationNode;
import player.Player;

/**
 * Interface represents a abstract dungeon.
 * In this project, it will be implemented by {@link DungeonImpl}.
 */
public interface Dungeon {

  /**
   * Select the start point and goal point.
   */
  void initializeGame();


  /**
   * Deside the game is over or not.
   * @return  If the game is over, return true, otherwise false.
   */
  boolean isGameOver();

  /**
   * Which entrance the player would like to choose.
   * @param direction The direction of the entrance.
   */
  void move(Direction direction) throws IllegalStateException, IllegalArgumentException;

  /**
   * Display the grid of this dungeon.
   * @return  A string for displaying purpose
   */
  String gridToString();

  /**
   * Add a player to this game.
   * @param player  The player joining this game.
   */
  void addPlayer(Player player);

  /**
   * The player pickup all treasures at current location.
   */
  void pickUpTreasures();

  /**
   * The player pickup all arrows at current location.
   */
  void pickUpArrows();

  /**
   * Print the state of the game,
   * which used for output the game state after every moves of the player.
   */
  String printGameState();

  /**
   * If the player successfully reach at the goal, return true,
   * else if the player is eaten by the monster, return false.
   * It will only be called when the game is over.
   * @return  True if the player reached the goal, false if the the player was eaten by the monster.
   */
  boolean reachGoal();

  /**
   * The player shoots an arrow toward a specific direction and steps.
   * @param direction The direction for where the arrow goes.
   * @param steps How many steps the player would like the arrow goes.
   * @return  If the arrow successfully hit a monster, return true, otherwise return false.
   */
  boolean shootArrow(Direction direction, int steps);

  /**
   * This method used for return a deep copy version of grid.
   * @return  A deep copy version of grid.
   */
  LocationNode[][] getGrid();

  /**
   * Describe the smell of the position.
   * @param x the x coordinate of the position.
   * @param y the y coordinate of the position.
   * @return  the description of the position.
   */
  String monsterSmell(int x, int y);

}
