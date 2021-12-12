package player;

import dungeon.Treasure;

/**
 * The interface for Player.
 */
public interface Player {

  /**
   * Describe the collection of the player.
   * @return  A String of a list of collections.
   */
  String collectionToString();

  /**
   * Describe the current location of the player.
   * @return  A String describing the location in the dungeon of the player
   */
  String locationToString();

  /**
   * Add a treasure to current colleciton.
   * @param treasure  The adding treasure
   */
  void collectTreasure(Treasure treasure);

  /**
   * Get the players' coordinate x.
   * @return  The x value
   */
  int getCurrenX();

  /**
   * Get the players' coordinate y.
   * @return  The y value
   */
  int getCurrenY();

  /**
   * Set the X coordinate.
   */
  void setCurrenX(int x);

  /**
   * Set the Y coordinate.
   */
  void setCurrenY(int y);

  /**
   * If the player meet a unbeaten monster, the player will be eaten by the monster.
   */
  void setEaten();

  /**
   * If the player is eaten by monster, return true, else false.
   * @return  If the player is eaten by monster, return true, else false.
   */
  boolean isEaten();

  /**
   * How many arrows the player is maintaining.
   * @return  The number of arrows of the player
   */
  int getnArrows();

  /**
   * Collect new crooked arrows.
   * @param nArrows The increment of arrows.
   */
  void setArrows(int nArrows);

  /**
   * If the player shoot an arrow, reduce the arrow by 1.
   */
  void reduceArrow();
}
