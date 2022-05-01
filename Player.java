import java.awt.event.*;
import java.awt.*;

public class Player {

  public int x;
  public int y;
  public final int width;
  public final int height;
  public boolean usePower = false;
  final int minPowerRadius = 16;
  final int maxPowerRadius = 128;
  int powerRadius = minPowerRadius;
  int powerX = 0;
  int powerY = 0;
  int powerTimer = 2;

  boolean playerDead = false;
  int playerHealth = 30;
  int takeHitTimer = 5;

  public int vx;
  public int vy;
  float friction = 0.99f;
  boolean moveLeft;
  boolean moveRight;
  boolean moveUp;
  boolean moveDown;


  public Player(){
    width = 64;
    height = 64;
    vx = 0;
    vy = 0;
    x = 0;
    y = 0;
  }

  public void update(){
    if(!playerDead){
      controlPlayer();
      x += vx;
      y += vy;
      powerX = x;
      powerY = y;
      if(usePower){
        powerAnimation();
      }
      x = Math.max(0, Math.min(x, World.WORLD_WIDTH - width));
      y = Math.max(0, Math.min(y, World.WORLD_HEIGHT - height));
    }
  }

  public void controlPlayer(){
    if(moveLeft && !moveRight){
      vx = -2;
    }
    if(moveRight && !moveLeft){
      vx = 2;
    }
    if(moveUp && !moveDown){
      vy = -2;
    }
    if(moveDown && !moveUp){
      vy = 2;
    }
    if(!moveLeft && !moveRight){
      vx *= friction;
    }
    if(!moveUp && !moveDown){
      vy *= friction;
    }
  }

  public void powerAnimation(){
    powerTimer --;
    if(powerTimer < 0){
      powerTimer = 0;
      powerRadius++;
    }
    powerX = x - (powerRadius - width) /2;
    powerY = y - (powerRadius - height) /2;
    if(powerRadius > maxPowerRadius){
      powerRadius = minPowerRadius;
    }
  }

  public void takeHit(){
    if(powerRadius < width){
      takeHitTimer --;
      if(takeHitTimer < 0){
        takeHitTimer = 5;
        playerHealth--;
        if(playerHealth < 0){
          playerDead = true;
        }
      }
    }
  }

  public void render(Graphics g){
    g.setColor(Color.WHITE);
    g.fillRect(x, y, width, height);
    if(usePower){
      g.setColor(Color.BLUE);
      g.drawOval(powerX, powerY, powerRadius, powerRadius);
    }
  }

  public void keyPressed(KeyEvent keyEvent){
    int keyCode = keyEvent.getKeyCode();
    switch(keyCode){
      case KeyEvent.VK_UP:
      moveUp = true;
      break;
      case KeyEvent.VK_DOWN:
      moveDown = true;
      break;
      case KeyEvent.VK_LEFT:
      moveLeft = true;
      break;
      case KeyEvent.VK_RIGHT:
      moveRight = true;
      break;
      case KeyEvent.VK_SPACE:
      usePower = true;
      break;
    }
    return;
  }

  public void keyReleased(KeyEvent keyEvent){
    int keyCode = keyEvent.getKeyCode();
    switch(keyCode){
      case KeyEvent.VK_UP:
      moveUp = false;
      break;
      case KeyEvent.VK_DOWN:
      moveDown = false;
      break;
      case KeyEvent.VK_LEFT:
      moveLeft = false;
      break;
      case KeyEvent.VK_RIGHT:
      moveRight = false;
      break;
      case KeyEvent.VK_SPACE:
      usePower = false;
      powerRadius = minPowerRadius;
      break;
    }
    return;
  }
}
