package view;

import controller.GuiController;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class captures clicks and passes them to the controller.
 */
public class MapClick extends MouseAdapter {
  private GuiController controller;
  
  /**
   * Constructor.
   * @param c the controller of the game.
   * @throws IllegalArgumentException for invalid controller.
   */
  public MapClick(GuiController c) throws IllegalArgumentException {
    if (c == null) {
      throw new IllegalArgumentException();
    }
    this.controller = c;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    // getX: column, getY: row.
    if (e.getButton() == MouseEvent.BUTTON3) {
      this.controller.handleRightClick(e.getY(), e.getX());
    } else if (e.getButton() == MouseEvent.BUTTON1) {
      this.controller.handleClick(e.getY(), e.getX());
    }
  }
}
