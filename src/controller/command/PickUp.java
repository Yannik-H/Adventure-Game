package controller.command;

import dungeon.Facade;

import java.io.IOException;

/**
 * This concrete class represents the player chooses to pick up treasures and arrows.
 */
public class PickUp implements DungeonCommand {
  @Override
  public void execute(Facade modelFacade, Appendable out) {
    try {
      try {
        String allFoundItems = modelFacade.pickUpAllStuff();
        out.append(allFoundItems).append("\n");
      } catch (IllegalStateException ise) {
        out.append("Please add a player to the dungeon first\n");
      }
      out.append("\n");
    } catch (IOException ioe) {
      throw new IllegalStateException("The output is no appendable");
    }
  }

}
