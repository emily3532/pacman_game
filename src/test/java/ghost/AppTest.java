/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ghost;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

class AppTest extends PApplet {
    @Test
    public void constructApp() {
        App classUnderTest = new App();
        assertNotNull(classUnderTest);

    }

    @Test
    public void drawAppTest(){
      App a = new App();
      PApplet.runSketch(new String[] {"ghost.app"}, a);
    }
}
