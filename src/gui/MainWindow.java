package gui;

import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * @author Jakob Calero
 * 
 */
public class MainWindow {

	// Minimum size of the window, cannot resize to smaller than this
	private static final int minX = 800;
	private static final int minY = 600;
	
	public JFrame mainFrame;

	/**
	 * Main window constructor. Builds the panel and all its content.
	 */
	public MainWindow() {
		initialize();
	}
	
	/**
	 * Initialises the main window
	 */
	private void initialize() {
		mainFrame = new JFrame();
		mainFrame.setTitle("GameProver v2");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setWindowBounds();
		
		StartPanel sp = new StartPanel(this);
		mainFrame.setContentPane(sp);
		mainFrame.validate();
	}

	/**
	 * Adjust the initial window position to a position appropriate for the
	 * users screen resolution. Generally tries to launch the window at the
	 * centre of the screen.
	 */
	private void setWindowBounds() {
		mainFrame.setMinimumSize(new Dimension(minX, minY));
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setSize(minX, minY);
	}
	
	public JFrame getMainFrame() {
		return mainFrame;
	}
	
	public void loadGamePanel(String proofString) {
		GamePanel gp = new GamePanel(this, proofString);
		mainFrame.setContentPane(gp);
		mainFrame.validate();
	}
	
	public void loadStartPanel() {
		StartPanel sp = new StartPanel(this);
		mainFrame.setContentPane(sp);
		mainFrame.validate();
	}
}
