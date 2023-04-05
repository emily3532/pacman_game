package ghost;
import processing.core.PImage;
import processing.core.PApplet;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;
import java.util.Random;

import org.json.simple.JSONArray;
public class Ghost extends Cell{
  private int x;
  private int y;
  private int initialx;
  private int initialy;
  private int xVel;
  private int yVel;
  private int targetX;
  private int targetY;
  private PImage sprite;
  private int speed;
  private String direction;
  private GhostType type;
  private Settings map;
  private int frames;
  private JSONArray modes;
  private int modeLen;
  private int mode;
  private PImage ghostIMG;
  private PImage fright;
  private Long frightLen;

  public Ghost(PImage sprite, int x, int y, int speed, GhostType type, Settings map){
    super(sprite, x, y);
    this.sprite = sprite;
    this.ghostIMG = sprite;
    this.initialx = x;
    this.initialy = y;
    this.x = x;
    this.y = y;
    this.xVel = 0;
    this.yVel = 0;
    this.speed = speed;
    this.direction = "nothing";
    this.type = type;
    this.targetX = 0;
    this.targetY = 0;
    this.map = map;
    this.frames = 1;
    this.modes = map.getModes();
    this.mode = 0;
    this.modeLen = 0;
    this.fright = fright;
    this.frightLen = (Long) (map.getFrightLen()*60);
  }

  /**
  * Sets the frightened Sprite
  * @param fright, PImage
  */
  public void setSprites(PImage fright){
    this.fright = fright;
  }

  /**
  * @return speed of Ghost
  */
  public int getSpeed(){
    return this.speed;
  }

