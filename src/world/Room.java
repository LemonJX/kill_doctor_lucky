package world;

import java.util.List;

/**
 * An interface representing a room in the World.
 */
public interface Room {
  
  /**
   * Get the index of this room.
   * @return the index.
   */
  public int getIndex();
  
  /**
   * Get the name of this room.
   * @return the name.
   */
  public String getName();
  
  /**
   * Get the 4 edges of this room.
   * @return the edges in the order of up, down, left, right.
   */
  public int[] getEdges();
  
  /**
   * Get the players in this room.
   * @param allPlayers  all the players in the game.
   * @return the players in this room.
   * @throws IllegalArgumentException for invalid argument.
   */
  public List<Player> getPlayers(List<Player> allPlayers) throws IllegalArgumentException;
  
  /**
   * Place the given item to this room.
   * @param item the item to place here.
   * @throws IllegalArgumentException for invalid argument.
   */
  public void addItem(Item item) throws IllegalArgumentException;
  
  /**
   * Get the items in this room.
   * @return the items in this room.
   */
  public List<Item> getItem();
  
  /**
   * Remove the item at itemIndex in this room and return the item.
   * @param itemIndex the index of the item in this room.
   * @return the removed item.
   * @throws IllegalArgumentException for invalid itemIndex.
   */
  public Item pickItem(int itemIndex) throws IllegalArgumentException;
  
  /**
   * Get whether the given room is next to this room.
   * @param other the given room.
   * @return whether the given room is next to this room.
   * @throws IllegalArgumentException for invalid argument.
   */
  public boolean nextTo(Room other) throws IllegalArgumentException;
  
  /**
   * Set the neighbors of this room.
   * @param rooms a list of all rooms in the World.
   * @throws IllegalArgumentException for invalid argument.
   */
  public void setNeighbors(List<Room> rooms) throws IllegalArgumentException;
  
  /**
   * Get the neighbors of this room.
   * @return a list of neighbors of this room.
   */
  public List<Room> getNeighbors();
  
  /**
   * Choose the ith neighbor of this room.
   * @param i the index of the chosen neighbor in this room.
   * @return the index of the chosen neighbor in the whole World.
   * @throws IllegalArgumentException for invalid argument.
   */
  public int chooseNeighbor(int i) throws IllegalArgumentException;
  
  /**
   * Give the information of this room when a player is looking around, 
   * including its name, players, items and neighbors. 
   * @param allPlayers  all the players in the game.
   * @return a string of information contains the name, players, items and neighbors of this room.
   * @throws IllegalArgumentException for invalid argument.
   */
  public String information(List<Player> allPlayers) throws IllegalArgumentException;
  
  /**
   * Give the information of this room when its neighbor is asking for information,
   * including its name, players, and items.
   * @param allPlayers  all the players in the game.
   * @return a string of information contains the name, players, and items of this room.
   * @throws IllegalArgumentException for invalid argument.
   */
  public String neighborInfo(List<Player> allPlayers) throws IllegalArgumentException;
  
  /**
   * Give the information of this room in html version (use <br/> to replace \n) 
   * for looking around, including its name, players, items and neighbors. 
   * @param allPlayers  all the players in the game.
   * @return a string of information of this room in html version.
   * @throws IllegalArgumentException for invalid argument.
   */
  public String informationForView(List<Player> allPlayers) throws IllegalArgumentException;
  
  /**
   * Give the information of this room in html version (use <br/> to replace \n) 
   * when its neighbor is asking for information, including its name, players, and items.
   * @param allPlayers  all the players in the game.
   * @return a string of information of this room in html version.
   * @throws IllegalArgumentException for invalid argument.
   */
  public String neighborInfoForView(List<Player> allPlayers) throws IllegalArgumentException;
  
  /**
   * Move the pet to this room.
   */
  public void petArrive();
  
  /**
   * Remove the pet from this room.
   */
  public void petLeave();
  
  /**
   * Check whether this room is invisible from its neighbors.
   * @return whether this room is invisible.
   */
  public boolean invisible();
  
  /**
   * Return the players that can see the attack in this room, including the attacker himself.
   * If this room is invisible, only the players in this room can see the attack.
   * If this room is visible, the players in this room and the neighboring room 
   * (include the invisible neighboring room from here) can see the attack.
   * @param allPlayers  all the players in the game.
   * @return a list of players that can see the attack in this room.
   * @throws IllegalArgumentException for invalid argument.
   */
  public List<Player> witness(List<Player> allPlayers) throws IllegalArgumentException;
}
