package controller.command;

import dungeon.Facade;

/**
 * This interface is for the actions in deungeon.
 */
public interface DungeonCommand {

  /**
   * Starting point for the controller.
   * @param modelFacade the model facade to use
   */
  void execute(Facade modelFacade, Appendable out);

}
