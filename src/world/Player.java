package world;

/**
 * An interface representing a player.
 */
public interface Player {
  int CAPACITY = 5;
  
  /**
   * Get the items of this player.
   * @return the items of this player.
   */
  public String itemInfo();
  
  /**
   * Give the information of this player, including its position and items.
   * 
   * @return a string of information.
   */
  public String information();
  
  /**
   * Get the name of this player.
   * 
   * @return the name.
   */
  public String getName();
  
  /**
   * Get the room that this player locates at.
   * 
   * @return the room of player.
   */
  public Room getRoom();
  
  /**
   * Return whether this player is controlled by human.
   * @return whether this player is controlled by human.
   */
  public boolean isHuman();
  
  /**
   * Add the given item to this player's bag.
   * 
   * @param item  the item to add.
   */
  public void addItem(Item item);
  
  /**
   * Move the player to the given room.
   * 
   * @param room  the new position of the player.
   * @throws IllegalArgumentException for an invalid room, which is not a neighbor.
   */
  public void moveTo(Room room) throws IllegalArgumentException;
  
  /**
   * Check whether the player's bag is full and cannot pick any more items.
   * @return whether the player's bag is full.
   */
  public boolean full();
  
  /**
   * Check whether the player has items in his bag.
   * @return whether the player has items.
   */
  public boolean noItem();
  
  /**
   * Automatically hold the item with the highest damage to kill the target.
   * @throws IllegalStateException for a human player.
   */
  public void automaticHoldItem() throws IllegalStateException;

  /**
   * A human player hold a chosen item to kill the target.
   * @param itemIndex the index of chosen item.
   * @throws IllegalStateException for a computer player.
   *         IllegalArgumentException for invalid argument.
   */
  public void humanHoldItem(int itemIndex) throws IllegalStateException, IllegalArgumentException;
  
  /**
   * Get the damage value of the item that the player is holding.
   * @return the damage value of the item that the player is holding.
   */
  public int getItemDamage();
  
  /**
   * Clear the damage value of the item that the player is holding when the item is used up.
   */
  public void usedUpItem();
}
