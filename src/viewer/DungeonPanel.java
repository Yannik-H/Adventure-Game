package viewer;

import controller.DungeonMvcController;
import dungeon.ReadOnlyFacade;
import dungeon.Treasure;
import location.Direction;
import location.LocationNode;
import monster.Monster;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

class DungeonPanel extends JPanel {

  private ReadOnlyFacade readOnlyFacade;
  private final Map<String, Image> imageResource;
  private UnitPanel[][] unitPanels;

  public DungeonPanel() {
    super();

    //this.setSize(1000,1000);
    this.setPreferredSize(new Dimension(1000, 1000));
    this.imageResource = new HashMap<>();
    readOnlyFacade = null;
    unitPanels = null;
    initializeImageHelper();
  }

  private void initializeImageHelper() {
    try {
      this.imageResource.put("North",
              ImageIO.read(getClass().getResourceAsStream("/dungeon-images-bw/N.png")));
      this.imageResource.put("NorthEast",
              ImageIO.read(getClass().getResourceAsStream("/dungeon-images-bw/NE.png")));
      this.imageResource.put("NorthEastSouth",
              ImageIO.read(getClass().getResourceAsStream("/dungeon-images-bw/NES.png")));
      this.imageResource.put("NorthEastSouthWest",
              ImageIO.read(getClass().getResourceAsStream("/dungeon-images-bw/NESW.png")));
      this.imageResource.put("NorthEastWest",
              ImageIO.read(getClass().getResourceAsStream("/dungeon-images-bw/NEW.png")));
      this.imageResource.put("NorthSouth",
              ImageIO.read(getClass().getResourceAsStream("/dungeon-images-bw/NS.png")));
      this.imageResource.put("NorthSouthWest",
              ImageIO.read(getClass().getResourceAsStream("/dungeon-images-bw/SWN.png")));
      this.imageResource.put("NorthWest",
              ImageIO.read(getClass().getResourceAsStream("/dungeon-images-bw/WN.png")));

      this.imageResource.put("South",
              ImageIO.read(getClass().getResourceAsStream("/dungeon-images-bw/S.png")));
      this.imageResource.put("SouthWest",
              ImageIO.read(getClass().getResourceAsStream("/dungeon-images-bw/SW.png")));

      this.imageResource.put("West",
              ImageIO.read(getClass().getResourceAsStream("/dungeon-images-bw/W.png")));

      this.imageResource.put("East",
              ImageIO.read(getClass().getResourceAsStream("/dungeon-images-bw/E.png")));
      this.imageResource.put("EastSouth",
              ImageIO.read(getClass().getResourceAsStream("/dungeon-images-bw/ES.png")));
      this.imageResource.put("EastSouthWest",
              ImageIO.read(getClass().getResourceAsStream("/dungeon-images-bw/ESW.png")));
      this.imageResource.put("EastWest",
              ImageIO.read(getClass().getResourceAsStream("/dungeon-images-bw/EW.png")));

      this.imageResource.put("arrow",
              ImageIO.read(getClass().getResourceAsStream("/dungeon-images-bw/arrow-white.png")));
      this.imageResource.put("player",
              ImageIO.read(getClass().getResourceAsStream("/dungeon-images-bw/player.png")));
      this.imageResource.put("blank",
              ImageIO.read(getClass().getResourceAsStream("/dungeon-images-bw/blank.png")));
      this.imageResource.put("diamond",
              ImageIO.read(getClass().getResourceAsStream("/dungeon-images-bw/diamond.png")));
      this.imageResource.put("otyugh",
              ImageIO.read(getClass().getResourceAsStream("/dungeon-images-bw/otyugh.png")));
      this.imageResource.put("ruby",
              ImageIO.read(getClass().getResourceAsStream("/dungeon-images-bw/ruby.png")));
      this.imageResource.put("sapphire",
              ImageIO.read(getClass().getResourceAsStream("/dungeon-images-bw/sapphire.png")));

      this.imageResource.put("stench1",
              ImageIO.read(getClass().getResourceAsStream("/dungeon-images-bw/stench01.png")));
      this.imageResource.put("stench2",
              ImageIO.read(getClass().getResourceAsStream("/dungeon-images-bw/stench02.png")));
    } catch (IOException ioe) {
      throw new IllegalStateException();
    }
  }

  protected void setReadOnlyFacade(ReadOnlyFacade readOnlyFacade) {
    this.readOnlyFacade = readOnlyFacade;
  }

