package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class StartPanel extends JPanel {

	/**
	 * Default serial version id.
	 */
	private static final long serialVersionUID = 1L;
	
	private JFrame mainFrame;
	private MainWindow mainWindow;

	// private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public StartPanel(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		this.mainFrame = mainWindow.getMainFrame();
		initialize();
	}

	private void initialize() {
		GridBagLayout startWindowLayout = new GridBagLayout();
		startWindowLayout.columnWidths = new int[] { 0, 300, 0, 0 };
		startWindowLayout.rowHeights = new int[] { 70, 300, 0, 0 };
		startWindowLayout.columnWeights = new double[] { 1.0, 1.0, 1.0,
				Double.MIN_VALUE };
		startWindowLayout.rowWeights = new double[] { 0.0, 0.0, 1.0,
				Double.MIN_VALUE };
		setLayout(startWindowLayout);
		buildTopPanel();
		buildCenterPanel();
	}

	/**
	 * Builds the center panel content for the main window. Contains the main
	 * control buttons.
	 */
	private void buildCenterPanel() {
		JPanel centerPanel = new JPanel();
		GridBagConstraints centerPanelConstraints = new GridBagConstraints();
		centerPanelConstraints.insets = new Insets(0, 0, 5, 5);
		centerPanelConstraints.gridx = 1;
		centerPanelConstraints.gridy = 1;
		add(centerPanel, centerPanelConstraints);
		GridBagLayout centerPanelLayout = new GridBagLayout();
		centerPanelLayout.columnWidths = new int[] { 150, 0 };
		centerPanelLayout.rowHeights = new int[] { 40, 40, 0, 0 };
		centerPanelLayout.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		centerPanelLayout.rowWeights = new double[] { 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		centerPanel.setLayout(centerPanelLayout);

		// New Proof Button
		JButton newProofButton = new JButton("New Proof");
		newProofButton.setPreferredSize(new Dimension(150, 40));
		GridBagConstraints newProofConstraints = new GridBagConstraints();
		newProofConstraints.fill = GridBagConstraints.BOTH;
		newProofConstraints.insets = new Insets(0, 0, 5, 0);
		newProofConstraints.gridx = 0;
		newProofConstraints.gridy = 0;
		centerPanel.add(newProofButton, newProofConstraints);
		newProofButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showNewProofDialog();
			}
		});

		// Load Proof Button
		JButton loadProofButton = new JButton("Load Proof");
		loadProofButton.setPreferredSize(new Dimension(150, 40));
		GridBagConstraints loadProofConstraints = new GridBagConstraints();
		loadProofConstraints.insets = new Insets(0, 0, 5, 0);
		loadProofConstraints.fill = GridBagConstraints.BOTH;
		loadProofConstraints.gridx = 0;
		loadProofConstraints.gridy = 1;
		centerPanel.add(loadProofButton, loadProofConstraints);
		loadProofButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				showLoadProofBrowser();
			}
		});

		// Quit Button
		JButton quitButton = new JButton("Quit");
		quitButton.setPreferredSize(new Dimension(150, 40));
		GridBagConstraints quitButtonConstraints = new GridBagConstraints();
		quitButtonConstraints.gridx = 0;
		quitButtonConstraints.gridy = 2;
		centerPanel.add(quitButton, quitButtonConstraints);
		quitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
	}

	/**
	 * Builds the top/title panel for the main window. Contains the title.
	 */
	private void buildTopPanel() {
		JPanel topPanel = new JPanel();
		GridBagConstraints topPanelConstraints = new GridBagConstraints();
		topPanelConstraints.gridwidth = 3;
		topPanelConstraints.fill = GridBagConstraints.BOTH;
		topPanelConstraints.insets = new Insets(0, 0, 5, 5);
		topPanelConstraints.gridx = 0;
		topPanelConstraints.gridy = 0;
		add(topPanel, topPanelConstraints);
		topPanel.setLayout(new BorderLayout(0, 0));

		JLabel welcomeLabel = new JLabel("Welcome to GameProver");
		welcomeLabel.setFont(new Font("Tahoma", Font.PLAIN, 29));
		welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		topPanel.add(welcomeLabel, BorderLayout.CENTER);
	}

	/**
	 * Displays the new proof dialog which allows a new proof to be started from
	 * input.
	 */
	protected void showNewProofDialog() {
		NewProofDialog newProofDialog = new NewProofDialog(mainWindow, mainFrame);
		newProofDialog.setVisible(true);
	}

	protected void showLoadProofBrowser() {
		LoadProofDialog loadProofDialog = new LoadProofDialog(mainWindow, mainFrame);
		loadProofDialog.setVisible(true);
	}
}
