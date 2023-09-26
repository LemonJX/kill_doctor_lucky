import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.FileReader;
import org.junit.Test;
import world.World;
import world.WorldImpl;

/**
 * This JUnit test is used to test the World interface and the WorldImpl class.
 */
public class WorldTest {

  @Test
  public void worldTest() {
    try {
      Readable file = new FileReader("res/mansion.txt");
      World instance = new WorldImpl(file);
      assertEquals(instance.getDoctorRoom().toString(), "(0)Armory");
      String world = "Doctor Lucky's Mansion: row 36 col 30, 21 rooms, 20 items.\n"
          + "(0)Armory; (1)Billiard Room; (2)Carriage House; (3)Dining Hall; (4)Drawing Room; "
          + "(5)Foyer; \n"
          + "(6)Green House; (7)Hedge Maze; (8)Kitchen; (9)Lancaster Room; (10)Library; \n"
          + "(11)Lilac Room; (12)Master Suite; (13)Nursery; (14)Parlor; (15)Piazza; \n"
          + "(16)Servants' Quarters; (17)Tennessee Room; (18)Trophy Room; (19)Wine Cellar; "
          + "(20)Winter Garden; \n"
          + "Target: Doctor Lucky(health 5, in room 0).\n"
          + "Pet: Fortune the Cat(in room 0).\n";
      assertEquals(instance.toString(), world);
      //System.out.print(instance.toString());
      
      // getRoomNumber()
      assertEquals(instance.getRoomNumber(), 21);
      // validRoomIndex()
      assertTrue(instance.validRoomIndex(0));
      assertTrue(instance.validRoomIndex(20));
      assertFalse(instance.validRoomIndex(21));
      assertFalse(instance.validRoomIndex(-1));
      
      // Players
      instance.createPlayer("Human1", 0, true);
      instance.createPlayer("Human2", 1, true);
      instance.createPlayer("Computer1", 2, false);
      String players = "Human1 (in (0)Armory)\nHuman2 (in (1)Billiard Room)\n"
          + "Computer1 (in (2)Carriage House)\n";
      assertEquals(instance.players(), players);
      
      // Move doctor and turns
      instance.setTurn(6);
      // Turn 1
      assertEquals(instance.getTurn().toString(), "Human1 (in (0)Armory)");
      instance.moveDoctor();
      assertEquals(instance.getDoctorRoom().toString(), "(1)Billiard Room");
      // Turn 2
      assertEquals(instance.getTurn().toString(), "Human2 (in (1)Billiard Room)");
      instance.moveDoctor();
      assertEquals(instance.getDoctorRoom().toString(), "(2)Carriage House");
      // Turn 3
      assertEquals(instance.getTurn().toString(), "Computer1 (in (2)Carriage House)");
      instance.moveDoctor();
      assertEquals(instance.getDoctorRoom().toString(), "(3)Dining Hall");
      // Turn 4
      assertEquals(instance.getTurn().toString(), "Human1 (in (0)Armory)");
      instance.moveDoctor();
      assertEquals(instance.getDoctorRoom().toString(), "(4)Drawing Room");
      // Turn 5
      assertEquals(instance.getTurn().toString(), "Human2 (in (1)Billiard Room)");
      instance.moveDoctor();
      assertEquals(instance.getDoctorRoom().toString(), "(5)Foyer");
      // Turn 6
      assertEquals(instance.getTurn().toString(), "Computer1 (in (2)Carriage House)");
      instance.moveDoctor();
      assertEquals(instance.getDoctorRoom().toString(), "(6)Green House");
      // Game over when reach the maximum turn
      assertTrue(instance.gameIsOver());
      // Result
      String result = "Oh no! Doctor Lucky has escaped. His remaining health: 5.\n";
      assertEquals(instance.result(), result);
      // Throw exception for keep going the game when exceeding maximum turn
      try {
        instance.getTurn();
        fail();
      } catch (IllegalStateException e) {
        assertEquals(e.getMessage(), "Already reach the maximum turn.");
      }
      // Check the doctor can move from the last room to the first room.
      for (int i = 0; i < instance.getRoomNumber(); i++) {
        instance.moveDoctor();
      }
      assertEquals(instance.getDoctorRoom().toString(), "(6)Green House");
    } catch (FileNotFoundException e) {
      fail();
    }
  }
  
