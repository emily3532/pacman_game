package ghost;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;

public class App extends PApplet {
    /**
    * Creates an applet instance.
    */
    public static final int WIDTH = 448;
    public static final int HEIGHT = 576;
    private Settings settings;
    private Player player;
    private int playerX;
    private int playerY;
    private Ghost ambusher;
    private Ghost chaser;
    private Ghost ignorant;
    private Ghost whim;
    private int ambusherX;
    private int ambusherY;
    private int chaserX;
    private int chaserY;
    private int ignorantX;
    private int ignorantY;
    private int whimX;
    private int whimY;
    private PFont font;
    private int debug;
    private boolean gameOver;
    private boolean win;
    private int count;
    public App() {
        this.playerX = 0;
        this.playerY = 0;
        this.player = player;
        this.ambusher = ambusher;
        this.ambusherX = 0;
        this.ambusherY = 0;
        this.chaser = chaser;
        this.chaserX = 0;
        this.chaserY = 0;
        this.ignorant = ignorant;
        this.ignorantX = 0;
        this.ignorantY = 0;
        this.whim = whim;
        this.whimX = 0;
        this.whimY = 0;
        this.font = font;
        this.debug = 0;
        this.gameOver = false;
        this.win = false;
        this.count = 0;
    }

    public void setup() {
        frameRate(60);
        setSettings();
        setPlayer();
        setGhosts();
    }


    public void setPlayer(){
      this.playerX = this.settings.getPlayerX();
      this.playerY = this.settings.getPlayerY();
      this.player = new Player(loadImage("src/main/resources/playerClosed.png"), 16*this.playerX, 16*this.playerY, this.settings.getSpeed());
      this.player.setSprites(loadImage("src/main/resources/playerUp.png"), loadImage("src/main/resources/playerDown.png"), loadImage("src/main/resources/playerLeft.png"), loadImage("src/main/resources/playerRight.png"));
      this.player.setSettings(this.settings);
    }

    public void setSettings(){
      this.settings = new Settings(this);
      this.settings.configReader();
      this.settings.mapReader();
      this.settings.mapCell(this);
    }

    public void setGhosts(){
      this.ambusherX = this.settings.getaX();
      this.ambusherY = this.settings.getaY();
      this.chaserX = this.settings.getcX();
      this.chaserY = this.settings.getcY();
      this.ignorantX = this.settings.getiX();
      this.ignorantY = this.settings.getiY();
      this.whimX = this.settings.getwX();
      this.whimY = this.settings.getwY();
      this.ambusher = new Ghost(loadImage("src/main/resources/ambusher.png"),16*this.ambusherX, 16*this.ambusherY, this.settings.getSpeed(), GhostType.AMBUSHER, this.settings);
      this.chaser = new Ghost(loadImage("src/main/resources/chaser.png"),16*this.chaserX, 16*this.chaserY, this.settings.getSpeed(),GhostType.CHASER, this.settings);
      this.ignorant = new Ghost(loadImage("src/main/resources/ignorant.png"),16*this.ignorantX, 16*this.ignorantY, this.settings.getSpeed(), GhostType.IGNORANT, this.settings);
      this.whim = new Ghost(loadImage("src/main/resources/whim.png"),16*this.whimX, 16*this.whimY, this.settings.getSpeed(), GhostType.WHIM, this.settings);
      this.ambusher.setSprites(loadImage("src/main/resources/frightened.png"));
      this.chaser.setSprites(loadImage("src/main/resources/frightened.png"));
      this.whim.setSprites(loadImage("src/main/resources/frightened.png"));
      this.ignorant.setSprites(loadImage("src/main/resources/frightened.png"));
    }


    public void check(){
      if(this.win || this.gameOver){
        if(this.count > 10){
          this.win = false;
          this.gameOver = false;
          setup();
        }

      }
    }

    public void settings() {
        size(WIDTH, HEIGHT);
        check();
    }

