package controller;

import world.World;

/**
 * Represents a Controller for the game in the World.
 */
public interface TextBaseController {

  /**
   * Execute a game of the World Model.
   *
   * @param w         a non-null the World Model
   * @param maxTurn   the maximum number of turns in the game. 
   * @throws IllegalArgumentException for invalid arguments.
   */
  void playGame(World w, int maxTurn) throws IllegalArgumentException;
}
