import javax.swing.*;

public class Game extends JFrame{

  Thread renderThread;

  World world;

  public Game(){
    setSize(725, 750);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    world = new World();
    add(world);
    renderThread = new Thread(new RenderTask());
    renderThread.start();
    setVisible(true);
  }

  public class RenderTask implements Runnable{
    @Override
    public void run(){
      while(world.gameRunning){
        world.update();
        world.repaint();
        try {
          Thread.sleep(1000/60);
        } catch(InterruptedException ioe){
          ioe.printStackTrace();
        }
      }
      dispose();
    }
  }

}
