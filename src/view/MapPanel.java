package view;

import java.awt.Graphics;
import javax.swing.JPanel;
import world.World;

/**
 * This class draws a grid on the panel.
 */
public class MapPanel extends JPanel {
  private static final long serialVersionUID = -7083924619099998893L;
  private World model;
  
  /**
   * Constructor.
   * @param world the model of the game.
   * @throws IllegalArgumentException for invalid model.
   */
  public MapPanel(World world) throws IllegalArgumentException {
    if (world == null) {
      throw new IllegalArgumentException();
    }
    this.model = world;
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    setSize(900, 1080);
    model.drawMap(g);
    
    // Use "/res/map.png" to draw:
    /*
    BufferedImage image;
    try {
      image = ImageIO.read(new File("res/map.png"));
    } catch (IOException ioe) {
      throw new IllegalStateException("cannot get the map file");
    }
    g.drawImage(image, 0, 0, this);
    */
  }
}
