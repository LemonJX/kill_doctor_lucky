package world;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the Room interface and represent a room in the World.
 */
public class RoomImpl implements Room {
  private String name;
  private int index;
  private int up;
  private int down;
  private int left;
  private int right;
  private List<Item> items;
  private List<Room> neighbors;
  private boolean hasPet;

  /**
   * Constructor.
   * @param name  the name of this room.
   * @param i the index of this room.
   * @param up  the upper boundary of this room.
   * @param left  the left boundary of this room.
   * @param down  the lower boundary of this room.
   * @param right the right boundary of this room.
   * @throws IllegalArgumentException for invalid arguments.
   */
  public RoomImpl(String name, int i, int up, int left, int down, int right) 
      throws IllegalArgumentException {
    if (name == null || i < 0 || up < 0 || left < 0 || down < 0 || right < 0) {
      throw new IllegalArgumentException("invalid arguments");
    }
    this.name = name;
    this.index = i;
    this.up = up;
    this.down = down;
    this.left = left;
    this.right = right;
    this.items = new ArrayList<Item>();
    this.neighbors = new ArrayList<Room>();
    this.hasPet = false;
  }
  
  @Override
  public int getIndex() {
    return this.index;
  }
  
  @Override
  public String getName() {
    return this.name;
  }
  
  @Override
  public int[] getEdges() {
    int[] edges = new int[4];
    edges[0] = this.up;
    edges[1] = this.down;
    edges[2] = this.left;
    edges[3] = this.right;
    return edges;
  }
  
  @Override
  public List<Player> getPlayers(List<Player> allPlayers) throws IllegalArgumentException {
    if (allPlayers == null) {
      throw new IllegalArgumentException();
    }
    List<Player> players = new ArrayList<Player>();
    for (Player p : allPlayers) {
      if (p.getRoom().getIndex() == this.index) {
        players.add(p);
      }
    }
    return players;
  }
  
  @Override
  public void addItem(Item item) throws IllegalArgumentException {
    if (item == null) {
      throw new IllegalArgumentException("invalid argument in addItem()");
    }
    this.items.add(item);
  }
  
  @Override
  public List<Item> getItem() {
    List<Item> result = new ArrayList<Item>();
    for (int i = 0; i < this.items.size(); i++) {
      result.add(this.items.get(i));
    }
    return result;
  }
  
  @Override
  public Item pickItem(int itemIndex) throws IllegalArgumentException {
    if (itemIndex < 0 || itemIndex >= this.items.size()) {
      throw new IllegalArgumentException("invalid argument in pickItem()");
    }
    return this.items.remove(itemIndex);
  }
  
  @Override
  public boolean nextTo(Room other) throws IllegalArgumentException {
    if (other == null) {
      throw new IllegalArgumentException("invalid argument in nextTo()");
    }
    if (this.up == other.getEdges()[1] + 1 
        && this.left < other.getEdges()[3] && this.right > other.getEdges()[2]) {
      return true;
    }
    if (this.down == other.getEdges()[0] - 1
        && this.left < other.getEdges()[3] && this.right > other.getEdges()[2]) {
      return true;
    }
    if (this.left == other.getEdges()[3] + 1 
        && this.up < other.getEdges()[1] && this.down > other.getEdges()[0]) {
      return true;
    }
    if (this.right == other.getEdges()[2] - 1 
        && this.up < other.getEdges()[1] && this.down > other.getEdges()[0]) {
      return true;
    }
    return false;
  }
  
  @Override
  public void setNeighbors(List<Room> rooms) throws IllegalArgumentException {
    if (rooms == null) {
      throw new IllegalArgumentException("invalid argument");
    }
    for (Room room : rooms) {
      if (this.nextTo(room)) {
        this.neighbors.add(room);
      }
    }
  }
  
  @Override
  public List<Room> getNeighbors() {
    List<Room> result = new ArrayList<Room>();
    for (int i = 0; i < this.neighbors.size(); i++) {
      result.add(this.neighbors.get(i));
    }
    return result;
  }
  
  @Override
  public int chooseNeighbor(int i) throws IllegalArgumentException {
    if (i < 0 || i >= this.neighbors.size()) {
      throw new IllegalArgumentException("invalid argument in chooseNeighbor()");
    }
    return this.neighbors.get(i).getIndex();
  }

