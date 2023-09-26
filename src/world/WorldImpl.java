package world;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.imageio.ImageIO;

/**
 * This class implements the World interface and represent a world.
 */
public class WorldImpl implements World {
  private int row;
  private int col;
  private String name;
  private Doctor dr;
  private Pet pet;
  private int roomNumber;
  private int itemNumber;
  private int playerNumber;
  private List<Room> rooms;
  private List<Item> items;
  private List<Player> players;
  private int maxTurn;
  private int turn;
  private Player winner;
  
  /**
   * Constructor.
   * @param file  the specification file
   */
  public WorldImpl(Readable file) {
    // Read Specification File
    if (file == null) {
      throw new IllegalArgumentException("Invalid filename");
    }
    List<String> lines = new ArrayList<String>();
    Scanner scan = new Scanner(file);
    while (scan.hasNextLine()) {
      lines.add(scan.nextLine());
    }
    scan.close();
    
    // Create the World with a player, rooms and items.
    try {
      this.row = Integer.valueOf(lines.get(0).split("\\s+", 3)[0]);
      this.col = Integer.valueOf(lines.get(0).split("\\s+", 3)[1]);
      this.name = lines.get(0).split("\\s+", 3)[2];
      
      int health = Integer.valueOf(lines.get(1).split("\\s+", 2)[0]);
      String doctor = lines.get(1).split("\\s+", 2)[1];
      String pet = lines.get(2).trim();
      this.roomNumber = Integer.valueOf(lines.get(3).trim());
      this.dr = new DoctorImpl(doctor, health, this.roomNumber);
      this.pet = new PetImpl(pet, this.dr.getRoom(), this.roomNumber);
      
      int itemStartLine = -1;
      this.rooms = new ArrayList<Room>();
      int roomIndex = 0;
      for (int i = 4; i < lines.size(); i++) {
        if (lines.get(i).trim().split(" ").length == 1) {
          itemStartLine = i;
          break;
        }
        String[] roomLine = lines.get(i).trim().split("\\s+", 5);
        int up = Integer.valueOf(roomLine[0]);
        int left = Integer.valueOf(roomLine[1]);
        int down = Integer.valueOf(roomLine[2]);
        int right = Integer.valueOf(roomLine[3]);
        Room room = new RoomImpl(roomLine[4], roomIndex, up, left, down, right);
        roomIndex += 1;
        this.rooms.add(room);
      }
      
      this.items = new ArrayList<Item>();
      this.itemNumber = Integer.valueOf(lines.get(itemStartLine).trim());
      for (int i = itemStartLine + 1; i < lines.size(); i++) {
        String[] itemLine = lines.get(i).trim().split("\\s+", 3);
        Item item = new ItemImpl(Integer.valueOf(itemLine[0]), 
            Integer.valueOf(itemLine[1]), itemLine[2]);
        this.items.add(item);
        if (item.getRoom() < 0 || item.getRoom() >= this.rooms.size()) {
          throw new IllegalArgumentException("Invalid file content: invalid room of an item.");
        } else {
          this.rooms.get(item.getRoom()).addItem(item); //Place this item to its room.
        }
      }
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid file content");
    }
    
    if (this.roomNumber != this.rooms.size()) {
      throw new IllegalArgumentException("Invalid file content: Mismatched # of rooms.");
    }
    if (this.itemNumber != this.items.size()) {
      throw new IllegalArgumentException("Invalid file content: Mismatched # of items.");
    }

    // Set neighbors
    for (Room room : this.rooms) {
      room.setNeighbors(this.rooms);
    }
    
    // Players and turns initialization
    this.players = new ArrayList<Player>();
    this.turn = 0;
    
    // Set pet
    this.rooms.get(this.pet.getRoom()).petArrive();
    
    this.winner = null;
  }
  
  /**
   * Get the left bound, upper bound, width and height of a room on the map.
   * @param room the room to find the rectangle drawn on the map.
   * @return the left bound, upper bound, width and height of a room on the map.
   */
  private int[] getRect(Room room) {
    int[] rec = new int[4];
    int left = room.getEdges()[2];
    rec[0] = left * 30 - 15;          // left bound
    int up = room.getEdges()[0];
    rec[1] = up * 30 - 15;            // up bound
    int right = room.getEdges()[3];
    rec[2] = (right - left + 1) * 30; // width
    int down = room.getEdges()[1];
    rec[3] = (down - up + 1) * 30;    // height
    return rec;
  }
  
  @Override
  public void generateMap() throws IllegalStateException {
    int width = 900;
    int height = 1080;
    BufferedImage bimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g = bimage.createGraphics();
    // Why setBackgroud() doesn't work?
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, width, height);
    
