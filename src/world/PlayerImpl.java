package world;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements Player interface and represent a player.
 */
public class PlayerImpl implements Player {
  //protected static final int CAPACITY = 5;
  private String name;
  private Room room;
  private boolean isHuman;
  private List<Item> items;
  private int holdItemDamage;

  /**
   * Protected constructor for use by subclasses.
   * 
   * @param name  the name of this player.
   * @param room  the initial location of this player.
   * @param isHuman whether the player is controlled by human.
   * @throws IllegalArgumentException for invalid arguments.
   */
  public PlayerImpl(String name, Room room, boolean isHuman) throws IllegalArgumentException {
    if (name == null || room == null) {
      throw new IllegalArgumentException("invalid argument");
    }
    this.name = name;
    this.room = room;
    this.isHuman = isHuman;
    this.items = new ArrayList<Item>();
    this.holdItemDamage = 0;
  }
  
  @Override
  public String itemInfo() {
    String myItems = "";
    for (Item i : this.items) {
      myItems += " " + i.toString() + ";";
    }
    return myItems;
  }
  
  @Override
  public String information() {
    return String.format("%s (in %s):%s\n", this.name, this.room.toString(), this.itemInfo());
  }
  
  @Override
  public String getName() {
    return this.name;
  }
  
  @Override
  public Room getRoom() {
    return this.room;
  }
  
  @Override
  public boolean isHuman() {
    return this.isHuman;
  }
  
  @Override
  public boolean full() {
    return this.items.size() >= Player.CAPACITY;
  }
  
  @Override
  public void addItem(Item item) {
    if (item == null) {
      throw new IllegalArgumentException("invalid argument for addItem()");
    }
    if (this.full()) {
      throw new IllegalStateException("Your bag is full and cannot hold any more items");
    }
    this.items.add(item);
  }
  
  @Override
  public void moveTo(Room room) throws IllegalArgumentException {
    if (!this.room.nextTo(room) || room == null) {
      throw new IllegalArgumentException("You are not moving to a neighboring room.");
    }
    this.room = room;
  }
  
  @Override
  public boolean noItem() {
    return this.items.isEmpty();
  }
  
  @Override
  public void automaticHoldItem() throws IllegalStateException {
    if (this.noItem()) {
      throw new IllegalStateException("No item");
    }
    int damage = 0;
    Item chosen = null;
    for (Item i : this.items) {
      if (i.getDamage() > damage) {
        damage = i.getDamage();
        chosen = i;
      }
    }
    this.items.remove(chosen);
    this.holdItemDamage = damage;
  }
  
  @Override
  public void humanHoldItem(int itemIndex) throws IllegalStateException, IllegalArgumentException {
    if (this.noItem()) {
      throw new IllegalStateException("No item");
    }
    if (!this.isHuman) {
      throw new IllegalStateException("Computer player cannot choose an item by a user.");
    }
    if (itemIndex < 0 || itemIndex >= this.items.size()) {
      throw new IllegalArgumentException("invalid itemIndex");
    }
    Item chosen = this.items.remove(itemIndex);
    this.holdItemDamage = chosen.getDamage();
  }
  
  @Override
  public int getItemDamage() {
    return this.holdItemDamage;
  }
  
  @Override
  public void usedUpItem() {
    this.holdItemDamage = 0;
  }
  
  @Override
  public String toString() {
    return String.format("%s (in %s)", name, room.toString());
    //return this.name;
  }
}
