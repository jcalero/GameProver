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
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import logic.Expression;
import logic.SavedProof;

public class ReplayPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private MainWindow mainWindow;
//	private JFrame mainFrame;
	private ProofStatePanel psPanel;
	private RewriteFrame rewriteFrame;
	private Game game;

	private JButton btnPrev;
	private JButton btnNext;
	private JButton btnCleanup;
	private JScrollPane proofScrollPane;
	private JLabel statusBarLabel;
	private JLabel statusBarIconLabel;
	private JList throwInList;
	private DefaultListModel throwInListModel;

	private SavedProof toProve;

	/**
	 * Create the panel.
	 */
	public ReplayPanel(MainWindow mainWindow, SavedProof toReplay) {
		this.mainWindow = mainWindow;
		this.toProve = toReplay;

		initialize();
	}

	/////////////////////////////////////
	// TODO: GET RID OF THIS SHIT! Instead add Game to the constructor of
	// GamePanel.
	// Can only do this once "Game" is constructed without any references to
	// GamePanel.
	public void setGame(Game game) {
		this.game = game;
	}
	public Game getGame() {
		return game;
	}
	////////////////////////////////////

	private void initialize() {
		setLayout(new BorderLayout(0, 0));

		JPanel topPanel = new JPanel();
		add(topPanel, BorderLayout.NORTH);
		GridBagLayout gbl_topPanel = new GridBagLayout();
		gbl_topPanel.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_topPanel.rowHeights = new int[] { 28, 0, 0, 0 };
		gbl_topPanel.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0,
				0.0, 0.0, 0.0, Double.MIN_VALUE };
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

		btnPrev = new JButton("Prev");
		btnPrev.setEnabled(false);
		btnPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onPrevButtonClicked();
			}
		});
		
		btnCleanup = new JButton("Cleanup");
		btnCleanup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCleanupButtonClicked();
			}
		});
		GridBagConstraints gbc_btnCleanup = new GridBagConstraints();
		gbc_btnCleanup.gridwidth = 2;
		gbc_btnCleanup.insets = new Insets(0, 0, 5, 5);
		gbc_btnCleanup.gridx = 5;
		gbc_btnCleanup.gridy = 0;
		topPanel.add(btnCleanup, gbc_btnCleanup);
		GridBagConstraints gbc_btnPrev = new GridBagConstraints();
		gbc_btnPrev.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnPrev.insets = new Insets(0, 0, 5, 5);
		gbc_btnPrev.gridx = 5;
		gbc_btnPrev.gridy = 1;
		topPanel.add(btnPrev, gbc_btnPrev);

		btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				onNextButtonClicked();
			}
		});
		GridBagConstraints gbc_btnNext = new GridBagConstraints();
		gbc_btnNext.fill = GridBagConstraints.BOTH;
		gbc_btnNext.insets = new Insets(0, 0, 5, 5);
		gbc_btnNext.gridx = 6;
		gbc_btnNext.gridy = 1;
		topPanel.add(btnNext, gbc_btnNext);

		JPanel bottomPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) bottomPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		add(bottomPanel, BorderLayout.SOUTH);

		statusBarIconLabel = new JLabel((String) null);
		statusBarIconLabel
				.setIcon(new ImageIcon(
						ReplayPanel.class
								.getResource("/javax/swing/plaf/metal/icons/ocean/question.png")));
		bottomPanel.add(statusBarIconLabel);

		statusBarLabel = new JLabel("This is the status bar");
		bottomPanel.add(statusBarLabel);

		JPanel centerPanel = new JPanel();
		add(centerPanel, BorderLayout.CENTER);
		GridBagLayout gbl_centerPanel = new GridBagLayout();
		gbl_centerPanel.columnWidths = new int[] { 0, 150, 0 };
		gbl_centerPanel.rowHeights = new int[] { 150, 0, 0 };
		gbl_centerPanel.columnWeights = new double[] { 1.0, 0.0,
				Double.MIN_VALUE };
		gbl_centerPanel.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		centerPanel.setLayout(gbl_centerPanel);

		proofScrollPane = new JScrollPane();
		GridBagConstraints gbc_proofScrollPane = new GridBagConstraints();
		gbc_proofScrollPane.gridheight = 2;
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

		JPanel panel_1 = new JPanel();
		panel_1.setPreferredSize(new Dimension(150, 150));
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 0;
		centerPanel.add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_panel_1.rowHeights = new int[] { 0, 0, 25, 0, 0 };
		gbl_panel_1.columnWeights = new double[] { 1.0, 0.0, 1.0,
				Double.MIN_VALUE };
		gbl_panel_1.rowWeights = new double[] { 1.0, 0.0, 0.0, 1.0,
				Double.MIN_VALUE };
		panel_1.setLayout(gbl_panel_1);

		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(ReplayPanel.class
				.getResource("/javax/swing/plaf/metal/icons/Inform.gif")));
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.fill = GridBagConstraints.VERTICAL;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 1;
		gbc_label.gridy = 1;
		panel_1.add(label, gbc_label);

		JTextArea textArea = new JTextArea();
		textArea.setWrapStyleWord(true);
		textArea.setText("Here's where the next step would be detailed.");
		textArea.setMinimumSize(new Dimension(100, 22));
		textArea.setLineWrap(true);
		textArea.setFont(UIManager.getFont("Label.font"));
		textArea.setEditable(false);
		textArea.setBackground(SystemColor.menu);
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.insets = new Insets(0, 0, 5, 5);
		gbc_textArea.gridx = 1;
		gbc_textArea.gridy = 2;
		panel_1.add(textArea, gbc_textArea);

		JScrollPane throwInScrollPane = new JScrollPane();
		throwInScrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		GridBagConstraints gbc_throwInScrollPane = new GridBagConstraints();
		gbc_throwInScrollPane.fill = GridBagConstraints.BOTH;
		gbc_throwInScrollPane.gridx = 1;
		gbc_throwInScrollPane.gridy = 1;
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

	private void onPrevButtonClicked() {
		game.replayPrev();
		updatePrevButton();
	}

	private void onNextButtonClicked() {
		game.replayNext();
	}
	
	private void onCleanupButtonClicked() {
		game.cleanupReplayHandler();
	}

	public void setPrevText(String text) {
		btnPrev.setText(text);
	}

	public void setPrevButtonState(boolean state) {
		btnPrev.setEnabled(state);
	}
	
	public void setNextButtonState(boolean state) {
		btnNext.setEnabled(state);
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
		 DoneReplayPanel donePanel = new DoneReplayPanel(); 
		 getProofScrollPane().setViewportView(donePanel);
	}

	public void loadAxioms() {
		throwInListModel = new DefaultListModel();

		ArrayList<SavedProof> userAxioms = game.getSavedProofs();
		for (SavedProof e : userAxioms) {
			throwInListModel.addElement(e);
		}

		throwInList.setModel(throwInListModel);
	}

	public void reloadDependencyList() {
		ArrayList<SavedProof> savedProofs = game.getSaveManager()
				.getSavedProofs();
		if (savedProofs.size() > throwInListModel.size()) {
			for (int i = throwInListModel.size(); i < savedProofs.size(); i++) {
				throwInListModel.addElement(savedProofs.get(i));
			}
		}
	}

	public void updatePrevButton() {
		if (game.getGameList().size() > 1) {
			btnPrev.setEnabled(true);
		} else {
			btnPrev.setEnabled(false);
		}
	}
	
	public void doneReplaying() {
		setPrevButtonState(false);
		setNextButtonState(false);
		setCleanupButtonState(false);
		remove(game.getCurrentDisplayPanel());
		setHelpText("");
		loadDonePanel();
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

			// Left mouse clicked => Menu
			if (e.getButton() == MouseEvent.BUTTON1) {
				throwInList.setSelectedIndex(selected);
				System.out.println("Right clicked on: " + exp);
				Object[] options = { "Replay", "Cancel" };
				int n = JOptionPane
						.showOptionDialog(this,
								"Do you wish to watch the replay of \"" + exp + "\"?",
								"Loading replay",
								JOptionPane.OK_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[1]);
				switch (n) {
				case 0:
					// Replay
					mainWindow.loadReplayPanel(proof);
				default:
					break;
				}
			} else {
				throwInList.setSelectedIndex(selected);
//				setHelpText("Help: Cannot do that.");
			}
		} else {
			throwInList.clearSelection();
		}

	}
}
