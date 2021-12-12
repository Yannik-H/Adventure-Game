package viewer;

import controller.DungeonMvcController;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

class DungeonMenuBar extends JMenuBar {

  private final JMenu settingMenu;
  private final JMenu reStart;
  private final JMenu reset;

  public DungeonMenuBar() {
    super();

    settingMenu = new JMenu("Setting");
    reStart = new JMenu("Restart");
    reset = new JMenu("Reset");

    this.add(settingMenu);
    this.add(reStart);
    this.add(reset);

    reStart.setEnabled(false);
    reset.setEnabled(false);

  }

  void addSettingSelectedListener(DungeonMvcController listener) {
    MouseAdapter settingSelectedAdapter = new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        listener.startSetting();
        settingMenu.setEnabled(false);
        reStart.setEnabled(true);
        reset.setEnabled(true);
      }
    };

    this.settingMenu.addMouseListener(settingSelectedAdapter);
  }

  void addStartSelectedListener(DungeonMvcController listener) {
    MouseAdapter startSelectedAdapter = new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        settingMenu.setEnabled(false);
        reset.setEnabled(true);
        listener.reStartGame();
      }
    };

    this.reStart.addMouseListener(startSelectedAdapter);
  }

  void addResetSelectedListener(DungeonMvcController listener) {
    MouseAdapter resetSelectedAdapter = new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        settingMenu.setEnabled(true);
        reset.setEnabled(false);
        reStart.setEnabled(false);
        listener.resetPerformer();
      }
    };

    this.reset.addMouseListener(resetSelectedAdapter);
  }
}
