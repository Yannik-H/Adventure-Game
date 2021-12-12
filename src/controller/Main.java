package controller;

import dungeon.Facade;
import dungeon.ModelFacade;
import viewer.DungeonSwingView;
import viewer.DungeonView;

import java.io.InputStreamReader;

/**
 * The main method for the game.
 * When the program starts, main method will give controll to the controller.
 */
public class Main {

  /**
   * The main method of the game, it will give control to the controller.
   * @param args  Not used here.
   */
  public static void main(String[] args) {


    if (args[0].equals("text")) {
      Readable input = new InputStreamReader(System.in);
      Appendable output = System.out;
      new DungeonConsoleController(input, output).playGame(new ModelFacade());
    }
    else if (args[0].equals("gui")) {
      Facade modelFacade = new ModelFacade();
      DungeonView dungeonSwingView = new DungeonSwingView(modelFacade);
      DungeonMvcController dungeonController = new DungeonMvcController(dungeonSwingView);
      dungeonController.playGame(modelFacade);
    }
  }

}
