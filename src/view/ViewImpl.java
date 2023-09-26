package view;

import controller.GuiController;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import world.Player;
import world.World;
import world.WorldImpl;

/**
 * Implement the View interface.
 */
public class ViewImpl extends JFrame implements View {
  private static final long serialVersionUID = -7083924619099998893L;
  private World model;
  private GuiController controller;
  private JFileChooser fileChooser;
  private JLabel readFileResult;
  private JSplitPane panel;
  private JTextField nameEnter;
  private JComboBox<String> roomEnter;
  private JComboBox<String> typeEnter;
  private JLabel currentPlayers;
  private ImageIcon blackMan;
  private ImageIcon blueMan;
  private JLabel[] players;
  private JLabel target;
  private JLabel turn;
  private JLabel humanActionResult;
  private DefaultListModel<String> computerActionList;
  private JList<String> computerAction;
  private JLabel gameState;
  private JLabel playerInfo;
  
  

  /**
   * Constructor.
   * @param model the model of the game.
   * @throws IllegalArgumentException for invalid model.
   */
  public ViewImpl(World model) throws IllegalArgumentException {
    super("Kill Doctor Lucky");
    if (model == null) {
      throw new IllegalArgumentException();
    }
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setExtendedState(JFrame.MAXIMIZED_BOTH);

    this.model = model;
    
    fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fileChooser.setFileFilter(new FileNameExtensionFilter("*.txt", "txt"));
    fileChooser.setDialogTitle("Choose your world specification file");
    
    JButton startButton = new JButton("start by a default world");
    JButton chooseFileButton = new JButton("start by your world");
    //JButton quitButton = new JButton("quit");
    startButton.setAlignmentX(CENTER_ALIGNMENT);
    chooseFileButton.setAlignmentX(CENTER_ALIGNMENT);
    //quitButton.setAlignmentX(CENTER_ALIGNMENT);
    readFileResult = new JLabel("");
    readFileResult.setForeground(Color.RED);
    
    JLabel welcome = new JLabel("WELCOME TO THE GAME");
    JLabel author = new JLabel("created by Jifan Xie");
    welcome.setFont(new Font(null, Font.BOLD, 35));
    welcome.setAlignmentX(CENTER_ALIGNMENT);
    author.setAlignmentX(CENTER_ALIGNMENT);
    JPanel leftPanel = new JPanel();
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
    leftPanel.add(Box.createVerticalGlue());
    leftPanel.add(welcome);
    leftPanel.add(Box.createVerticalStrut(20));
    leftPanel.add(author);
    leftPanel.add(Box.createVerticalGlue());
    
    JPanel rightPanel = new JPanel();
    rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
    rightPanel.add(Box.createVerticalGlue());
    rightPanel.add(startButton);
    rightPanel.add(Box.createVerticalStrut(20));
    rightPanel.add(chooseFileButton);
    rightPanel.add(Box.createVerticalStrut(20));
    rightPanel.add(readFileResult);
    rightPanel.add(Box.createVerticalGlue());
    
    panel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
    //panel.setDividerLocation(0.5);
    panel.setResizeWeight(0.5);
    
    startButton.addActionListener(new ButtonListener(this));
    chooseFileButton.addActionListener(new ButtonListener(this));
    
    add(panel);
  }

  @Override
  public void addListener(GuiController listener) throws IllegalArgumentException {
    if (listener == null) {
      throw new IllegalArgumentException();
    }
    this.controller = listener;
  }
  
  @Override
  public boolean openFileDialog() {
    int state = fileChooser.showOpenDialog(null);
    if (state == JFileChooser.APPROVE_OPTION) {
      try {
        Readable file = new FileReader(fileChooser.getSelectedFile());
        World w = new WorldImpl(file);
        this.model = w;
        this.controller.changeModel(w);
        return true;
      } catch (FileNotFoundException e) {
        readFileResult.setText("File not found");
      } catch (IllegalArgumentException e) {
        readFileResult.setText(e.getMessage());
      }
    }
    return false;
  }
  