  @Test
  public void worldTest2() {
    try {
      Readable file = new FileReader("res/mansion.txt");
      World instance = new WorldImpl(file);
      instance.createPlayer("Human1", 0, true);
      instance.setTurn(14);
      
      // Turn 1: Test look around
      String roomInfo1 = "(0)Armory\n[Players] Human1;\n"
          + "[Items] Revolver;\n"
          + "[Neighbors]\n"
          + "(1)Billiard Room: [Players] /; [Items] Billiard Cue;\n"
          + "(3)Dining Hall: [Players] /; [Items] /;\n"
          + "(4)Drawing Room: [Players] /; [Items] Letter Opener;\n"
          + "Doctor Lucky is in this room!\n"
          + "Fortune the Cat is in this room!\n";
      assertEquals(instance.lookAround(), roomInfo1);
      String roomInfoForView1 = "<html><body><p>Human1 look around (0)Armory: <br/>"
          + "[Players] Human1;<br/>"
          + "[Items] Revolver;<br/>"
          + "[Neighbors]<br/>"
          + "(1)Billiard Room: [Players] /; [Items] Billiard Cue;<br/>"
          + "(3)Dining Hall: [Players] /; [Items] /;<br/>"
          + "(4)Drawing Room: [Players] /; [Items] Letter Opener;<br/>"
          + "Fortune the Cat is in this room!</p></body></html>";
      assertEquals(instance.lookAroundForView(), roomInfoForView1);
      instance.moveDoctor();
      
      // Turn 2: Test move
      instance.move(1);
      String roomInfo2 = "(1)Billiard Room\n[Players] Human1;\n"
          + "[Items] Billiard Cue;\n"
          + "[Neighbors]\n"
          + "(3)Dining Hall: [Players] /; [Items] /;\n"
          + "(18)Trophy Room: [Players] /; [Items] Duck Decoy; Monkey Hand;\n"
          + "Doctor Lucky is in this room!\n";
      assertEquals(instance.lookAround(), roomInfo2);
      String roomInfoForView2 = "<html><body><p>Human1 look around (1)Billiard Room: <br/>"
          + "[Players] Human1;<br/>"
          + "[Items] Billiard Cue;<br/>"
          + "[Neighbors]<br/>"
          + "(3)Dining Hall: [Players] /; [Items] /;<br/>"
          + "(18)Trophy Room: [Players] /; [Items] Duck Decoy; Monkey Hand;<br/>"
          + "</p></body></html>";
      assertEquals(instance.lookAroundForView(), roomInfoForView2);
      instance.moveDoctor();
      
      // Turn 3: Test move exception
      try {
        instance.move(2);
        fail();
      } catch (IllegalArgumentException e) {
        assertEquals(e.getMessage(), "Not a neighboring room.");
      }
      instance.moveDoctor();
      
      // Turn 4: Test pick: item removed from room and appears in player's bag.
      instance.pick(0);
      String playerInfo = "Human1 (in (1)Billiard Room): Billiard Cue;\n";
      assertEquals(instance.getTurn().information(), playerInfo);
      String roomInfo3 = "(1)Billiard Room\n[Players] Human1;\n"
          + "[Items]\n[Neighbors]\n"
          + "(3)Dining Hall: [Players] /; [Items] /;\n"
          + "(18)Trophy Room: [Players] /; [Items] Duck Decoy; Monkey Hand;\n";
      assertEquals(instance.lookAround(), roomInfo3);
      String roomInfoForView3 = "<html><body><p>Human1 look around (1)Billiard Room: <br/>"
          + "[Players] Human1;<br/>"
          + "[Items]<br/>"
          + "[Neighbors]<br/>"
          + "(3)Dining Hall: [Players] /; [Items] /;<br/>"
          + "(18)Trophy Room: [Players] /; [Items] Duck Decoy; Monkey Hand;<br/>"
          + "</p></body></html>";
      assertEquals(instance.lookAroundForView(), roomInfoForView3);
      instance.moveDoctor();
      
      // Turn 5 - 6: Test no item to pick after move to an empty room
      instance.move(3);
      instance.moveDoctor();
      assertTrue(instance.noItemFound());
      try {
        instance.pick(0);
      } catch (IllegalArgumentException e) {
        assertEquals(e.getMessage(), "No such item here.");
      }
      instance.moveDoctor();
      
      // Turn 7 - 14: Test cannot pick more than 5 items.
      instance.move(8);
      instance.moveDoctor();
      instance.pick(1); // 2nd item: Sharp Knife in room 8
      instance.moveDoctor();
      instance.pick(0); // 3rd item: Crepe Pan in room 8
      instance.moveDoctor();
      instance.move(19);
      instance.moveDoctor();
      instance.pick(0); // 4th item: Rat Poison in room 19
      instance.moveDoctor();
      instance.pick(0); // 5th item: Piece of Rope in room 19
      instance.moveDoctor();
      instance.move(4);
      instance.moveDoctor();
      try {
        instance.pick(0); // Try to pick the 6th item
      } catch (IllegalArgumentException e) {
        assertEquals(e.getMessage(), "Your bag is full and cannot pick any more items.");
      }
      instance.moveDoctor();

      // Game over when reach the maximum turn
      assertTrue(instance.gameIsOver());
      // Result
      String result = "Oh no! Doctor Lucky has escaped. His remaining health: 5.\n";
      assertEquals(instance.result(), result);
    } catch (FileNotFoundException e) {
      fail();
    }
  }
  
