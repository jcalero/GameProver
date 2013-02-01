package gui;

import game.Game;
import game.SaveManager;
import game.StartModel;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.ToolTipManager;

import logic.Expression;
import logic.ProofState;

/**
 * @author Jakob Calero
 * 
 */
public class MainWindow {

	// Minimum size of the window, cannot resize to smaller than this
	private static final int minX = 800;
	private static final int minY = 600;
	
	private JFrame mainFrame;
	
	private SaveManager saveManager;

	/**
	 * Main window constructor. Builds the panel and all its content.
	 */
	public MainWindow(SaveManager saveManager) {
		this.saveManager = saveManager;
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
		ToolTipManager.sharedInstance().setInitialDelay(150);
		
		loadStartPanel();
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
	
	public void loadGamePanel(Expression expression) {
		//TODO: Move this to start model
		ProofState ps;
		ps = new ProofState(expression);
		
		GamePanel gp = new GamePanel(this, expression);
		
		//TODO: Move this to start model
		Game game = new Game(gp, ps, saveManager);
		gp.setGame(game);
		gp.loadAxioms();
		
		mainFrame.setContentPane(gp);
		mainFrame.validate();
	}
	
	public void loadStartPanel() {
		StartModel sm = new StartModel(saveManager);
		StartPanel sp = new StartPanel(this, sm);
		mainFrame.setContentPane(sp);
		mainFrame.validate();
	}
	
	public void loadReplayPanel(String proofString) {
		ReplayPanel rp  = new ReplayPanel(this, proofString);
		mainFrame.setContentPane(rp);
		mainFrame.validate();
	}
}
