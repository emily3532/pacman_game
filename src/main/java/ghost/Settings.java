package ghost;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.util.Scanner;
import java.lang.Long;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import processing.core.PImage;
import processing.core.PApplet;

public class Settings{
  private String mapfile;
  private long lives;
  private long speed;
  private JSONArray modeLengths;
  private String[][] map;
  private Cell[][] cellMap;
  private int playerX;
  private int playerY;
  private int ambusherX;
  private int ambusherY;
  private int chaserX;
  private int chaserY;
  private int ignorantX;
  private int ignorantY;
  private int whimX;
  private int whimY;
  private boolean fright;
  private long frightenedLength;

  public Settings(PApplet app){
    this.mapfile = mapfile;
    this.lives = 0;
    this.speed = 0;
    this.modeLengths = null;
    this.map = new String[36][28];
    this.cellMap = new Cell[36][28];
    this.playerX = 0;
    this.playerY = 0;
    this.fright = false;
    this.frightenedLength = 0;
  }

  /**
  * Reads a JSON configuration file named "config.json".
  * Extracts the map filename, lives, speed, length of frightened mode and length of other modes.
  * @throws FileNotFoundException if config.json cannot be found.
  * @throws ParseException if invalid JSON.
  * @throws IOException if error in reading the file.
  */
  public void configReader(){
    JSONParser jParse = new JSONParser();
    try {
      FileReader fRead = new FileReader("config.json");
      Object obj = jParse.parse(fRead);
      JSONObject jObject = (JSONObject) obj;
      this.mapfile = (String) jObject.get("map");
      this.lives = (Long) jObject.get("lives");
      this.speed = (Long) jObject.get("speed");
      this.frightenedLength = (Long) jObject.get("frightenedLength");
      this.modeLengths = (JSONArray) jObject.get("modeLengths");
    } catch (FileNotFoundException e) {
      System.out.println("File not found");
    } catch (IOException e) {
      System.out.println(e);
    } catch(ParseException e) {
      System.out.println(e);
    } return;
  }

  /**
  * Returns the modelengths from config.json as a JSONArray of ints.
  * @return modeLengths
  */
  public JSONArray getModes(){
    return this.modeLengths;
  }

  /**
  * Returns the modelengths from config.json as a JSONArray of ints.
  * @return modeLengths
  */
  public String getMapFile(){
    return this.mapfile;
  }

  /**
  * Reads the map file provided from config.json and creates an array of an array of Strings.
  * @throws FileNotFoundException if map file cannot be found.
  */
  public void mapReader(){
    try {
      File fMap = new File(this.mapfile);
      Scanner sMap = new Scanner(fMap);
      int k = 0;

      while (sMap.hasNextLine()) {
        String ln = sMap.nextLine();

        for (int i = 0; i< ln.length(); i++) {
          this.map[k][i] = String.valueOf(ln.charAt(i));
        }
        k+=1;
      }
      return;
    }
    catch(FileNotFoundException e){
      System.out.println("File not found");
    }
    return;
  }

  /**
  * returns a String[][] of the map.
  * @return map
  */
  public String[][] getMap(){
    configReader();
    mapReader();
    return this.map;
  }

  /**
  * Creates an array of objects from cells.
  * Identifies the X and Y coordinates of Ghost(s) and Player.
  * @param app, PApplet
  */
  public void mapCell(PApplet app){
    for(int y = 0; y < this.map.length; y++){
      for(int x = 0; x < this.map[y].length; x++){
        if(this.map[y][x].equals("0")){
        }
        else if (this.map[y][x].equals("1")){
          this.cellMap[y][x] = new Map(app.loadImage("src/main/resources/horizontal.png"), 16*x, 16*y);
        }
        else if (this.map[y][x].equals("2")){
          this.cellMap[y][x] = new Map(app.loadImage("src/main/resources/vertical.png"), 16*x, 16*y);
        }
        else if (this.map[y][x].equals("3")){
          this.cellMap[y][x] = new Map(app.loadImage("src/main/resources/upLeft.png"), 16*x, 16*y);
        }
        else if (this.map[y][x].equals("4")){
          this.cellMap[y][x] = new Map(app.loadImage("src/main/resources/upRight.png"), 16*x, 16*y);
        }
        else if (this.map[y][x].equals("5")){
          this.cellMap[y][x] = new Map(app.loadImage("src/main/resources/downLeft.png"), 16*x, 16*y);
        }
        else if (this.map[y][x].equals("6")){
          this.cellMap[y][x] = new Map(app.loadImage("src/main/resources/downRight.png"), 16*x, 16*y);
        }
        else if (this.map[y][x].equals("7")){
          this.cellMap[y][x] = new Fruit(app.loadImage("src/main/resources/fruit.png"), 16*x, 16*y);
        }
        else if (this.map[y][x].equals("8")){
          this.cellMap[y][x] = new SuperFruit(app.loadImage("src/main/resources/fruit.png"), 16*x -10, 16*y -8);
        }
        else if (this.map[y][x].equals("a")){
          this.ambusherX = x;
          this.ambusherY = y;
        }
        else if (this.map[y][x].equals("c")){
          this.chaserX = x;
          this.chaserY = y;
        }
        else if (this.map[y][x].equals("i")){
          this.ignorantX = x;
          this.ignorantY = y;
        }
        else if (this.map[y][x].equals("w")){
          this.whimX = x;
          this.whimY = y;
        }
        else if(this.map[y][x].equals("p")){
          this.playerX = x;
          this.playerY = y;
        }

      }
    }
  }

