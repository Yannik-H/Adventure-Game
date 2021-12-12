package monster;

/**
 * This class is the concrete class implement {@link Monster}.
 * It has hitTimes as its private field. If it was hit two times， it will die.
 */
public class Otyugh implements Monster {

  private int hitTimes;

  /**
   * Initialize the object by setting thiTimes equals to 0.
   */
  public Otyugh() {
    this.hitTimes = 0;
  }

  /**
   * This constructor used for deep copy.
   * @param otyugh  The object needed to be copied.
   */
  public Otyugh(Otyugh otyugh) {
    this.hitTimes = otyugh.getHitTimes();
  }

  @Override
  public void addHitTimes() throws IllegalStateException {
    if (isDead()) {
      throw new IllegalStateException("The monster is already dead.");
    }
    this.hitTimes += 1;
  }

  @Override
  public boolean isDead() {
    return hitTimes == 2;
  }

  @Override
  public int getHitTimes() {
    return hitTimes;
  }

  @Override
  public String toString() {
    return String.format("Otyughs' hit times: %d", hitTimes);
  }
}
