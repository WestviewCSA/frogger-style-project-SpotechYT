import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.lang.Math;
import java.awt.geom.AffineTransform;
import java.net.URL;
import java.util.ArrayList;

public class Frame extends JPanel implements ActionListener, MouseListener, KeyListener {
	
	//for any debugging code we add
	public static boolean debugging = false;
	
	//Timer related variables
	int waveTimer = 5; //each wave of enemies is 20s
	long ellapseTime = 0;
	
	//Font for score
	Font myFont = new Font("Courier", Font.BOLD, 25);
	
	//Colliding variables for wood and flowers
	boolean riding = false;
	
	//Score And Death Variables
	int score = 0;
	int lives = 5;
	boolean endGame = false;

	//Background Music File
	SimpleAudioPlayer backgroundMusic = new SimpleAudioPlayer("Bird That Carries You Over a Disproportionately Small Gap.wav", false);
	
	//Objects for all the game objects stored in arrays for easy use
	Duck duck = new Duck(490, 490);
	Heart hearts = new Heart(5, 510);
	KillZone water = new KillZone(0, 60, 1024, 173);
	CozyCoupe[] cozyR = new CozyCoupe[7];
	CozyCoupe[] cozyB = new CozyCoupe[5];
	CozyCoupe[] cozyP = new CozyCoupe[6];
	Wood[] wood1 = new Wood[5];
	Wood[] wood2 = new Wood[5];
	ArrayList<Flower> flower = new ArrayList<Flower>(5);
	ArrayList<LillyPad> lillys = new ArrayList<LillyPad>(5);
	
	//frame width/height
	int width = 1024;
	int height = 576;
	
	//Function to get image (used for background)
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
	
	public void paint(Graphics g) {
		super.paintComponent(g);
		
		// Add the background first thing so it's behind everything
		Image bkgnd = getImage("/imgs/Background.png");
		g.drawImage(bkgnd, 0, 0, 1010, 539, null);
		
		//Draw the score next
		g.setFont(myFont);
		g.setColor(Color.white);
		g.drawString("Score: " + score + "/5", 5, 503);
		
		//Draw the water kill zone, if debugging
		water.paint(g);
		
		//Checks current score and updates game accordingly
		if (score >= 5) {
			endGame = true;
			Image winScreen = getImage("/imgs/win.png");
			g.drawImage(winScreen, 0, 0, 1010, 539, null);
			g.drawString("Press R to Restart", 5, 20);
		} else if (lives <= 0) {
			endGame = true;
			Image loseScreen = getImage("/imgs/lose.png");
			g.drawImage(loseScreen, 0, 0, 1010, 539, null);
			g.drawString("Press R to Restart", 5, 20);
		}
		
		//If the game is not over, run collisions and paint objects.
		if (!endGame) {

			//Variable for log riding
			riding = false;
			
			/* Collision Detection & Drawing the objects */
			
			for(CozyCoupe obj : cozyR) {
				obj.paint(g);
				// invoke the collided method
				if (obj.collided(duck)) {
					duck.moveTo(490, 490);
					lives--;
					if (Frame.debugging) {
						System.out.println("Colided with Row 3");
					}
				}
			}
			for(CozyCoupe obj : cozyB) {
				obj.paint(g);
				// invoke the collided method
				if (obj.collided(duck)) {
					duck.moveTo(490, 490);
					lives--;
					if (Frame.debugging) {
						System.out.println("Colided with Row 2");
					}
				}
			}
			for(CozyCoupe obj : cozyP) {
				obj.paint(g);
				// invoke the collided method
				if (obj.collided(duck)) {
					duck.moveTo(490, 490);
					lives--;
					if (Frame.debugging) {
						System.out.println("Colided with Row 1");
					}
				}
			}
			
			for(Wood obj : wood1) {
				obj.paint(g);
				if (obj.collided(duck)) {
					duck.setSpeed(0, 3);
					riding = true;
					if (Frame.debugging) {
						System.out.println("Colided with Row 4");
					}
				}
			}
			for(Wood obj : wood2) {
				obj.paint(g);
				if (obj.collided(duck)) {
					duck.setSpeed(0, 4);
					riding = true;
					if (Frame.debugging) {
						System.out.println("Colided with Row 6");
					}
				}
			}
			
			for(Flower obj : flower) {
				obj.paint(g);
				if (obj.collided(duck)) {
					duck.setSpeed(0, -5);
					riding = true;
					if (Frame.debugging) {
						System.out.println("Colided with Row 5");
					}
				}
			}
			
			//Sets score for player if they land on a lily and move them to start again
			for(LillyPad obj : lillys) {
				obj.paint(g);
				if (obj.collided(duck) && !(obj.isReached())) {
					duck.moveTo(490, 490);
					obj.reached(true);
					score++;
					if (Frame.debugging) {
						System.out.println("Landed on LillyPad!");
					}
				} else if (obj.collided(duck) && obj.isReached()) {
					duck.moveTo(490, 490);
					lives--;
					if (Frame.debugging) {
						System.out.println("Landed on a used LillyPad!");
					}
				}
			}
			
			//If the duck is in the water and not riding a water object, kill it
			if(water.collided(duck) && !riding) {
				duck.moveTo(490, 490);
				lives--;
				if (Frame.debugging) {
					System.out.println("Touched the Water!");
				}
			}
			
			//Draw the hearts
			hearts.paint(g, lives);
			
			//If the duck gets of a water object, stop moving
			if (!riding) {
				duck.setSpeed(0, 0);
			}

			// if the duck leaves the screen, kill it, helps with logs and walls
			if(duck.x < 0 || duck.y < 0 || duck.y >= height-50 || duck.x >= width-10) {
				duck.moveTo(490, 490);
				lives--;
				if (Frame.debugging) {
					System.out.println("Left the Screen!");
				}
			}
		}
		
		// Paint the duck last so its on top
		duck.paint(g);
	}
	
