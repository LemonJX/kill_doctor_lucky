import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import world.Item;
import world.ItemImpl;
import world.Player;
import world.PlayerImpl;
import world.Room;
import world.RoomImpl;

/**
 * This JUnit test is used to test the Room interface and the RoomImpl class.
 */
public class RoomTest {
  private Room instance1;
  private Room instance2;
  private Room instance3;

  /**
   * Set up three test samples.
   */
  @Before
  public void setup() {
    instance1 = new RoomImpl("Room 1", 0, 0, 10, 20, 30); 
    instance2 = new RoomImpl("Room 2", 1, 21, 10, 40, 30); 
    instance3 = new RoomImpl("Room 3", 2, 5, 31, 10, 40); 
  }
  
  @Test
  public void getterTest() {
    // getIndex()
    assertEquals(instance1.getIndex(), 0);
    assertEquals(instance2.getIndex(), 1);
    assertEquals(instance3.getIndex(), 2);
    
    // getName()
    assertEquals(instance1.getName(), "Room 1");
    assertEquals(instance2.getName(), "Room 2");
    assertEquals(instance3.getName(), "Room 3");
    
    // getEdges()
    assertEquals(instance1.getEdges()[0], 0);
    assertEquals(instance1.getEdges()[1], 20);
    assertEquals(instance1.getEdges()[2], 10);
    assertEquals(instance1.getEdges()[3], 30);
    
    assertEquals(instance2.getEdges()[0], 21);
    assertEquals(instance2.getEdges()[1], 40);
    assertEquals(instance2.getEdges()[2], 10);
    assertEquals(instance2.getEdges()[3], 30);
    
    assertEquals(instance3.getEdges()[0], 5);
    assertEquals(instance3.getEdges()[1], 10);
    assertEquals(instance3.getEdges()[2], 31);
    assertEquals(instance3.getEdges()[3], 40);
  }
  
  @Test
  public void nextToTest() {
    assertTrue(instance1.nextTo(instance2));
    assertTrue(instance1.nextTo(instance3));
    assertFalse(instance2.nextTo(instance3));
  }
  
  @Test
  public void itemTest() {
    Item knife = new ItemImpl(0, 5, "Knife"); 
    Item candy = new ItemImpl(0, 2, "Candy");
    Item cake = new ItemImpl(0, 1, "Cake");
    instance1.addItem(knife);
    instance1.addItem(candy);
    instance1.addItem(cake);
    assertEquals(instance1.getItem().get(0), knife);
    assertEquals(instance1.getItem().get(1), candy);
    assertEquals(instance1.getItem().get(2), cake);
    String expect1 = "(0)Room 1\n[Players]\n[Items] Knife; Candy; Cake;\n[Neighbors]\n";
    assertEquals(instance1.information(new ArrayList<Player>()), expect1);
    String expectView1 = "look around (0)Room 1: <br/>[Players]<br/>"
        + "[Items] Knife; Candy; Cake;<br/>[Neighbors]<br/>";
    assertEquals(instance1.informationForView(new ArrayList<Player>()), expectView1);
    
    instance1.pickItem(0);  // Pick knife at index 0.
    assertEquals(instance1.getItem().get(0), candy);
    assertEquals(instance1.getItem().get(1), cake);
    String expect2 = "(0)Room 1\n[Players]\n[Items] Candy; Cake;\n[Neighbors]\n";
    assertEquals(instance1.information(new ArrayList<Player>()), expect2);
    String expectView2 = "look around (0)Room 1: <br/>[Players]<br/>"
        + "[Items] Candy; Cake;<br/>[Neighbors]<br/>";
    assertEquals(instance1.informationForView(new ArrayList<Player>()), expectView2);
    
    instance1.pickItem(1);  // Pick cake at index 1.
    assertEquals(instance1.getItem().get(0), candy);
    String expect3 = "(0)Room 1\n[Players]\n[Items] Candy;\n[Neighbors]\n";
    assertEquals(instance1.information(new ArrayList<Player>()), expect3);
    String expectView3 = "look around (0)Room 1: <br/>[Players]<br/>"
        + "[Items] Candy;<br/>[Neighbors]<br/>";
    assertEquals(instance1.informationForView(new ArrayList<Player>()), expectView3);
  }
  
  @Test
  public void playerTest() {
    List<Player> players = new ArrayList<Player>();
    Player player1 = new PlayerImpl("A", instance1, true);
    Player player2 = new PlayerImpl("B", instance2, true);
    Player player3 = new PlayerImpl("C", instance3, true);
    players.add(player1);
    players.add(player2);
    players.add(player3);
    
    assertEquals(instance1.getPlayers(players).get(0), player1);
    String room1 = "(0)Room 1\n[Players] A;\n[Items]\n[Neighbors]\n";
    assertEquals(instance1.information(players), room1);
    
    assertEquals(instance2.getPlayers(players).get(0), player2);
    String room2 = "(1)Room 2\n[Players] B;\n[Items]\n[Neighbors]\n";
    assertEquals(instance2.information(players), room2);
    
    assertEquals(instance3.getPlayers(players).get(0), player3);
    String room3 = "(2)Room 3\n[Players] C;\n[Items]\n[Neighbors]\n";
    assertEquals(instance3.information(players), room3);
    
    player1.moveTo(instance2);
    
    assertEquals(instance1.getPlayers(players).size(), 0);
    String newRoom1 = "(0)Room 1\n[Players]\n[Items]\n[Neighbors]\n";
    assertEquals(instance1.information(players), newRoom1);
    
    assertEquals(instance2.getPlayers(players).get(0), player1);
    assertEquals(instance2.getPlayers(players).get(1), player2);
    String newRoom2 = "(1)Room 2\n[Players] A; B;\n[Items]\n[Neighbors]\n";
    assertEquals(instance2.information(players), newRoom2);
  }
  
