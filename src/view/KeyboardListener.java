package view;

import controller.GuiController;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This class capture action on keyboard.
 */
public class KeyboardListener implements KeyListener {
  private GuiController controller;

  /**
   * Constructor.
   * @param c the controller of the game.
   * @throws IllegalArgumentException for invalid controller.
   */
  public KeyboardListener(GuiController c) throws IllegalArgumentException {
    if (c == null) {
      throw new IllegalArgumentException();
    }
    this.controller = c;
  }
  
  @Override
  public void keyPressed(KeyEvent e) {
    if (e.getKeyChar() == 'l') {
      this.controller.handleKeyL();
    } else if (e.getKeyChar() == 'p') {
      this.controller.handleKeyP();
    } else if (e.getKeyChar() == 'a') {
      this.controller.handleKeyA();
    }
  }
  
  @Override
  public void keyReleased(KeyEvent e) {
    // Do nothing
  }
  
  @Override
  public void keyTyped(KeyEvent e) {
    // Do nothing
  }
}
