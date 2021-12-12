package controller;

import controller.command.DungeonCommand;
import controller.command.Move;
import controller.command.PickUp;
import controller.command.Shoot;
import dungeon.Facade;
import location.Direction;
import player.PlayerImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *  This class represent a controller controlled by console inputs.
 *  This class implement its interface {@link DungeonController}.
 */
public class DungeonConsoleController implements DungeonController {

  private final Appendable out;
  private final Scanner scan;
  private boolean initializedFlag;

  /**
   * Constructor for the controller.
   *
   * @param in  the source to read from
   * @param out the target to print to
   */
  public DungeonConsoleController(Readable in, Appendable out) {
    if (in == null || out == null) {
      throw new IllegalArgumentException("Readable and Appendable can't be null");
    }
    this.out = out;
    scan = new Scanner(in);
    initializedFlag = false;
  }

  private boolean isLegalInteger(String input) {
    for (int i = 0; i < input.length(); i++) {
      if (!Character.isDigit(input.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  private Direction getDirectionFromConsole() throws IllegalStateException {
    Map<String, Direction> directionMap = new HashMap<>();
    directionMap.put("E", Direction.EAST);
    directionMap.put("S", Direction.SOUTH);
    directionMap.put("W", Direction.WEST);
    directionMap.put("N", Direction.NORTH);
    try {
      out.append("Where to?");
      String d = scan.next();
      while (!directionMap.containsKey(d)) {
        out.append("\n").append("Invalid input. Choose direction from [E, S, W, N]");
        d = scan.next();
      }
      return directionMap.get(d);
    } catch (IOException ioe) {
      throw new IllegalStateException("The output is not appendable.");
    }
  }

  @Override
  public void playGame(Facade mTemp) {
    if (mTemp == null) {
      throw new IllegalArgumentException("The model can not be null");
    }

    try {
      boolean sizeFlag = false;
      boolean percentageFlag = false;
      boolean wrapFlag = false;
      boolean interFlag = false;
      boolean monsterFlag = false;
      boolean printFlag = false;
      Integer length = null;
      Integer width = null;
      Integer interconnectivity = null;
      Integer percentage = null;
      Integer monster = null;
      boolean wrap = false;
      while (!initializedFlag) {
        if (!sizeFlag) {
          if (!printFlag) {
            out.append("Input the length and width of the dungeon, split with space\n");
            printFlag = true;
          }
          String element1 = this.scan.next();
          if (isLegalInteger(element1)) {
            if (!scan.hasNext()) {
              return;
            }
            String element2 = scan.next();
            while (!isLegalInteger(element2)) {
              out.append(element2).append(" is not a valid number\n");
              element2 = scan.next();
            }
            length = Integer.parseInt(element1);
            width = Integer.parseInt(element2);
            sizeFlag = true;
            printFlag = false;
          } else {
            out.append(element1).append(" is not a valid number\n");
          }
        } else if (!percentageFlag) {
          if (!printFlag) {
            out.append("Input what percentage of caves should have treasures\n");
            printFlag = true;
          }
          String element1 = this.scan.next();
          if (isLegalInteger(element1)) {
            percentage = Integer.parseInt(element1);
            percentageFlag = true;
            printFlag = false;
          } else {
            out.append(element1).append(" is not a valid number\n");
          }
        } else if (!wrapFlag) {
          if (!printFlag) {
            out.append("Create a wrapping dungeon?(y/n)\n");
            printFlag = true;
          }
          String element1 = this.scan.next();
          if (element1.equals("y")) {
            wrap = true;
            wrapFlag = true;
            printFlag = false;
          } else if (element1.equals("n")) {
            wrapFlag = true;
            printFlag = false;
          } else {
            out.append("Input y or n.\n");
          }
        } else if (!interFlag) {
          if (!printFlag) {
            out.append("What inter-connectivity the dungeon has?\n");
            printFlag = true;
          }
          String element1 = this.scan.next();
          if (isLegalInteger(element1)) {
            interconnectivity = Integer.parseInt(element1);
            interFlag = true;
            printFlag = false;
          } else {
            out.append(element1).append(" is not a valid number.\n");
          }
        } else if (!monsterFlag) {
          if (!printFlag) {
            out.append("How many monsters in the dungeon?\n");
            printFlag = true;
          }
          String element1 = this.scan.next();
          if (isLegalInteger(element1)) {
            monster = Integer.parseInt(element1);
            monsterFlag = true;
            printFlag = false;
          } else {
            out.append(element1).append(" is not a valid number.\n");
          }
        } else {
          try {
            mTemp.createDungeon(length, width, interconnectivity, percentage, wrap, 10);
            try {
              mTemp.addMonster(monster);
            } catch (IllegalArgumentException iae) {
              out.append("The number of monsters should be at least one.");
            }
            out.append("The dungeon looks like:\n");
            out.append(mTemp.showDungeon()).append("\n");
            out.append("Input \"C\" to start the game.");
            initializedFlag = true;
            if (scan.hasNext()) {
              if (scan.next().equals("Q")) {
                return;
              }
            }
          } catch (IllegalArgumentException iae) {
            out.append("Given parameters are illegal, input again\n");
            sizeFlag = false;
            percentageFlag = false;
            wrapFlag = false;
            interFlag = false;
            monsterFlag = false;
            printFlag = false;
            length = null;
            width = null;
            interconnectivity = null;
            percentage = null;
            monster = null;
            wrap = false;
            if (scan.hasNext()) {
              if (scan.next().equals("Q")) {
                return;
              }
            }
          }
        }
      }
    } catch (IOException ioe) {
      throw new IllegalStateException("The output is not appendable.");
    }

    // Add a player to the dungeon automatically.
    mTemp.addPlayer(new PlayerImpl());

    try {
      out.append("\n Game Started!\n\n");
      while (!mTemp.isGameOver()) {
        out.append(mTemp.printGameState()).append("\n");
        out.append("Move, Pickup, or Shoot (M-P-S)?");
        String in = scan.next();
        DungeonCommand cmd = null;
        switch (in) {
          case "Q":
            return;
          case "M":
            cmd = new Move(getDirectionFromConsole());
            break;
          case "P":
            cmd = new PickUp();
            break;
          case "S":
            out.append("No. of caves (1-5)?");
            String element1 = scan.next();
            while (!isLegalInteger(element1)) {
              out.append("\n").append(element1).append(" is not a valid input.");
              element1 = scan.next();
            }
            Direction shootDirection = getDirectionFromConsole();
            cmd = new Shoot(shootDirection, Integer.parseInt(element1));
            break;
          default:
            out.append("Illegal input.\n");
        }
        if (!mTemp.isGameOver() & cmd != null) {
          cmd.execute(mTemp, out);
        }
      }
      out.append(mTemp.printGameOverState());
    } catch (IOException ioe) {
      throw new IllegalStateException("");
    }
  }

  @Override
  public void startSetting() {
    // This controller will not use this method.
  }

  @Override
  public void reStartGame() {
    // This controller will not use this method.
  }

  @Override
  public void receiveParams(String length, String width, String interconnectivity,
                            String percentage, String wrap, String nMonsters) {
    // This controller will not use this method.
  }

  @Override
  public void move(int index) {
    // This controller will not use this method.
  }

  @Override
  public void move(Direction direction) {
    // This controller will not use this method.
  }

  @Override
  public void pickPerformer() {
    // This controller will not use this method.
  }

  @Override
  public void shootPerformer(Direction direction, int step) {
    // This controller will not use this method.
  }

  @Override
  public void waitDirection() {
    // This controller will not use this method.
  }

  @Override
  public void resetPerformer() {
    // This controller will not use this method.
  }

  @Override
  public void describePlayer() {
    // This controller will not use this method.
  }
}
