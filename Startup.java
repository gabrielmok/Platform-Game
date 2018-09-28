import java.awt.Color;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * 
 */

/*
 * Gabriel Mok
 * June 9th, 2015
 * Version #4
 */

public class Startup
{

	public static void main(String[] args) throws IOException, InterruptedException, LineUnavailableException, UnsupportedAudioFileException
	{
		//create a new JFrame
		JFrame frame = new JFrame();
		
		//set parameters for the JFrame
		frame.setVisible(true);
		frame.setSize(800, 700);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setTitle("Platform Free-fall");

		//create a new JPanel
		GamePanel panel = new GamePanel();
		
		//set parameters for the JPanel
		panel.setSize(frame.getWidth(), frame.getHeight());
		panel.setVisible(true);
		panel.setBackground(Color.DARK_GRAY);
		
		//place the JPanel into the JFrame
		frame.setContentPane(panel);
		
		//enable the MouseListener and KeyListener
		panel.addMouseListener(panel);
		panel.addKeyListener(panel);
		
		//set the focus to the GamePanel
		panel.requestFocus();
		
		//run the GamePanel
		while(true)
		{
			panel.run();
			panel.repaint();
			
			Thread.sleep(17);
		}
		
		
	}

}