  @Test
  public void worldTest3() {
    // In order to simplify the test, moveDoctorForTest() is used instead of moveDoctor()
    // to move the doctor to current player's room in each turn.
    try {
      Readable file = new FileReader("res/testfiles/mansion_health10.txt");
      World instance = new WorldImpl(file);
      instance.createPlayer("Human1", 0, true);
      instance.setTurn(20);
      
      // Turn 1: Attack without any item
      assertTrue(instance.getTurn().noItem());
      assertEquals(instance.attack(), "[Successful attack] Target -1, current health 9.");
      instance.moveDoctorForTest();
      
      // Turn 2-6: Pick items
      instance.pick(0); // Revolver (3)
      instance.moveDoctorForTest();
      instance.move(1);
      instance.moveDoctorForTest();
      instance.pick(0); // Billiard Cue (2)
      instance.moveDoctorForTest();
      instance.move(18);
      instance.moveDoctorForTest();
      instance.pick(0); // Duck Decoy (3)
      instance.moveDoctor();  // Dr.Lucky leaves current room
      
      // Turn 7: Attack without target
      assertFalse(instance.validAttack());
      assertEquals(instance.attack(), "[Invalid attack] Target is not in this room.");
      instance.moveDoctorForTest();
      
      // Turn 8-10: Attack with a chosen item
      assertTrue(instance.validAttack());
      assertFalse(instance.getTurn().noItem());
      instance.getTurn().humanHoldItem(2); // Duck Decoy (3)
      assertEquals(instance.attack(), "[Successful attack] Target -3, current health 6.");
      instance.moveDoctorForTest();
      
      assertTrue(instance.validAttack());
      assertFalse(instance.getTurn().noItem());
      instance.getTurn().humanHoldItem(1); // Billiard Cue (2)
      assertEquals(instance.attack(), "[Successful attack] Target -2, current health 4.");
      instance.moveDoctorForTest();
      
      assertTrue(instance.validAttack());
      assertFalse(instance.getTurn().noItem());
      instance.getTurn().humanHoldItem(0); // Revolver (3)
      assertEquals(instance.attack(), "[Successful attack] Target -3, current health 1.");
      instance.moveDoctorForTest();
      
      // Turn 11: Attack without any item
      assertTrue(instance.validAttack());
      assertTrue(instance.getTurn().noItem());
      assertEquals(instance.attack(), "[Successful attack] Target -1, current health 0.");
      instance.moveDoctorForTest();

      // Game over when Dr.Lucky is killed.
      assertTrue(instance.gameIsOver());
      String result = "Congratulations! Human1 kills Doctor Lucky and wins the game.\n";
      assertEquals(instance.result(), result);
    } catch (FileNotFoundException e) {
      fail();
    }
  }
  
