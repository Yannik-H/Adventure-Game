package viewer;

import controller.DungeonMvcController;
import location.Direction;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * This class used for getting the shooting distance.
 */
class ShootWindow extends JDialog {

  private final JTextField nStepInput;
  private final Direction direction;
  private final JButton confirm;

  /**
   * This window will pop out when "s" is pressed.
   */
  public ShootWindow(Direction direction) {

    setSize(500, 80);
    this.setLayout(new GridLayout(1, 3));

    JLabel distance = new JLabel("Shooting distance");
    nStepInput = new JTextField();
    confirm = new JButton("Confirm");

    this.add(distance);
    this.add(nStepInput);
    this.add(confirm);
    this.direction = direction;
  }

  private boolean isLegalInteger(String input) {
    for (int i = 0; i < input.length(); i++) {
      if (!Character.isDigit(input.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  void addClickedListener(DungeonMvcController listener) {
    JDialog thisWindow = this;
    MouseAdapter confirmClicked = new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        if (isLegalInteger(nStepInput.getText())) {
          listener.shootPerformer(direction, Integer.parseInt(nStepInput.getText()));
          thisWindow.dispose();
        }
      }
    };
    confirm.addMouseListener(confirmClicked);
  }
}