  @Test
  public void neighborTest() {
    // get neighbors
    List<Room> rooms = new ArrayList<Room>();
    rooms.add(instance1);
    rooms.add(instance2);
    rooms.add(instance3);
    instance1.setNeighbors(rooms);
    assertEquals(instance1.getNeighbors().get(0), instance2);
    assertEquals(instance1.getNeighbors().get(1), instance3);
    String expect = "(0)Room 1\n[Players]\n[Items]\n[Neighbors]\n"
        + "(1)Room 2: [Players] /; [Items] /;\n"
        + "(2)Room 3: [Players] /; [Items] /;\n";
    assertEquals(instance1.information(new ArrayList<Player>()), expect);
    
    // choose neighbors for moving
    assertEquals(instance1.chooseNeighbor(0), 1); // get index of instance2
    assertEquals(instance1.chooseNeighbor(1), 2); // get index of instance3
  }
  
  @Test
  public void petAndWitnessTest() {
    // set neighbors
    List<Room> rooms = new ArrayList<Room>();
    rooms.add(instance1);
    rooms.add(instance2);
    rooms.add(instance3);
    instance1.setNeighbors(rooms);
    instance2.setNeighbors(rooms);
    instance3.setNeighbors(rooms);
    //set players
    List<Player> players = new ArrayList<Player>();
    Player player1 = new PlayerImpl("A", instance1, true);
    Player player2 = new PlayerImpl("B", instance2, true);
    Player player3 = new PlayerImpl("C", instance3, true);
    players.add(player1);
    players.add(player2);
    players.add(player3);
    
    assertFalse(instance1.invisible());
    assertFalse(instance2.invisible());
    assertFalse(instance3.invisible());
    
    // Room2's information shows Room1 is a neighbor.
    String expect1 = "(1)Room 2\n[Players] B;\n[Items]\n[Neighbors]\n"
        + "(0)Room 1: [Players] A; [Items] /;\n";
    assertEquals(instance2.information(players), expect1);
    String expectView1 = "look around (1)Room 2: <br/>[Players] B;<br/>"
        + "[Items]<br/>[Neighbors]<br/>(0)Room 1: [Players] A; [Items] /;<br/>";
    assertEquals(instance2.informationForView(players), expectView1);
    // witness: Room1 - A,B,C; Room2 - A,B; Room3 - A,C; 
    assertEquals(instance1.witness(players).size(), 3);
    assertEquals(instance2.witness(players).size(), 2);
    assertEquals(instance3.witness(players).size(), 2);
    assertEquals(instance1.witness(players).get(0), player1);
    assertEquals(instance1.witness(players).get(1), player2);
    assertEquals(instance1.witness(players).get(2), player3);
    assertEquals(instance2.witness(players).get(0), player2);
    assertEquals(instance2.witness(players).get(1), player1);
    assertEquals(instance3.witness(players).get(0), player3);
    assertEquals(instance3.witness(players).get(1), player1);
    
    // When a pet is in Room1, Room1 is invisible from Room2.
    instance1.petArrive();
    assertTrue(instance1.invisible());
    String expect2 = "(1)Room 2\n[Players] B;\n[Items]\n[Neighbors]\n";
    assertEquals(instance2.information(players), expect2);
    String expectView2 = "look around (1)Room 2: <br/>[Players] B;<br/>"
        + "[Items]<br/>[Neighbors]<br/>";
    assertEquals(instance2.informationForView(players), expectView2);
    // witness: Room1 - A; Room2 - A,B; Room3 - A,C; 
    assertEquals(instance1.witness(players).size(), 1);
    assertEquals(instance2.witness(players).size(), 2);
    assertEquals(instance3.witness(players).size(), 2);
    assertEquals(instance1.witness(players).get(0), player1);
    assertEquals(instance2.witness(players).get(0), player2);
    assertEquals(instance2.witness(players).get(1), player1);
    assertEquals(instance3.witness(players).get(0), player3);
    assertEquals(instance3.witness(players).get(1), player1);
    
    // When the pet leaves Room1, Room1 is again visible from Room2.
    instance1.petLeave();
    assertFalse(instance1.invisible());
    String expect3 = "(1)Room 2\n[Players] B;\n[Items]\n[Neighbors]\n"
        + "(0)Room 1: [Players] A; [Items] /;\n";
    assertEquals(instance2.information(players), expect3);
    String expectView3 = "look around (1)Room 2: <br/>[Players] B;<br/>[Items]<br/>"
        + "[Neighbors]<br/>(0)Room 1: [Players] A; [Items] /;<br/>";
    assertEquals(instance2.informationForView(players), expectView3);
    // witness: Room1 - A,B,C; Room2 - A,B; Room3 - A,C; 
    assertEquals(instance1.witness(players).size(), 3);
    assertEquals(instance2.witness(players).size(), 2);
    assertEquals(instance3.witness(players).size(), 2);
    assertEquals(instance1.witness(players).get(0), player1);
    assertEquals(instance1.witness(players).get(1), player2);
    assertEquals(instance1.witness(players).get(2), player3);
    assertEquals(instance2.witness(players).get(0), player2);
    assertEquals(instance2.witness(players).get(1), player1);
    assertEquals(instance3.witness(players).get(0), player3);
    assertEquals(instance3.witness(players).get(1), player1);
  }
  
  @Test
  public void toStringTest() {
    assertEquals(instance1.toString(), "(0)Room 1");
    assertEquals(instance2.toString(), "(1)Room 2");
    assertEquals(instance3.toString(), "(2)Room 3");
  }
}
