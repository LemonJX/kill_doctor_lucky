package world;

import controller.GuiController;
import controller.GuiControllerImpl;
import controller.TextBaseController;
import controller.TextBaseControllerImpl;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.Readable;
import view.View;
import view.ViewImpl;

/**
 * An example driver for the World model.
 */
public class WorldDriver {
    
  /**
   * Driver program for the World to show how it works.
   * 
   * @param args Specification file and the maximum number of turns for the game.
   */
  public static void main(String[] args) {
    Readable file;
    int maxTurn;
    try {
      file = new FileReader("res/mansion.txt");
      //file = new FileReader(args[0]);
      maxTurn = 100;
      //maxTurn = Integer.valueOf(args[1]);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Invalid filename");
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid number of turns");
    }
    World w = new WorldImpl(file);
    //w.generateMap();
    
    /* Text base version */
    /*
    Readable input = new InputStreamReader(System.in);
    Appendable output = System.out;
    TextBaseController c = new TextBaseControllerImpl(input, output);
    c.playGame(w, maxTurn);
    */
    
    /* GUI version */
    
    View view = new ViewImpl(w);
    GuiController controller = new GuiControllerImpl(view, w, maxTurn);
    controller.playGame();
  }
}