  @Override
  public void setPanelForCreatePlayer() {
    panel.removeAll();
    
    /* Left panel for showing the map. */
    MapPanel map = new MapPanel(model);
    map.setPreferredSize(new Dimension(900, 1080));
    JScrollPane left = new JScrollPane(map);
    left.getVerticalScrollBar().setUnitIncrement(30);
    left.getHorizontalScrollBar().setUnitIncrement(30);

    /* Right panel for players' creation. */
    JLabel title1 = new JLabel("CREATE PLAYERS");
    title1.setFont(new Font(null, Font.BOLD, 30));
    title1.setAlignmentX(CENTER_ALIGNMENT);

    nameEnter = new JTextField(10);
    roomEnter = new JComboBox<String>(model.getRooms());
    typeEnter = new JComboBox<String>();
    typeEnter.addItem("human");
    typeEnter.addItem("computer");
    JButton createButton = new JButton("create");
    createButton.addActionListener(new ButtonListener(this));
    
    JLabel title2 = new JLabel(String.format("<html><body><p>Current Players: <br/>"
        + "(accept at most %d players)</p></body></html>", World.PLAYERCAPACITY));
    title2.setFont(new Font(null, Font.PLAIN, 20));
    title2.setAlignmentX(CENTER_ALIGNMENT);
    currentPlayers = new JLabel("/");
    currentPlayers.setAlignmentX(CENTER_ALIGNMENT);
    JButton finishButton = new JButton("finish");
    finishButton.addActionListener(new ButtonListener(this));
    
    JPanel right = new JPanel(new GridBagLayout());
    // this.setInGrid(column, row, width, height)
    right.add(title1, this.setInGrid(0, 0, 2, 40));
    right.add(new JLabel("name"), this.setInGrid(0, 1, 1, 10));
    right.add(nameEnter, this.setInGrid(1, 1, 1, 10));
    right.add(new JLabel("room"), this.setInGrid(0, 2, 1, 10));
    right.add(roomEnter, this.setInGrid(1, 2, 1, 10));
    right.add(new JLabel("controlled by"), this.setInGrid(0, 3, 1, 10));
    right.add(typeEnter, this.setInGrid(1, 3, 1, 10));
    right.add(createButton, this.setInGrid(1, 4, 1, 10));
    right.add(title2, this.setInGrid(0, 5, 2, 70));
    right.add(currentPlayers, this.setInGrid(0, 6, 2, 30));
    right.add(finishButton, this.setInGrid(1, 7, 1, 10));
    
    /* Add left and right panels. */
    panel.setLeftComponent(left);
    panel.setRightComponent(right);
    panel.updateUI();
    panel.repaint();
  }
  
  @Override
  public void refreshPlayersLabel() {
    String name = nameEnter.getText();
    if (!("".equals(name))) {
      controller.createPlayer(name, roomEnter.getSelectedIndex(), this.isHuman());
      currentPlayers.setText(model.playersForView());
    }
  }
  
  /**
   * Get whether the created player is controlled by human.
   * @return whether the created player is controlled by human.
   */
  private boolean isHuman() {
    return typeEnter.getSelectedIndex() == 0;
  }
  
  /**
   * Create a GridBagConstraints object for a component in the GridBagLayout.
   * @param x       the gridx of the GridBagConstraints.
   * @param y       the gridy of the GridBagConstraints.
   * @param width   the gridwidth of the GridBagConstraints.
   * @param height  the ipady of the GridBagConstraints.
   * @return a GridBagConstraints for the GridBagLayout.
   */
  private GridBagConstraints setInGrid(int x, int y, int width, int height) {
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = x;
    c.gridy = y;
    c.gridwidth = width;
    c.ipady = height;
    return c;
  }
  
