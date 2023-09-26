package controller;

import java.util.Random;
import view.View;
import world.World;

/**
 * Implement the TicTacToeController interface.
 */
public class GuiControllerImpl implements GuiController {
  private View view;
  private World model;
  private int maxTurn;

  /**
   * Constructor.
   * @param view  the view of the game.
   * @param model the model of the game.
   * @param maxTurn the maximum number of turns in this game.
   * @throws IllegalArgumentException for invalid arguments
   */
  public GuiControllerImpl(View view, World model, int maxTurn) throws IllegalArgumentException {
    if (view == null || model == null || maxTurn < 0) {
      throw new IllegalArgumentException();
    }
    this.view = view;
    this.model = model;
    this.maxTurn = maxTurn;
  }
  
  @Override
  public void playGame() {
    this.view.makeVisible();
    this.view.addListener(this);
  }
  
  @Override
  public void changeModel(World model) throws IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException();
    }
    this.model = model;
  }
  
  @Override
  public void createPlayer(String name, int room, boolean isHuman) throws IllegalArgumentException {
    if (name == null || !model.validRoomIndex(room)) {
      throw new IllegalArgumentException();
    }
    model.createPlayer(name, room, isHuman);
  }
  
  @Override
  public void start() {
    model.setTurn(maxTurn);
  }
  
  @Override
  public void handleClick(int row, int col) throws IllegalArgumentException {
    if (row < 0 || col < 0) {
      throw new IllegalArgumentException();
    }
    if (view.getPlayerFromClick(row, col) == -1) {
      if (!model.gameIsOver()) {
        if (model.moveForView(model.getRoomFromClick(row, col))) {
          view.movePlayerIcon();
          view.setHumanActionText(String.format("Move: %s", model.getTurn()));
          this.nextTurn();
        } else {
          view.setHumanActionText("You cannot move to this room.");
        }
      }
    } else {
      view.setPlayerInfoText(model.getPlayersList()
          .get(view.getPlayerFromClick(row, col)).information());
    }
  }

  @Override
  public void handleRightClick(int row, int col) {
    if (row < 0 || col < 0) {
      throw new IllegalArgumentException();
    }
    if (!model.gameIsOver() && model.getRoomFromClick(row, col) >= 0) {
      view.setHumanActionText(model.movePet(model.getRoomFromClick(row, col)));
      this.nextTurn();
    }
  }
  
  @Override 
  public void handleKeyL() {
    if (!model.gameIsOver()) {
      view.setHumanActionText(model.lookAroundForView());
      this.nextTurn();
    }
  }
  
  @Override 
  public void handleKeyP() {
    if (!model.gameIsOver()) {
      if (model.getTurn().full()) {
        view.setHumanActionText("Your bag is full and cannot hold any more items.");
      } else if (model.noItemFound()) {
        view.setHumanActionText("No item in this room.");
      } else {
        view.setHumanActionText(model.pick(0));
      }
      this.nextTurn();
    }
  }
  
  @Override 
  public void handleKeyA() {
    if (!model.gameIsOver()) {
      if (model.validAttack() && !model.getTurn().noItem()) {
        model.getTurn().automaticHoldItem();
      }
      view.setHumanActionText(model.attack());
      view.setGameStateText(String.format("[Dr.Lucky] current health %s.", model.getDrHealth()));
      this.nextTurn();
    }
  }
  
  /**
   * End a turn and start the next turn if game is not over. Call this after each turn action.
   */
  private void endThisTurn() {
    view.setCurrentPlayerToBlack();
    model.moveDoctor();
    view.setTargetIcon();
    if (!model.gameIsOver()) {
      view.setTurnText(String.format("%s, it's your turn!", model.getTurn()));
      view.setCurrentPlayerToBlue();
    }
  }

  /**
   * End this turn and let the game move forward until it's a human player's turn.
   */
  private void nextTurn() {
    this.endThisTurn();
    if (model.gameIsOver()) {
      view.setGameStateText(model.result());
    } else if (!model.getTurn().isHuman()) {
      this.computerActionLoop();
    }
  }
  
  @Override
  public void computerActionLoop() {
    while (!model.getTurn().isHuman()) {
      this.turnActionForComputer();
      this.endThisTurn();
      if (model.gameIsOver()) {
        view.setGameStateText(model.result());
        break;
      }
    }
  }
  
  /**
   * A computer player to take an action in a turn.
   */
  private void turnActionForComputer() {
    // no look around for a computer player.
    if (!model.gameIsOver()) {
      Random random = new Random();
      int action = random.nextInt(4);
      switch (action) {            
        case 0: //move
          int room1 = random.nextInt(model.getTurn().getRoom().getNeighbors().size());
          model.move(model.getTurn().getRoom().chooseNeighbor(room1));
          view.movePlayerIcon();
          view.addComputerAction(String.format("%s Move: %s", 
              model.getTurn().getName(), model.getTurn()));
          break;
        case 1: //pick
          if (model.getTurn().full()) {
            view.addComputerAction(String.format("%s: Your bag is full.", 
                model.getTurn().getName()));
          } else if (model.noItemFound()) {
            view.addComputerAction(String.format("%s: No item in this room.", 
                model.getTurn().getName()));
          } else {
            view.addComputerAction(model.pick(0));
          }
          break;
        case 2: //move pet
          int room2 = random.nextInt(model.getRoomNumber());
          view.addComputerAction(String.format("%s: %s", 
              model.getTurn().getName(), model.movePet(room2)));
          break;
        case 3://attack
          if (model.validAttack() && !model.getTurn().noItem()) {
            model.getTurn().automaticHoldItem();
          }
          view.addComputerAction(String.format("%s: %s", 
              model.getTurn().getName(), model.attack()));
          view.setGameStateText(String.format("[Dr.Lucky] current health %s.", 
              model.getDrHealth()));
          break;
        /*case 4: //look around 
          view.addComputerAction(model.lookAroundForView());
          break;*/
        default: throw new IllegalArgumentException("Invalid action");
      }
    }
  }
}
