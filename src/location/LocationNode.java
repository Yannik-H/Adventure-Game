package location;

import dungeon.Treasure;
import monster.Monster;

import java.util.List;


/**
 * The interface for a location in dungeon.
 */
public interface LocationNode {

  /**
   * Get the index of this node in the grid.
   * @return  the index
   */
  int getIndex();

  /**
   * Get all collected treasures.
   * @return  A list of treasures
   */
  List<Treasure> getTreasure();


  /**
   * Add a treasure to current cave.
   * @param treasure  The treasure needed to be added
   */
  void addTreasure(Treasure treasure);

  /**
   * Add a entrance to current node.
   * @param direction The direction for entrance.
   */
  void addDirection(Direction direction);

  /**
   * Get all entrance of this node.
   * @return  A list of directions
   */
  List<Direction> getDirection();

  /**
   * Toggle between tunnel and cave.
   * @return  true if this is a tunnel, otherwise false
   */
  boolean isTunnel();

  /**
   * Add a arrow to the location, the number of arrows will increase 1 when this method is called.
   */
  void addArrow();

  /**
   * Get all arrows from this location.
   * @return The number of arrows at this location.
   */
  int getNArrows();

  /**
   * This is a method used for deep copy. Distinguish it with getNArrows().
   * @return  The value of nArrows;
   */
  int copyNArrows();

  /**
   * Add a monster to the location.
   */
  void addMonster();

  /**
   * If the number of arrow equals to 0, return true, otherwise return false.
   * @return  If the number of arrow equals to 0, return true, otherwise return false.
   */
  boolean hasArrow();

  /**
   * Return the list of monsters.
   * @return  the list of monsters.
   */
  List<Monster> getLiveMonsters();

  /**
   * Describe how many arrows and what treasures here.
   * @return  A string of description.
   */
  String contentToString();

  /**
   * In {@link dungeon.DungeonImpl}, if an arrow precisely reach this position,
   * the monster at this location will be hit.
   */
  void hitOneMonster();

  /**
   * If the player pick up treasures, reset the list of treasures.
   */
  void resetTreasure();

}