  @Test
  public void worldTest4() {
    try {
      Readable file = new FileReader("res/mansion.txt");
      World instance = new WorldImpl(file);
      instance.createPlayer("A", 0, true);
      instance.createPlayer("B", 0, true);
      instance.setTurn(2);
      assertTrue(instance.getTurn().noItem());
      // A's attack is seen by B.
      assertEquals(instance.attack(), "[Invalid attack] Someone witnessed your attack.");
    } catch (FileNotFoundException e) {
      fail();
    }
  }
  
  @Test
  public void worldTest5() {
    try {
      Readable file = new FileReader("res/mansion.txt");
      World instance = new WorldImpl(file);
      instance.createPlayer("A", 0, true);
      instance.createPlayer("B", 0, true);
      instance.setTurn(16);
      
      // Turn 1: A picks Revolver (3)
      instance.pick(0);
      instance.moveDoctor();
      
      // Turn 2-3: B & A moves to room 4
      instance.move(4); // B
      instance.moveDoctor();
      instance.move(4); // A
      instance.moveDoctor();
      
      // Turn 4: B picks Letter Opener(2)
      instance.pick(0);
      instance.moveDoctor();
      
      // Turn 5: A attack with an item and be seen by B.
      instance.getTurn().humanHoldItem(0);
      assertEquals(instance.attack(), "[Invalid attack] Someone witnessed your attack.");
      assertTrue(instance.getTurn().noItem());  // A's item used up
      instance.moveDoctor();
      
      // Turn 6-10: Pick items and move to find target.
      instance.move(5); // B
      instance.moveDoctor();
      instance.move(3); // A
      instance.moveDoctor();
      instance.move(15); // B
      instance.moveDoctor();
      instance.move(14); // A
      instance.moveDoctor();
      instance.pick(0); // B picks Civil War Cannon(3).
      instance.moveDoctor();
      
      // Turn 11-14: Wait for target to arrive the current room of players.
      instance.moveDoctor(); // A
      instance.moveDoctor(); // B
      instance.moveDoctor(); // A
      instance.moveDoctor(); // B
      
      // Turn 15: A attack without item
      assertTrue(instance.getTurn().noItem());
      assertEquals(instance.attack(), "[Successful attack] Target -1, current health 4.");
      instance.moveDoctor();

      // Turn 16: B attack with Civil War Cannon(3)
      instance.getTurn().humanHoldItem(1);
      assertEquals(instance.attack(), "[Successful attack] Target -3, current health 1.");
      // Civil War Cannon(3) is used up.
      assertEquals(instance.getTurn().itemInfo(), " Letter Opener;");
      instance.moveDoctor();
      
      // Game over.
      assertTrue(instance.gameIsOver());
      String result = "Oh no! Doctor Lucky has escaped. His remaining health: 1.\n";
      assertEquals(instance.result(), result);
    } catch (FileNotFoundException e) {
      fail();
    }
  }
  
