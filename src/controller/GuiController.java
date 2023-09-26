package controller;

import world.World;

/**
 * Represents a Controller for GUI.
 */
public interface GuiController {
  /**
   * Start a game.
   */
  public void playGame();
  
  /**
   * Change the model for the controller.
   * @param model the model to change.
   * @throws IllegalArgumentException for invalid model.
   */
  public void changeModel(World model) throws IllegalArgumentException;
  
  /**
   * Let the model to create a player.
   * @param name    player's name.
   * @param room    player's room index.
   * @param isHuman whether player is controlled by human.
   * @throws IllegalArgumentException for invalid arguments.
   */
  public void createPlayer(String name, int room, boolean isHuman) throws IllegalArgumentException;
  
  /**
   * Start playing when finishing players creation.
   */
  public void start();

  /**
   * Handle a click on a room to move the player, or on a player to show its information.
   *
   * @param row the row of the click
   * @param col the column of the click
   * @throws IllegalArgumentException for invalid arguments.
   */
  public void handleClick(int row, int col) throws IllegalArgumentException;

  /**
   * Handle a click on a room to move the pet.
   * @param row the row of the click
   * @param col the column of the click
   * @throws IllegalArgumentException for invalid arguments.
   */
  public void handleRightClick(int row, int col) throws IllegalArgumentException;
  
  /**
   * Handle pressing L.
   */
  public void handleKeyL();
  
  /**
   * Handle pressing P.
   */
  public void handleKeyP();
  
  /**
   * Handle pressing A.
   */
  public void handleKeyA();

  /**
   * Let computer players take action until it's a human player's turn.
   */
  public void computerActionLoop();
}
