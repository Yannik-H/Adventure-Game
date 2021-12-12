package dungeon;

import location.Direction;
import location.LocationNode;
import location.TunnelOrCave;
import monster.Monster;
import player.Player;
import player.PlayerImpl;
import random.RandomIntegerGenerator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * This class implements its interface {@link Dungeon}.
 * It has a grid representing the structure of the dungeon, the size of the dungeon
 * is decided by the length and the width.
 * The player is the user object. The fields about coordinates
 * are the start point and the goal point.
 * The interconnectivity of the dungeon and what percentage of caves should have treasures are
 * inputted by user. The percentage should be 0 or larger than 20 and smaller than 100.
 */
public class DungeonImpl implements Dungeon {

  private LocationNode[][] grid;
  private Player player;
  private int startX;
  private int startY;
  private int goalX;
  private int goalY;
  private final int length;
  private final int width;
  private final boolean wrap;
  private final int interconnectivity;
  private final int treasureCavePercentage;
  private final HashMap<Integer, Pair<Integer, Integer>> index2XY;
  private final RandomIntegerGenerator random;
  private LocationNode[][] originalGrid;


  /**
   * Construct the dungeon with params.
   *
   * @param length            The length of the grid.
   * @param width             The width of the grid
   * @param interconnectivity How many interconnectivities should be considered
   * @param percentage        What percentages of caves should have treasures. Percentage should be
   *                          larger than 20
   * @param wrap              If true, wrap this dungeon, otherwise do not
   */
  public DungeonImpl(int length, int width, int interconnectivity, int percentage, boolean wrap)
          throws IllegalArgumentException {

    if (length <= 0 | width <= 0) {
      throw new IllegalArgumentException("Input should be positive");
    }

    if (manhattanDistance(1, 1, length, width) < 5) {
      throw new IllegalArgumentException("The dungeon is too small");
    }
    if ((percentage < 20 | percentage > 100) & percentage != 0) {
      throw new IllegalArgumentException("The percentage should be between 20 and 100");
    }

    if (wrap) {
      if (interconnectivity < 0 | interconnectivity > length * width * 2) {
        throw new IllegalArgumentException("The interconnectivity is wrong.");
      }
    } else {
      if (interconnectivity < 0 | interconnectivity > length * width * 2 - length - width) {
        throw new IllegalArgumentException("The interconnectivity is wrong.");
      }
    }

    index2XY = new HashMap<>();
    this.length = length;
    this.width = width;
    gridInitializationHelper();

    this.wrap = wrap;
    this.random = new RandomIntegerGenerator(0, 0);
    this.interconnectivity = interconnectivity;
    treasureCavePercentage = percentage;
    kruskal();
    this.player = null;
  }

  /**
   * Construct the dungeon with params and fixed randomSeed.
   *
   * @param length            The length of the grid.
   * @param width             The width of the grid
   * @param interconnectivity How many interconnectivities should be considered
   * @param percentage        What percentages of caves should have treasures. Percentage should be
   *                          larger than 20
   * @param wrap              If true, wrap this dungeon, otherwise do not
   * @param randomSeed        The fixed randomSeed
   */
  public DungeonImpl(int length, int width, int interconnectivity, int percentage,
                     boolean wrap, int randomSeed)
          throws IllegalArgumentException {

    if (length <= 0 | width <= 0) {
      throw new IllegalArgumentException("Input should be positive");
    }

    if (manhattanDistance(1, 1, length, width) < 5) {
      throw new IllegalArgumentException("The dungeon is too small");
    }
    if ((percentage < 20 | percentage > 100) & percentage != 0) {
      throw new IllegalArgumentException("The percentage should be between 20 and 100");
    }

    if (wrap) {
      if (interconnectivity < 0 | interconnectivity > length * width * 2) {
        throw new IllegalArgumentException("The interconnectivity is wrong.");
      }
    } else {
      if (interconnectivity < 0 | interconnectivity > length * width * 2 - length - width) {
        throw new IllegalArgumentException("The interconnectivity is wrong.");
      }
    }

    index2XY = new HashMap<>();
    this.length = length;
    this.width = width;
    gridInitializationHelper();

    this.wrap = wrap;
    this.random = new RandomIntegerGenerator(0, 0, randomSeed);
    this.interconnectivity = interconnectivity;
    treasureCavePercentage = percentage;
    kruskal();
    this.player = null;
    this.originalGrid = this.getGrid();
  }

