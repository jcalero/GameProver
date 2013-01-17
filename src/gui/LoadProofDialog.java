package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class LoadProofDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	protected final JPanel contentPanel = new JPanel();
	private MainWindow mainWindow;
	
	// Constraints
	private static final int minX = 350;
	private static final int minY = 350;
	private static final int WIDTH = 450;
	private static final int HEIGHT = 350;

	/**
	 * Create the dialog.
	 */
	public LoadProofDialog(MainWindow mainWindow, Frame owner) {
		super(owner, true);
		this.mainWindow = mainWindow;
		setDialogBounds();
		setLocationRelativeTo(owner);
		setAlwaysOnTop(true);
		setResizable(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		setTitle("Choose a proof to load");
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0};
		gbl_contentPanel.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		contentPanel.add(scrollPane, gbc_scrollPane);
		
		JList list = new JList();
		scrollPane.setViewportView(list);
		list.setModel(new AbstractListModel() {
			private static final long serialVersionUID = 1L;
			String[] values = new String[] {"A&B->B&A", "(x=y)&(y=z)->(x=z)", "More", "proofs", "would", "appear", "here", "if", "this", "was", "a", "tied", "to", "the", "real", "data."};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		
		JTextArea txtrinfoBoxHere = new JTextArea();
		txtrinfoBoxHere.setLineWrap(true);
		txtrinfoBoxHere.setFont(UIManager.getFont("Label.font"));
		txtrinfoBoxHere.setMinimumSize(new Dimension(200, 22));
		txtrinfoBoxHere.setMaximumSize(new Dimension(200, 2147483647));
		txtrinfoBoxHere.setEditable(false);
		txtrinfoBoxHere.setText("Info Box\r\n\r\nHere one could display some basic data about the proof, like how many steps it took to prove it last time and the dependencies it relies on.");
		GridBagConstraints gbc_txtrinfoBoxHere = new GridBagConstraints();
		gbc_txtrinfoBoxHere.fill = GridBagConstraints.VERTICAL;
		gbc_txtrinfoBoxHere.gridx = 1;
		gbc_txtrinfoBoxHere.gridy = 0;
		contentPanel.add(txtrinfoBoxHere, gbc_txtrinfoBoxHere);

		JPanel buttonPane = new JPanel();
		FlowLayout fl_buttonPane = new FlowLayout(FlowLayout.CENTER);
		buttonPane.setLayout(fl_buttonPane);
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		JButton loadButton = new JButton("Load");
		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				loadAction();
			}
		});
		loadButton.setActionCommand("Load");
		buttonPane.add(loadButton);
		getRootPane().setDefaultButton(loadButton);
		
		JButton replayButton = new JButton("Replay");
		replayButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				replayAction();
			}
		});
		replayButton.setActionCommand("Replay");
		buttonPane.add(replayButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancelAction();
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
	}
	
	private void setDialogBounds() {
		setSize(WIDTH, HEIGHT);
		setMinimumSize(new Dimension(minX, minY));
	}
	
	private void loadAction() {
		mainWindow.loadGamePanel("");
		setVisible(false);
	}
	
	private void replayAction() {
		mainWindow.loadGamePanel("");
		setVisible(false);
	}
	
	private void cancelAction() {
		setVisible(false);
	}
	


}
