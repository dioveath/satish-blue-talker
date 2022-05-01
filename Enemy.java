import java.util.Random;
import java.awt.*;

public class Enemy {

  int x;
  int y;
  int width;
  int height;
  Random random = new Random();
  int vx = 0;
  int vy = 0;
  boolean takeDamage = false;
  Color myColor = Color.RED;
  int health = 5;
  int takeDamageTimer = 5;
  public boolean dead = false;

  int chaseRadius = 200;
  boolean setDestination = false;
  int targetCenterX;
  int targetCenterY;
  int chaseRefreshRate = 20;

  public Enemy(){
    width =(int) (50 + random.nextFloat() * 20);
    x = (int) (random.nextFloat() * (World.WORLD_WIDTH - width));
    height =(int) (50 + random.nextFloat() * 20);
    y = (int) (random.nextFloat() * (World.WORLD_HEIGHT - height));
    vx = (int) (random.nextFloat() * 2 + 1);
    vy = (int) (random.nextFloat() * 2 + 1);
  }

  public void update(){
    if(takeDamage){
      takeDamageTimer--;
      if(takeDamageTimer < 0){
        takeDamageTimer = 5;
        health--;
        switch(health){
          case 4:
          myColor = new Color(10, 10, 10);
          break;
          case 3:
          myColor = new Color(10, 10, 20);
          break;
          case 2:
          myColor = new Color(10, 10, 40);
          break;
          case 1:
          myColor = new Color(10, 10, 100);
          break;
          case 0:
          destroy();
          break;
        }
      }
    } else {
      takeDamageTimer = 5;
    }
  }

  public void chasePlayer(Player player){
    if(!setDestination){
      int playerCenterX = player.x + (player.width/2);
      int playerCenterY = player.y + (player.height/2);
      targetCenterX = playerCenterX;
      targetCenterY = playerCenterY;
      setDestination = true;
    }
    if(setDestination){
      chaseRefreshRate--;
      if(chaseRefreshRate < 0){
        chaseRefreshRate = 0;
        setDestination = false;
      }
      int enemyCenterX = x + (width/2);
      int enemyCenterY = y + (height/2);

      int vectorx =   enemyCenterX  - targetCenterX;
      int vectory =   enemyCenterY - targetCenterY;

      int distance = (int) (Math.sqrt(vectorx * vectorx + vectory * vectory));

      if(distance < chaseRadius){
        x += (vectorx > 0) ? -vx : vx;
        y += (vectory > 0) ? -vy : vy;
      }
    }
  }

  public void render(Graphics g){
    if(!dead){
      g.setColor(myColor);
      g.fillRect(x, y, width, height);
    }
  }

  public void destroy(){
    dead = true;
  }

}
