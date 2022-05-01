import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

public class World extends JPanel{

public final static int WORLD_WIDTH = 700;
public final static int WORLD_HEIGHT = 700;

enum GameState {
  LOADING,
  MENU,
  PLAY,
  END
};

GameState currentState = GameState.LOADING;
boolean gameRunning = true;

public Player player;
ArrayList<Enemy> enemies = new ArrayList<Enemy>();
int enemySize = 10;


int totalScore;
int score;
int numberOfDeads = 0;
boolean resetLevel = false;
boolean resetGame = false;
boolean endGame = false;
int toEndGameTimer = 50;
int toMainMenuTimer = 100;

public Font myFont;

public World(){
  setFocusable(true);
  player = new Player();
  myFont = new Font("monofur", Font.PLAIN, 40);
  addKeyListener(new MainMenuKeyAdapter());
  for(int i = 0; i < enemySize; i++){
    enemies.add(new Enemy());
  }
}

public void update(){
  if(currentState == GameState.PLAY){
    if(resetLevel){
      enemies.clear();
      for(int i = 0; i < enemySize; i++){
        enemies.add(new Enemy());
      }
      player.x = 0;
      player.y = 0;
      numberOfDeads = 0;
      if(resetGame){
        totalScore = 0;
        player.playerDead = false;
        player.playerHealth = 30;
      }
      resetLevel = false;
      resetGame = false;
    }
    if(!player.playerDead){
      player.update();
      enemyUpdate();
      score = numberOfDeads;
      if(numberOfDeads == enemySize){
        totalScore += score;
        resetLevel = true;
      }
    }
    if(player.playerDead){
      toEndGameTimer--;
      if(toEndGameTimer < 0){
        toEndGameTimer = 50;
        currentState = GameState.END;
      }
    }
  }
}

 @Override
 public void paintComponent(Graphics g){
   super.paintComponent(g);
   g.setColor(Color.BLACK);
   g.fillRect(0, 0, WORLD_WIDTH + 100, WORLD_HEIGHT + 100);
   switch(currentState){
     case LOADING:
     renderLoading(g);
     break;
     case MENU:
     resetLevel = true;
     resetGame = true;
     renderMenu(g);
     break;
     case PLAY:
     renderPlay(g);
     break;
     case END:
     renderEnd(g);
     break;
   }
 }

 int loadingTimer;

 public void renderLoading(Graphics g){
   g.setColor(Color.GREEN);
   loadingTimer+= 10;
   if(loadingTimer > WORLD_HEIGHT){
     currentState = GameState.MENU;
   }
   g.fillRect(0, WORLD_HEIGHT - 25, loadingTimer, WORLD_HEIGHT - 20);
 }

 int pointerState = 0;
 int pointerx = WORLD_WIDTH / 2 - 70;
 int pointery = WORLD_HEIGHT / 2 - 5;
 final int lastPointer = WORLD_HEIGHT /2 - 5;

 public void renderMenu(Graphics g){
   g.setColor(Color.WHITE);
   g.fillRect(WORLD_WIDTH/2 - 50, WORLD_HEIGHT/2 - 10, 120, 20);
   g.fillRect(WORLD_WIDTH/2 - 50, WORLD_HEIGHT/2 - 50, 120, 20);
   g.fillRect(WORLD_WIDTH/2 - 50, WORLD_HEIGHT/2 - 90, 120, 20);
   g.setColor(Color.BLACK);
   g.drawString("Play", WORLD_WIDTH/2, WORLD_HEIGHT/2 - 80  );
   g.drawString("Help", WORLD_WIDTH/2, WORLD_HEIGHT/2 - 40);
   g.drawString("Exit", WORLD_WIDTH/2, WORLD_HEIGHT/2);
   g.setColor(Color.WHITE);
   g.fillRect(pointerx, pointery, 10, 10);
   g.setFont(myFont);
   g.drawString("Satish : The Blue Talker", WORLD_WIDTH/2 - 150, 100);
 }

int instructionTimer = 1000;
 public void renderPlay(Graphics g){
   if(resetLevel){
     g.setColor(Color.PINK);
     g.setFont(myFont);
     g.drawString("LOADING...", WORLD_WIDTH/2 - 25, WORLD_HEIGHT/2);
   } else {
     g.setColor(new Color(10, 100, 10));
     g.fillRect(0, 0, WORLD_WIDTH, WORLD_HEIGHT);
     instructionTimer--;
     if(instructionTimer > 0){
       g.setColor(Color.PINK);
       g.drawString("Press ARROW KEYS to move and SPACE to shoot Power waves", WORLD_WIDTH/2 - 190, 150);
     }
     g.setColor(Color.PINK);
     g.drawString("Score: " + score, WORLD_WIDTH/2 - 20, 100);
     player.render(g);
     for(int i = 0; i < enemySize; i++){
       enemies.get(i).render(g);
     }
     if(player.playerDead){
       g.setFont(myFont);
       g.drawString("YOUR FINAL SCORE: " + totalScore, WORLD_WIDTH/2 - 60, WORLD_HEIGHT/2);
     }
   }
 }

