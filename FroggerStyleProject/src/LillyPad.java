import java.awt.Color;
import java.lang.Math;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.net.URL;

public class LillyPad{
	private Image LillyPad, LillyDuck;
	private AffineTransform tx;

	int width, height;
	int screenW = 1024;
	int screenH = 576;
	boolean reached = false;
	int x, y;						//position of the object
	double scaleWidth = .2;		//change to scale image
	double scaleHeight = .2; 	//change to scale image
	
	public LillyPad() {
		LillyPad = getImage("/imgs/"+"LillyPad.png"); 		//load the lillypad image
		LillyDuck = getImage("/imgs/"+"DuckOnLilly.png"); 	//load the lillypad duck image

		//Sprite initial variables
		width = 230;
		height = 276;
		
		x = -width;
		y = 300;
		
		tx = AffineTransform.getTranslateInstance(0, 0);
		
		init(x, y);		//initialize the location of the image
	}
	
	public LillyPad(int x, int y) {
		this();
		
		this.x = x;
		this.y = y;
	}
	
	//Check for collisions
	public boolean collided(Duck character) {
		// represent each object as a rectangle and see if they intersect
		Rectangle main = character.hitbox();
		Rectangle thisObject = new Rectangle(x, y, (int)(width*scaleWidth), (int)(height*scaleHeight));
		
		return main.intersects(thisObject);
	}
	
	//Getters and setters for reached goal to know if the duck made it to the lily
	public void reached(boolean t) {
		reached = t;
	}
	
	public boolean isReached() {
		return reached;
	}

	public void paint(Graphics g) {
		//these are the 2 lines of code needed draw an image on the screen
		Graphics2D g2 = (Graphics2D) g;
		
		init(x,y);
		
		//Sprites per status
		if (reached) {
			g2.drawImage(LillyDuck, tx, null);
		} else {
			g2.drawImage(LillyPad, tx, null);
		}
		
		//draw hit box based on x, y, width, height ONLY IF DEBUGGING
		if (Frame.debugging) {
			g.setColor(Color.red);
			g2.drawRect(x, y, (int)(width*scaleWidth), (int)(height*scaleHeight));
		}
	}
	
	private void init(double a, double b) {
		tx.setToTranslation(a, b);
		tx.scale(scaleWidth, scaleHeight);
	}

	private Image getImage(String path) {
		Image tempImage = null;
		try {
			URL imageURL = LillyPad.class.getResource(path);
			tempImage = Toolkit.getDefaultToolkit().getImage(imageURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempImage;
	}

}