    g.setColor(Color.BLACK);
    g.setFont(new Font(null, Font.PLAIN, 20));
    for (Room room : this.rooms) {
      g.drawString(room.toString(), room.getEdges()[2] * 30, room.getEdges()[0] * 30);
      g.drawRect(this.getRect(room)[0], this.getRect(room)[1], 
          this.getRect(room)[2], this.getRect(room)[3]);
    }
    g.dispose();
    try {
      ImageIO.write(bimage, "png", new File("map.png")); //"res/map.png"
    } catch (IOException e) {
      throw new IllegalStateException("Fail to create the image.");
    }
  }
  
  @Override
  public void drawMap(Graphics g) {
    int width = 900;
    int height = 1080;
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, width, height);
    
    g.setColor(Color.BLACK);
    g.setFont(new Font(null, Font.PLAIN, 13));
    for (Room room : this.rooms) {
      g.drawString(room.toString(), room.getEdges()[2] * 30, room.getEdges()[0] * 30);
      g.drawRect(this.getRect(room)[0], this.getRect(room)[1], 
          this.getRect(room)[2], this.getRect(room)[3]);
    }
  }
  
  @Override
  public int getRoomFromClick(int row, int col) throws IllegalArgumentException {
    if (row < 0 || col < 0) {
      throw new IllegalArgumentException();
    }
    for (int i = 0; i < this.roomNumber; i++) {
      int[] rec = this.getRect(this.rooms.get(i));
      if (row > rec[1] && row < rec[1] + rec[3] 
          && col > rec[0] && col < rec[0] + rec[2]) {
        return i;
      }
    }
    return -1;
  }
  
  @Override
  public int getRoomNumber() {
    return this.roomNumber;
  }
  
  @Override
  public String[] getRooms() {
    String[] rooms = new String[this.roomNumber];
    for (int i = 0; i < this.roomNumber; i++) {
      rooms[i] = this.rooms.get(i).toString();
    }
    return rooms;
  }
  
  @Override
  public boolean validRoomIndex(int room) {
    return room >= 0 && room < this.roomNumber;
  }
  
  @Override
  public void moveDoctor() {
    this.dr.move();
    this.turn += 1;
  }
  
  @Override
  public void moveDoctorForTest() {
    this.dr.moveTo(this.getTurn().getRoom().getIndex());
    this.turn += 1;
  }
  
  @Override
  public Room getDoctorRoom() {
    return this.rooms.get(this.dr.getRoom());
  }
  
  @Override
  public void createPlayer(String name, int room, boolean human) throws IllegalArgumentException {
    if (this.players.size() < World.PLAYERCAPACITY) {
      if (name == null) {
        throw new IllegalArgumentException("Invalid name");
      }
      if (!this.validRoomIndex(room)) {
        throw new IllegalArgumentException("Invalid room index");
      }
      Player player;
      if (human) {
        player = new PlayerImpl(name, this.rooms.get(room), true);
      } else {
        player = new PlayerImpl(name, this.rooms.get(room), false);
      }
      this.players.add(player);
    }
  }
  
  @Override
  public boolean hasComputerPlayer() {
    for (Player p : this.players) {
      if (!p.isHuman()) {
        return true;
      }
    }
    return false;
  }
  
  @Override
  public List<Player> getPlayersList() {
    List<Player> copy = new ArrayList<Player>();
    for (Player p : this.players) {
      copy.add(p);
    }
    return copy;
  }
  
  @Override
  public String players() {
    StringBuilder players = new StringBuilder();
    for (Player p : this.players) {
      players.append(p.toString()).append("\n");
    }
    return players.toString();
  }
  
  @Override
  public String playersForView() {
    StringBuilder players = new StringBuilder("<html><body><p align=\"center\">");
    for (Player p : this.players) {
      players.append(p.toString()).append("<br/>");
    }
    players.append("</p></body></html>");
    return players.toString();
  }
  
  @Override
  public int playerNumber() {
    return this.players.size();
  }
  
  @Override
  public void setTurn(int maxTurn) throws IllegalArgumentException {
    this.playerNumber = this.players.size();
    if (maxTurn < this.playerNumber) {
      throw new IllegalArgumentException("Invalid maximum number of turns.");
    }
    this.maxTurn = maxTurn;
  }
  
  @Override
  public int getTurnIndex() {
    return this.turn % this.playerNumber;
  }
  
  @Override
  public Player getTurn() throws IllegalStateException {
    if (this.turn >= this.maxTurn) {
      throw new IllegalStateException("Already reach the maximum turn.");
    }
    return this.players.get(this.getTurnIndex());
  }
  
  @Override
  public boolean gameIsOver() {
    return this.turn >= this.maxTurn || this.winner != null;
  }
  
  @Override
  public String lookAround() {
    StringBuilder info = new StringBuilder(this.getTurn().getRoom().information(this.players));
    if (this.dr.getRoom() == this.getTurn().getRoom().getIndex()) {
      info.append(this.dr.toString()).append(" is in this room!\n");
    }
    if (this.pet.getRoom() == this.getTurn().getRoom().getIndex()) {
      info.append(this.pet.toString()).append(" is in this room!\n");
    }
    return info.toString();
  }
  
  @Override
  public String lookAroundForView() {
    String info = String.format("%s %s", this.getTurn().getName(), 
        this.getTurn().getRoom().informationForView(this.players));
    if (this.pet.getRoom() == this.getTurn().getRoom().getIndex()) {
      return String.format("<html><body><p>%s%s is in this room!</p></body></html>", 
          info, this.pet.toString());
    } else {
      return String.format("<html><body><p>%s</p></body></html>", info);
    }
  }
  
  @Override
  public String pick(int itemIndex) throws IllegalArgumentException {
    Player p = this.getTurn();
    if (p.full()) {
      throw new IllegalArgumentException("Your bag is full and cannot pick any more items.");
    }
    if (itemIndex < 0 || itemIndex >= p.getRoom().getItem().size()) {
      throw new IllegalArgumentException("No such item here.");
    }
    Item item = p.getRoom().pickItem(itemIndex);
    item.picked();
    p.addItem(item);
    return String.format("%s picked: %s\n", p.getName(), item.toString());
  }
  
  @Override
  public boolean noItemFound() {
    return this.getTurn().getRoom().getItem().size() == 0;
  }
  
  @Override
  public void move(int roomIndex) throws IllegalArgumentException {
    Player p = this.getTurn();
    if (!this.validRoomIndex(roomIndex)) {
      throw new IllegalArgumentException("Invalid room index.");
    } else if (!p.getRoom().nextTo(this.rooms.get(roomIndex))) {
      throw new IllegalArgumentException("Not a neighboring room.");
    } else {
      p.moveTo(this.rooms.get(roomIndex));
    }
  }
  
  @Override
  public boolean moveForView(int roomIndex) {
    if (this.validRoomIndex(roomIndex) 
        && this.getTurn().getRoom().nextTo(this.rooms.get(roomIndex))) {
      this.getTurn().moveTo(this.rooms.get(roomIndex));
      return true;
    }
    return false;
  }
  
  @Override
  public String movePet(int roomIndex) throws IllegalArgumentException {
    if (!this.validRoomIndex(roomIndex)) {
      throw new IllegalArgumentException("Invalid room index.");
    }
    this.rooms.get(this.pet.getRoom()).petLeave();
    this.pet.move(roomIndex);
    this.rooms.get(this.pet.getRoom()).petArrive();
    return String.format("%s is in %s.", this.pet.toString(), 
        this.rooms.get(this.pet.getRoom()).toString());
  }
  
  @Override
  public boolean validAttack() {
    return this.getTurn().getRoom().getIndex() == this.dr.getRoom() ? true : false;
  }
  
  @Override
  public String attack() {
    if (this.validAttack()) {
      // Use the item and get the damage value
      int damage = this.getTurn().getItemDamage();
      if (damage == 0) {
        damage = 1;
      }
      this.getTurn().usedUpItem();
      
      // Check if the attack is witnessed. if not, make an attack.
      if (this.getTurn().getRoom().witness(this.players).size() > 1) { // attacker is in the witness
        return "[Invalid attack] Someone witnessed your attack.";
      } else {
        this.dr.beHurt(damage);
        if (this.dr.getHealth() <= 0) {
          this.winner = this.getTurn();
        }
        return String.format("[Successful attack] Target -%d, current health %d.", 
            damage, this.dr.getHealth());
      }
    } else {
      return "[Invalid attack] Target is not in this room.";
    }
  }
  
  @Override
  public int getDrHealth() {
    return this.dr.getHealth();
  }
  
  @Override
  public String result() {
    if (this.winner != null) {
      return String.format("Congratulations! %s kills %s and wins the game.\n", 
          this.winner.getName(), this.dr.toString());
    } else {
      return String.format("Oh no! %s has escaped. His remaining health: %d.\n", 
          this.dr.toString(), this.dr.getHealth());
    }
  }
  
  @Override
  public String toString() {
    StringBuilder info = new StringBuilder(String.format("%s: row %d col %d, "
        + "%d rooms, %d items.\n", this.name, this.row, this.col,  
        this.roomNumber, this.itemNumber));
    for (int i = 0; i < this.roomNumber; i++) {
      info.append(this.rooms.get(i).toString()).append("; ");
      if (i != 0 && i % 5 == 0) {
        info.append("\n");
      }
    }
    info.append(String.format("Target: %s(health %d, in room %d).\nPet: %s(in room %d).\n", 
        this.dr.toString(), this.dr.getHealth(), this.dr.getRoom(), 
        this.pet.toString(), this.pet.getRoom()));
    return info.toString();
  }
}