 public void renderEnd(Graphics g){
   g.setColor(Color.PINK);
   g.setFont(myFont);
   g.drawString("SATISH", WORLD_WIDTH/2 - 10, WORLD_HEIGHT/2);
   toMainMenuTimer--;
   if(toMainMenuTimer < 0){
     toMainMenuTimer = 100;
     currentState = GameState.MENU;
   }
 }

 public void enemyUpdate(){
   numberOfDeads = 0;
    for(Enemy currentEnemy : enemies){
     if(currentEnemy.dead){
       numberOfDeads++;
       continue;
     }
     currentEnemy.chasePlayer(player);
     currentEnemy.update();
     if(isColliding(player, currentEnemy)){
       player.takeHit();
     }
     if(player.usePower){
       if(isInRadius(currentEnemy)){
         currentEnemy.takeDamage = true;
       } else {
         currentEnemy.takeDamage = false;
       }
     } else {
       currentEnemy.takeDamage = false;
     }
   }
 }

 public boolean isColliding(Player player, Enemy enemy){
   int playerCenterX = player.x + (player.width/2);
   int playerCenterY = player.y + (player.height/2);
   int enemyCenterX = enemy.x + (enemy.width/2);
   int enemyCenterY = enemy.y + (enemy.height/2);

   int combinedHalfWidth = (player.width/2 + enemy.width/2);
   int combinedHalfHeight = (player.height/2 + enemy.height/2);

   int vectorx =   enemyCenterX  - playerCenterX;
   int vectory =   enemyCenterY - playerCenterY;

   int overlapX = combinedHalfWidth - Math.abs(vectorx);
   int overlapY = combinedHalfHeight - Math.abs(vectory);

   if(combinedHalfWidth > Math.abs(vectorx)){
     if(combinedHalfHeight > Math.abs(vectory)){
       if(Math.abs(vectorx) < Math.abs(vectory)){
         if(vectory > 0){
           player.y -= overlapY;
         } else {
           player.y += overlapY;
         }
       } else {
         if(vectorx > 0){
           player.x -= overlapX;
         } else {
           player.x += overlapX;
         }
       }
       return true;
     }
   }

   return false;
 }

 public boolean isInRadius(Enemy enemy){
   int playerCenterX = player.x + (player.width/2);
   int playerCenterY = player.y + (player.height/2);
   int enemyCenterX = enemy.x + (enemy.width/2);
   int enemyCenterY = enemy.y + (enemy.height/2);

   int vectorx =   enemyCenterX  - playerCenterX;
   int vectory =   enemyCenterY - playerCenterY;

   int distance = (int) (Math.sqrt(vectorx * vectorx + vectory * vectory));

   if(distance < player.powerRadius){
     return true;
   }
   return false;
 }


public class MainMenuKeyAdapter extends KeyAdapter {

  public MainMenuKeyAdapter(){

  }

  @Override
  public void keyPressed(KeyEvent keyEvent){
    if(currentState == GameState.MENU){
      switch(keyEvent.getKeyCode()){
        case KeyEvent.VK_UP:
        pointery = pointery - 40;
        if(pointery < lastPointer - 80){
          pointery = lastPointer;
        }
        break;
        case KeyEvent.VK_DOWN:
        pointery = pointery + 40;
        if(pointery > lastPointer){
          pointery = lastPointer - 80;
        }
        break;
        case KeyEvent.VK_ENTER:
        switch(pointery){
          case lastPointer - 80:
          currentState = GameState.PLAY;
          break;
          case lastPointer - 40:
          break;
          case lastPointer:
          gameRunning = false;
          break;
        }
        break;
      }
    } else if(currentState == GameState.PLAY){
        if(keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE){
          currentState = GameState.MENU;
        } else {
          player.keyPressed(keyEvent);
        }
      }
  }

  @Override
  public void keyReleased(KeyEvent keyEvent){
    player.keyReleased(keyEvent);
  }
}

}