  @Test
  public void worldTest6() {
    try {
      Readable file = new FileReader("res/mansion.txt");
      World instance = new WorldImpl(file);
      instance.createPlayer("A", 0, true);
      instance.createPlayer("B", 1, true);
      instance.setTurn(10);

      // Pet is in room 0.
      // Turn 1: A attack in a invisible room with B in a neighboring room.
      assertEquals(instance.attack(), "[Successful attack] Target -1, current health 4.");
      instance.moveDoctor();
      
      // Turn 2-3: look around: A can see B, but B cannot see A.
      // B: 
      String info1 = "(1)Billiard Room\n[Players] B;\n[Items] Billiard Cue;\n"
          + "[Neighbors]\n"
          + "(3)Dining Hall: [Players] /; [Items] /;\n"
          + "(18)Trophy Room: [Players] /; [Items] Duck Decoy; Monkey Hand;\n"
          + "Doctor Lucky is in this room!\n";
      assertEquals(instance.lookAround(), info1);
      instance.moveDoctor();
      // A: 
      String info2 = "(0)Armory\n[Players] A;\n[Items] Revolver;\n"
          + "[Neighbors]\n"
          + "(1)Billiard Room: [Players] B; [Items] Billiard Cue;\n"
          + "(3)Dining Hall: [Players] /; [Items] /;\n"
          + "(4)Drawing Room: [Players] /; [Items] Letter Opener;\n"
          + "Fortune the Cat is in this room!\n";
      assertEquals(instance.lookAround(), info2);
      //System.out.println(instance.lookAround());
      instance.moveDoctor();

      // Turn 4: B moves the pet to room 8
      assertEquals(instance.movePet(8), "Fortune the Cat is in (8)Kitchen.");
      instance.moveDoctor();
      
      // Turn 5-6: A & B move to room 3
      instance.move(3);
      instance.moveDoctor();
      instance.move(3);
      instance.moveDoctor();
      
      // Turn 7: A moves to room 8
      instance.move(8);
      instance.moveDoctor();
      
      // Turn 8: B cannot see neighbor room 8 since the pet is in room 8.
      String info3 = "(3)Dining Hall\n[Players] B;\n[Items]\n[Neighbors]\n"
          + "(0)Armory: [Players] /; [Items] Revolver;\n"
          + "(1)Billiard Room: [Players] /; [Items] Billiard Cue;\n"
          + "(4)Drawing Room: [Players] /; [Items] Letter Opener;\n"
          + "(14)Parlor: [Players] /; [Items] /;\n"
          + "(17)Tennessee Room: [Players] /; [Items] /;\n"
          + "(18)Trophy Room: [Players] /; [Items] Duck Decoy; Monkey Hand;\n"
          + "(19)Wine Cellar: [Players] /; [Items] Rat Poison; Piece of Rope;\n";
      assertEquals(instance.lookAround(), info3);
      instance.moveDoctor();
      
      // Turn 9: A attack successfully since this room is invisible from B.
      assertEquals(instance.attack(), "[Successful attack] Target -1, current health 3.");
      instance.attack();
      instance.moveDoctor();
      
    } catch (FileNotFoundException e) {
      fail();
    }
  }

  @Test
  public void exception1Test() {
    try {
      new WorldImpl(null);
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "Invalid filename");
    }
  }
  
  @Test
  public void exception2Test() {  // [room] 30 six 35 11 Winter Garden
    try {
      Readable file = new FileReader("res/testfiles/mansion_test1.txt");
      new WorldImpl(file);
      fail();
    } catch (FileNotFoundException exp) {
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "Invalid file content");
    }
  }
  
  @Test
  public void exception3Test() {  // [room] 30 35 11 Winter Garden
    try {
      Readable file = new FileReader("res/testfiles/mansion_test2.txt");
      new WorldImpl(file);
      fail();
    } catch (FileNotFoundException exp) {
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "Invalid file content");
    }
  }
  
  @Test
  public void exception4Test() {  // [item] 21 3 Crepe Pan
    try {
      Readable file = new FileReader("res/testfiles/mansion_test3.txt");
      new WorldImpl(file);
      fail();
    } catch (FileNotFoundException exp) {
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "Invalid file content: invalid room of an item.");
    }
  }
  
  @Test
  public void exception5Test() {  // room number 2
    try {
      Readable file = new FileReader("res/testfiles/mansion_test4.txt");
      new WorldImpl(file);
      fail();
    } catch (FileNotFoundException exp) {
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "Invalid file content: Mismatched # of rooms.");
    }
  }
  
  @Test
  public void exception6Test() {  // item number 2
    try {
      Readable file = new FileReader("res/testfiles/mansion_test5.txt");
      new WorldImpl(file);
      fail();
    } catch (FileNotFoundException exp) {
      fail();
    } catch (IllegalArgumentException e) {
      assertEquals(e.getMessage(), "Invalid file content: Mismatched # of items.");
    }
  }
}
