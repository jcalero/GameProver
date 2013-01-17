package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private MainWindow mainWindow;
	private JFrame mainFrame;
	
	private String proofString;

	/**
	 * Create the panel.
	 */
	public GamePanel(MainWindow mainWindow, String proofString) {
		this.mainWindow = mainWindow;
		this.mainFrame = mainWindow.getMainFrame();
		this.proofString = proofString;
		initialize();
	}

	private void initialize() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel topPanel = new JPanel();
		add(topPanel, BorderLayout.NORTH);
		GridBagLayout gbl_topPanel = new GridBagLayout();
		gbl_topPanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_topPanel.rowHeights = new int[]{0, 0, 0, 0};
		gbl_topPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_topPanel.rowWeights = new double[]{1.0, 0.0, 1.0, Double.MIN_VALUE};
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
		gbl_panel.columnWidths = new int[]{228, 0};
		gbl_panel.rowHeights = new int[]{16, 0};
		gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel currentProofLabel = new JLabel("Here the current proof is displayed");
		currentProofLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_currentProofLabel = new GridBagConstraints();
		gbc_currentProofLabel.fill = GridBagConstraints.BOTH;
		gbc_currentProofLabel.gridx = 0;
		gbc_currentProofLabel.gridy = 0;
		panel.add(currentProofLabel, gbc_currentProofLabel);
		currentProofLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		if (!proofString.isEmpty()) {
			currentProofLabel.setText(proofString);
		}
		
		JButton btnNewButton_1 = new JButton("Cleanup");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCleanupButtonClicked();
			}
		});
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_1.gridx = 5;
		gbc_btnNewButton_1.gridy = 0;
		topPanel.add(btnNewButton_1, gbc_btnNewButton_1);
		
		JButton btnUndo = new JButton("Undo");
		btnUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onUndoButtonClicked();
			}
		});
		GridBagConstraints gbc_btnUndo = new GridBagConstraints();
		gbc_btnUndo.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnUndo.insets = new Insets(0, 0, 5, 5);
		gbc_btnUndo.gridx = 5;
		gbc_btnUndo.gridy = 1;
		topPanel.add(btnUndo, gbc_btnUndo);
		
		JPanel bottomPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) bottomPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		add(bottomPanel, BorderLayout.SOUTH);
		
		JLabel lblNewLabel_1 = new JLabel((String) null);
		lblNewLabel_1.setIcon(new ImageIcon(GamePanel.class.getResource("/javax/swing/plaf/metal/icons/ocean/question.png")));
		bottomPanel.add(lblNewLabel_1);
		
		JLabel lblThisIsThe = new JLabel("This is the status bar");
		bottomPanel.add(lblThisIsThe);
		
		JPanel centerPanel = new JPanel();
		add(centerPanel, BorderLayout.CENTER);
		GridBagLayout gbl_centerPanel = new GridBagLayout();
		gbl_centerPanel.columnWidths = new int[]{0, 0, 0};
		gbl_centerPanel.rowHeights = new int[]{0, 0};
		gbl_centerPanel.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_centerPanel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		centerPanel.setLayout(gbl_centerPanel);
		
		JScrollPane proofScrollPane = new JScrollPane();
		GridBagConstraints gbc_proofScrollPane = new GridBagConstraints();
		gbc_proofScrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_proofScrollPane.fill = GridBagConstraints.BOTH;
		gbc_proofScrollPane.gridx = 0;
		gbc_proofScrollPane.gridy = 0;
		centerPanel.add(proofScrollPane, gbc_proofScrollPane);
		
		JPanel proofPanel = new JPanel();
		proofScrollPane.setViewportView(proofPanel);
		
		JLabel lblNewLabel = new JLabel("Here's where the proof will be played");
		proofPanel.add(lblNewLabel);
		
		JScrollPane throwInScrollPane = new JScrollPane();
		GridBagConstraints gbc_throwInScrollPane = new GridBagConstraints();
		gbc_throwInScrollPane.anchor = GridBagConstraints.EAST;
		gbc_throwInScrollPane.fill = GridBagConstraints.VERTICAL;
		gbc_throwInScrollPane.gridx = 1;
		gbc_throwInScrollPane.gridy = 0;
		centerPanel.add(throwInScrollPane, gbc_throwInScrollPane);
		
		JList list = new JList();
		list.setPreferredSize(new Dimension(150, 0));
		list.setModel(new AbstractListModel() {
			private static final long serialVersionUID = 1L;
			String[] values = new String[] {"(0+1)=1", "(x+0)=x", "(x+(y+1))=((x+y)+1)", "(x*0)=0", "(x*(y+1))=((x*y)+x)", "A&B->C", "z\\xczsldslsaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		throwInScrollPane.setViewportView(list);
	}

	private void onMenuButtonClicked() {
		mainWindow.loadStartPanel();
	}
	
	private void onCleanupButtonClicked() {
		
	}
	
	private void onUndoButtonClicked() {
		
	}
}