  /**
  * @return cell version of map
  */
  public Cell[][] getCellMap(){
    return this.cellMap;
  }

  /**
  * Draws the map and lives of Waka
  */
  public void draw(PApplet app){
    for(Cell[] a : this.cellMap){
      for(Cell cell : a){
        if(cell == null){
        } else{
            app.image(cell.getSprite(), cell.getX(), cell.getY());
        }
      }
    }
    for(int l = 0; l < this.lives;l++){
      Cell live = new Cell(app.loadImage("src/main/resources/playerRight.png"), 16*(l)+(l*16)+4, 16*34);
      app.image(live.getSprite(), live.getX(), live.getY());
    }
  }

  /**
  * @return players X value
  */
  public int getPlayerX(){
    return this.playerX;
  }

  /**
  * @return players Y value
  */
  public int getPlayerY(){
    return this.playerY;
  }

  /**
  * @return ambusher X value
  */
  public int getaX(){
    return this.ambusherX;
  }

  /**
  * @return ambusher Y value
  */
  public int getaY(){
    return this.ambusherY;
  }

  /**
  * @return chaser X value
  */
  public int getcX(){
    return this.chaserX;
  }

  /**
  * @return chaser Y value
  */
  public int getcY(){
    return this.chaserY;
  }

  /**
  * @return ignorant X value
  */
  public int getiX(){
    return this.ignorantX;
  }

  /**
  * @return ignorant Y value
  */
  public int getiY(){
    return this.ignorantY;
  }

  /**
  * @return whim X value
  */
  public int getwX(){
    return this.whimX;
  }

  /**
  * @return whim Y value
  */
  public int getwY(){
    return this.whimY;
  }


  /**
  * @return speed
  */
  public int getSpeed(){
    return (int) this.speed;
  }

  /**
  * @return lives of Waka
  */
  public Long getLives(){
    return this.lives;
  }

  /**
  * @return length of Frightened mode
  */
  public Long getFrightLen(){
    return this.frightenedLength;
  }

