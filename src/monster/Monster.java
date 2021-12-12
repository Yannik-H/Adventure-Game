package monster;

/**
 * This interface represents monsters in the dungeon.
 */
public interface Monster {

  /**
   * Decide the monster is dead or not. If it is dead, return true, else false.
   * @return  If it is dead, return true, else false.
   */
  boolean isDead();

  /**
   * If this monster is hit, its hitTimes plus 1.
   */
  void addHitTimes();

  /**
   * Get the hit times.
   * @return  The number of hit times.
   */
  int getHitTimes();

}
