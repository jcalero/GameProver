package game;

import gui.GamePanel;
import gui.ProofStatePanel;
import gui.ReplayPanel;
import gui.RewriteFrame;

import java.awt.Dimension;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JScrollPane;

import logic.Expression;
import logic.GameState;
import logic.SavedProof;

public class GameManager {

	private GamePanel gamePanel;
	private ReplayPanel replayPanel;
	private ProofStatePanel currentDisplayPanel;

	private boolean isReplaying;

	private static ArrayList<GameState> gameList = new ArrayList<GameState>();
	private ArrayList<SavedProof> savedProofs = new ArrayList<SavedProof>();
	
	//TODO: Get rid of this
	public JLabel rwIn;
	
	//TODO: Make set/getters
	public boolean wasCleanup;

	public GameManager(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	
	public GameManager(ReplayPanel replayPanel) {
		this.replayPanel = replayPanel;
	}

	// TODO: Move this to GamePanel and fetch game list from
	// "Game" specific object.
	public void updateUndoButton() {
		if (getGameList().size() > 1) {
			gamePanel.setUndoText("Undo (" + (getGameList().size() - 1) + ")");
			gamePanel.setUndoButtonState(true);
		} else {
			gamePanel.setUndoText("Undo");
			gamePanel.setUndoButtonState(false);
		}
	}

	// TODO: Move this to
	public void updateFrame() {
		if (currentDisplayPanel != null) {
			currentDisplayPanel.update();

			JScrollPane proofArea = gamePanel.getProofScrollPane();

			// Resize and repaint
			int s = currentDisplayPanel.getHsize();
			currentDisplayPanel.setPreferredSize(new Dimension(proofArea
					.getWidth(), s + 10));
			currentDisplayPanel.revalidate();
			// repaint(); (Not needed???)
		}

		// Check whether proof is finished
		if (isDone()) {
			doneBehaviour();
		}
		if (gameList.size() <= 1) {
			gamePanel.setUndoText("Undo");
			gamePanel.setUndoButtonState(false);
		}
	}

	// TODO: Move this to "Game" specific object.
	public ArrayList<GameState> getGameList() {
		return gameList;
	}
	
	// TODO: Update references to this to point to GamePanel instead. 
	public RewriteFrame getrwframe() {
		return gamePanel.getRewriteFrame();
	}
	
	// TODO: Move this to "Game" specific object.
	public ProofStatePanel getCurrentDisplayPanel() {
		return currentDisplayPanel;
	}
	
	public ArrayList<SavedProof> getSavedProofs() {
		return savedProofs;
	}
	
	public void setReplaying(boolean state) {
		isReplaying = state;
	}
	
	//TODO: Make references to this point to GamePanel instead.
	public void setHelptxt(String text) {
		gamePanel.setHelpText(text);
	}
	
	public void setRWexp(Expression exp, String type) {
		gamePanel.getRewriteFrame().setdispExp(exp, type);
	}

	public boolean isReplaying() {
		return isReplaying;
	}
	
	//TODO: Move this to "Game" specific object.
	public boolean isDone() {
		return currentDisplayPanel.isDone();
	}

	//TODO: Move this to "Game" specific object.
	public void doneBehaviour() {
		gameList.clear();
		// play(win);
		gamePanel.setCleanupButtonState(false);
		setReplaying(false);
		gamePanel.remove(currentDisplayPanel);
//		rwframe.setdispExp(null, null);
//		rwframe.restart();
		gamePanel.setHelpText("Help:");
//		gamePanel.setStatusBarIcon(null);

//		done = new DonePanel(this);
//		scrollPanel.setViewportView(done);
		gamePanel.loadDonePanel();
	}
	
	//TODO: Move this to main Window?? Or keep here.
	public void play(URL sound) {
		// Does nothing at the moment as we've disabled sound.
	}

}