  /**
  * Checks if Waka has stepped on fruit (and eats it).
  * Checks if Waka has stepped on a SuperFruit and turns on fright mode.
  * @return count of fruit left
  */
  public int stepCell(int xv, int yv, String direction){
    int fruitCount = 0;
    for(int y = 0; y < this.map.length; y++){
      for(int x = 0; x < this.map[y].length; x++){
        if(this.cellMap[y][x]!= null && this.cellMap[y][x].getClass() == Fruit.class){
          fruitCount+=1;
        }
        else if(this.cellMap[y][x]!= null && this.cellMap[y][x].getClass() == SuperFruit.class){
          fruitCount+=1;
        }
      }
    }
    if(direction.equals("u")){
      yv+=8;
      xv+=6;
      if(this.cellMap[(yv)/16][(xv)/16] != null){
        if(this.cellMap[(yv)/16][(xv)/16].getClass() == Fruit.class){
          this.cellMap[(yv)/16][(xv)/16] = null;
        }
        else if(this.cellMap[(yv)/16][(xv)/16]!= null && this.cellMap[(yv)/16][(xv)/16].getClass() == SuperFruit.class){
          fruitCount+=1;
          this.fright = true;
          this.cellMap[(yv)/16][(xv)/16] = null;
        }
      }
    }
    else if(direction.equals("d")){
      yv+=8;
      xv+=6;
      if(this.cellMap[(yv)/16][(xv)/16] != null){
        if(this.cellMap[(yv)/16][(xv)/16].getClass() == Fruit.class){
          this.cellMap[(yv)/16][(xv)/16] = null;
        }
        else if(this.cellMap[(yv)/16][(xv)/16]!= null && this.cellMap[(yv)/16][(xv)/16].getClass() == SuperFruit.class){
          fruitCount+=1;
          this.fright = true;
          this.cellMap[(yv)/16][(xv)/16] = null;
        }
      }
    }
    else if(direction.equals("l")){
      yv+=6;
      xv+=8;
      if(this.cellMap[(yv)/16][(xv)/16] != null){
        if(this.cellMap[(yv)/16][(xv)/16].getClass() == Fruit.class){
          this.cellMap[(yv)/16][(xv)/16] = null;
        }
        else if(this.cellMap[(yv)/16][(xv)/16]!= null && this.cellMap[(yv)/16][(xv)/16].getClass() == SuperFruit.class){
          fruitCount+=1;
          this.fright = true;
          this.cellMap[(yv)/16][(xv)/16] = null;

        }
      }
    }
    else if(direction.equals("r")){
      yv+=6;
      xv+=8;
      if(this.cellMap[(yv)/16][(xv)/16] != null){
        if(this.cellMap[(yv)/16][(xv)/16].getClass() == Fruit.class){
          this.cellMap[(yv)/16][(xv)/16] = null;
        }
        else if(this.cellMap[(yv)/16][(xv)/16]!= null && this.cellMap[(yv)/16][(xv)/16].getClass() == SuperFruit.class){
          fruitCount+=1;
          this.fright = true;
          this.cellMap[(yv)/16][(xv)/16] = null;
        }
      }
    }
    return fruitCount;
  }

/**
* Checks if next cell is a wall
* @return whether next cell is a wall
*/
  public boolean nextCell(int xval, int yval, String direction){
    if(direction.equals("u")){
      if(this.cellMap[(yval+4)/16][(xval+4)/16] != null){
        if(this.cellMap[(yval+4)/16][(xval+4)/16].getClass() == Map.class){
          int boxLeft = this.cellMap[(yval+4)/16][(xval+4)/16].getX();
          int boxRight = this.cellMap[(yval+4)/16][(xval+4)/16].getX() +16;
          int boxTop = this.cellMap[(yval+4)/16][(xval+4)/16].getY();
          int boxBottom = this.cellMap[(yval+4)/16][(xval+4)/16].getY()+16;

          int playerLeft = xval+4;
          int playerTop = yval-4;
          int playerRight = xval + 4+16;
          int playerBottom = yval-4+16;
          if(playerLeft < boxRight && playerBottom > boxTop && playerTop< boxBottom){
            return true;
          }
        }
      }
    }
    else if(direction.equals("d")){
      //yval-=16;
      if(this.cellMap[(yval+12)/16][(xval+4)/16] != null){
        //System.out.println(this.cellMap[(yval+12)/16][(xval+4)/16].getClass());
        if(this.cellMap[(yval+12)/16][(xval+4)/16].getClass() == Map.class){
          int boxLeft = this.cellMap[(yval+12)/16][(xval+4)/16].getX();
          int boxRight = this.cellMap[(yval+12)/16][(xval+4)/16].getX() +16;
          int boxTop = this.cellMap[(yval+12)/16][(xval+4)/16].getY();
          int boxBottom = this.cellMap[(yval+12)/16][(xval+4)/16].getY()+16;

          int playerLeft = xval+4;
          int playerTop = yval+12;
          int playerRight = xval + 4+16;
          int playerBottom = yval+12+16;
          if(playerLeft < boxRight && playerBottom > boxTop && playerTop< boxBottom){
            return true;
          }
        }
      }
    }
    else if(direction.equals("l")){
      //yval-=16;
      if(this.cellMap[(yval+4)/16][(xval+4)/16] != null){
        if(this.cellMap[(yval+4)/16][(xval+4)/16].getClass() == Map.class){
          int boxLeft = this.cellMap[(yval+4)/16][(xval+4)/16].getX();
          int boxRight = this.cellMap[(yval+4)/16][(xval+4)/16].getX() +16;
          int boxTop = this.cellMap[(yval+4)/16][(xval+4)/16].getY();
          int boxBottom = this.cellMap[(yval+4)/16][(xval+4)/16].getY()+16;

          int playerLeft = xval-10;
          int playerTop = yval+4;
          int playerRight = xval -10+16;
          int playerBottom = yval+4+16;
          if(playerLeft < boxRight && playerBottom > boxTop && playerTop< boxBottom){
            return true;
          }
        }
      }
    }
    else if(direction.equals("r")){
      //yval-=16;
      if(this.cellMap[(yval+4)/16][(xval+10)/16] != null){
        if(this.cellMap[(yval+4)/16][(xval+10)/16].getClass() == Map.class){
          int boxLeft = this.cellMap[(yval+4)/16][(xval+10)/16].getX();
          int boxRight = this.cellMap[(yval+4)/16][(xval+10)/16].getX() +16;
          int boxTop = this.cellMap[(yval+4)/16][(xval+10)/16].getY();
          int boxBottom = this.cellMap[(yval+4)/16][(xval+10)/16].getY()+16;

          int playerLeft = xval+14;
          int playerTop = yval+4;
          int playerRight = xval+14+16;
          int playerBottom = yval+4+16;
          if(playerLeft < boxRight && playerBottom > boxTop && playerTop< boxBottom){
            return true;
          }
        }
      }
    }
    return false;
  }

  public boolean removeLife(){
    this.lives-=1;

    if(this.lives  == 0 ){
      return true;
    }
    return false;
  }

  public boolean getFright(){
    return this.fright;
  }

  public void setFright(){
    this.fright = false;
  }


}
