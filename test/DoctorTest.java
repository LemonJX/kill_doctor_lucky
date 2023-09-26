import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import world.Doctor;
import world.DoctorImpl;

/**
 * This JUnit test is used to test the Doctor interface and the DoctorImpl class.
 */
public class DoctorTest {
  private Doctor dr;
  
  @Before
  public void setup() {
    dr = new DoctorImpl("Doctor Lucky", 50, 3);
  }

  @Test
  public void moveTest() {
    assertEquals(dr.getRoom(), 0);
    dr.move();
    assertEquals(dr.getRoom(), 1);
    dr.move();
    assertEquals(dr.getRoom(), 2);
    dr.move();
    assertEquals(dr.getRoom(), 0);
  }

  @Test
  public void getHealthTest() {
    assertEquals(dr.getHealth(), 50);
  }
  
  @Test
  public void killTest() {
    assertFalse(dr.isKilled());
    
    dr.beHurt(10);
    assertEquals(dr.getHealth(), 40);
    assertFalse(dr.isKilled());
    
    dr.beHurt(40);
    assertEquals(dr.getHealth(), 0);
    assertTrue(dr.isKilled());
  }

  @Test
  public void toStringTest() {
    assertEquals(dr.toString(), "Doctor Lucky");
  }

}