  /**
   * Help to get the information of this room, including its players, items and neighbors. 
   * @param allPlayers  all the players in the game.
   * @param inHtml      whether the neighbors information are in html version.
   * @return an array of strings contains the players, items and neighbors.
   */
  private String[] getInformation(List<Player> allPlayers, boolean inHtml) {
    StringBuilder players = new StringBuilder();
    for (Player p : this.getPlayers(allPlayers)) {
      players.append(" ").append(p.getName()).append(";");
    }
    StringBuilder items = new StringBuilder();
    for (Item i : this.items) {
      items.append(" ").append(i.toString()).append(";");
    }
    StringBuilder neighbors = new StringBuilder();
    for (Room r : this.neighbors) {
      if (!r.invisible()) {
        if (inHtml) {
          neighbors.append(r.neighborInfoForView(allPlayers));
        } else {
          neighbors.append(r.neighborInfo(allPlayers));
        }
      }
    }
    String[] info = {players.toString(), items.toString(), neighbors.toString()};
    return info;
  }

  /**
   * Help to get the information of this room when its neighbor is asking for information,
   * including its players and items.
   * @param allPlayers  all the players in the game.
   * @return an array of strings contains the players and items.
   */
  private String[] getNeighborInfo(List<Player> allPlayers) {
    StringBuilder players = new StringBuilder();
    for (Player p : this.getPlayers(allPlayers)) {
      players.append(" ").append(p.getName()).append(";");
    }
    if ("".equals(players.toString())) {
      players.append(" /;");
    }
    StringBuilder items = new StringBuilder();
    for (Item i : this.items) {
      items.append(" ").append(i.toString()).append(";");
    }
    if ("".equals(items.toString())) {
      items.append(" /;");
    }
    String[] neighborInfo = {players.toString(), items.toString()};
    return neighborInfo;
  }
  
  @Override
  public String information(List<Player> allPlayers) throws IllegalArgumentException {
    if (allPlayers == null) {
      throw new IllegalArgumentException("invalid argument");
    }
    String[] info = new String[3];
    info = this.getInformation(allPlayers, false);
    return String.format("%s\n[Players]%s\n[Items]%s\n[Neighbors]\n%s", 
        this.toString(), info[0], info[1], info[2]);
  }
  
  @Override
  public String neighborInfo(List<Player> allPlayers) throws IllegalArgumentException {
    if (allPlayers == null) {
      throw new IllegalArgumentException("invalid argument");
    }
    String[] neighborInfo = new String[2];
    neighborInfo = this.getNeighborInfo(allPlayers);
    return String.format("%s: [Players]%s [Items]%s\n", 
        this.toString(), neighborInfo[0], neighborInfo[1]);
  }
  
  @Override
  public String informationForView(List<Player> allPlayers) throws IllegalArgumentException {
    String[] info = new String[3];
    info = this.getInformation(allPlayers, true);
    return String.format("look around %s: <br/>"
        + "[Players]%s<br/>[Items]%s<br/>[Neighbors]<br/>%s", 
        this.toString(), info[0], info[1], info[2]);
  }
  
  @Override
  public String neighborInfoForView(List<Player> allPlayers) throws IllegalArgumentException {
    if (allPlayers == null) {
      throw new IllegalArgumentException("invalid argument");
    }
    String[] neighborInfo = new String[2];
    neighborInfo = this.getNeighborInfo(allPlayers);
    return String.format("%s: [Players]%s [Items]%s<br/>", 
        this.toString(), neighborInfo[0], neighborInfo[1]);
  }
  
  @Override
  public void petArrive() {
    this.hasPet = true;
  }
  
  @Override
  public void petLeave() {
    this.hasPet = false;
  }
  
  @Override
  public boolean invisible() {
    return this.hasPet;
  }
  
  @Override
  public List<Player> witness(List<Player> allPlayers) throws IllegalArgumentException {
    if (allPlayers == null) {
      throw new IllegalArgumentException("invalid argument");
    }
    if (this.invisible()) {
      return this.getPlayers(allPlayers);
    } else {
      List<Player> witness = new ArrayList<Player>();
      for (Player p : this.getPlayers(allPlayers)) {
        witness.add(p);
      }
      for (Room r : this.neighbors) { // including invisible neighboring rooms
        for (Player p : r.getPlayers(allPlayers)) {
          witness.add(p);
        }
      }
      return witness;
    }
  }
  
  @Override
  public String toString() {
    return String.format("(%d)%s", this.index, this.name);
  }
}
