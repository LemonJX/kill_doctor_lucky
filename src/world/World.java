package world;

import java.awt.Graphics;
import java.util.List;

/**
 * An interface representing a world.
 */
public interface World {
  int PLAYERCAPACITY = 10;
  
  /**
   * Generate a PNG file showing the map of the World.
   */
  public void generateMap();
  
  /**
   * Draw the map on a panel to show in view.
   * @param g the Graphics of a JPanel to draw the map.
   */
  public void drawMap(Graphics g);
  
  /**
   * Get the room index from the position of a click.
   * @param row the y coordinate of the click.
   * @param col the z coordinate of the click.
   * @return the room index of the click.
   * @throws IllegalArgumentException for invalid argument.
   */
  public int getRoomFromClick(int row, int col) throws IllegalArgumentException;
  
  /**
   * Get the number of rooms in the World.
   * @return the number of rooms.
   */
  public int getRoomNumber();
  
  /**
   * Get an array of rooms for room selection in JComboBox in View.
   * @return the array of rooms for JComboBox.
   */
  public String[] getRooms();
  
  /**
   * Check whether the room index is valid.
   * @param room the index of room.
   * @return whether the room index is valid.
   */
  public boolean validRoomIndex(int room);
  
  /**
   * In each turn, move Dr. Lucky to the next room and increase the turn number by one.
   */
  public void moveDoctor();
  
  /**
   * In each turn, move Dr. Lucky to the room of current player.
   * This method is for testing whether attack is working well for a player.
   */
  public void moveDoctorForTest();
  
  /**
   * Get the room that Dr. Lucky located.
   * @return  the room with Dr. Lucky.
   */
  public Room getDoctorRoom();
  
  /**
   * Create a player in the World.
   * 
   * @param name  the name of the player.
   * @param room  the index of the room the player starts at.
   * @param human whether the player is controlled by human. 
   *              true: controlled by human; false: controlled by computer.
   * @throws IllegalArgumentException for invalid room index.
   */
  public void createPlayer(String name, int room, boolean human) throws IllegalArgumentException;
  
  /**
   * Check whether there exist computer players in this game.
   * @return whether there exist computer players in this game.
   */
  public boolean hasComputerPlayer();
  
  /**
   * Return a list of all players.
   * @return a list of all players.
   */
  public List<Player> getPlayersList();
  
  /**
   * Return all the players in the world.
   * @return a string containing all the players in the world.
   */
  public String players();
  
  /**
   * Return all the players in the world in html version for showing in a JLabel.
   * @return a string containing all the players in the world in html.
   */
  public String playersForView();
  
  /**
   * Return the number of players in the world.
   * @return the number of players in the world.
   */
  public int playerNumber();
  
  /**
   * Set the maximum number of turns.
   * [Attention] This will be called after all players are created.
   * 
   * @param maxTurn the maximum number of turns.
   * @throws IllegalArgumentException for invalid maximum number of turns.
   */
  public void setTurn(int maxTurn) throws IllegalArgumentException;
  
  /**
   * Get the index of the player of this turn.
   * @return the index of player of this turn.
   */
  public int getTurnIndex();
  
  /**
   * Get the player of this turn.
   * @return the player of this turn.
   * @throws IllegalStateException for exceeding maximum number of turns.
   */
  public Player getTurn() throws IllegalStateException;
  
  /**
   * Get whether the game is over.
   * @return whether the game is over.
   */
  public boolean gameIsOver();
  
  /**
   * The player looks around and get the information of its room.
   * @return the information of the player's current room.
   */
  public String lookAround();
  
  /**
   * The player looks around and get the information of its room for the view.
   * @return the information of the player's current room in html version.
   */
  public String lookAroundForView();
  
  /**
   * The player picks a specified item in the room the player locates at.
   * @param itemIndex the index of item to pick in the room.
   * @return a description of this pick: who picks what item.
   * @throws IllegalArgumentException for invalid arguments.
   */
  public String pick(int itemIndex) throws IllegalArgumentException;
  
  /**
   * Check whether there exists any item in the room the player locates at.
   * @return whether there exists an item to pick.
   */
  public boolean noItemFound();
  
  /**
   * Move the player to a neighboring room.
   * @param roomIndex the index of the destination room.
   * @throws IllegalArgumentException for invalid arguments.
   */
  public void move(int roomIndex) throws IllegalArgumentException;
  
  /**
   * Move the player to a neighboring room. Do nothing for invalid index.
   * @param roomIndex the index of the destination room.
   * @return whether the move is successful.
   */
  public boolean moveForView(int roomIndex);
  
  /**
   * Move the pet to a specified room.
   * @param roomIndex  the index of the destination room.
   * @return a string to describe the current position of the pet.
   * @throws IllegalArgumentException for invalid arguments.
   */
  public String movePet(int roomIndex) throws IllegalArgumentException;
  
  /**
   * Check whether Dr.Lucky is in the current room.
   * @return whether Dr.Lucky is in the current room.
   */
  public boolean validAttack();
  
  /**
   * Attempt to attack Dr.Lucky.
   * @return the result of the attack.
   */
  public String attack();
  
  /**
   * Get the health of Dr.Lucky.
   * @return the health of Dr.Lucky.
   */
  public int getDrHealth();
  
  /**
   * Show the information of all players.
   * @return a string of information of all players.
   */
  public String result();
}
