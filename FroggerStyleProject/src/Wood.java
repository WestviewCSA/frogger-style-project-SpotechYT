import java.awt.Color;
import java.lang.Math;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.net.URL;

public class Wood{
	private Image sprite;
	private AffineTransform tx;

	int width, height;
	int screenW = 1024;
	int screenH = 576;
	int x, y;						//position of the object
	int vx;							//movement variables
	double scaleWidth = .3125;		//change to scale image
	double scaleHeight = .3125; 	//change to scale image
	
	public Wood() {
		sprite  = getImage("/imgs/"+"wood.png"); //load the wood image

		//Sprite initial variables
		width = 294;
		height = 138;
		
		x = -width;
		y = 300;
		
		vx = 0;
		
		tx = AffineTransform.getTranslateInstance(0, 0);
		
		init(x, y);		//initialize the location of the image
	}
	
	
	public Wood(int x, int y, int vx) {
		this();
		
		this.x = x;
		this.y = y;
		this.vx = vx;
	}
	
	//Check if collied with duck
	public boolean collided(Duck character) {
		// represent each object as a rectangle and see if they intersect
		Rectangle main = character.hitbox();
		Rectangle thisObject = new Rectangle(x, y, (int)(width*scaleWidth), (int)(height*scaleHeight));
		
		return main.intersects(thisObject);
	}

	public void paint(Graphics g) {
		//these are the 2 lines of code needed draw an image on the screen
		Graphics2D g2 = (Graphics2D) g;
		
		x += vx;
		
		//If off screen, come back
		if(vx > 0) {
			if(x >= screenW) {
				x = -(int)(width*scaleWidth);
			}
		} else if (vx < 0) {
			if(x <= (int)-width*scaleWidth) {
				x = screenW;
			}
		}
		
		init(x,y);

		//Draw wood log
		g2.drawImage(sprite, tx, null);
		
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
			URL imageURL = Wood.class.getResource(path);
			tempImage = Toolkit.getDefaultToolkit().getImage(imageURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempImage;
	}

}
