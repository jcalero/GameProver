package gui;

import game.Game;
import game.SaveManager;
import game.StartModel;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.ToolTipManager;

import logic.Expression;
import logic.ProofState;
import logic.SavedProof;

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
		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				autoSave();
			}
		});
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
	
	private void autoSave() {
		try {
			saveManager.saveAutoSaveFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	
	public void loadReplayPanel(SavedProof savedProof) {		
		ReplayPanel replayPanel = new ReplayPanel(this, savedProof);
		
		Game game = new Game(replayPanel, savedProof, saveManager);
		replayPanel.setGame(game);
		replayPanel.loadAxioms();

		mainFrame.setContentPane(replayPanel);
		mainFrame.validate();
	}
}
