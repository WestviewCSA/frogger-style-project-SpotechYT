import java.awt.Color;
import java.lang.Math;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.net.URL;

public class Heart{
	private Image H5, H4, H3, H2, H1, H0;
	private AffineTransform tx;

	int width, height;
	int screenW = 1024;
	int screenH = 576;
	boolean reached = false;
	int x, y;						//position of the object
	double scaleWidth = .5;		//change to scale image
	double scaleHeight = .5; 	//change to scale image
	
	public Heart() {
		//Images for each heart level
		H5 = getImage("/imgs/"+"Heart-5.png");
		H4 = getImage("/imgs/"+"Heart-4.png");
		H3 = getImage("/imgs/"+"Heart-3.png");
		H2 = getImage("/imgs/"+"Heart-2.png");
		H1 = getImage("/imgs/"+"Heart-1.png");
		H0 = getImage("/imgs/"+"Heart-0.png");

		//Sprite initial variables
		width = 252;
		height = 48;
		
		x = 0;
		y = 0;
		
		tx = AffineTransform.getTranslateInstance(0, 0);
		
		init(x, y);		//initialize the location of the image
	}
	
	public Heart(int x, int y) {
		this();
		
		this.x = x;
		this.y = y;
	}

	public void paint(Graphics g, int lives) {
		//these are the 2 lines of code needed draw an image on the screen
		Graphics2D g2 = (Graphics2D) g;
		
		init(x,y);
		
		//Set image accordingly
		if (lives == 5) {
			g2.drawImage(H5, tx, null);
		} else if (lives == 4) {
			g2.drawImage(H4, tx, null);
		} else if (lives == 3) {
			g2.drawImage(H3, tx, null);
		} else if (lives == 2) {
			g2.drawImage(H2, tx, null);
		} else if (lives == 1) {
			g2.drawImage(H1, tx, null);
		} else if (lives == 0) {
			g2.drawImage(H0, tx, null);
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
			URL imageURL = Heart.class.getResource(path);
			tempImage = Toolkit.getDefaultToolkit().getImage(imageURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempImage;
	}

}
