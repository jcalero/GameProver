package gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import logic.Expression;

public class DonePanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private GamePanel gamePanel;
	private Expression proved;

	private JButton btnAddToAxioms;
	private JButton btnSaveForReplay;

	/**
	 * Create the panel.
	 */
	public DonePanel(GamePanel gamePanel, Expression provedExpression) {
		this.gamePanel = gamePanel;
		this.proved = provedExpression;

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 180, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 20, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 0.0, 1.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblCongratulations = new JLabel("Congratulations!");
		lblCongratulations.setFont(new Font("Tahoma", Font.PLAIN, 22));
		GridBagConstraints gbc_lblCongratulations = new GridBagConstraints();
		gbc_lblCongratulations.insets = new Insets(0, 0, 5, 5);
		gbc_lblCongratulations.gridx = 1;
		gbc_lblCongratulations.gridy = 1;
		add(lblCongratulations, gbc_lblCongratulations);
		JLabel doneLabel = new JLabel("You've proven the theorem!");
		doneLabel.setFont(new Font("Tahoma", Font.PLAIN, 22));
		GridBagConstraints gbc_doneLabel = new GridBagConstraints();
		gbc_doneLabel.fill = GridBagConstraints.VERTICAL;
		gbc_doneLabel.insets = new Insets(0, 0, 5, 5);
		gbc_doneLabel.gridx = 1;
		gbc_doneLabel.gridy = 2;
		add(doneLabel, gbc_doneLabel);

		JLabel lblWhatDoYou = new JLabel("What do you wish to do now?");
		GridBagConstraints gbc_lblWhatDoYou = new GridBagConstraints();
		gbc_lblWhatDoYou.insets = new Insets(0, 0, 5, 5);
		gbc_lblWhatDoYou.gridx = 1;
		gbc_lblWhatDoYou.gridy = 4;
		add(lblWhatDoYou, gbc_lblWhatDoYou);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 3;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 5;
		add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 107, 107, 0, 0 };
		gbl_panel.rowHeights = new int[] { 23, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, 0.0, 0.0, 1.0,
				Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		btnAddToAxioms = new JButton("Add to axioms");
		btnAddToAxioms.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				axiomsButtonClickedEvent();
			}
		});
		GridBagConstraints gbc_btnAddToAxioms = new GridBagConstraints();
		gbc_btnAddToAxioms.fill = GridBagConstraints.BOTH;
		gbc_btnAddToAxioms.insets = new Insets(0, 0, 0, 5);
		gbc_btnAddToAxioms.gridx = 1;
		gbc_btnAddToAxioms.gridy = 0;
		panel.add(btnAddToAxioms, gbc_btnAddToAxioms);

		btnSaveForReplay = new JButton("Save for replay");
		btnSaveForReplay.setEnabled(false);
		GridBagConstraints gbc_btnSaveForReplay = new GridBagConstraints();
		gbc_btnSaveForReplay.insets = new Insets(0, 0, 0, 5);
		gbc_btnSaveForReplay.fill = GridBagConstraints.BOTH;
		gbc_btnSaveForReplay.gridx = 2;
		gbc_btnSaveForReplay.gridy = 0;
		panel.add(btnSaveForReplay, gbc_btnSaveForReplay);
	}

	private void axiomsButtonClickedEvent() {
		gamePanel.addExpressionToAxiomList(proved);
		btnAddToAxioms.setEnabled(false);
		btnAddToAxioms.setText("Done!");
	}
}
