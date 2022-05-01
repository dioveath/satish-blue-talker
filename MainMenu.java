import javax.swing.*;
import java.awt.*;

public class MainMenu extends JPanel {

  public MainMenu(){
  }

  @Override
  public void paintComponent(Graphics g){
    super.paintComponent(g);
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, World.WORLD_WIDTH, World.WORLD_HEIGHT);
    g.setColor(Color.RED);
    g.fillRect(World.WORLD_WIDTH/2, World.WORLD_HEIGHT/2, 200, 50);
  }
}
