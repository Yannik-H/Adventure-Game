package viewer;

import controller.DungeonMvcController;

import java.io.IOException;

/**
 * This is a mock view for testing the controller in MVC.
 */
public class MockView implements DungeonView {

  Appendable log;

  /**
   * Constructor only takes a appendable param as input for recording message.
   * @param log The appendable object for recording.
   */
  public MockView(Appendable log) {
    this.log = log;
  }


  @Override
  public void refresh() {
    // This method did nothing, just for simply overwrite.
  }

  @Override
  public void makeVisible() {
    // This method did nothing, just for simply overwrite.
  }

  @Override
  public void addClickedListener(DungeonMvcController listener) {
    // This method did nothing, just for simply overwrite.
  }

  @Override
  public void addClickedListenerToDungeonPanel(DungeonMvcController listener) {
    // This method did nothing, just for simply overwrite.
  }

  @Override
  public void addKeyListener(DungeonMvcController listener) {
    // This method did nothing, just for simply overwrite.
  }

  @Override
  public void setPromptInformation(String info) {
    try {
      log.append(info);
    } catch (IOException ioe) {
      throw new IllegalStateException();
    }
  }

  @Override
  public void initializeDungeonView() {
    // This method did nothing, just for simply overwrite.
  }

  @Override
  public void renewDungeonUnitContent(int x, int y) {
    // This method did nothing, just for simply overwrite.
  }

  @Override
  public void renewAllDungeonUnits() {
    // This method did nothing, just for simply overwrite.
  }

  @Override
  public void renewDungeonUnitContentWithoutVisited() {
    // This method did nothing, just for simply overwrite.
  }

  @Override
  public void waitShootingDirection() {
    // This method did nothing, just for simply overwrite.
  }

  @Override
  public void resetDungeonPanel() {
    try {
      log.append("Reset successfully.");
    } catch (IOException ioe) {
      throw new IllegalStateException();
    }
  }

  @Override
  public void setShootingDirectionSettingStatus(boolean shootingDirectionSettingStatus) {
    // This method did nothing, just for simply overwrite.
  }
}
