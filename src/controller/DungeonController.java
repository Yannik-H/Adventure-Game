package controller;

import dungeon.Facade;
import location.Direction;

/**
 * Represents a Controller for dungeon: handle user moves by executing them using the model;
 * convey move outcomes to the user in some form.
 */
public interface DungeonController {

  /**
   * Execute a single game of dungeon given a dungeon facade.
   * When the game is over or the user input "q" or "Q", the playGame method ends.
   *
   * @param m a non-null tic tac toe Model
   */
  void playGame(Facade m);

  /**
   * Start the setting of the game.
   */
  void startSetting();

  /**
   * restart playing the game.
   */
  void reStartGame();

  /**
   * This method used for accept params from the view.
   * @param length            The length of the grid.
   * @param width             The width of the grid
   * @param interconnectivity How many interconnectivities should be considered
   * @param percentage        What percentages of caves should have treasures. Percentage should be
   *                          larger than 20
   * @param wrap              If true, wrap this dungeon, otherwise do not
   * @param nMonsters         How many monsters to add
   */
  void receiveParams(String length, String width, String interconnectivity,
                     String percentage, String wrap, String nMonsters);

  /**
   * Move action after a specific unit is clicked.
   * @param index The index of the clicked unit.
   */
  void move(int index);

  /**
   * Move action after a specific key is pressed.
   * @param direction The direction to move.
   */
  void move(Direction direction);

  /**
   * When the user press "p", this action performer will have the player pick up something.
   */
  void pickPerformer();

  /**
   * When the user press "s" and direction key, this performer will have the player shoot an arrow.
   */
  void shootPerformer(Direction direction, int steps);

  /**
   * Wait for the input direction for shooting after "s" is pressed.
   */
  void waitDirection();

  /**
   * When the reset button is clicked, perform reset actions.
   */
  void resetPerformer();

  void describePlayer();
}
