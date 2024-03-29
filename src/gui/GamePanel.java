package gui;

import game.Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import logic.Expression;
import logic.LogicStep;
import logic.Rule;
import logic.SavedProof;
import parser.MyExpressionParser;

public class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private MainWindow mainWindow;
	private JFrame mainFrame;
	private ProofStatePanel psPanel;
	private RewriteFrame rewriteFrame;
	private Game game;
	
	private JFileChooser fileChooser;
	private JButton btnUndo;
	private JButton btnCleanup;
	private JScrollPane proofScrollPane;
	private JLabel statusBarLabel;
	private JLabel statusBarIconLabel;
	private JList throwInList;
	private DefaultListModel throwInListModel;

	private Expression toProve;

	/**
	 * Create the panel.
	 */
	public GamePanel(MainWindow mainWindow, Expression expression) {
		this.mainWindow = mainWindow;
		this.mainFrame = mainWindow.getMainFrame();
		this.toProve = expression;
		
		fileChooser = new JFileChooser();
		rewriteFrame = new RewriteFrame(mainFrame, this);

		initialize();

//		ProofState ps;
//		ps = new ProofState(toProve);

//		gameManager = new Game(this, ps);
//		gameManager = game;
//		loadAxioms();
	}
	
	//TODO: GET RID OF THIS SHIT! Instead add Game to the constructor of GamePanel.
	// Can only do this once "Game" is constructed without any references to GamePanel.
	public void setGame(Game game) {
		this.game = game;
	}
	public Game getGame() {
		return game;
	}

	private void initialize() {
		setLayout(new BorderLayout(0, 0));

		JPanel topPanel = new JPanel();
		add(topPanel, BorderLayout.NORTH);
		GridBagLayout gbl_topPanel = new GridBagLayout();
		gbl_topPanel.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_topPanel.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_topPanel.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0,
				0.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_topPanel.rowWeights = new double[] { 1.0, 0.0, 1.0,
				Double.MIN_VALUE };
		topPanel.setLayout(gbl_topPanel);

		JButton btnNewButton = new JButton("Menu");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onMenuButtonClicked();
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.VERTICAL;
		gbc_btnNewButton.gridheight = 2;
		gbc_btnNewButton.anchor = GridBagConstraints.WEST;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 0;
		topPanel.add(btnNewButton, gbc_btnNewButton);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(Color.WHITE);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridheight = 2;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 3;
		gbc_panel.gridy = 0;
		topPanel.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 228, 0 };
		gbl_panel.rowHeights = new int[] { 16, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		JLabel currentProofLabel = new JLabel(
				"Here the current proof is displayed");
		currentProofLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_currentProofLabel = new GridBagConstraints();
		gbc_currentProofLabel.fill = GridBagConstraints.BOTH;
		gbc_currentProofLabel.gridx = 0;
		gbc_currentProofLabel.gridy = 0;
		panel.add(currentProofLabel, gbc_currentProofLabel);
		currentProofLabel.setFont(new Font("Tahoma", Font.BOLD, 13));

		currentProofLabel.setText(toProve.toString());

		btnCleanup = new JButton("Cleanup");
		btnCleanup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCleanupButtonClicked();
			}
		});
		GridBagConstraints gbc_btnCleanup = new GridBagConstraints();
		gbc_btnCleanup.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnCleanup.insets = new Insets(0, 0, 5, 5);
		gbc_btnCleanup.gridx = 5;
		gbc_btnCleanup.gridy = 0;
		topPanel.add(btnCleanup, gbc_btnCleanup);

		btnUndo = new JButton("Undo");
		btnUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onUndoButtonClicked();
			}
		});
		
		JButton btnSave = new JButton("Save...");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onSaveButtonClicked();
			}
		});
		GridBagConstraints gbc_btnSave = new GridBagConstraints();
		gbc_btnSave.fill = GridBagConstraints.BOTH;
		gbc_btnSave.insets = new Insets(0, 0, 5, 5);
		gbc_btnSave.gridx = 6;
		gbc_btnSave.gridy = 0;
		topPanel.add(btnSave, gbc_btnSave);
		GridBagConstraints gbc_btnUndo = new GridBagConstraints();
		gbc_btnUndo.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnUndo.insets = new Insets(0, 0, 5, 5);
		gbc_btnUndo.gridx = 5;
		gbc_btnUndo.gridy = 1;
		topPanel.add(btnUndo, gbc_btnUndo);
		
		JButton btnLoad = new JButton("Load...");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onLoadButtonClicked();
			}
		});
		GridBagConstraints gbc_btnLoad = new GridBagConstraints();
		gbc_btnLoad.fill = GridBagConstraints.BOTH;
		gbc_btnLoad.insets = new Insets(0, 0, 5, 5);
		gbc_btnLoad.gridx = 6;
		gbc_btnLoad.gridy = 1;
		topPanel.add(btnLoad, gbc_btnLoad);

		JPanel bottomPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) bottomPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		add(bottomPanel, BorderLayout.SOUTH);

		statusBarIconLabel = new JLabel((String) null);
		statusBarIconLabel
				.setIcon(new ImageIcon(
						GamePanel.class
								.getResource("/javax/swing/plaf/metal/icons/ocean/question.png")));
		bottomPanel.add(statusBarIconLabel);

		statusBarLabel = new JLabel("This is the status bar");
		bottomPanel.add(statusBarLabel);

		JPanel centerPanel = new JPanel();
		add(centerPanel, BorderLayout.CENTER);
		GridBagLayout gbl_centerPanel = new GridBagLayout();
		gbl_centerPanel.columnWidths = new int[] { 0, 150, 0 };
		gbl_centerPanel.rowHeights = new int[] { 0, 0 };
		gbl_centerPanel.columnWeights = new double[] { 1.0, 0.0,
				Double.MIN_VALUE };
		gbl_centerPanel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		centerPanel.setLayout(gbl_centerPanel);

		proofScrollPane = new JScrollPane();
		GridBagConstraints gbc_proofScrollPane = new GridBagConstraints();
		gbc_proofScrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_proofScrollPane.fill = GridBagConstraints.BOTH;
		gbc_proofScrollPane.gridx = 0;
		gbc_proofScrollPane.gridy = 0;
		centerPanel.add(proofScrollPane, gbc_proofScrollPane);

		JPanel proofPanel = new JPanel();
		JLabel lblNewLabel = new JLabel(
				"Here's where the proof should be displayed");
		proofPanel.add(lblNewLabel);

		if (psPanel == null) {
			proofScrollPane.setViewportView(proofPanel);
		} else {
			proofScrollPane.setViewportView(psPanel);
		}

		JScrollPane throwInScrollPane = new JScrollPane();
		throwInScrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GridBagConstraints gbc_throwInScrollPane = new GridBagConstraints();
		gbc_throwInScrollPane.fill = GridBagConstraints.BOTH;
		gbc_throwInScrollPane.gridx = 1;
		gbc_throwInScrollPane.gridy = 0;
		centerPanel.add(throwInScrollPane, gbc_throwInScrollPane);

		throwInList = new JList();
		throwInList.setPreferredSize(new Dimension(150, 0));
		throwInScrollPane.setViewportView(throwInList);
		throwInList.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				listClickHandler(e);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}
		});
	}

	private void onMenuButtonClicked() {
		mainWindow.loadStartPanel();
	}

	private void onCleanupButtonClicked() {
		game.cleanupHandler();
	}

	private void onUndoButtonClicked() {
		game.undoFrame();
		updateUndoButton();
	}
	
	private void onSaveButtonClicked() {
		int returnVal = fileChooser.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			game.saveAxiomListToFile(file);
		}
	}
	
	private void onLoadButtonClicked() {
		int returnVal = fileChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			game.loadAxiomListFromFile(file);
			reloadThrowInList();
		}
	}

	public void setUndoText(String text) {
		btnUndo.setText(text);
	}

	public void setUndoButtonState(boolean state) {
		btnUndo.setEnabled(state);
	}

	public void setCleanupButtonState(boolean state) {
		btnCleanup.setEnabled(state);
	}

	public void setHelpText(String text) {
		statusBarLabel.setText(text);
	}

	public void setStatusBarIcon(ImageIcon icon) {
		statusBarIconLabel.setIcon(icon);
	}

	public JScrollPane getProofScrollPane() {
		return proofScrollPane;
	}

	public RewriteFrame getRewriteFrame() {
		return rewriteFrame;
	}

	public void loadDonePanel() {
		DonePanel donePanel = new DonePanel(game, this);
		getProofScrollPane().setViewportView(donePanel);
	}

	public void loadAxioms() {
		throwInListModel = new DefaultListModel();
		for (String s : game.getBaseAxioms()) {
			Expression exp;
			try {
				exp = MyExpressionParser.parse(s);
				SavedProof sp = new SavedProof(exp);
				throwInListModel.addElement(sp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		ArrayList<SavedProof> userAxioms = game.getSavedProofs();
		for (SavedProof e : userAxioms) {
			throwInListModel.addElement(e);
		}

		throwInList.setModel(throwInListModel);
	}

	public void reloadThrowInList() {
		ArrayList<SavedProof> savedProofs = game.getSaveManager()
				.getSavedProofs();
		if (savedProofs.size() < throwInListModel.size()) {
			// TODO: Make this more efficient by not using loadAxioms.
			// E.g. Check for containment of savedProof in throwInListModel
			// and only delete the ones that are not contained in savedProof.
			loadAxioms();
		} else if (savedProofs.size() > throwInListModel.size()) {
			for (int i = throwInListModel.size(); i < savedProofs.size(); i++) {
				throwInListModel.addElement(savedProofs.get(i));
			}
		}
	}

//	public void addExpressionToAxiomList(Expression exp) {
//		// userTheorems.add(toProve.toString());
//		gameManager.getStepManager().save();
//		throwInListModel.addElement(exp);
//	}

	private void updateUndoButton() {
		if (game.getGameList().size() > 1) {
			btnUndo.setEnabled(true);
			btnUndo.setText("Undo (" + (game.getGameList().size() - 1)
					+ ")");
		} else {
			btnUndo.setText("Undo");
			btnUndo.setEnabled(false);
		}
	}

	private void listClickHandler(MouseEvent e) {
		Rectangle r = throwInList.getCellBounds(0,
				throwInList.getLastVisibleIndex());
		int selected = -1;

		if (r != null && r.contains(e.getPoint())) {
			selected = throwInList.locationToIndex(e.getPoint());
		}

		if (selected != -1) {
			SavedProof proof = (SavedProof) throwInListModel
					.getElementAt(selected);
			Expression exp = proof.getExpression();

			boolean theorem = false;
			if (selected < game.getBaseAxioms().length)
				theorem = true;
			else
				theorem = false;

			// Left mouse clicked and a proof is going on => Throw in
			if (e.getButton() == MouseEvent.BUTTON1
					&& game.getGameList().size() > 0) {
				if (exp.containsTermVars()) {
					// throwIn.setVisible(true);

					// Operating on chars to preserve brackets
					char[] charArray = exp.toString().toCharArray();
					String toParse = "";
					for (int i = 0; i < charArray.length; i++) {
						if (Character.isLetter(charArray[i])) {
							toParse += "?" + charArray[i];
						} else {
							toParse += charArray[i];
						}
					}
					try {
						exp = MyExpressionParser.parse(toParse);
					} catch (Exception ex) {
						ex.printStackTrace();
						System.out
								.println("ERROR: Failed to parse variable with ? added");
					}
					LogicStep ls = new LogicStep(Rule.AddAssumVar, game
							.getCurrentDisplayPanel().getAbsoluteDepth());
					ls.setNewExpression(exp);
					game.getStepManager().applyRule(ls);
					game.updateFrame();
				} else {
					throwInList.setSelectedIndex(selected);
					System.out.println("Left clicked on: " + exp);
					// throwIn.setVisible(true);
					LogicStep ls = new LogicStep(Rule.AddAssum, game
							.getCurrentDisplayPanel().getAbsoluteDepth());
					ls.setNewExpression(exp);
					game.getStepManager().applyRule(ls);
					game.updateFrame();
				}
			}
			// Right mouse clicked and not clicked on a theorem => Menu
			else if (e.getButton() == MouseEvent.BUTTON3 && !theorem) {
				throwInList.setSelectedIndex(selected);
				System.out.println("Right clicked on: " + exp);
				Object[] options = { "Throw in", "Replay", "Prove" };
				int n = JOptionPane
						.showOptionDialog(this,
								"Choose what to do with this theorem.",
								"Loading theorem",
								JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[2]);
				switch (n) {
				case 0:
					if (game.getGameList().size() < 1) {
						throwInList.clearSelection();
						return;
					} else {
						// throwIn.setVisible(true);
						LogicStep ls = new LogicStep(Rule.AddAssum, game
								.getCurrentDisplayPanel().getAbsoluteDepth());
						ls.setNewExpression(exp);
						game.getStepManager().applyRule(ls);
						game.updateFrame();
					}
					break;
				case 1:
					// setGoalHandler(exp);
					// replayManager = new ReplayManager(this);
					// replayManager.load(proof);
					// setReplaying(true);
					break;
				case 2:
					// setGoalHandler(exp);
					break;
				default:
					break;
				}
			} else {
				throwInList.setSelectedIndex(selected);
				setHelpText("Help: Cannot do that.");
			}
		} else {
			throwInList.clearSelection();
		}

	}
}