  /**
  * Resets the Ghost to initial position and movement
  */
  public void restart(){
    this.x = initialx;
    this.y = initialy;
    this.xVel = 0;
    this.yVel = 0;
    this.direction = "nothing";
    this.targetX = 0;
    this.targetY = 0;
    this.frames = 1;
    this.modes = map.getModes();
    this.mode = 0;
    this.modeLen = 0;
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
  
  /**
  * Sets mode length variable from JSONArray of mode lengths
  */
  public void setModeLen(){

    if (this.mode < this.modes.size()) {
      Long x = (Long) this.modes.get(this.mode);
      this.modeLen = x.intValue()*60;
      this.mode+=1;
    }
    else {
      this.mode = 0;
    }
  }

  /**
  * @return mode length of current mode
  */
  public int getModeLen(){
    return this.modeLen;
  }

  /**
  * @return the amount of frames the mode has been in for
  */
  public int getMode(){
    return this.mode;
  }

  /**
  * Handles main logic for ghost including the mode type, making sure
  * ghosts don't walk into walls and automates the movement based on
  * list of available moves for the ghost.
  * @param players xval and yval, chasers xval and yval, direction of player and fright mode
  */
  public void tick(int pxval, int pyval, int cxval, int cyval, String dir, boolean fright){
    if (fright) {
      Random r = new Random();
      int rand = r.nextInt(576 + 576) - 576;
      this.targetX+=rand;
      this.targetY+=rand;
      this.sprite = this.fright;
      if (this.frames > this.frightLen) {
        this.frames = 0;
        map.setFright();
      }
    }
    else{
      this.sprite = this.ghostIMG;
      if (this.frames > this.modeLen) {
        this.frames = 0;
        setModeLen();
      }
      if(mode%2 == 1){
        scatter();
      }
      else if(mode%2 == 0){
        chase(pxval, pyval, cxval, cyval, dir);
      }
    }

    this.frames+=1;

    if (this.x > 432) {
      moveLeft();
    }
    if (this.y > 512) {
      moveUp();
    }
    if (this.x < 0) {
      moveRight();
    }
    if (this.y < 48) {
      moveDown();
    }

    this.x += this.xVel;
    this.y += this.yVel;

    if (moveOps()!= null) {
      String movement = whichCell(moveOps());
      if (movement.equals("l")) {
        moveLeft();
      }
      else if (movement.equals("u")) {
        moveUp();
      }
      else if (movement.equals("r")) {
        moveRight();
      }
      else if (movement.equals("d")) {
        moveDown();
      }
    }
  }

  /**
  * Handles main logic for ghost including the mode type, making sure
  * ghosts don't walk into walls and automates the movement based on
  * list of available moves for the ghost.
  * @param players xval and yval, direction of player and fright mode
  */
  public void tick(int pxval, int pyval, String dir, boolean fright){
    if (fright) {
      this.sprite = this.fright;
      Random r = new Random();
      int rand = r.nextInt(576 + 576) - 576;
      this.targetX+=rand;
      this.targetY-=rand;

      if (this.frames > this.frightLen) {
        this.frames = 0;
        map.setFright();
      }
    }
    else{
      this.sprite = this.ghostIMG;

      if (this.frames > this.modeLen) {
        this.frames = 0;
        setModeLen();
      }
      if(mode%2 == 1){
        scatter();
      }
      else if(mode%2 == 0){
        chase(pxval, pyval, dir);
      }
    }

    this.frames+=1;

    if (this.x > 432) {
      moveLeft();
    }
    if (this.y > 512) {
      moveUp();
    }
    if (this.x < 0) {
      moveRight();
    }
    if (this.y < 48) {
      moveDown();
    }

    this.x += this.xVel;
    this.y += this.yVel;

    if (moveOps()!= null) {
      String movement = whichCell(moveOps());
      if (movement.equals("l")) {
        moveLeft();
      }
      else if (movement.equals("u")) {
        moveUp();
      }
      else if (movement.equals("r")) {
        moveRight();
      }
      else if (movement.equals("d")) {
        moveDown();
      }
    }
  }


  /**
  * Implements scatter mode where ghosts go to corresponding corners.
  * Target cell is set to corner.
  */
  public void scatter(){
    if (this.type == GhostType.AMBUSHER) {
      this.targetX = 448;
      this.targetY = 0;
    }
    else if (this.type == GhostType.CHASER) {
      this.targetX = 0;
      this.targetY = 0;
    }
    else if (this.type == GhostType.IGNORANT) {
      this.targetX = 448;
      this.targetY = 600;
    }
    else if (this.type == GhostType.WHIM) {
      this.targetX = 0;
      this.targetY = 600;
    }
  }

  /**
  * Implements chase mode where ghosts follow certain logic.
  * Target cell is set to the logic of the ghosts chase mode.
  * @param Players x and y vals and direction
  */
  public void chase(int x, int y, String direction){
    this.targetX = x-4 + 8;
    this.targetY = y-4 + 8;
    if (this.type == GhostType.AMBUSHER) {
      if (direction.equals("u")) {
        targetY+=104;
      }
      else if (direction.equals("d")) {
        targetY-=104;
      }
      else if (direction.equals("l")) {
        targetX-=104;
      }
      else if (direction.equals("r")) {
        targetX+=104;
      }
    }
  }

  /**
  * Implements chase mode where ghosts follow certain logic.
  * Target cell is set to the logic of the ghosts chase mode.
  * @param Players x and y vals and direction, chasers x and y vals
  */
  public void chase(int x, int y, int chaseX, int chaseY, String direction){
    this.targetX = x-4 +8;
    this.targetY = y-4 +8;
    if (this.type == GhostType.IGNORANT) {
      int ghostX = this.x/16;
      int ghostY = this.y/16;
      int tarX = this.targetX/16;
      int tarY = this.targetY/16;

      double dist = euclidian(ghostX, ghostY, tarX, tarY);
      if (dist < 8) {
        this.targetX = 0;
        this.targetY = 600;
      }
    }
    else if (this.type == GhostType.WHIM) {
      if (direction.equals("u")) {
        targetY+=52;
      }
      else if (direction.equals("d")) {
        targetY-=52;
      }
      else if (direction.equals("l")) {
        targetX-=52;
      }
      else if (direction.equals("r")) {
        targetX+=52;
      }
      this.targetX = 2* this.targetX;
      this.targetY= 2* this.targetY;
    }
  }

  /**
  * Finds the possible moves for Ghost
  * @return ArrayList<String> of direction moves possible
  */
  public ArrayList<String> moveOps(){
    ArrayList<String> possibleMoves = new ArrayList<String>();
    if (!(this.map.nextCell(this.x+16, this.y, "r"))) {
      possibleMoves.add("r");
    }
    if (!(this.map.nextCell(this.x, this.y+16, "d"))) {
      possibleMoves.add("d");
    }
    if (!(this.map.nextCell(this.x-16, this.y, "l"))) {
      possibleMoves.add("l");
    }
    if (!(this.map.nextCell(this.x, this.y-16, "u"))) {
      possibleMoves.add("u");
    }
    if (possibleMoves.size() > 0) {
      return possibleMoves;
    }
    else{
      return null;
    }
  }

  /**
  * Finds which cell is the best cell for Ghost to move to.
  * Based on target location of Ghost and possible moves available.
  * @return String move of the shortest length from target
  */
  public String whichCell(ArrayList<String> moveOptions){
    ArrayList<String> shortStr = new ArrayList<String>();
    String shortest = "";
    double shortint = 100000;
    for (String move : moveOptions) {
      int nextX = this.x ;
      int nextY = this.y ;
      if (move.equals("r")) {
        nextX+=16;
        double right = euclidian(targetX, targetY, nextX, nextY);
        if (right <= shortint) {
          if (!(this.direction.equals("l"))) {
            shortest = "r";
            shortint = right;
          }
        }
      }
      else if (move.equals("d")) {
        nextY+=16;
        double down = euclidian(targetX, targetY, nextX, nextY);
        if(down <= shortint){
          if (!(this.direction.equals("u"))) {
            shortest = "d";
            shortint = down;
          }
        }
      }
      else if (move.equals("l")) {
        nextX-=16;
        double left = euclidian(targetX, targetY, nextX, nextY);
        if (left <= shortint) {
          if (!(this.direction.equals("r"))) {
            shortest = "l";
            shortint = left;
          }
        }
      }
      else if (move.equals("u")) {
        nextY-=16;
        double up = euclidian(targetX, targetY, nextX, nextY);
        if (up <= shortint) {
          if (!(this.direction.equals("d"))) {
            shortest = "u";
            shortint = up;
          }
        }
      }
    }
    return shortest;
  }

  /**
  * Checks if Ghost can move into space
  * @param whether Ghosts step is into a wall
  */
  public void step(boolean stepped){
    if (stepped) {
      if (this.direction.equals("u")) {
        moveDown();
        return;
      }
      else if (this.direction.equals("d")) {
        moveUp();
        return;
      }
      else if (this.direction.equals("l")) {
        moveRight();
        return;
      }
      else if (this.direction.equals("r")) {
        moveLeft();
        return;
      }
    }
  }

  /**
  * @return straight line distance (Euclidian distance)
  */
  public double euclidian(int x1, int y1, int x2, int y2){
    return(Math.sqrt((y2-y1)*(y2-y1)+(x2-x1)*(x2-x1)));
  }

  /**
  * Sets the target location for Ghost
  */
  public void setTarget(int x, int y){
    this.targetX = x;
    this.targetY = y;
  }

  /**
  * @return target X value
  */
  public int getTargetX(){
    return this.targetX;
  }

  /**
  * @return target Y value
  */
  public int getTargetY(){
    return this.targetY;
  }

  /**
  * Draws Ghost object
  * @param PApplet object
  */
  public void draw(PApplet app){
    app.image(this.sprite, this.x-4, this.y-4);
  }

  /**
  * Moves Ghost right, only if in middle of path
  */
  public void moveRight(){
    if(checkYMid()){
      this.direction = "r";
      this.xVel = 1*this.speed;
      this.yVel = 0*this.speed;
    }
  }

  /**
  * Moves Ghost left, only if in middle of path
  */
  public void moveLeft(){
    if(checkYMid()){
      this.direction = "l";
      this.xVel = -1*this.speed;
      this.yVel = 0*this.speed;
    }
  }

  /**
  * Moves Ghost up, only if in middle of path
  */
  public void moveUp(){
    if(checkXMid()){
      this.direction = "u";
      this.yVel = -1*this.speed;
      this.xVel = 0*this.speed;
    }
  }

  /**
  * Moves Ghost down, only if in middle of path
  */
  public void moveDown(){
    if(checkXMid()){
      this.direction = "d";
      this.yVel = 1*this.speed;
      this.xVel = 0*this.speed;
    }
  }

  /**
  * @return width of Ghost
  */
  public int getWidth(){
    return this.sprite.width;
  }

  /**
  * @return width of Ghost
  */
  public int getHeight(){
    return this.sprite.height;
  }

  /**
  * @return direction of Ghost movement
  */
  public String getDirection(){
    return this.direction;
  }

  /**
  * Checks if Ghost X is in the middle of a path
  * @return whether in the middle
  */
  public boolean checkXMid(){
    if((this.x+8)%16 == 8){
      return true;
    }
    return false;
  }

  /**
  * Checks if Ghost Y is in the middle of a path
  * @return whether in the middle
  */
  public boolean checkYMid(){
    if((this.y+8)%16 == 8){
      return true;
    }
    return false;
  }


}
