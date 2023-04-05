package ghost;
import processing.core.PImage;
import processing.core.PApplet;
public class Player extends Cell{
  private int x;
  private int y;
  private int initialx;
  private int initialy;
  private int xVel;
  private int yVel;
  private PImage sprite;
  private PImage closed;
  private PImage open;
  private PImage left;
  private PImage right;
  private PImage up;
  private PImage down;
  private int pac;
  private int speed;
  private String direction;
  private boolean alive;
  private String queued;
  private Settings set;

  /**
  * Constructor for a Waka Player, requires a sprite Closed image, starting x coordinate,
  * starting y coordinate and speed valueOf
  * @param sprite, A closed Waka
  * @param x, initial X value of Waka
  * @param y, initial Y value of Waka
  * @param speed, speed of Waka from config file
  */
  public Player(PImage sprite, int x, int y, int speed){
    super(sprite, x, y);
    this.sprite = sprite;
    this.closed = sprite;
    this.open = open;
    this.left = left;
    this.right = right;
    this.up = up;
    this.down = down;
    this.initialx = x;
    this.initialy = y;
    this.x = x;
    this.y = y;
    this.xVel = 0;
    this.yVel = 0;
    this.speed = speed;
    this.pac = 1;
    this.direction = "nothing";
    this.alive = true;
    this.queued = "queued";
    this.set = set;
  }

  /**
  * Provides Player with Settings object used in App.
  * @param setting, Settings object
  */
  public void setSettings(Settings setting){
    this.set = setting;
  }

  /**
  * Resets player to original position after colliding with a Ghost.
  */
  public void collide(){
    this.alive = false;
    this.x = this.initialx;
    this.y = this.initialy;
    this.xVel = 0;
    this.yVel = 0;
    this.direction = "nothing";
    this.queued = "";
    this.open = this.right;
  }

  /**
  * Handles main logic for Player.
  * Checks if Waka is alive and not outside the game screen.
  * Continues movement and alternates between open and closed frames.
  */
  public void tick() {
    if (!(this.alive)) {
      this.alive = true;
    }
    if(this.x > 448) {
      move("l");
    }
    if(this.y > 576) {
      move("u");
    }
    if(this.x < 0) {
      move("r");
    }
    if(this.y < 0) {
      move("d");
    }

    this.x += this.xVel;
    this.y += this.yVel;

    if(this.pac < 8) {
      this.sprite = this.open;
      this.pac+=1;
    }
    else {
      this.sprite = this.closed;
      this.pac+=1;

      if (this.pac > 16) {
        this.pac -= 16;
      }
    }
  }

  /**
  * Draws the Waka with an offset.
  */
  public void draw(PApplet app){
    app.image(this.sprite,this.x-4, this.y-4);
  }

  /**
  * Changes movement of Waka.
  */
  public void move(String direc){
    if (direc.equals("u")) {
      if (checkXMid()) {
        this.open = this.up;
        this.direction = "u";
        this.yVel = -1*this.speed;
        this.xVel = 0*this.speed;
      }
      else{
        this.queued = "u";
      }
    }
    else if (direc.equals("d")) {

      if (checkXMid()) {
        this.open = this.down;
        this.direction = "d";
        this.yVel = 1*this.speed;
        this.xVel = 0*this.speed;
      }
      else{
        this.queued = "d";
      }
    }
    else if (direc.equals("l")) {

      if (checkYMid()) {
        this.open = this.left;
        this.direction = "l";
        this.xVel = -1*this.speed;
        this.yVel = 0*this.speed;
      }
      else{
        this.queued = "l";
      }
    }
    else if (direc.equals("r")) {

      if (checkYMid()) {
        this.open = this.right;
        this.direction = "r";
        this.xVel = 1*this.speed;
        this.yVel = 0*this.speed;
      }
      else{
        this.queued = "r";
      }
    }
  }

  /**
  * @return width
  */
  public int getWidth() {
    return this.sprite.width;
  }

  /**
  * @return width
  */
  public int getHeight() {
    return this.sprite.height;
  }

  /**
  * Sets the PImages for each Waka direction
  * @param u, d, l, r, PImage for each direction
  */
  public void setSprites(PImage u, PImage d, PImage l, PImage r){
    this.left = l;
    this.right = r;
    this.up = u;
    this.down = d;
    this.open = r;
  }

  /**
  * @return direction
  */
  public String getDirection(){
    return this.direction;
  }

  /**
  * Sets direction of Waka
  * @param d, direction String
  */
  public void setDirection(String d){
    this.direction = d;
  }

  /**
  * Checks if Waka X is in the middle of a path
  * @return whether in the middle
  */
  public boolean checkXMid(){
    if ((this.x+8)%16 == 8) {
      return true;
    }
    return false;
  }

  /**
  * Checks if Waka Y is in the middle of a path
  * @return whether in the middle
  */
  public boolean checkYMid(){
    if ((this.y+8)%16 == 8) {
      return true;
    }
    return false;
  }

  /**
  * Checks if any moves are queued to move.
  */
  public void checkQ(){
    if (this.queued.equals("u")) {
      if (!(set.nextCell(this.x, this.y-16, "u"))) {
        this.queued = "";
        move("u");
      }
      return;
    }
    else if (this.queued.equals("d")) {
      if (!(set.nextCell(this.x, this.y+16, "d"))) {
        this.queued = "";
        move("d");
      }
      return;
    }
    else if (this.queued.equals("l")) {
      if (!(set.nextCell(this.x-16, this.y, "l"))) {
        this.queued = "";
        move("l");
      }
      return;
    }
    else if (this.queued.equals("r")) {
      if (!(set.nextCell(this.x+16, this.y, "r"))) {
        this.queued = "";
        move("r");
      }
      return;
    }
    return;
  }

  /**
  * Checks if Waka's step will be in a wall, if not then will cause
  * Waka to move.
  */
  public void step (boolean stepped) {
    if (stepped) {
      if (this.direction.equals("u")) {
        if (!(set.nextCell(this.x, this.y, "d"))) {
          move("d");
        }
        return;
      }
      else if (this.direction.equals("d")) {
        if (!(set.nextCell(this.x, this.y, "u"))) {
          move("u");
        }
        return;
      }
      else if (this.direction.equals("l")) {
        if (!(set.nextCell(this.x, this.y, "r"))) {
          move("r");
        }
        return;
      }
      else if (this.direction.equals("r")) {
        if(!(set.nextCell(this.x, this.y, "l"))){
          move("l");
        }
        return;
      }
    }
    else{
      move(this.direction);
    }
  }

  /**
  * Queues a moves
  * @param move String
  */
  public void queue(String q){
    this.queued = q;
  }

  /**
  * Checks if Waka has hit a ghost
  * @param xval, yval of ghost
  * @return whether ghost has been hit
  */
  public boolean ghostStep(int xval, int yval){
    if (xval/16 == this.x/16 && this.y/16 == yval/16) {
      return true;
    }
    return false;
  }

  /**
  * @return speed of Waka
  */
  public int getSpeed(){
    return this.speed;
  }
  
  /**
  * @return x, X value of cell
  */
  public int getX(){
    return this.x;
  }
  /**
  * @return y, Y value of cell
  */
  public int getY(){
    return this.y;
  }
}
