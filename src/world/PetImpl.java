package world;

/**
 * This class implements the Pet interface and represent a pet.
 */
public class PetImpl implements Pet {
  private int room;
  private String name;
  private int totalRoom;
  
  /**
   * Constructor.
   * 
   * @param name  the name of the pet.
   * @param room  the index of room that the pet located.
   * @param totalRoom the total number of rooms in the world of the pet.
   * @throws IllegalArgumentException for invalid arguments.
   */
  public PetImpl(String name, int room, int totalRoom) throws IllegalArgumentException {
    if (room < 0 || room >= totalRoom || name == null) {
      throw new IllegalArgumentException("invalid argument");
    }
    this.name = name;
    this.room = room;
    this.totalRoom = totalRoom;
  }
  
  @Override
  public int getRoom() {
    return this.room;
  }

  @Override
  public void move(int room) throws IllegalArgumentException {
    if (room < 0 || room >= this.totalRoom) {
      throw new IllegalArgumentException("invalid argument");
    }
    this.room = room;
  }
  
  @Override
  public String toString() {
    return this.name;
  }
}
