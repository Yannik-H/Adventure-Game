package location;

import java.util.Comparator;

/**
 * This class represents four direction that a player can choose.
 * It has for types representing four directions.
 */
public enum Direction implements Comparator<Direction> {
  EAST("East"), SOUTH("South"), WEST("West"), NORTH("North");

  private final String direction;

  Direction(String direction) {
    this.direction = direction;
  }


  @Override
  public String toString() {
    return direction;
  }

  @Override
  public int compare(Direction o1, Direction o2) {
    return o1.toString().compareTo(o2.toString());
  }
}

