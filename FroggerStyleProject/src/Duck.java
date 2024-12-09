import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.net.URL;

public class Duck{
	private Image up, down, upI, downI;	
	private AffineTransform tx;
	
	int frameCount = 0;
	boolean currentFrame = false;
	int dir = 0; 					//0-forward, 1-backward, 2-left, 3-right
	int width, height;
	int x, y;						//position of the object
	int vx, vy;						//movement variables
	int widthS, heightS;			//width * S and height * S
	double scale = .125;		//change to scale image

	public Duck() {
		up 	= getImage("/imgs/"+"Duck1.png"); //load the first frame image for Duck
		down= getImage("/imgs/"+"Duck2.png"); //load the second frame image for Duck
		
		//Inverted TODO
		//upI 	= getImage("/imgs/"+"Duck1I.png"); //load the first frame image for Duck
		//downI	= getImage("/imgs/"+"Duck2I.png"); //load the second frame image for Duck

		//Sprite initial variables
		width = 252;
		height = 342;
		
		x = 0;
		y = 0;
		
		vx = 0;
		vy = 0;
		
		widthS = (int)(width * scale);
		heightS = (int)(height * scale);
		
		tx = AffineTransform.getTranslateInstance(0, 0);
		
		init(x, y);		//initialize the location of the image
		
	}

	public void paint(Graphics g) {
		//these are the 2 lines of code needed draw an image on the screen
		Graphics2D g2 = (Graphics2D) g;
		
		x += vx;
		y += vy;	
		
		init(x,y);
		
		//Animation stuff
		frameCount++;
		if (frameCount%10== 0) {
			currentFrame = !currentFrame;
		}
		if (currentFrame) {
			g2.drawImage(up, tx, null);
		} else {
			g2.drawImage(down, tx, null);
		}
		
		
		
		//draw hitbox based on x, y, width, height ONLY IF DEBUGGING
		if (Frame.debugging) {
			g.setColor(Color.red);
			g2.drawRect(x, y, widthS, heightS);
		}
	}
	
	public Duck(int x, int y) {
		this();
		
		this.x = x;
		this.y = y;
		
		widthS = (int)(width * scale);
		heightS = (int)(height * scale);
	}
	
	//output of duck rectangle for collisions in other classes
	public Rectangle hitbox() {
		Rectangle main = new Rectangle(x, y, widthS, heightS);
		return main;
	}
	
	public void move(int dir) {
		switch(dir) {
		
		// Up
		case 0:
			y -= heightS+20;
			break;
		
		// Down
		case 1:
			y += heightS+20;
			break;
			
		// Left
		case 2:
			x -= widthS;
			break;
		
		// Right
		case 3:
			x += widthS;
			break;
			
		}
		if (Frame.debugging) {
			System.out.println("Duck Position: " + x + ", " + y);
		}
	}
	
	public void setSpeed(int dir, int v) {
		switch(dir) {
		
		// X Direction
		case 0:
			vx = v;
			break;
			
		// Y Direction
		case 1:
			vy = v;
			break;
		}
	}
	
	public void moveTo(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	private void init(double a, double b) {
		tx.setToTranslation(a, b);
		tx.scale(scale, scale);
	}

	private Image getImage(String path) {
		Image tempImage = null;
		try {
			URL imageURL = Duck.class.getResource(path);
			tempImage = Toolkit.getDefaultToolkit().getImage(imageURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempImage;
	}

}