  public Player getPlayer() {
    return new PlayerImpl((PlayerImpl) player);
  }

  private void gridInitializationHelper() {
    grid = new LocationNode[width][length];
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < length; j++) {
        int index = i * length + j + 1;
        grid[i][j] = new TunnelOrCave(index);
        index2XY.put(index, new Pair<>(j, i));
      }
    }
  }

  private int manhattanDistance(int x1, int y1, int x2, int y2) {
    return Math.abs(x1 - x2) + Math.abs(y1 - y2);
  }

  private int shortestPath(int startIndex, int goalIndex) {

    if (startIndex == goalIndex) {
      return 0;
    }
    Queue<Integer> queue = new LinkedList<>();
    queue.offer(startIndex);
    Set<Integer> explored = new HashSet<>();
    explored.add(startIndex);
    int layerCount = 0;
    while (!queue.isEmpty()) {
      int layerNodeCount = queue.size();
      for (int i = 0; i < layerNodeCount; i++) {
        int tmpIndex = queue.poll();
        Pair<Integer, Integer> tmpXY = index2XY.get(tmpIndex);
        for (Direction direction : grid[tmpXY.getSecond()][tmpXY.getFirst()].getDirection()) {
          int nextIndex = positionTransform(tmpXY.getFirst(), tmpXY.getSecond(), direction);
          if (nextIndex == goalIndex) {
            return layerCount + 1;
          }
          if (!explored.contains(nextIndex)) {
            explored.add(nextIndex);
            queue.offer(nextIndex);
          }
        }
      }
      layerCount += 1;
    }

    return layerCount;
  }

  private Set<Pair<Integer, Integer>> edgesHelper(int x, int y) {
    Set<Pair<Integer, Integer>> pairPoints = new HashSet<>();
    Set<Pair<Integer, Integer>> nextPoint = new HashSet<>();
    int currentIndex = grid[y][x].getIndex();
    int eastX = x + 1;
    int southY = y + 1;
    int westX = x - 1;
    int northY = y - 1;
    nextPoint.add(new Pair<>(eastX, y));
    nextPoint.add(new Pair<>(x, southY));
    nextPoint.add(new Pair<>(westX, y));
    nextPoint.add(new Pair<>(x, northY));
    for (Pair<Integer, Integer> point : nextPoint) {
      if (point.getFirst() < 0) {
        // grid[y][-1] -> grid[y][length]
        if (wrap) {
          pairPoints.add(new Pair<>(currentIndex, grid[point.getSecond()][length - 1].getIndex()));
        }
      } else if (point.getSecond() < 0) {
        // grid[-1][x] -> grid[width][x]
        if (wrap) {
          pairPoints.add(new Pair<>(currentIndex, grid[width - 1][point.getFirst()].getIndex()));
        }
      } else if (point.getFirst() == length) {
        // grid[y][length] -> grid[y][0]
        if (wrap) {
          pairPoints.add(new Pair<>(currentIndex, grid[point.getSecond()][0].getIndex()));
        }
      } else if (point.getSecond() == width) {
        // grid[width][x] -> grid[0][x]
        if (wrap) {
          pairPoints.add(new Pair<>(currentIndex, grid[0][point.getFirst()].getIndex()));
        }
      } else if (point.getFirst() >= 0 & point.getSecond() >= 0) {
        pairPoints.add(new Pair<>(currentIndex,
                grid[point.getSecond()][point.getFirst()].getIndex()));
      }
    }
    return pairPoints;
  }

  private Set<List<Integer>> getEdges() {
    Set<List<Integer>> edges = new HashSet<>();

    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[0].length; j++) {
        for (Pair<Integer, Integer> tmpPointPair : edgesHelper(j, i)) {
          List<Integer> tmpEdges = new ArrayList<>();
          tmpEdges.add(tmpPointPair.getFirst());
          tmpEdges.add(tmpPointPair.getSecond());
          List<Integer> reverseTempEdges = new ArrayList<>();
          reverseTempEdges.add(tmpPointPair.getSecond());
          reverseTempEdges.add(tmpPointPair.getFirst());
          if (!edges.contains(new ArrayList<>(reverseTempEdges))) {
            edges.add(tmpEdges);
          }
        }
      }
    }
    return edges;
  }

  private void addDirection(int firstNodeIndex, int secondNodeIndex)
          throws IllegalArgumentException {
    int firstX = index2XY.get(firstNodeIndex).getFirst();
    int firstY = index2XY.get(firstNodeIndex).getSecond();
    int secondX = index2XY.get(secondNodeIndex).getFirst();
    int secondY = index2XY.get(secondNodeIndex).getSecond();
    if (firstNodeIndex == secondNodeIndex + length | (firstY == 0 & secondY == width - 1)) {
      grid[firstY][firstX].addDirection(Direction.NORTH);
      grid[secondY][secondX].addDirection(Direction.SOUTH);
    } else if (firstNodeIndex + 1 == secondNodeIndex | (firstX == length - 1 & secondX == 0)) {
      grid[firstY][firstX].addDirection(Direction.EAST);
      grid[secondY][secondX].addDirection(Direction.WEST);
    } else if (firstNodeIndex + length == secondNodeIndex | (firstY == width - 1 & secondY == 0)) {
      grid[firstY][firstX].addDirection(Direction.SOUTH);
      grid[secondY][secondX].addDirection(Direction.NORTH);
    } else if (firstNodeIndex - 1 == secondNodeIndex | (firstX == 0 & secondX == length - 1)) {
      grid[firstY][firstX].addDirection(Direction.WEST);
      grid[secondY][secondX].addDirection(Direction.EAST);
    } else {
      if (!wrap) {
        throw new IllegalArgumentException("Input indexes are wrong");
      }
    }
  }

  private void kruskal() {
    List<Set<Integer>> pointsSet = new ArrayList<>();
    List<List<Integer>> interConnectedPairs = new ArrayList<>();
    List<List<Integer>> allEdges = new ArrayList<>(getEdges());

    random.setUpperBound(allEdges.size() - 1);
    int randomIndex = random.getRandomInt();
    Set<Integer> tmpEdge = new HashSet<>(allEdges.get(randomIndex));
    addDirection(allEdges.get(randomIndex).get(0), allEdges.get(randomIndex).get(1));
    pointsSet.add(tmpEdge);
    allEdges.remove(randomIndex);

    while (!(pointsSet.size() == 1 & pointsSet.get(0).size() == length * width)) {
      random.setUpperBound(allEdges.size() - 1);
      randomIndex = random.getRandomInt();
      List<Integer> tmp = allEdges.get(randomIndex);
      int firstPointSetIndex = -1;
      int secondPointSetIndex = -1;
      for (int i = 0; i < pointsSet.size(); i++) {
        if (pointsSet.get(i).contains(tmp.get(0))) {
          firstPointSetIndex = i;
        }
        if (pointsSet.get(i).contains(tmp.get(1))) {
          secondPointSetIndex = i;
        }
      }
      if (firstPointSetIndex == secondPointSetIndex & firstPointSetIndex != -1) {
        interConnectedPairs.add(tmp);
        allEdges.remove(randomIndex);
      } else if (firstPointSetIndex == secondPointSetIndex & firstPointSetIndex == -1) {
        pointsSet.add(new HashSet<>(tmp));
        addDirection(tmp.get(0), tmp.get(1));
        allEdges.remove(randomIndex);
      } else if (firstPointSetIndex == -1 & secondPointSetIndex != -1) {
        pointsSet.get(secondPointSetIndex).add(tmp.get(0));
        addDirection(tmp.get(0), tmp.get(1));
        allEdges.remove(randomIndex);
      } else if (firstPointSetIndex != -1 & secondPointSetIndex == -1) {
        pointsSet.get(firstPointSetIndex).add(tmp.get(1));
        addDirection(tmp.get(0), tmp.get(1));
        allEdges.remove(randomIndex);
      } else {
        pointsSet.get(firstPointSetIndex).addAll(pointsSet.get(secondPointSetIndex));
        pointsSet.remove(secondPointSetIndex);
        addDirection(tmp.get(0), tmp.get(1));
        allEdges.remove(randomIndex);
      }
    }

    interConnectedPairs.addAll(allEdges);

    Set<Integer> interIndex = new HashSet<>();
    random.setUpperBound(interConnectedPairs.size() - 1);
    if (interconnectivity >= interConnectedPairs.size()) {
      for (int i = 0; i < interConnectedPairs.size(); i++) {
        interIndex.add(i);
      }
    } else {
      while (interIndex.size() < interconnectivity) {
        interIndex.add(random.getRandomInt());
      }
    }
    for (int index : interIndex) {
      List<Integer> pair = interConnectedPairs.get(index);
      addDirection(pair.get(0), pair.get(1));
    }
  }

  @Override
  public void initializeGame() {
    random.setUpperBound(width * length - 1);
    int randomStartIndex = random.getRandomInt() + 1;
    this.startY = index2XY.get(randomStartIndex).getSecond();
    this.startX = index2XY.get(randomStartIndex).getFirst();
    int randomGoalIndex = random.getRandomInt() + 1;
    this.goalY = index2XY.get(randomGoalIndex).getSecond();
    this.goalX = index2XY.get(randomGoalIndex).getFirst();
    while (shortestPath(grid[startY][startX].getIndex(), grid[goalY][goalX].getIndex()) < 5) {
      randomStartIndex = random.getRandomInt() + 1;
      this.startY = index2XY.get(randomStartIndex).getSecond();
      this.startX = index2XY.get(randomStartIndex).getFirst();
      randomGoalIndex = random.getRandomInt() + 1;
      this.goalY = index2XY.get(randomGoalIndex).getSecond();
      this.goalX = index2XY.get(randomGoalIndex).getFirst();
    }

    this.addTreasure();
    this.addArrow();

    this.originalGrid = this.getGrid();
  }


  /**
   * Add Otyughs to dungeon, there should be at least one monster in the dungeon.
   * @param nMonsters How many monsters to add.
   */
  void addMonsters(int nMonsters) throws IllegalArgumentException {
    if (nMonsters <= 0) {
      throw new IllegalArgumentException("The number of monsters should be at least one.");
    }
    int monsterCount = 0;
    random.setUpperBound(width * length - 1);
    while (monsterCount < nMonsters - 1) {
      int locationIndex = random.getRandomInt() + 1;
      int locationX = index2XY.get(locationIndex).getFirst();
      int locationY = index2XY.get(locationIndex).getSecond();
      if (!grid[locationY][locationX].isTunnel() & (locationX != startX | locationY != startY)) {
        grid[locationY][locationX].addMonster();
        monsterCount += 1;
      }
    }
    grid[goalY][goalX].addMonster();

    this.originalGrid = this.getGrid();
  }

  private void addArrow() {
    int totalLocations = this.width * this.length;
    double arrowLocations = 0;
    random.setUpperBound(totalLocations - 1);
    while (Math.round(arrowLocations / totalLocations) * 100 < treasureCavePercentage) {
      int locationIndex = random.getRandomInt() + 1;
      int locationX = index2XY.get(locationIndex).getFirst();
      int locationY = index2XY.get(locationIndex).getSecond();
      if (!grid[locationY][locationX].hasArrow()) {
        arrowLocations += 1;
        grid[locationY][locationX].addArrow();
      }
    }
  }

  private void addTreasure() {
    int totalCaves = 0;
    double treasureCaves = 0;
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < length; j++) {
        if (!grid[i][j].isTunnel()) {
          totalCaves += 1;
        }
      }
    }

    while (Math.round(treasureCaves / totalCaves) * 100 <= treasureCavePercentage) {
      random.setUpperBound(width - 1);
      random.setUpperBound(2);
      int treasureIndex = random.getRandomInt();
      Treasure tmpTreasure;
      switch (treasureIndex) {
        case 0:
          tmpTreasure = Treasure.RUBY;
          break;
        case 1:
          tmpTreasure = Treasure.DIAMOND;
          break;
        case 2:
          tmpTreasure = Treasure.SAPPHIRE;
          break;
        default:
          tmpTreasure = null;
          break;
      }
      int tempY = random.getRandomInt();
      random.setUpperBound(length - 1);
      int tempX = random.getRandomInt();
      if (grid[tempY][tempX].isTunnel()) {
        continue;
      }
      if (grid[tempY][tempX].getTreasure().size() == 0) {
        treasureCaves += 1;
      }
      grid[tempY][tempX].addTreasure(tmpTreasure);
    }
  }

  @Override
  public boolean isGameOver() {
    return player.getCurrenX() == goalX & player.getCurrenY() == goalY
            | player.isEaten();
  }

  private int positionTransform(int x, int y, Direction direction) {
    if (!grid[y][x].getDirection().contains(direction)) {
      throw new IllegalArgumentException(
              String.format("There is no entrance going %S", direction.toString()));
    }

    int nextX = x;
    int nextY = y;
    switch (direction) {
      case EAST:
        nextX = x + 1;
        if (nextX >= length) {
          nextX = 0;
        }
        break;
      case WEST:
        nextX = x - 1;
        if (nextX < 0) {
          nextX = length - 1;
        }
        break;
      case NORTH:
        nextY = y - 1;
        if (nextY < 0) {
          nextY = width - 1;
        }
        break;
      case SOUTH:
        nextY = y + 1;
        if (nextY >= width) {
          nextY = 0;
        }
        break;
      default:
        break;
    }
    return grid[nextY][nextX].getIndex();
  }

  @Override
  public void move(Direction direction) throws IllegalStateException, IllegalArgumentException {
    if (player == null) {
      throw new IllegalStateException("Please add player first");
    }
    if (isGameOver()) {
      throw new IllegalStateException("The game is over");
    }
    int playerX = player.getCurrenX();
    int playerY = player.getCurrenY();
    if (!grid[playerY][playerX].getDirection().contains(direction)) {
      throw new IllegalArgumentException(
              String.format("There is no entrance going %S", direction.toString()));
    }

    int nextIndex = positionTransform(playerX, playerY, direction);
    int nextX = index2XY.get(nextIndex).getFirst();
    int nextY = index2XY.get(nextIndex).getSecond();
    player.setCurrenX(nextX);
    player.setCurrenY(nextY);

    random.setUpperBound(1);
    if (grid[nextY][nextX].getLiveMonsters().size() > 0) {
      List<Monster> tmpMonsters = grid[nextY][nextX].getLiveMonsters();
      for (Monster monster : tmpMonsters) {
        if (monster.getHitTimes() > 0 & random.getRandomInt() == 1) {
          player.setEaten();
          break;
        } else if (monster.getHitTimes() == 0) {
          player.setEaten();
          break;
        }
      }
    }
  }

  @Override
  public boolean shootArrow(Direction direction, int steps)
          throws IllegalArgumentException, IllegalStateException, NullPointerException {
    if (steps <= 0 | steps > 5) {
      throw new IllegalArgumentException("The steps should be positive and smaller than 5.");
    }
    if (player == null) {
      throw new IllegalArgumentException("Please add player first");
    }
    if (player.getnArrows() <= 0) {
      throw new IllegalStateException("The player is out of arrows.");
    }
    int arrowIndex = -1;
    int tmpX = player.getCurrenX();
    int tmpY = player.getCurrenY();
    if (!grid[tmpY][tmpX].getDirection().contains(direction)) {
      throw new IllegalArgumentException(
              String.format("The arrow can not go %s", direction.toString()));
    }
    while (steps > 0) {
      arrowIndex = positionTransform(tmpX, tmpY, direction);
      tmpX = index2XY.get(arrowIndex).getFirst();
      tmpY = index2XY.get(arrowIndex).getSecond();
      steps -= 1;

      if (steps != 0 & !grid[tmpY][tmpX].isTunnel()
              & !grid[tmpY][tmpX].getDirection().contains(direction)) {
        return false;
      }
      HashMap<Direction, Direction> opposite = new HashMap<>();
      opposite.put(Direction.EAST, Direction.WEST);
      opposite.put(Direction.SOUTH, Direction.NORTH);
      opposite.put(Direction.WEST, Direction.EAST);
      opposite.put(Direction.NORTH, Direction.SOUTH);
      if (grid[tmpY][tmpX].isTunnel()) {
        Set<Direction> tmpDirection = new HashSet<>(grid[tmpY][tmpX].getDirection());
        tmpDirection.remove(opposite.get(direction));
        direction = tmpDirection.toArray(new Direction[1])[0];
      }
    }

    player.reduceArrow();

    if (grid[tmpY][tmpX].getLiveMonsters().size() > 0) {
      grid[tmpY][tmpX].hitOneMonster();
      return true;
    } else {
      return false;
    }
  }

  @Override
  public void pickUpTreasures() throws IllegalStateException {
    if (player == null) {
      throw new IllegalStateException("The player has not been initialized.");
    }

    int playerX = player.getCurrenX();
    int playerY = player.getCurrenY();
    List<Treasure> treasures = grid[playerY][playerX].getTreasure();
    while (treasures.size() != 0) {
      player.collectTreasure(treasures.remove(0));
    }
    grid[playerY][playerX].resetTreasure();
  }

  @Override
  public void pickUpArrows()  throws IllegalStateException {
    if (player == null) {
      throw new IllegalStateException("The player has not been initialized.");
    }
    player.setArrows(grid[player.getCurrenY()][player.getCurrenX()].getNArrows());
  }

  @Override
  public String monsterSmell(int x, int y) {
    Queue<Integer> queue = new LinkedList<>();
    queue.offer(grid[y][x].getIndex());
    Set<Integer> explored = new HashSet<>();
    explored.add(grid[y][x].getIndex());
    Map<Integer, Integer> layerMonster = new HashMap<>();
    layerMonster.put(1,0);
    layerMonster.put(2,0);
    layerMonster.put(0,0);
    int layerCount = 0;
    while (!queue.isEmpty() & layerCount <= 2) {
      int layerNodeCount = queue.size();
      for (int i = 0; i < layerNodeCount; i++) {
        int tmpIndex = queue.poll();
        Pair<Integer, Integer> tmpXY = index2XY.get(tmpIndex);
        int nMonster = layerMonster.get(layerCount);
        layerMonster.remove(layerCount);
        layerMonster.put(layerCount, nMonster
                + grid[tmpXY.getSecond()][tmpXY.getFirst()].getLiveMonsters().size());
        for (Direction direction : grid[tmpXY.getSecond()][tmpXY.getFirst()].getDirection()) {
          int nextIndex = positionTransform(tmpXY.getFirst(), tmpXY.getSecond(), direction);
          if (!explored.contains(nextIndex)) {
            explored.add(nextIndex);
            queue.offer(nextIndex);
          }
        }
      }
      layerCount += 1;
    }

    if (layerMonster.get(1) == 0 & layerMonster.get(2) == 1) {
      return "You smell something terrible nearby\n";
    } else if (layerMonster.get(1) > 0 | (layerMonster.get(1) + layerMonster.get(2) > 1)) {
      return "You smell something even more terrible nearby\n";
    } else {
      return "";
    }
  }

  @Override
  public String printGameState() {
    if (player == null) {
      return "No player in the dungeon.\n";
    }
    StringBuilder res = new StringBuilder();
    res.append(monsterSmell(player.getCurrenX(), player.getCurrenY()));
    String locationType =
            grid[player.getCurrenY()][player.getCurrenX()].isTunnel() ? "tunnel" : "cave";
    res.append(String.format("You are in a %s\n", locationType));
    if (!grid[player.getCurrenY()][player.getCurrenX()].contentToString().equals("")) {
      res.append("You find ");
      res.append(grid[player.getCurrenY()][player.getCurrenX()].contentToString());
    }
    List<Direction> tmp = grid[player.getCurrenY()][player.getCurrenX()].getDirection();
    tmp.sort(Comparator.naturalOrder());
    res.append(String.format("Doors lead to the %s\n",
            tmp.toString()));
    return res.toString();
  }

  @Override
  public boolean reachGoal() {
    return isGameOver() & !player.isEaten();
  }

  @Override
  public LocationNode[][] getGrid() {
    LocationNode[][] copy = new LocationNode[width][length];
    for (int i  = 0; i < width; i++) {
      for (int j = 0; j < length; j++) {
        copy[i][j] = new TunnelOrCave((TunnelOrCave) grid[i][j]);
      }
    }

    return copy;
  }

  @Override
  public String gridToString() {

    StringBuilder s = new StringBuilder();
    for (int i = 0; i < width; i++) {
      StringBuilder str1 = new StringBuilder();
      StringBuilder str2 = new StringBuilder();
      StringBuilder str3 = new StringBuilder();
      for (int j = 0; j < length; j++) {
        String west = grid[i][j].getDirection().contains(Direction.WEST) ? "-" : " ";
        String east = grid[i][j].getDirection().contains(Direction.EAST) ? "-" : " ";
        String north = grid[i][j].getDirection().contains(Direction.NORTH) ? "|" : " ";
        String south = grid[i][j].getDirection().contains(Direction.SOUTH) ? "|" : " ";
        str1.append(String.format("   %s   ", north));
        str2.append(String.format("%s(%d,%d)%s", west, j, i, east));
        str3.append(String.format("   %s   ", south));
      }
      s.append(str1);
      s.append("\n");
      s.append(str2);
      s.append("\n");
      s.append(str3);
      s.append("\n");
    }
    return s.toString();
  }

  @Override
  public void addPlayer(Player player) {
    this.player = player;
    this.player.setCurrenX(startX);
    this.player.setCurrenY(startY);
  }

  void resetGrid() {
    for (int i  = 0; i < width; i++) {
      for (int j = 0; j < length; j++) {
        grid[i][j] = new TunnelOrCave((TunnelOrCave) originalGrid[i][j]);
      }
    }
  }

  public int getStartX() {
    return startX;
  }

  public int getStartY() {
    return startY;
  }

  public int getGoalX() {
    return goalX;
  }

  public int getGoalY() {
    return goalY;
  }

  static class Pair<T, U> {
    private final T first;
    private final U second;

    Pair(T first, U second) {
      this.first = first;
      this.second = second;
    }


    public T getFirst() {
      return first;
    }

    public U getSecond() {
      return second;
    }
  }
}