  protected void initializeDungeonView() {
    if (readOnlyFacade == null) {
      throw new IllegalStateException("Set the facade first");
    }
    if (this.readOnlyFacade.getGrid() == null) {
      throw new IllegalStateException("The dungeon is not initialized.");
    }
    LocationNode[][] grid = this.readOnlyFacade.getGrid();
    int col = grid[0].length;
    int row = grid.length;
    this.setPreferredSize(new Dimension(100 * col, 100 * row));
    this.setLayout(new GridLayout(row, col));
    //this.setPreferredSize(new Dimension(100 * col, 100 * row));
    this.unitPanels = new UnitPanel[row][col];

    // Create single unit view in the dungeon
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        int index = grid[i][j].getIndex();
        int narrows = grid[i][j].copyNArrows();
        List<Direction> directions = grid[i][j].getDirection();
        List<Treasure> treasures = grid[i][j].getTreasure();
        List<Monster> monsters = grid[i][j].getLiveMonsters();
        Image unit = this.imageResource.get(this.catDirectionName(directions));
        List<Image> itemImages = this.getAllItemsImages(treasures, monsters, narrows, j, i);

        Image smell = getSmellImage(j, i);

        UnitPanel unitPanel = new UnitPanel(index, unit, smell, itemImages);
        unitPanel.setPreferredSize(new Dimension(100, 100));
        this.unitPanels[i][j] = unitPanel;
        this.add(unitPanel);
      }
    }

    int playerX = readOnlyFacade.getPlayerX();
    int playerY = readOnlyFacade.getPlayerY();
    unitPanels[playerY][playerX].setVisited(true);
  }

  protected void addClickedListener(DungeonMvcController listener) {
    if (readOnlyFacade == null) {
      throw new IllegalStateException("Set the facade first");
    }
    if (unitPanels == null) {
      throw new IllegalStateException("The units are not initialized yet.");
    }

    for (int i = 0; i < unitPanels.length; i++) {
      for (int j = 0; j < unitPanels[0].length; j++) {
        MouseAdapter mouseAdapter = new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            Object source = e.getSource();
            UnitPanel thisPanel = (UnitPanel) source;
            listener.move(thisPanel.getIndex());
          }
        };
        unitPanels[i][j].addMouseListener(mouseAdapter);
      }
    }

  }

  protected void renewComponent(int x, int y) throws IllegalArgumentException {
    if (readOnlyFacade == null) {
      throw new IllegalStateException("Set the facade first");
    }
    if (this.readOnlyFacade.getGrid() == null) {
      throw new IllegalStateException("The dungeon is not initialized.");
    }

    LocationNode[][] grid = this.readOnlyFacade.getGrid();

    int narrows = grid[y][x].copyNArrows();
    List<Treasure> treasures = grid[y][x].getTreasure();
    List<Monster> monsters = grid[y][x].getLiveMonsters();
    List<Image> itemImages = this.getAllItemsImages(treasures, monsters, narrows, x, y);

    Image smell = getSmellImage(x, y);

    this.unitPanels[y][x].renewContent(itemImages, smell);

    int playerX = readOnlyFacade.getPlayerX();
    int playerY = readOnlyFacade.getPlayerY();
    unitPanels[playerY][playerX].setVisited(true);
  }

  protected void renewAllComponent() throws IllegalStateException {
    if (readOnlyFacade == null) {
      throw new IllegalStateException("Set the facade first");
    }
    if (this.readOnlyFacade.getGrid() == null) {
      throw new IllegalStateException("The dungeon is not initialized.");
    }
    LocationNode[][] grid = this.readOnlyFacade.getGrid();
    int col = grid[0].length;
    int row = grid.length;

    // Create single unit view in the dungeon
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        int narrows = grid[i][j].copyNArrows();
        List<Treasure> treasures = grid[i][j].getTreasure();
        List<Monster> monsters = grid[i][j].getLiveMonsters();
        List<Image> itemImages = this.getAllItemsImages(treasures, monsters, narrows, j, i);
        Image smell = getSmellImage(j, i);
        this.unitPanels[i][j].renewContent(itemImages, smell);
        this.unitPanels[i][j].setVisited(false);
      }
    }

    int playerX = readOnlyFacade.getPlayerX();
    int playerY = readOnlyFacade.getPlayerY();
    unitPanels[playerY][playerX].setVisited(true);

  }

  protected void renewAllComponentWithoutVisited() throws IllegalStateException {
    if (readOnlyFacade == null) {
      throw new IllegalStateException("Set the facade first");
    }
    if (this.readOnlyFacade.getGrid() == null) {
      throw new IllegalStateException("The dungeon is not initialized.");
    }
    LocationNode[][] grid = this.readOnlyFacade.getGrid();
    int col = grid[0].length;
    int row = grid.length;

    // Create single unit view in the dungeon
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        int narrows = grid[i][j].copyNArrows();
        List<Treasure> treasures = grid[i][j].getTreasure();
        List<Monster> monsters = grid[i][j].getLiveMonsters();
        List<Image> itemImages = this.getAllItemsImages(treasures, monsters, narrows, j, i);
        Image smell = getSmellImage(j, i);
        this.unitPanels[i][j].renewContent(itemImages, smell);
      }
    }

    int playerX = readOnlyFacade.getPlayerX();
    int playerY = readOnlyFacade.getPlayerY();

  }

  private String catDirectionName(List<Direction> directions) {
    Set<Direction> directionsSet = new HashSet<>(directions);

    StringBuilder sb = new StringBuilder();
    if (directionsSet.contains(Direction.NORTH)) {
      sb.append(Direction.NORTH.toString());
    }
    if (directionsSet.contains(Direction.EAST)) {
      sb.append(Direction.EAST.toString());
    }
    if (directionsSet.contains(Direction.SOUTH)) {
      sb.append(Direction.SOUTH.toString());
    }
    if (directionsSet.contains(Direction.WEST)) {
      sb.append(Direction.WEST.toString());
    }
    return sb.toString();
  }

  private Image getSmellImage(int x, int y) {
    String smellRepresentation = readOnlyFacade.positionSmell(x, y);
    if (smellRepresentation.equals("You smell something terrible nearby\n")) {
      return this.imageResource.get("stench1");
    } else if (smellRepresentation.equals("You smell something even more terrible nearby\n")) {
      return this.imageResource.get("stench2");
    } else {
      return null;
    }
  }

  private List<Image> getAllItemsImages(List<Treasure> treasures, List<Monster> monsters,
                                        int nArrows, int x, int y) {
    List<Image> images = new ArrayList<>();

    for (Treasure treasure : treasures) {
      images.add(this.imageResource.get(treasure.toString().toLowerCase()));
    }

    for (int i = 0; i < nArrows; i++) {
      images.add(this.imageResource.get("arrow"));
    }

    for (Monster monster : monsters) {
      images.add(this.imageResource.get("otyugh"));
    }

    return images;
  }

  @Override
  protected void paintComponent(Graphics g) {

    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    if (unitPanels == null) {
      g2d.drawImage(this.imageResource.get("blank"), 0, 0, this.getWidth(), this.getHeight(), null);
    }

  }



  class UnitPanel extends JPanel {

    private final Image unit;
    private Image smell;
    private List<Image> items;
    private final int index;
    private boolean visited;

    public UnitPanel(int index, Image unit, Image smell, List<Image> items) {
      super();
      this.unit = unit;
      this.items = items;
      this.index = index;
    }

    public void setVisited(boolean visited) {
      this.visited = visited;
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D) g;

      if (!visited) {
        g2d.drawImage(imageResource.get("blank"), 0, 0, this.getWidth(), this.getHeight(), null);
      }
      else {
        int w = this.getWidth();
        int h = this.getHeight();
        g2d.drawImage(unit, 0, 0, this.getWidth(), this.getHeight(), null);
        if (smell != null) {
          g2d.drawImage(smell, 0, 0, this.getWidth(), this.getHeight(), null);
        }
        int playerX = readOnlyFacade.getPlayerX();
        int playerY = readOnlyFacade.getPlayerY();
        int playerIndex = readOnlyFacade.getGrid()[playerY][playerX].getIndex();
        if (playerIndex == index) {
          g2d.drawImage(imageResource.get("player"), this.getWidth() / 3,
                  this.getHeight() / 3, this.getWidth() / 3, this.getHeight() / 3, null);
        }
        int offsetX = 0;
        int offsetY = 0;
        for (Image itemImage : items) {
          if (offsetX + 30 > this.getWidth()) {
            offsetX = 0;
            offsetY += 30;
          }
          g2d.drawImage(itemImage, offsetX, offsetY, 30, 30, null);
          offsetX += 30;
        }
      }

    }

    protected void renewContent(List<Image> items, Image smell) {
      this.items = items;
      this.smell = smell;
    }

    public int getIndex() {
      return index;
    }
  }

}
