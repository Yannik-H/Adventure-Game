package dungeon;


import location.LocationNode;

/**
 * This is the read-only model facade interface,
 * this facade can only retrieve information from the model,
 * and can not make any modification on the model.
 */
public interface ReadOnlyFacade {

  /**
   * Get the deep copy of the grid of the dungeon.
   * @return  The grid of the dungeon.
   */
  LocationNode[][] getGrid();

  /**
   * Describe the location of the player,
   * also provide information about the cave/tunnel that player in.
   * @return  The description about the players' location.
   */
  String describePlayerLocation();

  /**
   * Display the structure of the dungeon.
   * @return  A String showing dungeon.
   */
  String showDungeon();

  /**
   * Print the state of the game.
   * @return  A string describing the game state.
   */
  String printGameState();

  /**
   * Print the state when the game is over.
   * @return  A string describing the game result.
   */
  String printGameOverState();

  /**
   * If the player reach at the goal or eaten by monsters, return true, else false.
   * @return  If the game is over, return true, else false.
   */
  boolean isGameOver();

  /**
   * If the player reach at the goal, return true, else false.
   * @return  If the player reach at the goal, return true, else false.
   */
  boolean reachGoal();

  /**
   * If the player is out of arrows, return true, else false.
   * @return If the player is out of arrows, return true, else false.
   */
  boolean isOutOfArrow();

  /**
   * Return the x coordinate of the player.
   * @return the x coordinate of the player.
   */
  int getPlayerX();

  /**
   * Return the y coordinate of the player.
   * @return the y coordinate of the player.
   */
  int getPlayerY();

  /**
   * Describe the smell of the position.
   * @param x the x coordinate of the position.
   * @param y the y coordinate of the position.
   * @return  the description of the position.
   */
  String positionSmell(int x, int y);


}
