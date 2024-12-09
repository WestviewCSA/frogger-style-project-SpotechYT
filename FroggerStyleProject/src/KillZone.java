import java.awt.Color;
import java.lang.Math;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.net.URL;

public class KillZone{

	int width, height;
	int x, y;
	
	public KillZone(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	//Check for collisions
	public boolean collided(Duck character) {
		// represent each object as a rectangle and see if they intersect
		Rectangle main = character.hitbox();
		Rectangle thisObject = new Rectangle(x, y, width, height);
		
		return main.intersects(thisObject);
	}

	public void paint(Graphics g) {
		//these are the 2 lines of code needed draw an image on the screen
		Graphics2D g2 = (Graphics2D) g;
		
		//draw hit box based on x, y, width, height ONLY IF DEBUGGING
		if (Frame.debugging) {
			g.setColor(Color.yellow);
			g2.fillRect(x, y, width, height);
		}
	}
}
