package player;

import dungeon.Treasure;

import java.util.ArrayList;
import java.util.List;


/**
 *  This concrete Player class implements its interface {@link Player}.
 *  This class has position information about coordinates which is the x, y value.
 *  Its field treasures represents collected treasures
 */
public class PlayerImpl implements  Player {

  private int currenX;
  private int currenY;
  private List<Treasure> treasures;
  private boolean isEaten;
  private int nArrows;

  /**
   * Initializing fields without any parameters.
   */
  public PlayerImpl() {
    this.currenX = -1;
    this.currenY = -1;
    treasures = new ArrayList<>();
    nArrows = 3;
    isEaten = false;
  }

  /**
   * This constructor used for deep copy.
   * @param other The player object needed to be copied.
   */
  public PlayerImpl(PlayerImpl other) {
    this.currenX = other.getCurrenX();
    this.currenY = other.getCurrenY();
    treasures = other.getTreasures();
    nArrows = other.getnArrows();
    isEaten = other.isEaten();
  }

  @Override
  public String collectionToString() {
    return treasures.toString();
  }

  @Override
  public String locationToString() {
    return String.format("X: %d, Y: %d", currenX, currenY);
  }

  @Override
  public void collectTreasure(Treasure treasure) {
    this.treasures.add(treasure);
  }

  @Override
  public int getCurrenX() {
    return currenX;
  }

  @Override
  public int getCurrenY() {
    return currenY;
  }

  @Override
  public void setCurrenX(int currenX) {
    this.currenX = currenX;
  }

  @Override
  public void setCurrenY(int currenY) {
    this.currenY = currenY;
  }

  @Override
  public boolean isEaten() {
    return isEaten;
  }

  @Override
  public int getnArrows() {
    return nArrows;
  }

  @Override
  public void setEaten() {
    isEaten = true;
  }

  @Override
  public void setArrows(int nArrows) {
    this.nArrows += nArrows;
  }

  @Override
  public void reduceArrow() {
    this.nArrows -= 1;
  }

  public List<Treasure> getTreasures() {
    return new ArrayList<>(treasures);
  }
}
