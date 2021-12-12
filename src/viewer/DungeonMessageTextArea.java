package viewer;

import javax.swing.JTextArea;

class DungeonMessageTextArea extends JTextArea {

  public DungeonMessageTextArea() {
    super();

    this.setEditable(false);
    //this.setLineWrap(true);
    this.setWrapStyleWord(true);
    this.setText("Set the dungeon first! \n"
            + "Press S to shoot \n"
            + "Press P to pick up \n"
            + "Press D for location information.");
  }
}
