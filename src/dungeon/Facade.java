package dungeon;


import location.Direction;
import player.Player;

/**
 * This is the read-write facade interface for the model in MVC design pattern.
 */
public interface Facade extends ReadOnlyFacade {

  /**
   * Create the dungeon with params.
   *
   * @param length            The length of the grid.
   * @param width             The width of the grid
   * @param interconnectivity How many interconnectivities should be considered
   * @param percentage        What percentages of caves should have treasures. Percentage should be
   *                          larger than 20
   * @param wrap              If true, wrap this dungeon, otherwise do not
   */
  void createDungeon(int length, int width, int interconnectivity,
                            int percentage, boolean wrap);

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
  void createDungeon(int length, int width, int interconnectivity,
                            int percentage, boolean wrap, int randomSeed);

  /**
   * Add a player to the dungeon.
   * @param player  The player joining this game.
   */
  void addPlayer(Player player);

  /**
   * Which entrance the player would like to choose.
   * @param direction The direction of the entrance.
   */
  void movePlayer(Direction direction);

  /**
   * Add Otyughs to dungeon, there should be at least one monster in the dungeon.
   * @param nMonster How many monsters to add.
   */
  void addMonster(int nMonster);

  /**
   * The player shoot an arrow to a sepcific direction.
   * @param direction Where the arrow goes.
   * @param steps How far the arrow can go.
   * @return  If the arrow hit a monster return true, otherwise false.
   */
  boolean shootArrow(Direction direction, int steps);

  /**
   * If the player choose to pickup, this model will pickup all what the player found.
   * @return  A string about what the player get.
   */
  String pickUpAllStuff();

  /**
   * Recover the dungeon to its original status.
   */
  void resetDungeon();

}
