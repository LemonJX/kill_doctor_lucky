package world;

/**
 * An interface representing a player.
 */
public interface Item {

  /**
   * Get the index of room the item located.
   * 
   * @return the index of room.
   */
  public int getRoom();
  
  /**
   * Get the damage of the item.
   * 
   * @return the damage value.
   */
  public int getDamage();
  
  /**
   * The item is picked by a player.
   */
  public void picked();
}
