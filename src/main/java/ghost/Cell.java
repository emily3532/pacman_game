package ghost;
import processing.core.PImage;
import processing.core.PApplet;
class Cell{
  private int x;
  private int y;
  private PImage sprite;

  public Cell(PImage sprite, int x, int y){
    this.x = x;
    this.y = y;
    this.sprite = sprite;
  }
  public void tick(){
  }
  /**
  * Draws the Cell.
  */
  public void draw(PApplet app){
    app.image(this.sprite,this.x, this.y);
  }
  /**
  * @return sprite, PImage of Cell
  */
  public PImage getSprite(){
    return sprite;
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
