package world;

/**
 * An interface representing Dr.Lucky.
 */
public interface Doctor {
  /**
   * Get the health of Dr.Lucky.
   * 
   * @return the health of Dr.Lucky.
   */
  public int getHealth();

  /**
   * Get the index of room Dr.Lucky located.
   * 
   * @return the the index of room.
   */
  public int getRoom();
  
  /**
   * Move Dr.Lucky to the next room.
   */
  public void move();
  
  /**
   * Move Dr.Lucky to the specified room. This is used in testing computer players.
   * The attacks of computer players are random, often when the target is out of the room. 
   * Moving targets to their room makes it easy to test whether the attack is working well. 
   * @param room  the index of the specified room.
   */
  public void moveTo(int room);
  
  /**
   * Reduce the health of Dr.Lucky.
   * 
   * @param damage  the amount of health to reduce.
   * @throws IllegalArgumentException for invalid damage value.
   */
  public void beHurt(int damage) throws IllegalArgumentException;
  
  /**
   * Check whether Dr.Lucky is killed by a player.
   * @return whether Dr.Lucky is killed.
   */
  public boolean isKilled();
}
