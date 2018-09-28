import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;


public class GamePanel extends JPanel implements MouseListener, KeyListener
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//create a new BufferedImage for the background
	BufferedImage bg;
	
	//set the co-ordinates for the background
	int bgX = 0, bgY = 0;
	
	//set how fast the background scrolls
	double bgMovement = 2;
	
	//create a new BufferedImage for the main character
	BufferedImage main;
	BufferedImage mainMirrored;
	
	//set the co-ordinates for the main character
	int mainX = 350, mainY = 300;
	
	//set how fast the main character scrolls
	int mainMovement = 8;
	
	//determines whether or not the main character is facing left or right
	boolean left = false;
	
	//set variables for jumping
	boolean jumping = false;
	double jump = 0.0;
	int jumpTime = 0;
	
	//create a new BufferedImage for the bullets
	BufferedImage fireball;
	BufferedImage fireballMirrored;
	BufferedImage iceball;
	BufferedImage iceballMirrored;
	
	//set the coordinates for the bullets
	int ballX = 0, ballY = 0;
	
	//set how fast the main character moves
	int ballMovement = 8;
	
	//determines whether or not the bullet is active
	boolean inPlay = false;
	
	//determines whether or not the bullet is facing left or right
	boolean ballLeftStart = false;
	
	//create a new BufferedImage for the enemy
	BufferedImage enemy;
	BufferedImage enemyMirrored;
	
	//sets the number of enemies
	int numEnemies = 2;
	
	//initialize the arrays for the enemy coordinates
	int[] enemyX = new int[numEnemies];
	int[] enemyY = new int[numEnemies];
	
	/*initialize the arrays that determines whether 
	or not the enemy is facing left or right*/
	boolean[] enemyActive = new boolean[numEnemies];
	boolean[] eRight = new boolean[numEnemies];
	
	//create a new BufferedImage for the platforms
	BufferedImage platform;
	
	//set the number of platforms
	int numPlatforms = 4;
	
	//sets how fast the platforms scrolls
	double pMovement = 1;
	
	//determines which platform the main character is currently on
	int platformCount = 1;
	
	//determines whether or not the main character is falling
	boolean falling = true;
	
	//determines whether or not the main character is on the bottom platform
	boolean bottomPlatform = false;
	
	//initialize the arrays for the platform coordinates
	int[][] platformX = new int[2][numPlatforms];
	int[] platformY = new int[numPlatforms];
	
	//determines whether or not the platform is on the screen
	boolean[] platformInPlay = new boolean[numPlatforms];
	
	//create a new BufferedImage for the hearts
	BufferedImage heart;
	BufferedImage emptyHeart;
	
	//determines how many lives the main character has lost
	int livesLost = 0;
	
	//determines whether or not the heart is full or empty
	boolean[] heartEmpty = new boolean[5];
	
	//determines whether or not the main character becomes temporarily invincible
	boolean invincible = true;
	int curTime = 0;
	
	//create a new BufferedImage for the screens
	BufferedImage startScreen;
	BufferedImage instructions;
	BufferedImage endScreen;
	
	/*miscellaneous variables for time, screens, incrementing speeds, 
	score, what type of bullets and movement*/
	double time = 0.0;
	int state = 1;
	int frames = 0;
	int score = 0;
	int powerup = 0;
	boolean[] movement = new boolean[4];
	
	GamePanel() throws IOException, LineUnavailableException, UnsupportedAudioFileException
	{
		/*
		 * Takes the pictures from the project and places them inside of their
		 * proper BufferedImages
		 */
		
		URL img = getClass().getResource("clouds.jpg");
		bg = ImageIO.read(img);
		
		img = getClass().getResource("rocketSheep.gif");
		main = ImageIO.read(img);
		
		img = getClass().getResource("rocketSheepMirror.png");
		mainMirrored = ImageIO.read(img);
		
		img = getClass().getResource("fireball.png");
		fireball = ImageIO.read(img);
		
		img = getClass().getResource("fireballMirrored.png");
		fireballMirrored = ImageIO.read(img);
		
		img = getClass().getResource("iceball.png");
		iceball = ImageIO.read(img);
		
		img = getClass().getResource("iceballMirrored.png");
		iceballMirrored = ImageIO.read(img);
		
		img  = getClass().getResource("enemy.gif");
		enemy = ImageIO.read(img);
		
		img = getClass().getResource("enemyInvert.gif");
		enemyMirrored = ImageIO.read(img);
		
		img = getClass().getResource("platform.png");
		platform = ImageIO.read(img);
		
		img = getClass().getResource("heart.png");
		heart = ImageIO.read(img);
		
		img = getClass().getResource("empty heart.png");
		emptyHeart = ImageIO.read(img);
		
		img = getClass().getResource("startMenu.png");
		startScreen = ImageIO.read(img);
		
		img = getClass().getResource("instructions.png");
		instructions = ImageIO.read(img);
		
		img = getClass().getResource("endScreen.png");
		endScreen = ImageIO.read(img);
		
		
		//inserting in background music
		Clip bgMusic = AudioSystem.getClip();
		URL music = getClass().getResource("Ferrari.wav");
		bgMusic.open(AudioSystem.getAudioInputStream(music));
		bgMusic.loop(Clip.LOOP_CONTINUOUSLY);
		
		//presets random X and Y values for each enemy
		for(int i = 0; i < enemyX.length; i++)
		{
			enemyX[i] = (int)(Math.random() * 650) + 75;
			enemyY[i] = (int)(Math.random() * 550) + 75;
		}
		
		//presets the top platform to be in the middle
		platformX[0][0] = -300;
		platformX[1][0] = platformX[0][0] + 805;
		platformY[0] = 175;
		
		//presets random X and Y values for each platform
		for(int i = 1; i < numPlatforms; i++)
		{
			platformX[0][i] = (int) (Math.random() * 600) - 680;
			platformX[1][i] = platformX[0][i] + 805;
			platformY[i] = (i + 1) * 175;
		}

	}
	
	public void paint(Graphics g)
	{
		/*
		 * Create a switch that determines when to paint the
		 * start screen, instructions screen, actual game and
		 * end screen
		 */
		switch(state)
		{
		case 1:
			//draws the start screen
			g.drawImage(startScreen, 0, 0, this);
			break;
		case 2:
			//draw the instructions screen
			g.drawImage(instructions, 0, -25, this);
			break;
		case 3:
			//draws the background
			g.drawImage(bg, bgX, bgY + 349, this);
			g.drawImage(bg, bgX + 697, bgY + 349, this);
			g.drawImage(bg, bgX, bgY, this);
			g.drawImage(bg, bgX + 697, bgY, this);
			g.drawImage(bg, bgX, bgY - 349, this);
			g.drawImage(bg, bgX + 697, bgY - 349, this);
			g.drawImage(bg, bgX, bgY - 698, this);
			g.drawImage(bg, bgX + 697, bgY - 698, this);
			
			/*
			 * Determines whether or not the main character is facing left
			 * or right and paints the corresponding image 
			 */
			if(left)
				g.drawImage(mainMirrored, mainX - main.getWidth() / 2, mainY - (int) jump, this);
			else
				g.drawImage(main, mainX - main.getWidth() / 2, mainY - (int) jump, this);
			
			//determines whether or not the bullet is active
			if(inPlay)
			{
				//determines which type of bullet is used
				if(powerup == 0)
				{
					/*
					 * Determines whether or not the bullet should
					 * be fired from the left or right side of the main
					 * character
					 */
					if(ballLeftStart)
						g.drawImage(fireballMirrored, ballX - fireball.getWidth() / 2, ballY - fireball.getHeight() / 2, this);
					else
						g.drawImage(fireball, ballX - fireball.getWidth() / 2, ballY - fireball.getHeight() / 2, this);
				}
				else if(powerup == 1)
				{
					if(ballLeftStart)
						g.drawImage(iceballMirrored, ballX - fireball.getWidth() / 2, ballY - fireball.getHeight() / 2, this);
					else
						g.drawImage(iceball, ballX - fireball.getWidth() / 2, ballY - fireball.getHeight() / 2, this);
				}
				
			}
			
			//create a for loop to run through each platform
			for(int i = 0; i < numPlatforms; i++)
			{
				//paints the platform if it is on the screen
				if(platformInPlay[i] == false)
				{
					g.drawImage(platform, platformX[0][i], platformY[i], this);
					g.drawImage(platform, platformX[1][i], platformY[i], this);
				}
			}
			
			//create a for loop to run through each enemy
			for(int i = 0; i < numEnemies; i++)
			{
				//paints an enemy if it is on the screen
				if(enemyActive[i])
				{
					//paints an enemy depending on the direction the enemy is moving
					if(eRight[i])
						g.drawImage(enemy, enemyX[i] - enemy.getWidth() / 2, enemyY[i] - enemy.getHeight() / 2, this);
					else
						g.drawImage(enemyMirrored, enemyX[i] - enemy.getWidth() / 2, enemyY[i] - enemy.getHeight() / 2, this);
				}
			}
			
			
			//sets the font colour to black
			g.setColor(Color.BLACK);
			
			//sets the font to Impact, bolded with 36 sized font
			g.setFont(new Font("Impact", Font.BOLD, 36));
			
			//prints the time in the top left corner of the screen
			g.drawString(String.valueOf((Math.round(time / 60 * 10) / 10.0)), 20, 40);
			
			//prints the score in the top right corner of the screen
			g.drawString("Score: " + score, 610, 40);
			
			//creates a for loop to run through each life
			for(int i = 0; i < 5; i++)
			{
				//paints a full heart
				if(heartEmpty[i] == false)
					g.drawImage(heart, (300 + (i * 35)), 20, this);
				//paints an empty heart
				else
					g.drawImage(emptyHeart, (300 + (i * 35)), 20, this);
			}
			
			/*
			 * Increments the platform movement, main character
			 * movement, and bullet movement. This if statement
			 * gradually makes the time until the next speed is
			 * incremented longer. i.e. 15 seconds for speed 1, 
			 * 30 seconds for speed 2, 45 seconds for speed 3, 
			 * etc...
			 */
			if(frames > (pMovement * 885))
			{
				pMovement++;
				mainMovement++;
				frames = 0;
				ballMovement++;
			}
			break;
		case 4:
			//print the end game screen
			g.drawImage(endScreen, 0, 0, this);
			break;
		}
		
		
		
	}
	
	public void run()
	{
		/*
		 * Creates a switch that only runs when the 
		 * video game state is on
		 */
		switch(state)
		{
		case 3:
			//if the main character loses 5 lives, the game is over
			if(livesLost == 5)
				state++;
			
			//increments the number of frames and time
			frames++;
			time++;
			
			//makes the background scroll
			bgY += bgMovement;
			
			/*
			 * If the background has reached the bottom of
			 * the screen, it goes back to the top of the
			 * screen
			 */
			if(bgY > 698)
				bgY = 0;
			
			//makes the character move left and right, accordingly
			if(movement[0])
				mainX -= mainMovement;
			if(movement[2])
				mainX += mainMovement;	
			
			//creates a for loop to run through each platform
			for(int i = 0; i < numPlatforms; i++)
			{
				//makes the platforms move upwards
				platformY[i] -= pMovement;
				
				
				/*
				 * If the platforms reach the top of the platform,
				 * they are re-painted at the bottom of the screen
				 * and given a new X value
				 */
				if(platformY[i] < 0)
				{
					platformInPlay[i] = false;
					platformY[i] = 700;
					platformX[0][i] = (int) (Math.random() * 600) - 680;
					platformX[1][i] = platformX[0][i] + 805;
				}
			}
			
			//determines whether or not the main character is on the bottom platform
			if(mainY > 475)
				bottomPlatform = true;
			else
				bottomPlatform = false;
			
			//determines whether or not the main character is in between the two platforms
			if((mainX >= (platformX[0][platformCount % 4] + 730)) && (mainX <= (platformX[1][platformCount % 4] - 50)))
			{
				//makes falling true if the main character is not on the bottom platform
				if(!bottomPlatform)
				{
					falling = true;
					platformCount++;
				}
			}	
			
			//checks whether or not the main character is on the platform
			if(mainY > (platformY[platformCount % 4] - 60))
				mainY = platformY[platformCount % 4] - 60;
			
			//checks whether or not the main character has passed the bottom of the screen
			if(mainY > this.getHeight() - main.getHeight())
				mainY = this.getHeight() - main.getHeight();
			
			//checks whether or not the main character has passed the top of the screen
			if(mainY < -20)
			{	
				/*
				 * Checks if the main character has not lost all their lives
				 * and is not invincible
				 */
				if(livesLost < 5 && !invincible)
				{			
					/*
					 * Once the main character has lost a life, they become invincible for
					 * 1.5 seconds and one heart becomes empty
					 */
					invincible = true;
					mainY = 300;
					livesLost++;
					heartEmpty[livesLost - 1] = true;
					curTime = 0;
				}
			}
			
			//makes the player invincible for 1.5 seconds
			if(invincible)
			{
				curTime++;
				if(curTime > 90)
					invincible = false;
			}
			
			//checks whether or not the character has passed the right side of the screen 
			if(mainX > this.getWidth() - main.getWidth() / 2)
				mainX = this.getWidth() - main.getWidth() / 2;
			
			//checks whether or not the character has passed the left side of the screen
			if(mainX < 30)
				mainX = 30;
			
			//checks whether or not the main character is falling
			if(falling)
				mainY += 8;
			
			//checks whether or not the main character is jumping
			if(jumping)
			{
				//creates a parabola for the pump
				jump = -(3.0/25.0) * Math.pow(jumpTime - 25, 2) + 75;
				jumpTime++;
				
				//stops the parabola once the character touches the platform
				if(jump < 0)
					jumping = false;
			}
			
			//checks whether or not the bullet is active
			if(inPlay)
			{
				/*
				 * Determines which way the bullet moves:
				 * Left to right or right to left
				 */
				if(ballLeftStart)
					ballX -= ballMovement;
				else
					ballX += ballMovement;
			}
			
			//checks whether or not the bullet has exited the screen
			if(ballX > 800 || ballX < 0 || ballY > 698)
				inPlay = false;
			
			//creates a for loop to runs through each enemy
			for(int i = 0; i < numEnemies; i++)
			{
				//checks whether or not the enemy is active
				if(enemyActive[i])
				{
					/*
					 * Determines whether the enemy moves from
					 * left to right or right to left
					 */
					if(eRight[i] == false)
						enemyX[i]--;
					else
						enemyX[i]++;
				}
				
				//checks whether or not the enemy has reached the left or right wall
				if(enemyX[i] < (0 + enemy.getWidth() / 2))
					eRight[i] = true;
				else if(enemyX[i] > (800 - enemy.getWidth() / 2))
					eRight[i] = false;
				
				/*
				 * Checks whether or not the bullet is
				 * active and there is an enemy 
				 */
				if(inPlay && enemyActive[i])
				{
					//checks the distance between the bullet and the enemy
					double distance = 0;
					distance = Math.sqrt(Math.pow(enemyY[i] - ballY, 2) + Math.pow(enemyX[i] - ballX, 2));
					
					if(distance <= 42)
					{
						//runs through each enemy
						for(int j = 0; j < numEnemies; j++)
						{
							//makes the bullet not active
							inPlay = false;
							
							//checks which enemies are close enough to the bullet
							if(Math.abs(ballX - enemyX[j]) < 42)
							{
								//makes the enemy not active
								enemyActive[j] = false;
								
								//sets a new X and Y value for the enemy 
								enemyX[j] = (int)(Math.random() * 650) + 75;
								enemyY[j] = (int)(Math.random() * 550) + 75;
								score++;
							}
						}
					}
				}
				
				//checks the distance between the main character and the enemy
				double mainDistance = 0;
				mainDistance = Math.sqrt(Math.pow((enemyY[i] + enemy.getHeight() / 2) - mainY, 2) + Math.pow((enemyX[i] + enemy.getWidth() / 2) - mainX, 2));
				
				if(mainDistance <= 60)
				{
					/*
					 * Checks if the main character has lost all their lives
					 * and is not invincible
					 */
					if(livesLost < 5 && !invincible)
					{
						//enemies is not active
						enemyActive[i] = false;
						
						//main character becomes invincible
						invincible = true;
						
						//main character loses a life
						livesLost++;
						
						//heart becomes empty
						heartEmpty[livesLost - 1] = true;
						curTime = 0;
						
						//sets a new X value for the enemy
						enemyX[i] = (int)(Math.random() * 650) + 75;
						enemyY[i] = (int)(Math.random() * 550) + 75;
					}
				}
			}
			
			//spawns a new enemy every 2 seconds
			if(time % 120 == 0)
			{
				//checks if there are any enemies that are not active
				boolean enemyCheck = false;
				for(int i = 0; i < numEnemies && !enemyCheck; i++)
				{
					if(enemyActive[i] == false)
					{
						enemyCheck = true;
						enemyActive[i] = true;
					}
				}

			}
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}
	
	@Override
	public void keyPressed(KeyEvent e) 
	{	
		/*
		 * Creates a switch to determine which keys
		 * can be pressed during each game state
		 */
		switch(state)
		{
		case 1:
			//start screen
			switch(e.getKeyChar())
			{
			case ' ':
				//space bar starts the game
				state = 3;
				break;
			case 'i':
				//the i key brings the player to the instructions screen
				state = 2;
				break;
			}
			break;
		case 2:
			//instructions screen
			switch(e.getKeyCode())
			{
			case 32:
				//space bar starts the game
				state = 3;
				break;
			case 8:
				//backspace key brings the player back to the start screen
				state = 1;
				break;
			}
			break;
		case 3:
			//video game
			switch(e.getKeyCode())
			{
			//a key and left arrow makes the player move left
			case 37:
			case 65:
				movement[0] = true;
				left = true;
				break;
			//w key and up arrow makes the player jump
			case 38:
			case 87:
				//checks whether or not the main character is already jumping
				if(!jumping)
				{
					jumping = true;
					jumpTime = 0;
				}
				break;
			case 39:
			case 68:
				//d key and right arrows makes the player move right
				movement[2] = true;
				left = false;
				break;
			case 49:
				//makes the bullet a fireball
				powerup = 0;
				break;
			case 50:
				//makes the bullet an iceball
				powerup = 1;
				break;
			}
			break;
		case 4:
			//end game screen
			switch(e.getKeyCode())
			{
			case 32:
				//space bar restarts the game and resets all variables
				state--;
				mainX = 350;
				mainY = 200;
				mainMovement = 8;
				jumping = false;
				jump = 0.0;
				jumpTime = 0;
				ballMovement = 8;
				inPlay = false;
				time = 0.0;
				pMovement = 1;
				platformCount = 1;
				bottomPlatform = false;
				livesLost = 0;
				invincible = true;
				curTime = 0;
				frames = 0;
				score = 0;
				powerup = 0;
				
				for(int i = 0; i < numEnemies; i++)
				{
					enemyX[i] = (int)(Math.random() * 650) + 75;
					enemyY[i] = (int)(Math.random() * 550) + 75;
					enemyActive[i] = false;
				}
				
				platformX[0][0] = -300;
				platformX[1][0] = platformX[0][0] + 805;
				platformY[0] = 0;
				
				for(int i = 1; i < numPlatforms; i++)
				{
					platformX[0][i] = (int) (Math.random() * 600) - 680;
					platformX[1][i] = platformX[0][i] + 805;
					platformY[i] = i * 175;
				}
				
				for(int i = 0; i < 4; i++)
				{
					platformInPlay[i] = false;
					movement[i] = false;
				}
				
				for(int i = 0; i < 5; i++)
				{
					heartEmpty[i] = false;
				}
				break;
			}
			break;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) 
	{
		//creates a switch that only can be used during the game
		switch(state)
		{
		//checks when the left and right keys are released
		case 3:
			switch(e.getKeyCode())
			{
			case 37:
			case 65:
				movement[0] = false;
				break;
			case 39:
			case 68:
				movement[2] = false;
				break;
			}
			break;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e)
	{
		//creates a switch that can only be used during the game
		switch(state)
		{
		case 3:
			//checks if the bullet is already active
			if(inPlay == false)
			{
				/*
				 * Determines the starting location of the bullet
				 * depending on the direction the main character is
				 * facing
				 */
				if(left)
				{
					ballX = mainX - 40;
					ballY = mainY - (int)jump;
					ballLeftStart = true;
				}
				else
				{	
					ballX = mainX + 48;
					ballY = mainY - (int)jump;
					ballLeftStart = false;
				}
				
				inPlay = true;
			}
			break;
		}
	
	}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
	
}