package dungeon;

/**
 * Enum class represents three kinds of treasures.
 * It has three enum types representing three kinds of treasures.
 */
public enum Treasure {
  RUBY("Ruby"), DIAMOND("Diamond"), SAPPHIRE("Sapphire");

  private final String treasureName;

  Treasure(String treasureName) {
    this.treasureName = treasureName;
  }


  @Override
  public String toString() {
    return this.treasureName;
  }
}
