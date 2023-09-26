package view;

import controller.GuiController;

/**
 * A view for the game: display the map and provide visual interface for users.
 */
public interface View {

  /**
   * Set up the controller to handle some events in this view.
   * @param listener the controller
   */
  void addListener(GuiController listener) throws IllegalArgumentException;
  
  /**
   * Open a dialog to choose a file and change to new model from the file.
   * @return whether succeed to change to new model.
   */
  public boolean openFileDialog();
  
  /**
   * Set the panel in the view for "Create Players" stage.
   */
  public void setPanelForCreatePlayer();
  
  /**
   * Show created players on a label according to user's input.
   */
  public void refreshPlayersLabel();
  
  /**
   * Set the panel in the view for "Play Game" stage.
   */
  public void setPanelForGame();

  /**
   * move the current player's icon to its room.
   */
  public void movePlayerIcon();
  
  /**
   * Set text in the action result label.
   * @param str the text to show in the label.
   */
  public void setHumanActionText(String str);
  
  /**
   * Add an action in computer actions list.
   * @param str the action of a computer player.
   */
  public void addComputerAction(String str);
  
  /**
   * Set text in the game state label.
   * @param str the text to show in the label.
   */
  public void setGameStateText(String str);
  
  /**
   * Set text in the turn label.
   * @param str the text to show in the label.
   */
  public void setTurnText(String str);
  
  /**
   * Set text in the player's information label.
   * @param str the text to show in the label.
   */
  public void setPlayerInfoText(String str);
  
  /**
   * Set the current player's icon to black.
   */
  public void setCurrentPlayerToBlack();
  
  /**
   * Set the current player's icon to blue.
   */
  public void setCurrentPlayerToBlue();
  
  /**
   * Get the player's index from a click on a player's icon.
   * @param row the row of the click.
   * @param col the column of the click.
   * @return the player index. If cannot find a player, return -1.
   */
  public int getPlayerFromClick(int row, int col) throws IllegalArgumentException;
  
  /**
   * Set the position and size of the target icon.
   */
  public void setTargetIcon();

  /**
   * Make the view visible to start the game session.
   */
  void makeVisible();
}
