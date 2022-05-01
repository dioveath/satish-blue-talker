import javax.swing.*;
import java.awt.*;

public class LoadingScreen extends JPanel {

  int x = 0;
  boolean loadingComplete;

  public LoadingScreen(){

  }

  public void paintComponent(Graphics g){
    super.paintComponent(g);
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, World.WORLD_WIDTH, World.WORLD_HEIGHT);
    g.setColor(Color.GREEN);
    x++;
    if(x > World.WORLD_WIDTH){
      loadingComplete = true;
    }
    g.fillRect(0, World.WORLD_HEIGHT - 25, x, World.WORLD_HEIGHT - 20);
    repaint();
  }
}
