import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import world.Pet;
import world.PetImpl;

/**
 * This JUnit test is used to test the Pet interface and the PetImpl class.
 */
public class PetTest {
  private Pet instance;

  @Before
  public void setup() {
    instance = new PetImpl("Fortune the Cat", 0, 21);
  }
  
  @Test
  public void getRoomTest() {
    assertEquals(instance.getRoom(), 0);
  }
  
  @Test
  public void moveTest() {
    instance.move(1);
    assertEquals(instance.getRoom(), 1);
    instance.move(20);
    assertEquals(instance.getRoom(), 20);
    try {
      instance.move(21);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "invalid argument");
    }
    try {
      instance.move(-1);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "invalid argument");
    }
  }
  
  @Test
  public void toStringTest() {
    assertEquals(instance.toString(), "Fortune the Cat");
  }

}
