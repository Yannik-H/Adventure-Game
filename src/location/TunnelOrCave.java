package location;

import dungeon.Treasure;
import monster.Monster;
import monster.Otyugh;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class implements its interface {@link LocationNode}.
 * A tunnel has 0 treasure and cave can has more than one treasures.
 * The directions represents which direction can player go from this tunnel/cave.
 */
public class TunnelOrCave implements LocationNode {

  private int index;
  private List<Treasure> treasure;
  private Set<Direction> directions;
  private int nArrows;
  private List<Monster> monsters;

  /**
   * Construct caves and record its index in the grid.
   * @param index The unique identification for this cave in the grid.
   */
  public TunnelOrCave(int index) throws IllegalArgumentException {
    if (index <= 0) {
      throw new IllegalArgumentException("The index of the node should be positive");
    }
    this.index = index;
    this.directions = new HashSet<>();
    treasure = new ArrayList<>();
    nArrows = 0;
    monsters = new ArrayList<>();
  }

  /**
   * This constructor used for deep copy.
   * @param tunnelOrCave  The object needed to be copied.
   */
  public TunnelOrCave(TunnelOrCave tunnelOrCave) {
    this.index = tunnelOrCave.getIndex();
    this.treasure = tunnelOrCave.getTreasure();
    this.directions = new HashSet<>(tunnelOrCave.getDirection());
    this.nArrows = tunnelOrCave.copyNArrows();
    this.monsters = tunnelOrCave.getLiveMonsters();
  }

  @Override
  public int getIndex() {
    return index;
  }

  @Override
  public List<Treasure> getTreasure() {
    return new ArrayList<>(treasure);
  }


  @Override
  public void addTreasure(Treasure treasure) {
    this.treasure.add(treasure);
  }

  @Override
  public void addDirection(Direction direction) {
    directions.add(direction);
  }

  @Override
  public List<Direction> getDirection() {
    return new ArrayList<>(directions);
  }

  @Override
  public boolean isTunnel() {
    return directions.size() == 2;
  }

  @Override
  public void addArrow() {
    nArrows += 1;
  }

  @Override
  public void addMonster() {
    monsters.add(new Otyugh());
  }

  @Override
  public List<Monster> getLiveMonsters() {
    List<Monster> copy = new ArrayList<>();
    for (Monster monster : monsters) {
      if (!monster.isDead()) {
        copy.add(new Otyugh((Otyugh) monster));
      }
    }
    return copy;
  }

  @Override
  public String contentToString() {
    String arrowStr = nArrows == 0 ? "" : String.format("%d arrows ", nArrows);
    String treasuresString = treasure.size() == 0 ? "" : treasure.toString();
    if (arrowStr.equals("") & treasuresString.equals("")) {
      return "";
    } else {
      if (treasuresString.equals("")) {
        return arrowStr + "\n";
      } else if (arrowStr.equals("")) {
        return treasuresString + "\n";
      }
      return arrowStr + "and " + treasuresString + "\n";
    }
  }

  @Override
  public void hitOneMonster() {
    for (int i = 0; i < monsters.size(); i ++) {
      if (!monsters.get(i).isDead()) {
        monsters.get(i).addHitTimes();
        if (monsters.get(i).isDead()) {
          monsters.remove(i);
        }
        break;
      }
    }
  }

  @Override
  public void resetTreasure() {
    this.treasure = new ArrayList<Treasure>();
  }

  @Override
  public boolean hasArrow() {
    return this.nArrows > 0;
  }

  @Override
  public int getNArrows() {
    int tmpArrows = nArrows;
    nArrows = 0;
    return tmpArrows;
  }

  @Override
  public int copyNArrows() {
    return nArrows;
  }

  @Override
  public String toString() {
    return String.format("Arrows:%d, Treasures: %s, Entrances: %s, Monsters: %s",
            nArrows, treasure.toString(), directions.toString(), monsters.toString());
  }
}
