package game;

import gui.GamePanel;
import gui.ProofStatePanel;
import gui.ReplayPanel;
import gui.RewriteFrame;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JScrollPane;

import logic.Expression;
import logic.GameState;
import logic.ProofState;
import logic.ReplayManager;
import logic.SavedProof;
import logic.StepManager;

public class Game {

	private GamePanel gamePanel;
	private ProofState proofState;
	private ReplayPanel replayPanel;
	private ProofStatePanel currentDisplayPanel;
	private StepManager stepManager;
	private Expression theorem;
	private SaveManager saveManager;
	private ReplayManager replayManager;

	private boolean isReplaying;

	private static ArrayList<GameState> gameList = new ArrayList<GameState>();
//	private static ArrayList<Expression> userAxioms = new ArrayList<Expression>();

//	private static ArrayList<SavedProof> savedProofs = new ArrayList<SavedProof>();

	// TODO: Get rid of this
	public JLabel rwIn;

	// TODO: Make set/getters
	public boolean wasCleanup;

	public Game(GamePanel gamePanel, ProofState initialState, SaveManager saveManager) {
		this.gamePanel = gamePanel;
		this.proofState = initialState;
		this.theorem = initialState.getGoal(0);
		this.saveManager = saveManager;
		
		initialiseGoal(theorem);
	}

	public Game(ReplayPanel replayPanel, SavedProof savedProof, SaveManager saveManager) {
		Expression exp = savedProof.getExpression();
		this.proofState = new ProofState(exp);
		this.replayPanel = replayPanel;
		this.theorem = proofState.getGoal(0);
		this.saveManager = saveManager;
		
		this.replayManager = new ReplayManager(this);;
		
		initialiseReplay(savedProof);
	}

