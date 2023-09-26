import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import world.Item;
import world.ItemImpl;

/**
 * This JUnit test is used to test the Item interface and the ItemImpl class.
 */
public class ItemTest {
  private Item instance;

  @Before
  public void setup() {
    instance = new ItemImpl(0, 5, "Knife");
  }
  
  @Test
  public void getRoomTest() {
    assertEquals(instance.getRoom(), 0);
  }

  @Test
  public void getDamageTest() {
    assertEquals(instance.getDamage(), 5);
  }
  
  @Test
  public void pickedTest() {
    instance.picked();
    assertEquals(instance.getRoom(), -1);
  }
  
  @Test
  public void toStringTest() {
    assertEquals(instance.toString(), "Knife");
  }
}
