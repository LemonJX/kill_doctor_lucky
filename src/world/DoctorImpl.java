package world;

/**
 * This class implements the Doctor interface and represent Dr. Lucky.
 */
public class DoctorImpl implements Doctor {
  private int health;
  private String name;
  private int room;
  private final int totalRoom;
  private boolean killed;
  
  /**
   * Constructor.
   * @param name  the name of target character.
   * @param health  the health of target character.
   * @param totalRoom the total number of rooms that the Doctor can move through.
   * @throws IllegalArgumentException for invalid arguments.
   */
  public DoctorImpl(String name, int health, int totalRoom) throws IllegalArgumentException {
    if (name == null || health <= 0 || totalRoom <= 0) {
      throw new IllegalArgumentException("invalid argument");
    }
    this.name = name;
    this.health = health;
    this.room = 0;
    this.totalRoom = totalRoom;
    this.killed = false;
  }
  
  @Override
  public int getHealth() {
    return this.health;
  }
  
  @Override
  public int getRoom() {
    return this.room;
  }

  @Override
  public void move() {
    this.room = (this.room + 1) % totalRoom;
  }
  
  @Override
  public void moveTo(int room) {
    this.room = room;
  }
  
  @Override
  public void beHurt(int damage) throws IllegalArgumentException {
    if (damage < 0) {
      throw new IllegalArgumentException("invalid damage value");
    }
    this.health -= damage;
    if (this.health <= 0) {
      this.killed = true;
    }
  }
  
  @Override
  public boolean isKilled() {
    return this.killed;
  }

  @Override
  public String toString() {
    return this.name;
  }
}