	public static void main(String[] arg) {
		Frame f = new Frame();
	}
	
	public Frame() {
		//Set window title
		JFrame f = new JFrame("Ducker: The Game");
		
		//Set Favicon
		f.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/imgs/Duck1.png")));
		
		f.setSize(new Dimension(width, height));
		f.setBackground(Color.black);
		f.add(this);
		f.setResizable(false);
 		f.addMouseListener(this);
		f.addKeyListener(this);
	
		//Start playing background music
		backgroundMusic.play();

		//For loops to initialize objects in each array
		for(int i = 0; i < cozyR.length; i++) {
			cozyR[i] = new CozyCoupe(i*width/cozyR.length, 285, -6, 1);
		}
		for(int i = 0; i < cozyB.length; i++) {
			cozyB[i] = new CozyCoupe(i*width/cozyB.length, 350, 3, 3);
		}
		for(int i = 0; i < cozyP.length; i++) {
			cozyP[i] = new CozyCoupe(i*width/cozyP.length, 415, -4, 2);
		}
		
		for(int i = 0; i < wood1.length; i++) {
			wood1[i] = new Wood(i*width/wood1.length, 186, 3);
		}
		for(int i = 0; i < wood2.length; i++) {
			wood2[i] = new Wood(i*width/wood2.length, 62, 4);
		}
		
		for(int i = 0; i < 5; i++) {
			flower.add(new Flower(i*width/5, 124, -5));
		}
		
		//Lillys need to be separate for special coordinates
		lillys.add(new LillyPad(115, 0));
		lillys.add(new LillyPad(835, 0));
		lillys.add(new LillyPad(298, 0));
		lillys.add(new LillyPad(481, 0));
		lillys.add(new LillyPad(658, 0));
		
		//the cursor image must be outside of the src folder
		//you will need to import a couple of classes to make it fully 
		//functional! use eclipse quick-fixes
		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
				new ImageIcon("DuckCursor.png").getImage(),
				new Point(0,0),"duck cursor"));	
		
		
		Timer t = new Timer(16, this);
		t.start();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent m) {
		
	
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if (Frame.debugging) {
			System.out.println("Key Pressed: " + arg0.getKeyCode());
		}
		
		// W clicked, move up
		if(arg0.getKeyCode()==87) {
			duck.move(0);
		}
		// S clicked, move down
		if (arg0.getKeyCode()==83) {
			duck.move(1);
		}
		// A clicked, move left
		if (arg0.getKeyCode()==65) {
			duck.move(2);
		}
		// D clicked, move right
		if (arg0.getKeyCode()==68) {
			duck.move(3);
		}
		
		//R clicked, restart
		if (arg0.getKeyCode()==82) {
			//Reset variables
			score = 0;
			lives = 5;
			endGame = false;
			
			//Move player to start
			duck.moveTo(490, 490);
			
			//Reset LillyPads
			for(LillyPad obj : lillys) {
				obj.reached(false);
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
