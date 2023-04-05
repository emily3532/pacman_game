package ghost;
import processing.core.PImage;
import processing.core.PApplet;
public class SuperFruit extends Cell{
  private int x;
  private int y;
  private int yVel;
  private PImage sprite;

  public SuperFruit(PImage sprite, int x, int y){
    super(sprite, x, y);
    this.sprite = sprite;
    this.x = x;
    this.y = y;

  }
  public PImage getSprite(){

    this.sprite.resize(32,32);
    return this.sprite;

  }


}
