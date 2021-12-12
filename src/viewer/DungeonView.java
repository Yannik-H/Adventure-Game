package viewer;

import controller.DungeonMvcController;

/**
 * This is the viewer interface for dungeon game.
 */
public interface DungeonView {

  /**
   * Refresh the view to reflect any changes in the game state.
   */
  void refresh();

  /**
   * Make the view visible to start the game session.
   */
  void makeVisible();

  /**
   * This method used for catching the mouse clicking event,
   * and the actual actions are in the controller.
   * @param listener The controller of the MVC pattern
   */
  void addClickedListener(DungeonMvcController listener);

  /**
   * Add clicked listener to the dungeonPanel after initializing the dungeon.
   * @param listener  The controller for performing actions
   */
  void addClickedListenerToDungeonPanel(DungeonMvcController listener);

  /**
   * This method used for catching the keyboard event,
   * and the actual actions are in the controller.
   * @param listener The controller of the MVC pattern
   */
  void addKeyListener(DungeonMvcController listener);

  /**
   * This method used for display the prompt information from the controller.
   * @param info  A string representing the prompt information from the controller.
   */
  void setPromptInformation(String info);

  /**
   * Initialize the view of the dungeon after creating the dungeon.
   */
  void initializeDungeonView();

  /**
   * Renew a specific unit in the dungeon.
   * @param x the x coordinate of the unit for updating
   * @param y the y coordinate of the unit for updating
   */
  void renewDungeonUnitContent(int x, int y);

  /**
   * Renew pictures of all units in the dungeon.
   */
  void renewAllDungeonUnits();

  /**
   * Renew pictures of all units in the dungeon without change the visited status in the unit.
   * This method used for shooting specifically.
   */
  void renewDungeonUnitContentWithoutVisited();

  /**
   * When "s" is pressed, viewer will wait for next key pressed.
   */
  void waitShootingDirection();

  /**
   * When the reset button clicked, reset the dungeon panel.
   */
  void resetDungeonPanel();

  /**
   * Set the waiting shooting direction status.
   * @param shootingDirectionSettingStatus  boolean value implying is wating or not.
   */
  void setShootingDirectionSettingStatus(boolean shootingDirectionSettingStatus);
}