	// TODO: Move this to GamePanel and fetch game list from
	// "Game" specific object.
	public void updateUndoButton() {
		if (gamePanel == null && replayPanel != null) {
			if (getGameList().size() > 1) {
				replayPanel.setPrevButtonState(true);
			} else {
				replayPanel.setPrevButtonState(false);
			}
			return;
		}
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
		if (gamePanel == null) {
			updateReplayFrame();
			return;
		}
		if (currentDisplayPanel != null) {
			currentDisplayPanel.update();

			JScrollPane proofArea = gamePanel.getProofScrollPane();

			// Resize and repaint
			int s = currentDisplayPanel.getHeight();
			currentDisplayPanel.setPreferredSize(new Dimension(proofArea
					.getWidth(), s + 10));
			currentDisplayPanel.revalidate();
			gamePanel.repaint(); // (Not needed???)
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
	
	public void updateReplayFrame() {
		if (currentDisplayPanel != null) {
			currentDisplayPanel.update();

			JScrollPane proofArea = replayPanel.getProofScrollPane();

			// Resize and repaint
			int s = currentDisplayPanel.getHeight();
			currentDisplayPanel.setPreferredSize(new Dimension(proofArea
					.getWidth(), s + 10));
			currentDisplayPanel.revalidate();
			replayPanel.repaint(); // (Not needed???)
		}

		// Check whether proof is finished
		if (isDone()) {
			doneReplayBehaviour();
		}
		if (gameList.size() <= 1) {
			replayPanel.setPrevButtonState(false);
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
		return saveManager.getSavedProofs();
	}
	
	public StepManager getStepManager() {
		return stepManager;
	}
	
	public SaveManager getSaveManager() {
		return saveManager;
	}
	
	public Expression getTheorem() {
		return theorem;
	}

	public void setReplaying(boolean state) {
		isReplaying = state;
	}

	// TODO: Make references to this point to GamePanel instead.
	public void setHelptxt(String text) {
		if (gamePanel == null) {
			replayPanel.setHelpText(text);
			return;
		}
		gamePanel.setHelpText(text);
	}

	public void setRWexp(Expression exp, String type) {
		gamePanel.getRewriteFrame().setdispExp(exp, type);
		gamePanel.getRewriteFrame().setVisible(true);
	}

	public boolean isReplaying() {
		return isReplaying;
	}

	// TODO: Move this to "Game" specific object.
	public boolean isDone() {
		return currentDisplayPanel.isDone();
	}
	
	public void save() {
		saveManager.saveLocally(stepManager.getCurrentProof());
	}
	
	public void cleanupHandler() {
		cleanup();
		currentDisplayPanel.record();
		updateFrame();
	}
	
	public void cleanupReplayHandler() {
		cleanupReplay();
		currentDisplayPanel.record();
		updateReplayFrame();
	}
	
	public void cleanup() {
		ProofStatePanel panel = currentDisplayPanel;
		ProofState proofState = panel.logicState;
		int subStateToShowIndex = -1;
		boolean doCleanup = false;
		if (proofState.isEmptyState()) {
			wasCleanup = true;
			doCleanup = true;
			proofState.setHideFlag(true);
		}

		for (int i = 0; i < proofState.getSubstates().size(); i++) {
			ProofState subState = proofState.getSubstate(i);
			boolean oldSubStateFlag = subState.getHideFlag();
			subState.cleanup();
			if (subState.getHideFlag() && !oldSubStateFlag) {
				doCleanup = true;
			}
			if (!subState.getHideFlag() && subStateToShowIndex == -1) {
				subStateToShowIndex = i;
			}
		}

		if (doCleanup) {
			if (subStateToShowIndex == -1) {
				setStateToShow(proofState);
			} else {
				setStateToShow(proofState.getSubstate(subStateToShowIndex));
			}
		}
	}
	
	public void cleanupReplay() {
		ProofStatePanel panel = currentDisplayPanel;
		ProofState proofState = panel.logicState;
		int subStateToShowIndex = -1;
		boolean doCleanup = false;
		if (proofState.isEmptyState()) {
			wasCleanup = true;
			doCleanup = true;
			proofState.setHideFlag(true);
		}

		for (int i = 0; i < proofState.getSubstates().size(); i++) {
			ProofState subState = proofState.getSubstate(i);
			boolean oldSubStateFlag = subState.getHideFlag();
			subState.cleanup();
			if (subState.getHideFlag() && !oldSubStateFlag) {
				doCleanup = true;
			}
			if (!subState.getHideFlag() && subStateToShowIndex == -1) {
				subStateToShowIndex = i;
			}
		}

		if (doCleanup) {
			if (subStateToShowIndex == -1) {
				setReplayStateToShow(proofState);
			} else {
				setReplayStateToShow(proofState.getSubstate(subStateToShowIndex));
			}
		}
	}

	// TODO: Move this to "Game" specific object.
	public void doneBehaviour() {
		gameList.clear();
		// play(win);
		gamePanel.setCleanupButtonState(false);
		setReplaying(false);
		gamePanel.remove(currentDisplayPanel);
		// rwframe.setdispExp(null, null);
		// rwframe.restart();
		gamePanel.setHelpText("Help:");
		// gamePanel.setStatusBarIcon(null);

		// done = new DonePanel(this);
		// scrollPanel.setViewportView(done);
		gamePanel.loadDonePanel();
	}
	// TODO: Move this to "Replay" specific object.
	public void doneReplayBehaviour() {
		gameList.clear();
		// play(win);
		setReplaying(false);
		
		replayPanel.doneReplaying();
	}
	

	// TODO: Move this to main Window?? Or keep here.
	public void play(URL sound) {
		// Does nothing at the moment as we've disabled sound.
	}

	public void initialiseGoal(Expression newGoal) {
		gamePanel.setUndoButtonState(false);
		gamePanel.setUndoText("Undo");

		proofState.getProofStateList().clear();
		proofState.getProofStateList().add(proofState);
		
		gameList.clear();
		setStateToShow(proofState);

		GameState gameState = new GameState();
		gameState.setProofStateList(proofState.getProofStateList());
		gameState.setDisplayStateIndex(proofState.getDepth());
		gameList.add(gameState);

		stepManager = new StepManager(this);
		stepManager.start(proofState.getGoal(0));
		
		updateFrame();
	}
	
	public void initialiseReplay(SavedProof savedProof) {
		
		proofState.getProofStateList().clear();
		proofState.getProofStateList().add(proofState);
		
		gameList.clear();
		setReplayStateToShow(proofState);

		GameState gameState = new GameState();
		gameState.setProofStateList(proofState.getProofStateList());
		gameState.setDisplayStateIndex(proofState.getDepth());
		gameList.add(gameState);

		stepManager = new StepManager(this);
		stepManager.start(proofState.getGoal(0));
		
		replayManager.load(savedProof);
		
		setReplaying(true);
		
		updateReplayFrame();
	}

	public void setStateToShow(ProofState pf) {
		gamePanel.invalidate();

		if (currentDisplayPanel != null) {
			gamePanel.remove(currentDisplayPanel);
		}

		ProofStatePanel pfPanel = new ProofStatePanel(this, pf);
		JScrollPane scrollPanel = gamePanel.getProofScrollPane();
		scrollPanel.setViewportView(pfPanel);

		int s = pfPanel.getHeight();
		pfPanel.setPreferredSize(new Dimension(scrollPanel.getWidth(), s));
		currentDisplayPanel = pfPanel;
		gamePanel.repaint();

		gamePanel.validate();
	}
	
	public void setReplayStateToShow(ProofState pf) {
		replayPanel.invalidate();

		if (currentDisplayPanel != null) {
			replayPanel.remove(currentDisplayPanel);
		}

		ProofStatePanel pfPanel = new ProofStatePanel(this, pf);
		JScrollPane scrollPanel = replayPanel.getProofScrollPane();
		scrollPanel.setViewportView(pfPanel);

		int s = pfPanel.getHeight();
		pfPanel.setPreferredSize(new Dimension(scrollPanel.getWidth(), s));
		currentDisplayPanel = pfPanel;
		replayPanel.repaint();

		replayPanel.validate();
	}
	
	public void undoFrame() {
		System.out.println("[UNDO]: Starting undo");
		setStateToShow(currentDisplayPanel.undo());
		if (!wasCleanup)
			stepManager.undo();
		if (isReplaying() && replayManager != null) {
			replayManager.prev();
		}
		wasCleanup = false;
		System.out.println("[UNDO]: Undo finished");
	}
	
	public void undoReplayFrame() {
		System.out.println("[UNDO]: Starting undo");
		setReplayStateToShow(currentDisplayPanel.undo());
		if (!wasCleanup)
			stepManager.undo();
		if (isReplaying() && replayManager != null) {
			replayManager.prev();
		}
		wasCleanup = false;
		System.out.println("[UNDO]: Undo finished");
	}
	
	public void saveAxiomListToDefault() {
		try {
			saveManager.saveAutoSaveFile();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void saveAxiomListToFile(File file) {
		try {
			saveManager.saveToFile(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void loadAxiomListFromDefault() {
		try {
			saveManager.loadAutoSaveFile();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void loadAxiomListFromFile(File file) {
		try {
			saveManager.loadFromFile(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public String[] getBaseAxioms() {
		return saveManager.getAxioms();
	}

	public boolean getWasCleanup() {
		return wasCleanup;
	}
	
	public void setWasCleanup(boolean state) {
		wasCleanup = state;
	}
	
	public void replayNext() {
		replayManager.next();
	}
	
	public void replayPrev() {
		undoReplayFrame();
	}
}
