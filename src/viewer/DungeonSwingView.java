package viewer;

import controller.DungeonMvcController;
import dungeon.ReadOnlyFacade;
import location.Direction;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;


/**
 * This concrete class implements its interface {@link DungeonView} and extends JFrame.
 * Its constructor takes a read-only model facade.
 */
public class DungeonSwingView extends JFrame implements DungeonView {

  private final ReadOnlyFacade readOnlyFacade;
  private final DungeonMenuBar dungeonMenubar;
  private final DungeonMessageTextArea dungeonMessageTextArea;
  private final DungeonPanel dungeonPanel;
  private boolean shootingDirectionSettingStatus;


  /**
   * This constructor takes in a read-only model facade.
   * @param readOnlyFacade a read-only model facade.
   */
  public DungeonSwingView(ReadOnlyFacade readOnlyFacade) {

    super("Dungeon");

    if (readOnlyFacade == null) {
      throw new IllegalArgumentException("Model facade can not be null");
    }

    this.setFocusable(true);
    this.setSize(400,300);
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.setLayout(new BorderLayout());
    JPanel centerPane = new JPanel();
    this.add(centerPane, BorderLayout.CENTER);

    centerPane.setLayout(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.BOTH;

    this.readOnlyFacade = readOnlyFacade;
    this.shootingDirectionSettingStatus = false;

    this.dungeonMenubar = new DungeonMenuBar();
    //centerPane.add(dungeonMenubar, gbc);
    this.setJMenuBar(dungeonMenubar);

    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 4;
    gbc.gridheight = 10;
    gbc.weightx = 0.4;
    gbc.weighty = 1;
    this.dungeonMessageTextArea = new DungeonMessageTextArea();
    JScrollPane scrollPane = new JScrollPane(dungeonMessageTextArea);
    centerPane.add(scrollPane, gbc);

    gbc.gridx = 4;
    gbc.gridy = 0;
    gbc.gridwidth = 6;
    gbc.gridheight = 10;
    gbc.weightx = 0.6;
    gbc.weighty = 1;
    dungeonPanel = new DungeonPanel();
    JScrollPane dungeonScrollPane = new JScrollPane(dungeonPanel);
    centerPane.add(dungeonScrollPane, gbc);

  }

  @Override
  public void addClickedListener(DungeonMvcController listener) {
    dungeonMenubar.addSettingSelectedListener(listener);
    dungeonMenubar.addStartSelectedListener(listener);
    dungeonMenubar.addResetSelectedListener(listener);
  }

  @Override
  public void addClickedListenerToDungeonPanel(DungeonMvcController listener) {
    dungeonPanel.addClickedListener(listener);
  }

  private void shootingWindowHelper(Direction direction, DungeonMvcController listener) {
    ShootWindow shootWindow = new ShootWindow(direction);
    shootWindow.addClickedListener(listener);
    shootWindow.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
    shootWindow.setLocation(50, 50);
    shootWindow.setVisible(true);
  }



  @Override
  public void addKeyListener(DungeonMvcController listener) {
    KeyAdapter keyTyped = new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP & readOnlyFacade.getGrid() != null
                & !shootingDirectionSettingStatus) {
          listener.move(Direction.NORTH);
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP & readOnlyFacade.getGrid() != null
                & shootingDirectionSettingStatus) {
          dungeonPanel.setEnabled(true);
          shootingWindowHelper(Direction.NORTH, listener);
          shootingDirectionSettingStatus = false;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT & readOnlyFacade.getGrid() != null
                & !shootingDirectionSettingStatus) {
          listener.move(Direction.EAST);
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT & readOnlyFacade.getGrid() != null
                & shootingDirectionSettingStatus) {
          dungeonPanel.setEnabled(true);
          shootingWindowHelper(Direction.EAST, listener);
          shootingDirectionSettingStatus = false;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN & readOnlyFacade.getGrid() != null
                & !shootingDirectionSettingStatus) {
          listener.move(Direction.SOUTH);
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN & readOnlyFacade.getGrid() != null
                & shootingDirectionSettingStatus) {
          dungeonPanel.setEnabled(true);
          shootingWindowHelper(Direction.SOUTH, listener);
          shootingDirectionSettingStatus = false;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT & readOnlyFacade.getGrid() != null
                & !shootingDirectionSettingStatus) {
          listener.move(Direction.WEST);
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT & readOnlyFacade.getGrid() != null
                & shootingDirectionSettingStatus) {
          dungeonPanel.setEnabled(true);
          shootingWindowHelper(Direction.WEST, listener);
          shootingDirectionSettingStatus = false;
        }
        else if (e.getKeyCode() == KeyEvent.VK_S) {
          listener.waitDirection();
        }
        else if (e.getKeyCode() == KeyEvent.VK_P) {
          listener.pickPerformer();
        }
        else if (e.getKeyCode() == KeyEvent.VK_D) {
          listener.describePlayer();
        }
      }
    };
    this.addKeyListener(keyTyped);
  }



  @Override
  public void setPromptInformation(String info) {
    this.dungeonMessageTextArea.setText(info);
  }

  @Override
  public void initializeDungeonView() {
    dungeonPanel.setReadOnlyFacade(readOnlyFacade);
    dungeonPanel.initializeDungeonView();
    dungeonPanel.repaint();
    this.refresh();
  }

  @Override
  public void renewDungeonUnitContent(int x, int y) {
    dungeonPanel.renewComponent(x, y);
    dungeonPanel.repaint();
  }

  @Override
  public void renewDungeonUnitContentWithoutVisited() {
    dungeonPanel.renewAllComponentWithoutVisited();
    dungeonPanel.repaint();
  }

  @Override
  public void renewAllDungeonUnits() {
    dungeonPanel.renewAllComponent();
    dungeonPanel.repaint();
  }

  @Override
  public void waitShootingDirection() {
    this.shootingDirectionSettingStatus = true;
    this.dungeonPanel.setEnabled(false);
  }

  @Override
  public void resetDungeonPanel() {
    this.dispose();
  }

  @Override
  public void setShootingDirectionSettingStatus(boolean shootingDirectionSettingStatus) {
    this.shootingDirectionSettingStatus = shootingDirectionSettingStatus;
  }

  @Override
  public void refresh() {
    repaint();
  }

  @Override
  public void makeVisible() {
    setVisible(true);
  }
}
