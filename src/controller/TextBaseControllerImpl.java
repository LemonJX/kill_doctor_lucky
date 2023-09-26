package controller;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import world.World;

/**
 * This starter files is for students to implement a console controller for the
 * TicTacToe MVC assignment.
 */
public class TextBaseControllerImpl implements TextBaseController {

  private final Appendable out;
  private final Scanner scan;

  /**
   * Constructor for the controller.
   * 
   * @param in  the source to read from
   * @param out the target to print to
   * @throws IllegalArgumentException for invalid arguments.
   */
  public TextBaseControllerImpl(Readable in, Appendable out) throws IllegalArgumentException {
    if (in == null || out == null) {
      throw new IllegalArgumentException("Readable and Appendable can't be null");
    }
    this.out = out;
    scan = new Scanner(in);
  }

  @Override
  public void playGame(World w, int maxTurn) throws IllegalArgumentException {
    if (w == null) {
      throw new IllegalArgumentException("Null the World Model");
    }
    try {
      out.append("---------------------Initialization---------------------\n");
      out.append(w.toString());
      out.append(String.format("Number of turns: %d\n", maxTurn));
      out.append("---------------------Create Players---------------------\n");
      out.append(String.format("The number of players is limited to %d.\n", World.PLAYERCAPACITY));
      int humanPlayerNumber = 0;
      int computerPlayerNumber = 0;
      out.append("Number of players controlled by human: ");
      if (scan.hasNext()) {
        String number1 = scan.next();
        try {
          humanPlayerNumber = Integer.valueOf(number1);
        } catch (NumberFormatException e) {
          throw new IllegalArgumentException(String.format("Not a valid number: %s\n", number1));
        }
      }
      out.append("Number of players controlled by computer: ");
      if (scan.hasNext()) {
        String number2 = scan.next();
        try {
          computerPlayerNumber = Integer.valueOf(number2);
        } catch (NumberFormatException e) {
          throw new IllegalArgumentException(String.format("Not a valid number: %s\n", number2));
        }
      }
      for (int i = 0; i < humanPlayerNumber; i++) {
        String name = "";
        int roomIndex = -1;
        out.append("Create a player controlled by human.\n");
        out.append("Name (without spaces): ");
        if (scan.hasNext()) {
          name = scan.next();
        }
        out.append("Room (index): ");
        if (scan.hasNext()) {
          String index = scan.next();
          try {
            roomIndex = Integer.valueOf(index);
          } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid room index.");
          }
          w.createPlayer(name, roomIndex, true);
        }
      }
      Random random = new Random();
      for (int i = 0; i < computerPlayerNumber; i++) {
        String name = String.format("Computer%d", i + 1);
        int roomIndex = random.nextInt(w.getRoomNumber());
        w.createPlayer(name, roomIndex, false);
      }
      out.append(String.format("Players: \n%s", w.players()));

      w.setTurn(maxTurn);
      
      out.append("---------------------Start the Game---------------------\n");
      out.append("In each turn, a player can: \nM - Move to neighboring room, or\n"
          + "P - Pick up an item in this room by giving the index of the item, or\n"
          + "L - Look around to get the information of this room, or\n"
          + "C - Move the cat of Dr.Lucky to a room and make the room invisible, or\n"
          + "A - Attack Dr.Lucky if he is in the same room as you and no one see your attack.\n\n");
      while (!w.gameIsOver()) {
        if (!w.getTurn().isHuman()) {
          out.append(String.format("%s\n", w.getTurn().toString()));
          int action = random.nextInt(5);
          switch (action) {            
            case 0: //move
              int room1 = random.nextInt(w.getTurn().getRoom().getNeighbors().size());
              w.move(w.getTurn().getRoom().chooseNeighbor(room1));
              out.append(String.format("%s\n", w.getTurn().toString()));
              break;
            case 1: //pick
              if (w.getTurn().full()) {
                out.append("Your bag is full and cannot hold any more items.\n");
              } else if (w.noItemFound()) {
                out.append("No item in this room.\n");
              } else {
                int itemIndex = random.nextInt(w.getTurn().getRoom().getItem().size());
                out.append(w.pick(itemIndex));
              }
              break;
            case 2: //look around 
              out.append(w.lookAround());
              break;
            case 3: //move pet
              int room2 = random.nextInt(w.getRoomNumber());
              out.append(String.format("%s\n", w.movePet(room2)));
              break;
            case 4://attack Dr.Lucky
              if (w.validAttack() && !w.getTurn().noItem()) {
                w.getTurn().automaticHoldItem();
              }
              out.append(String.format("%s\n", w.attack()));
              break;
            default: throw new IllegalArgumentException("Invalid action");
          }
        } else {
          out.append(String.format("%s\n", w.getTurn().toString()));
          String action;
          while (true) {
            out.append("Move, pick, look around, move the cat, or attack? Enter M/P/L/C/A: ");
            action = scan.next();
            if ("M".equals(action) || "P".equals(action) || "L".equals(action) 
                || "C".equals(action) || "A".equals(action)) {
              break;
            } else {
              out.append("Invalid action.\n");
            }
          }
          switch (action) {            
            case "M":
              String room;
              while (true) {
                out.append("Enter a neigboring room (index) to move: ");
                room = scan.next();
                try {
                  w.move(Integer.valueOf(room));
                  break;
                } catch (NumberFormatException e) {
                  out.append("Invalid index.\n");
                } catch (IllegalArgumentException e) {
                  out.append(String.format("%s\n", e.getMessage()));
                }
              }
              out.append(String.format("%s\n", w.getTurn().toString()));
              break;
            case "P":
              if (w.getTurn().full()) {
                out.append("Your bag is full and cannot hold any more items.\n");
                break;
              }
              if (w.noItemFound()) {
                out.append("No item in this room.\n");
                break;
              }
              out.append("Pick an item in this room. ");
              String item;
              while (true) {
                out.append("Enter the index of item (start from 0): ");
                item = scan.next();
                try {
                  out.append(w.pick(Integer.valueOf(item)));
                  break;
                } catch (NumberFormatException e) {
                  out.append("Invalid index.\n");
                } catch (IllegalArgumentException e) {
                  out.append(String.format("%s\n", e.getMessage()));
                }
              }
              break;
            case "L": 
              out.append(w.lookAround());
              break;
            case "C":
              while (true) {
                out.append("Enter a room (index) to move the pet: ");
                String roomIndex = scan.next();
                try {
                  out.append(String.format("%s\n", w.movePet(Integer.valueOf(roomIndex))));
                  break;
                } catch (NumberFormatException e) {
                  out.append("Invalid index.\n");
                } catch (IllegalArgumentException e) {
                  out.append(String.format("%s\n", e.getMessage()));
                }
              }
              break;
            case "A":
              if (w.validAttack() && !w.getTurn().noItem()) {
                while (true) {
                  out.append(String.format("Chose one of your item to attack: %s\n", 
                      w.getTurn().itemInfo()));
                  out.append("Enter an item (index): ");
                  String itemIndex = scan.next();
                  try {
                    w.getTurn().humanHoldItem(Integer.valueOf(itemIndex));
                    break;
                  } catch (NumberFormatException e) {
                    out.append("Invalid index.\n");
                  } catch (IllegalArgumentException e) {
                    out.append(String.format("%s\n", e.getMessage()));
                  }
                }
              }
              out.append(String.format("%s\n", w.attack()));
              break;
            default: throw new IllegalArgumentException("Invalid action");
          }
        }
        w.moveDoctor();
        //w.moveDoctorForTest();
        out.append(String.format("Doctor Lucky moves to %s\n\n", w.getDoctorRoom().toString()));
      }
      out.append("---------------------Game is Over! ---------------------\n");
      out.append(w.result());
      //w.generateMap(); // produce the map
    } catch (IOException ioe) {
      throw new IllegalStateException("Append failed", ioe);
    }
  }
}