  @Override
  public void setPanelForGame() {
    if (!("".equals(model.players()))) {
      controller.start();
      
      panel.removeAll();
      
      /* Left panel for showing the map. */
      MapPanel map = new MapPanel(model);
      map.setPreferredSize(new Dimension(900, 1080)); // This make scrolling works.
      map.setLayout(null);  // This make setBounds() of components works.
      JScrollPane left = new JScrollPane(map);
      left.getVerticalScrollBar().setUnitIncrement(30);
      left.getHorizontalScrollBar().setUnitIncrement(30);
      
      // Add players on the map
      blackMan = new ImageIcon("res/images/black.png"); // new ImageIcon("images/black.png");
      blueMan = new ImageIcon("res/images/blue.png"); // new ImageIcon("images/blue.png");
      players = new JLabel[model.playerNumber()];
      target = new JLabel();
      for (int i = 0; i < model.playerNumber(); i++) {
        players[i] = new JLabel();
        players[i].setIcon(blackMan);
        //players[i].addMouseListener(new MapClick(controller));
      }
      players[0].setIcon(blueMan); // players[model.getTurnIndex()]
      target.setIcon(new ImageIcon("res/images/red.png"));  // new ImageIcon("images/red.png")

      for (int i = 0; i < model.playerNumber(); i++) {
        int[] pos = this.getPlayerIconPosition(i);
        players[i].setBounds(pos[0], pos[1], pos[2], pos[3]);
        //this.setPlayerIcon(i);
        map.add(players[i]);
      }
      this.setTargetIcon();
      map.add(target);
      
      // Add listeners
      map.addMouseListener(new MapClick(controller));
      panel.addKeyListener(new KeyboardListener(controller));
      panel.requestFocusInWindow(); // Let the panel be focused so that the KeyListener works.

      /* Right panel for play the game. */
      String instructions = String.format("<html><body><p>Try to kill Dr.Lucky in red! <br/>"
          + "If the game reaches the maximum turn, he will escape. <br/>"
          + "Click a player to see its items in \"player's information\" part.<br/><br/>"
          + "You can take one of the following action: <br/>"
          + "Click a neighboring room - Move to this neighboring room.<br/>"
          + "Right click a room - Move the pet to this room and make it invisible.<br/>"
          + "Press P - Pick up an item in this room for attack.<br/>"
          + "Press L - Look around to get the information of this room.<br/>"
          + "Press A - Attack Dr.Lucky if he is in your room and no one see your attack.<br/><br/>"
          + "Your attack will fail if it is witnessed by others.<br/>"
          + "If there is a pet in your room, your room is invisible to the neighbors.<br/>"
          + "You can only carry a maximum of %d items.</p></body></html>", Player.CAPACITY);
      JLabel instruct = new JLabel(instructions);
      instruct.setForeground(Color.GRAY);
      gameState = new JLabel(String.format("[Dr.Lucky] current health %s.", model.getDrHealth()));
      gameState.setForeground(Color.RED);
      turn = new JLabel(String.format("%s, it's your turn!", model.getTurn()));
      turn.setFont(new Font(null, Font.BOLD, 20));
      humanActionResult = new JLabel("result of your action");
      playerInfo = new JLabel("player's information");
      
      instruct.setAlignmentX(CENTER_ALIGNMENT);
      gameState.setAlignmentX(CENTER_ALIGNMENT);
      turn.setAlignmentX(CENTER_ALIGNMENT);
      humanActionResult.setAlignmentX(CENTER_ALIGNMENT);
      playerInfo.setAlignmentX(CENTER_ALIGNMENT);

      JPanel right = new JPanel();
      right.setBorder(BorderFactory.createEmptyBorder(30, 25, 0, 25));
      right.setLayout(new BoxLayout(right, BoxLayout.PAGE_AXIS));
      right.add(instruct);
      right.add(Box.createVerticalStrut(15));
      right.add(gameState);
      right.add(Box.createVerticalStrut(40));
      right.add(turn);
      right.add(Box.createVerticalStrut(15));
      right.add(humanActionResult);
      if (model.hasComputerPlayer()) {
        computerActionList = new DefaultListModel<String>();
        computerActionList.addElement("computer actions");
        computerAction = new JList<String>(computerActionList);
        computerAction.setVisibleRowCount(3);
        computerAction.setAlignmentX(CENTER_ALIGNMENT);
        JScrollPane listScrollPane = new JScrollPane(computerAction);
        right.add(Box.createVerticalStrut(15));
        right.add(listScrollPane);
      }
      right.add(Box.createVerticalStrut(40));
      right.add(playerInfo);
      right.add(Box.createVerticalGlue());
      
      /* Add left and right panels. */
      panel.setLeftComponent(left);
      panel.setRightComponent(right);
      panel.updateUI();
      panel.repaint();
      
      if (!model.getTurn().isHuman()) {
        controller.computerActionLoop();
      }
    }
  }
  
