package world;

/**
 * An interface representing a pet.
 */
public interface Pet {

  /**
   * Get the index of room the pet located.
   * 
   * @return the index of room.
   */
  public int getRoom();
  
  /**
   * Move the item to the specified room.
   * 
   * @param room  the index of the destination of this move.
   * @throws IllegalArgumentException for invalid room index.
   */
  public void move(int room) throws IllegalArgumentException;
}
