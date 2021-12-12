package controller;

import controller.command.DungeonCommand;
import controller.command.Move;
import controller.command.PickUp;
import controller.command.Shoot;
import dungeon.Facade;
import dungeon.ModelFacade;
import location.Direction;
import location.LocationNode;
import player.PlayerImpl;
import viewer.DungeonSwingView;
import viewer.DungeonView;
import viewer.SettingWindow;

import java.awt.Dialog;

/**
 * This class implement its interface {@link DungeonController}. This is the controller in MVC
 * design pattern.
 */
public class DungeonMvcController implements DungeonController {

  private DungeonView dungeonView;
  private Facade modelFacade;

  /**
   * This constructor takes in a view object.
   * @param dungeonView The view of the MVC dungeon
   */
  public DungeonMvcController(DungeonView dungeonView) {
    if (dungeonView == null) {
      throw new IllegalArgumentException("The view can't be null");
    }
    this.dungeonView = dungeonView;
    this.dungeonView.addClickedListener(this);
    this.dungeonView.addKeyListener(this);
  }

  @Override
  public void playGame(Facade m) {

    if (m == null) {
      throw new IllegalArgumentException("The model can't be null");
    }
    this.modelFacade = m;
    this.dungeonView.makeVisible();
  }

  @Override
  public void startSetting() {

    if (modelFacade == null) {
      throw new IllegalStateException("The model facade can not be null;");
    }
    dungeonView.setShootingDirectionSettingStatus(false);
    SettingWindow settingWindow = new SettingWindow();
    settingWindow.addClickedListener(this);
    settingWindow.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
    settingWindow.setLocation(50, 50);
    settingWindow.setVisible(true);
  }

  @Override
  public void reStartGame() {
    dungeonView.setShootingDirectionSettingStatus(false);
    modelFacade.resetDungeon();
    modelFacade.addPlayer(new PlayerImpl());
    dungeonView.renewAllDungeonUnits();
  }

  private boolean isLegalInteger(String input) {
    for (int i = 0; i < input.length(); i++) {
      if (!Character.isDigit(input.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void receiveParams(String length, String width, String interconnectivity,
                            String percentage, String wrap, String nMonsters) {
    if (isLegalInteger(length) & isLegalInteger(width) & isLegalInteger(interconnectivity)
            & isLegalInteger(percentage) & isLegalInteger(nMonsters) & wrap != null) {
      boolean tempWrap = false;
      if (wrap.equals("y")) {
        tempWrap = true;
      }
      try {
        modelFacade.createDungeon(Integer.parseInt(length), Integer.parseInt(width),
                Integer.parseInt(interconnectivity), Integer.parseInt(percentage), tempWrap, 10);
        modelFacade.addMonster(Integer.parseInt(nMonsters));
        modelFacade.addPlayer(new PlayerImpl());

        dungeonView.initializeDungeonView();
        dungeonView.addClickedListenerToDungeonPanel(this);
      } catch (IllegalArgumentException iae) {
        dungeonView.setPromptInformation("Can not instantiate dungeon with given settings.");
      } catch (IllegalStateException ise) {
        dungeonView.setPromptInformation("The player is not added yet.");
      }
    } else {
      dungeonView.setPromptInformation("Invalid settings");
    }
  }



  @Override
  public void move(int index) {
    dungeonView.setShootingDirectionSettingStatus(false);
    int playerX = modelFacade.getPlayerX();
    int playerY = modelFacade.getPlayerY();
    LocationNode[][] grid = modelFacade.getGrid();
    int playerindex = grid[playerY][playerX].getIndex();
    int gridWidth = grid[0].length;
    int gridHeight = grid.length;
    StringBuilder message = new StringBuilder();
    DungeonCommand move = null;
    if (index == playerindex + 1
            | (playerX == gridWidth - 1 & index == playerindex - gridWidth + 1)) {
      move = new Move(Direction.EAST);
    } else if (index == playerindex + gridWidth
            | (playerY == gridHeight - 1 & index == playerindex - (gridHeight - 1) * gridWidth)) {
      move = new Move(Direction.SOUTH);
    } else if (index == playerindex - 1 | (playerX == 0 & index == playerindex + gridWidth - 1)) {
      move = new Move(Direction.WEST);
    } else if (index == playerindex - gridWidth
            | (playerY == 0 & index == playerindex + (gridHeight - 1) * gridWidth)) {
      move = new Move(Direction.NORTH);
    }

    if (move != null) {
      move.execute(modelFacade, message);
      dungeonView.setPromptInformation(message.toString());
      dungeonView.renewDungeonUnitContent(modelFacade.getPlayerX(), modelFacade.getPlayerY());
      if (modelFacade.isGameOver()) {
        dungeonView.setPromptInformation(modelFacade.printGameOverState());
      }
    }
  }

  @Override
  public void move(Direction direction) {

    if (modelFacade.getGrid() == null) {
      dungeonView.setPromptInformation("Set up the dungeon first.");
    }
    dungeonView.setShootingDirectionSettingStatus(false);
    DungeonCommand move = new Move(direction);
    StringBuilder message = new StringBuilder();
    move.execute(modelFacade, message);
    dungeonView.setPromptInformation(message.toString());
    if (modelFacade.isGameOver()) {
      dungeonView.setPromptInformation(modelFacade.printGameOverState());
    }
    dungeonView.renewDungeonUnitContent(modelFacade.getPlayerX(), modelFacade.getPlayerY());
  }

  @Override
  public void pickPerformer() {
    if (modelFacade.getGrid() == null) {
      dungeonView.setPromptInformation("Please set the dungeon first.");
    }
    else {
      dungeonView.setShootingDirectionSettingStatus(false);
      DungeonCommand pickup = new PickUp();
      StringBuilder message = new StringBuilder();
      pickup.execute(modelFacade, message);
      dungeonView.setPromptInformation(message.toString());
      dungeonView.renewDungeonUnitContent(modelFacade.getPlayerX(), modelFacade.getPlayerY());
    }
  }

  @Override
  public void shootPerformer(Direction direction, int step) {
    if (modelFacade.getGrid() == null) {
      dungeonView.setPromptInformation("Please set the dungeon first.");
    }
    else {
      DungeonCommand shoot = new Shoot(direction, step);
      StringBuilder message = new StringBuilder();
      shoot.execute(modelFacade, message);
      dungeonView.setPromptInformation(message.toString());
      dungeonView.renewDungeonUnitContentWithoutVisited();
    }
  }

  @Override
  public void waitDirection() {
    if (modelFacade.getGrid() == null) {
      dungeonView.setPromptInformation("Please set the dungeon first.");
    }
    else {
      dungeonView.setPromptInformation("Press arrow keys to imply the shooting direction.");
      dungeonView.waitShootingDirection();
    }
  }

  @Override
  public void resetPerformer() {
    modelFacade = new ModelFacade();
    dungeonView.resetDungeonPanel();
    dungeonView = new DungeonSwingView(modelFacade);
    dungeonView.addClickedListener(this);
    dungeonView.addKeyListener(this);
    dungeonView.makeVisible();
  }

  @Override
  public void describePlayer() {
    if (dungeonView == null | modelFacade == null) {
      throw new IllegalStateException();
    }

    if (modelFacade.getGrid() == null) {
      dungeonView.setPromptInformation("Please set the dungeon first.");
    }

    dungeonView.setPromptInformation(modelFacade.describePlayerLocation());
  }
}