  /**
   * Get the position and size of a player's icon.
   * @param playerIndex the index of the player.
   * @return the left bound, upper bound, width and height of a player icon.
   */
  private int[] getPlayerIconPosition(int playerIndex) {
    // left border of the icon
    int playerX;
    int roomLeft = model.getPlayersList().get(playerIndex).getRoom().getEdges()[2];
    if (playerIndex >= 0 && playerIndex <= 2) {
      playerX = 30 * roomLeft + (playerIndex + 1) * 30;  // 0, 1, 2
    } else if (playerIndex <= 6) {
      playerX = 30 * roomLeft + (playerIndex - 3) * 30;  // 3, 4, 5, 6
    } else {
      playerX = 30 * roomLeft + (playerIndex - 7) * 30;  // 7, 8, 9
    }
    // up border of the icon
    int playerY;
    int roomUp = model.getPlayersList().get(playerIndex).getRoom().getEdges()[0];
    if (playerIndex >= 0 && playerIndex <= 2) {
      playerY = 30 * roomUp + 10;
    } else if (playerIndex <= 6) {
      playerY = 30 * roomUp + 42;
    } else {
      playerY = 30 * roomUp + 74;
    }
    int[] pos = {playerX, playerY, 20, 30};   // {left, up, width, height}
    return pos;
  }
  
  @Override
  public int getPlayerFromClick(int row, int col) throws IllegalArgumentException {
    if (row < 0 || col < 0) {
      throw new IllegalArgumentException();
    }
    for (int i = 0; i < players.length; i++) {
      int[] pos = this.getPlayerIconPosition(i);
      if (row > pos[1] && row < pos[1] + pos[3] 
          && col > pos[0] && col < pos[0] + pos[2]) {
        return i;
      }
    }
    return -1;
  }
  
  @Override
  public void setTargetIcon() {
    int roomLeft = model.getDoctorRoom().getEdges()[2];
    int roomUp = model.getDoctorRoom().getEdges()[0];
    target.setBounds(roomLeft * 30, roomUp * 30 + 10, 20, 30);
  }
  
  @Override
  public void movePlayerIcon() {
    int[] pos = this.getPlayerIconPosition(model.getTurnIndex());
    players[model.getTurnIndex()].setBounds(pos[0], pos[1], pos[2], pos[3]);
  }
  
  @Override
  public void setHumanActionText(String str) {
    humanActionResult.setText(str);
  }
  
  @Override
  public void addComputerAction(String str) {
    if (computerActionList.contains("computer actions")) {
      computerActionList.remove(0);
    }
    computerActionList.insertElementAt(str, 0);
    computerAction.setModel(computerActionList);
  }
  
  @Override
  public void setGameStateText(String str) {
    gameState.setText(str);
  }
  
  @Override
  public void setTurnText(String str) {
    turn.setText(str);
  }
  
  @Override
  public void setPlayerInfoText(String str) {
    playerInfo.setText(str);
  }
  
  @Override
  public void setCurrentPlayerToBlack() {
    players[model.getTurnIndex()].setIcon(blackMan);
  }
  
  @Override
  public void setCurrentPlayerToBlue() {
    players[model.getTurnIndex()].setIcon(blueMan);
  }
  
  @Override
  public void makeVisible() {
    this.setVisible(true);
  }
}
