import java.awt.Color;
import java.lang.Math;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.net.URL;
import java.awt.Rectangle;

public class CozyCoupe{
	private Image rw, rg, bw, bg, pw, pg, rwi, rgi, bwi, bgi, pwi, pgi, CozyColor;
	private AffineTransform tx;

	int width, height;
	int screenW = 1024;
	int screenH = 576;
	int x, y;						//position of the object
	int vx;							//movement variables
	double scaleWidth = .0625;		//change to scale image
	double scaleHeight = .0625; 	//change to scale image
	
	public CozyCoupe() {
		rg  = getImage("/imgs/"+"Red-Gold.png"); //load the other colors
		rw 	= getImage("/imgs/"+"Red-White.png"); //load the first color for Car

		pw  = getImage("/imgs/"+"Pink-White.png"); //load the other colors
		pg  = getImage("/imgs/"+"Pink-Gold.png"); //load the other colors
		
		//These two have bigger hit boxes
		bw  = getImage("/imgs/"+"Blue-White.png"); //load the other colors
		bg  = getImage("/imgs/"+"Blue-Gold.png"); //load the other colors
		
		//inverted images
		rgi  = getImage("/imgs/"+"Red-GoldI.png"); //load the other colors
		rwi  = getImage("/imgs/"+"Red-WhiteI.png"); //load the first color for Car
		pwi  = getImage("/imgs/"+"Pink-WhiteI.png"); //load the other colors
		pgi  = getImage("/imgs/"+"Pink-GoldI.png"); //load the other colors
		//These two have bigger hit boxes
		bwi  = getImage("/imgs/"+"Blue-WhiteI.png"); //load the other colors
		bgi  = getImage("/imgs/"+"Blue-GoldI.png"); //load the other colors

		//Sprite initial variables
		width = 920;
		height = 962;
		
		x = -width;
		y = 300;
		
		vx = 0;
		
		tx = AffineTransform.getTranslateInstance(0, 0);
		
		init(x, y);		//initialize the location of the image
	}
	
	
	public CozyCoupe(int x, int y, int vx, int color) {
		this();
		
		this.x = x;
		this.y = y;
		this.vx = vx;
		
		//Pick a random color
		int dogColor = (int)(Math.random() * (2)) + 1;
		
		//Color assignment & direction facing
		if (vx > 0) {
			if (color == 1) {
				this.CozyColor = (dogColor==1)?rwi:rgi;
	
				width = 920;
				height = 962;
			} else if (color == 2) {
				this.CozyColor = (dogColor==1)?pwi:pgi;
	
				width = 920;
				height = 962;
			} else if (color == 3) {
				this.CozyColor = (dogColor==1)?bwi:bgi;
	
				width = 1150;
				height = 962;
			}
		}
		
		else {
			if (color == 1) {
				this.CozyColor = (dogColor==1)?rw:rg;
	
				width = 920;
				height = 962;
			} else if (color == 2) {
				this.CozyColor = (dogColor==1)?pw:pg;
	
				width = 920;
				height = 962;
			} else if (color == 3) {
				this.CozyColor = (dogColor==1)?bw:bg;
	
				width = 1150;
				height = 962;
			}
		}
	}
	
	public boolean collided(Duck character) {
		// represent each object as a rectangle and see if they intersect
		Rectangle main = character.hitbox();
		Rectangle thisObject = new Rectangle(x, y, (int)(width*scaleWidth), (int)(height*scaleHeight));
		
		return main.intersects(thisObject);
	}

	public void paint(Graphics g) {
		//these are the 2 lines of code needed draw an image on the screen
		Graphics2D g2 = (Graphics2D) g;
		
		//Move according to velocity
		x += vx;
		
		//If of screen, get back on
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

		//Draw dog, finally
		g2.drawImage(CozyColor, tx, null);
		
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
			URL imageURL = CozyCoupe.class.getResource(path);
			tempImage = Toolkit.getDefaultToolkit().getImage(imageURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempImage;
	}

}
