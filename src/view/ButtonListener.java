package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class action on buttons.
 */
public class ButtonListener implements ActionListener {
  private View view;
  
  
  /**
   * Constructor.
   * @param view the view to accept the actions.
   * @throws IllegalArgumentException for invalid view.
   */
  public ButtonListener(View view) throws IllegalArgumentException {
    super();
    if (view == null) {
      throw new IllegalArgumentException();
    }
    this.view = view;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if ("start by a default world".equals(e.getActionCommand())) {
      view.setPanelForCreatePlayer();
    }
    if ("start by your world".equals(e.getActionCommand())) {
      if (view.openFileDialog()) {
        view.setPanelForCreatePlayer();
      }
    }
    if ("create".equals(e.getActionCommand())) {
      view.refreshPlayersLabel();
    }
    if ("finish".equals(e.getActionCommand())) {
      view.setPanelForGame();
    }
  }
}
