package controller.command;

import dungeon.Facade;
import location.Direction;

import java.io.IOException;

/**
 * This concrete class represents the player chooses to move.
 */
public class Move implements DungeonCommand {

  private final Direction direction;

  /**
   * This constructor takes the direction as input.
   * @param direction The direction for going
   */
  public Move(Direction direction) {
    this.direction = direction;
  }

  @Override
  public void execute(Facade modelFacade, Appendable out) throws IllegalStateException {
    try {
      try {
        modelFacade.movePlayer(direction);
      } catch (IllegalStateException ise) {
        out.append("The game state is wrong\n");
      } catch (IllegalArgumentException iae) {
        out.append(String.format("There is no door leads to %s\n", direction));
      }
      out.append("\n");
    } catch (IOException ioe) {
      throw new IllegalStateException("The output is not appendable.");
    }
  }
}
