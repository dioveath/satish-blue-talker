import java.awt.event.*;

public class PlayerKeyAdapter extends KeyAdapter{

  Player player;

  public PlayerKeyAdapter(Player player){
    this.player = player;
  }

  @Override
  public void keyPressed(KeyEvent keyEvent){
    player.keyPressed(keyEvent);
  }

  @Override
  public void keyReleased(KeyEvent keyEvent){
    player.keyReleased(keyEvent);
  }
}
