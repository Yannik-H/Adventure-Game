package viewer;

import controller.DungeonMvcController;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


/**
 * This class used for set the parameters of the dungeon. Such as the width, length, the percentage
 * of caves having treasures.
 */
public class SettingWindow extends JDialog {

  private final JTextField nRowInput;
  private final JTextField nColInput;
  private final JTextField interConnectivityInput;
  private final JTextField percentageInput;
  private final JTextField nMonsterInput;
  private final JRadioButton yesButton;
  private final JButton submitButton;
  private final JButton resetButton;
  private final ButtonGroup wrapGroup;

  /**
   * This window will pop up when the setting button is clicked.
   */
  public SettingWindow() {

    setSize(400, 400);
    this.setLayout(new GridLayout(7, 2));

    nRowInput = new JTextField();
    nColInput = new JTextField();
    interConnectivityInput = new JTextField();
    percentageInput = new JTextField();
    nMonsterInput = new JTextField();

    JLabel nRow = new JLabel("Row");
    JLabel nCol = new JLabel("Col");
    JLabel interConnectivity = new JLabel("Inter connectivity");
    JLabel percentage = new JLabel("Percentage");
    JLabel wrap = new JLabel("Wrap");
    JLabel nMonster = new JLabel("Monster");

    Container container = getContentPane();

    container.add(nRow);
    container.add(nRowInput);
    container.add(nCol);
    container.add(nColInput);
    container.add(interConnectivity);
    container.add(interConnectivityInput);
    container.add(percentage);
    container.add(percentageInput);
    container.add(nMonster);
    container.add(nMonsterInput);
    container.add(wrap);

    yesButton = new JRadioButton("Yes");
    JRadioButton noButton = new JRadioButton("No");
    wrapGroup = new ButtonGroup();
    wrapGroup.add(yesButton);
    wrapGroup.add(noButton);
    JPanel buttonGroupPanel = new JPanel();
    buttonGroupPanel.add(yesButton);
    buttonGroupPanel.add(noButton);
    container.add(buttonGroupPanel);

    submitButton = new JButton("Submit");
    resetButton = new JButton("Reset");

    container.add(submitButton);
    container.add(resetButton);

    nRowInput.setText("4");
    nColInput.setText("3");
    interConnectivityInput.setText("3");
    percentageInput.setText("20");
    nMonsterInput.setText("2");
    yesButton.setSelected(true);
  }

  /**
   * This method used for apply action performer in the controller on two button here.
   * @param listener  The MVC controller of the project.
   */
  public void addClickedListener(DungeonMvcController listener) {
    JDialog thisWindow = this;
    MouseAdapter submitClicked = new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        String wrap = null;
        if (yesButton.isSelected()) {
          wrap = "y";
        }
        else {
          wrap = "n";
        }
        thisWindow.dispose();
        listener.receiveParams(nColInput.getText(), nRowInput.getText(),
                interConnectivityInput.getText(), percentageInput.getText(),
                wrap, nMonsterInput.getText());
      }
    };
    submitButton.addMouseListener(submitClicked);

    MouseAdapter resetClicked = new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        nRowInput.setText("");
        nColInput.setText("");
        interConnectivityInput.setText("");
        percentageInput.setText("");
        nMonsterInput.setText("");
        wrapGroup.clearSelection();
      }
    };
    resetButton.addMouseListener(resetClicked);
  }

}