    public void draw() {
        if(this.gameOver){
          background(0, 0, 0);
          fill(000000);
          rect(-1, -1, WIDTH + 1, HEIGHT +1);
          font = createFont("PressStart2P-Regular.ttf", 32);
          fill(255, 255, 255);
          textFont(font);
          text("GAME OVER", 100, 240 );
          this.count+=1;
        }
        else if(this.win){
          background(0, 0, 0);
          fill(000000);
          rect(-1, -1, WIDTH + 1, HEIGHT +1);
          font = createFont("PressStart2P-Regular.ttf", 32);
          fill(255, 255, 255);
          textFont(font);
          text("YOU WIN", 150, 300 );
          this.count+=1;
        }
        else{

          background(0, 0, 0);
          fill(000000);
          this.rect(-1, -1, WIDTH + 1, HEIGHT +1);
          this.settings.draw(this);
          this.player.tick();
          if(settings.stepCell(this.player.getX(), this.player.getY(), this.player.getDirection())==0){
            this.win = true;
            }
          this.player.checkQ();
          this.player.step(settings.nextCell(this.player.getX(), this.player.getY(), this.player.getDirection()));

          this.player.draw(this);
          this.ambusher.tick(this.player.getX(), this.player.getY(), this.player.getDirection(), this.settings.getFright());
          this.ambusher.step(settings.nextCell(this.ambusher.getX(), this.ambusher.getY(), this.ambusher.getDirection()));
          this.ambusher.draw(this);
          this.chaser.tick(this.player.getX(), this.player.getY(), this.player.getDirection(), this.settings.getFright());
          this.chaser.draw(this);
          this.ignorant.tick(this.player.getX(), this.player.getY(), this.chaser.getX(), this.chaser.getY(), this.player.getDirection(), this.settings.getFright());
          this.ignorant.draw(this);
          this.whim.tick(this.player.getX(), this.player.getY(), this.chaser.getX(), this.chaser.getY(), this.player.getDirection(), this.settings.getFright());
          this.whim.draw(this);
          if(this.player.ghostStep(this.ambusher.getX(), this.ambusher.getY())){
            if(!(settings.getFright())){
              this.player.collide();
              this.ambusher.restart();
              this.chaser.restart();
              this.whim.restart();
              this.ignorant.restart();
              if(this.settings.removeLife()){
                this.gameOver = true;
              }
            }
          }
          else if(this.player.ghostStep(this.chaser.getX(), this.chaser.getY())){
            if(!(settings.getFright())){
              this.player.collide();
              this.ambusher.restart();
              this.chaser.restart();
              this.whim.restart();
              this.ignorant.restart();
              if(this.settings.removeLife()){
                this.gameOver = true;
              }
            }

          }
          else if(this.player.ghostStep(this.ignorant.getX(), this.ignorant.getY())){
            if(!(settings.getFright())){
              this.player.collide();
              this.ambusher.restart();
              this.chaser.restart();
              this.whim.restart();
              this.ignorant.restart();
              if(this.settings.removeLife()){
                this.gameOver = true;

              }
            }
          }
          else if(this.player.ghostStep(this.whim.getX(), this.whim.getY())){
            if(!(settings.getFright())){
              this.player.collide();
              this.ambusher.restart();
              this.chaser.restart();
              this.whim.restart();
              this.ignorant.restart();
              if(this.settings.removeLife()){
                this.gameOver = true;
              }
            }
          }
        if(this.debug%2 == 1){
          stroke(255);
          line(this.player.getX()+8, this.player.getY()+8, this.ambusher.getX()+8, this.ambusher.getY()+8);
          stroke(255);
          line(this.player.getX()+8, this.player.getY()+8, this.chaser.getX()+8, this.chaser.getY()+8);
          stroke(255);
          line(this.player.getX()+8, this.player.getY()+8, this.ignorant.getX()+8, this.ignorant.getY()+8);
          stroke(255);
          line(this.player.getX()+8, this.player.getY()+8, this.whim.getX()+8, this.whim.getY()+8);
        }
        }

        // rect( this.ambusher.getX()+2, this.ambusher.getY()+2, 16, 16 );
        // rect( this.player.getX()+4, this.player.getY()-4, 16, 16 );

    }
//|| this.player.ghostStep(this.chaser.getX(), this.chaser.getY()) ||this.player.ghostStep(this.ignorant.getX(), this.ignorant.getY()) || this.player.ghostStep(this.whim.getX(), this.whim.getY())){

    public void keyPressed(){

      if(key == CODED && keyCode == UP ){
        if(!(this.settings.nextCell(this.player.getX(), this.player.getY()-16, "u"))){
          this.player.move("u");
        }
        else{
          this.player.queue("u");
        }
      }
      if(key == CODED && keyCode == DOWN ){
        if(!(this.settings.nextCell(this.player.getX(), this.player.getY()+16, "d"))){
          this.player.move("d");
        }
        else{
          this.player.queue("d");
        }
      }
      if(key == CODED && keyCode == LEFT ){
        if(!(this.settings.nextCell(this.player.getX()-16, this.player.getY(), "l"))){
          this.player.move("l");
        }
        else{
          this.player.queue("l");
        }
      }
      if(key == CODED && keyCode == RIGHT ){
        if(!(this.settings.nextCell(this.player.getX()+16, this.player.getY(), "r"))){
          this.player.move("r");
        }
        else{
          this.player.queue("r");
        }

      }
      if( key == ' '){
        this.debug +=1;
      }
    }


    public static void main(String[] args) {
        PApplet.main("ghost.App");

    }

}
