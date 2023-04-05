package ghost;
import processing.core.PImage;
import processing.core.PApplet;
public class Map extends Cell{
  private int x;
  private int y;
  private PImage sprite;

  public Map(PImage sprite, int x, int y){
    super(sprite, x, y);
    this.sprite = sprite;
    this.x = x;
    this.y = y;
  }
}
