package world;

/**
 * This class implements the Player interface and represent a player.
 */
public class ItemImpl implements Item {
  private int room;
  private int damage;
  private String name;
  
  /**
   * Constructor.
   * @param room  the index of room that this item located.
   * @param damage  the damage value of this item.
   * @param name  the name of this item.
   * @throws IllegalArgumentException for invalid arguments.
   */
  public ItemImpl(int room, int damage, String name) throws IllegalArgumentException {
    if (room < 0 || damage < 0 || name == null) {
      throw new IllegalArgumentException("invalid argument");
    }
    this.name = name;
    this.room = room;
    this.damage = damage;
  }
  
  @Override
  public int getRoom() {
    return this.room;
  }

  @Override
  public int getDamage() {
    return this.damage;
  }

  @Override
  public void picked() {
    this.room = -1;
  }
  
  @Override
  public String toString() {
    return this.name;
  }
}
