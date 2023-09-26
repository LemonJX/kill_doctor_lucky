import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import world.Item;
import world.ItemImpl;
import world.Player;
import world.PlayerImpl;
import world.Room;
import world.RoomImpl;

/**
 * This JUnit test is used to test the Player interface and its implementation classes.
 */
public class PlayerTest {
  private Room room1;
  private Room room2;
  private Room room3;
  private Player human;
  private Player computer;
  
  /**
   * Set up rooms and players for the following tests.
   */
  @Before
  public void setup() {
    room1 = new RoomImpl("Room 1", 0, 0, 10, 20, 30); 
    room2 = new RoomImpl("Room 2", 1, 21, 10, 40, 30); 
    room3 = new RoomImpl("Room 3", 2, 5, 31, 10, 40); 
    human = new PlayerImpl("Human", room1, true);
    computer = new PlayerImpl("Computer", room2, false);
  }

  @Test
  public void getterTest() {
    assertEquals(human.getName(), "Human");
    assertEquals(computer.getName(), "Computer");
    assertEquals(human.getRoom(), room1);
    assertEquals(computer.getRoom(), room2);
  }

  @Test
  public void moveToTest() {
    computer.moveTo(room1);
    assertEquals(computer.information(), "Computer (in (0)Room 1):\n");
    human.moveTo(room3);
    assertEquals(human.information(), "Human (in (2)Room 3):\n");
    try {
      human.moveTo(room2);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "You are not moving to a neighboring room.");
    }
  }
  
  @Test
  public void addItemTest() {
    human.addItem(new ItemImpl(0, 0, "A"));
    human.addItem(new ItemImpl(0, 0, "B"));
    human.addItem(new ItemImpl(0, 0, "C"));
    human.addItem(new ItemImpl(0, 0, "D"));
    human.addItem(new ItemImpl(0, 0, "E"));
    assertEquals(human.information(), "Human (in (0)Room 1): A; B; C; D; E;\n");
    assertTrue(human.full());
    try {
      human.addItem(new ItemImpl(0, 0, "F"));
      fail();
    } catch (IllegalStateException e) {
      assertEquals(e.getMessage(), "Your bag is full and cannot hold any more items");
    }
  }
  
  @Test
  public void holdItemTest() {
    assertTrue(human.noItem());
    assertTrue(computer.noItem());
    assertEquals(human.getItemDamage(), 0);
    assertEquals(computer.getItemDamage(), 0);
    try {
      human.humanHoldItem(0);
      fail();
    } catch (IllegalStateException e) {
      assertEquals(e.getMessage(), "No item");
    }
    try {
      computer.automaticHoldItem();
      fail();
    } catch (IllegalStateException e) {
      assertEquals(e.getMessage(), "No item");
    }
    
    Item a = new ItemImpl(0, 1, "A");
    Item b = new ItemImpl(0, 2, "B");
    Item c = new ItemImpl(0, 3, "C");
    human.addItem(a);
    human.addItem(b);
    human.addItem(c);
    computer.addItem(a);
    computer.addItem(b);
    computer.addItem(c);
    assertFalse(human.noItem());
    assertFalse(computer.noItem());
    
    computer.automaticHoldItem();
    assertEquals(computer.getItemDamage(), 3);
    computer.usedUpItem();
    assertEquals(computer.getItemDamage(), 0);
    computer.automaticHoldItem();
    assertEquals(computer.getItemDamage(), 2);
    computer.usedUpItem();
    assertEquals(computer.getItemDamage(), 0);
    computer.automaticHoldItem();
    assertEquals(computer.getItemDamage(), 1);
    computer.usedUpItem();
    assertEquals(computer.getItemDamage(), 0);
    assertTrue(computer.noItem());
    
    try {
      human.humanHoldItem(-1);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "invalid itemIndex");
    }
    try {
      human.humanHoldItem(3);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "invalid itemIndex");
    }
    
    human.humanHoldItem(2);
    assertEquals(human.getItemDamage(), 3);
    human.usedUpItem();
    assertEquals(human.getItemDamage(), 0);
    human.humanHoldItem(0);
    assertEquals(human.getItemDamage(), 1);
    human.usedUpItem();
    assertEquals(human.getItemDamage(), 0);
    human.humanHoldItem(0);
    assertEquals(human.getItemDamage(), 2);
    human.usedUpItem();
    assertEquals(human.getItemDamage(), 0);
    assertTrue(human.noItem());
    
    try {
      computer.addItem(a);
      computer.humanHoldItem(0);
      fail();
    } catch (IllegalStateException e) {
      assertEquals(e.getMessage(), "Computer player cannot choose an item by a user.");
    }
  }

  @Test
  public void toStringTest() {
    assertEquals(human.toString(), "Human (in (0)Room 1)");
    assertEquals(computer.toString(), "Computer (in (1)Room 2)");
  }

}
