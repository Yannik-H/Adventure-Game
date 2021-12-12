package controller.command;

import dungeon.Facade;
import location.Direction;

import java.io.IOException;

/**
 * This concrete class represents the player chooses to shoot an arrow.
 */
public class Shoot implements DungeonCommand {

  private final Direction direction;
  private final int step;
  private boolean hitFlag;

  /**
   * The constructor take where and how far the arrow will go.
   * @param direction The direction where the arrow will go.
   * @param step The number of steps the arrow will go.
   */
  public Shoot(Direction direction, int step) {
    this.direction = direction;
    this.step = step;
    hitFlag = false;
  }

  @Override
  public void execute(Facade modelFacade, Appendable out) {
    try {
      try {
        hitFlag = modelFacade.shootArrow(direction, step);
        if (hitFlag) {
          out.append("You hear a great howl in the distance\n");
        } else {
          out.append("You shoot an arrow into the darkness\n");
        }
        if (modelFacade.isOutOfArrow()) {
          out.append("You are out of arrows, explore to find more\n");
        }
      } catch (IllegalStateException ise) {
        out.append("You are out of arrows, explore to find more\n");
      } catch (IllegalArgumentException iae) {
        out.append("The number of step should be positive\n");
      } catch (NullPointerException npe) {
        out.append("Please add a player to the dungeon first\n");
      }
      out.append("\n");
    } catch (IOException ioe) {
      throw new IllegalStateException("The output is not appendable");
    }
  }
}
