import java.awt.Color;
import java.lang.Math;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.net.URL;

public class Flower{
	private Image full, half, quarter;
	private AffineTransform tx;
	
	int frameCount = 0;
	int currentFrame = 1;
	int width, height;
	int screenW = 1024;
	int screenH = 576;
	boolean invisible = false;		//enable/disable collisions
	int x, y;						//position of the object
	int vx;							//movement variables
	double scaleWidth = .1875;		//change to scale image
	double scaleHeight = .1875; 	//change to scale image
	
	public Flower() {
		full  = getImage("/imgs/"+"Flower-1.png"); //load the full flower
		half  = getImage("/imgs/"+"Flower-2.png"); //load the half flower
		quarter  = getImage("/imgs/"+"Flower-3.png"); //load the quarter flower
		
		//Sprite initial variables
		width = 970;
		height = 250;
		
		x = -width;
		y = 300;
		
		vx = 0;
		
		tx = AffineTransform.getTranslateInstance(0, 0);
		
		init(x, y);		//initialize the location of the image
	}
	
	
	public Flower(int x, int y, int vx) {
		this();
		
		this.x = x;
		this.y = y;
		this.vx = vx;
	}
	
	public boolean collided(Duck character) {
		//Check if the flower is invisible
		if (invisible) {
			if (Frame.debugging) {
				//System.out.println("Leaf is Invisible");
			}
			return false;
		}
		
		// represent each object as a rectangle and see if they intersect
		Rectangle main = character.hitbox();
		Rectangle thisObject = new Rectangle(x, y, (int)(width*scaleWidth), (int)(height*scaleHeight));
		
		return main.intersects(thisObject);
	}

	public void paint(Graphics g) {
		//these are the 2 lines of code needed draw an image on the screen
		Graphics2D g2 = (Graphics2D) g;
		
		x += vx;
		
		//Animation stuff & invisible flower code
		frameCount++;
		if (frameCount%50 == 0) {
			currentFrame += 1;
		}
		if (currentFrame == 1) {
			g2.drawImage(full, tx, null);
		} else if (currentFrame == 2) {
			g2.drawImage(half, tx, null);
		} else if (currentFrame == 3) {
			g2.drawImage(quarter, tx, null);
		} else if (currentFrame == 4) {
			//Disable Collisions
			invisible = true;
		} else if (currentFrame == 5) {
			currentFrame = 1;
			//Re-Enable Collisions
			invisible = false;
		}
		
		
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
			URL imageURL = Flower.class.getResource(path);
			tempImage = Toolkit.getDefaultToolkit().getImage(imageURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempImage;
	}

}
