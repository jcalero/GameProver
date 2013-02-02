package gui;

import game.StartModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import logic.SavedProof;

public class LoadReplayDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	protected final JPanel contentPanel = new JPanel();
	private MainWindow mainWindow;
	private DefaultListModel proofListModel;
	private JList proofList;
	private StartModel startModel;
	private JButton loadButton;

	// Constraints
	private static final int minX = 350;
	private static final int minY = 350;
	private static final int WIDTH = 450;
	private static final int HEIGHT = 350;

	/**
	 * Create the dialog.
	 */
	public LoadReplayDialog(MainWindow mainWindow, Frame owner,
			StartModel startModel) {
		super(owner, true);
		this.mainWindow = mainWindow;
		this.startModel = startModel;

		setDialogBounds();
		setLocationRelativeTo(owner);
		setAlwaysOnTop(true);
		setResizable(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Choose a proof to watch the replay of");

		initialise();

		// startModel.loadDefaultDataSet();
		updateProofListModel();
	}

	private void initialise() {
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 1.0, 0.0,
				Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		contentPanel.add(scrollPane, gbc_scrollPane);

		proofList = new JList();
		scrollPane.setViewportView(proofList);
		proofList.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				listClickHandler(e);
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});

		JTextArea txtrinfoBoxHere = new JTextArea();
		txtrinfoBoxHere.setLineWrap(true);
		txtrinfoBoxHere.setFont(UIManager.getFont("Label.font"));
		txtrinfoBoxHere.setMinimumSize(new Dimension(200, 22));
		txtrinfoBoxHere.setMaximumSize(new Dimension(200, 2147483647));
		txtrinfoBoxHere.setEditable(false);
		txtrinfoBoxHere
				.setText("Info Box\r\n\r\nHere one could display some basic data about the proof, like how many steps it took to prove it last time and the dependencies it relies on.");
		GridBagConstraints gbc_txtrinfoBoxHere = new GridBagConstraints();
		gbc_txtrinfoBoxHere.fill = GridBagConstraints.VERTICAL;
		gbc_txtrinfoBoxHere.gridx = 1;
		gbc_txtrinfoBoxHere.gridy = 0;
		contentPanel.add(txtrinfoBoxHere, gbc_txtrinfoBoxHere);

		JPanel buttonPane = new JPanel();
		FlowLayout fl_buttonPane = new FlowLayout(FlowLayout.CENTER);
		buttonPane.setLayout(fl_buttonPane);
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		loadButton = new JButton("Load");
		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				loadAction();
			}
		});
		loadButton.setActionCommand("Load");
		loadButton.setEnabled(false);
		buttonPane.add(loadButton);
		getRootPane().setDefaultButton(loadButton);

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
		int selected = proofList.getSelectedIndex();
		SavedProof proof = (SavedProof) proofListModel.getElementAt(selected);
		mainWindow.loadReplayPanel(proof);
		setVisible(false);
	}

	private void cancelAction() {
		setVisible(false);
	}

	private void updateProofListModel() {
		proofListModel = new DefaultListModel();
		proofListModel = startModel.updateProofListModel();
		if (proofListModel != null) {
			proofList.setModel(proofListModel);
		} else {
			proofList.setModel(new DefaultListModel());
		}
	}

	public void reload() {
		updateProofListModel();
		loadButton.setEnabled(false);
		proofList.clearSelection();
	}

	private void listClickHandler(MouseEvent e) {
		Rectangle r = proofList.getCellBounds(0,
				proofList.getLastVisibleIndex());
		int selected = -1;

		if (r != null && r.contains(e.getPoint())) {
			selected = proofList.locationToIndex(e.getPoint());
		}

		// Left mouse clicked => Select proof, update info box.
		if (selected != -1 && e.getButton() == MouseEvent.BUTTON1) {
			proofList.setSelectedIndex(selected);
			loadButton.setEnabled(true);
		} else {
			proofList.clearSelection();
			loadButton.setEnabled(false);
		}
	}

}
