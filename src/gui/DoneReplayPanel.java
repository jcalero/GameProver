package gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class DoneReplayPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public DoneReplayPanel() {
		initialise();
	}
	
	private void initialise() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 180, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 0.0, 1.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 0.0, 0.0,
				1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblCongratulations = new JLabel("Replay Finished");
		lblCongratulations.setFont(new Font("Tahoma", Font.PLAIN, 22));
		GridBagConstraints gbc_lblCongratulations = new GridBagConstraints();
		gbc_lblCongratulations.insets = new Insets(0, 0, 5, 5);
		gbc_lblCongratulations.gridx = 1;
		gbc_lblCongratulations.gridy = 1;
		add(lblCongratulations, gbc_lblCongratulations);
		JLabel doneLabel = new JLabel("The theorem has been proven!");
		doneLabel.setFont(new Font("Tahoma", Font.PLAIN, 22));
		GridBagConstraints gbc_doneLabel = new GridBagConstraints();
		gbc_doneLabel.fill = GridBagConstraints.VERTICAL;
		gbc_doneLabel.insets = new Insets(0, 0, 5, 5);
		gbc_doneLabel.gridx = 1;
		gbc_doneLabel.gridy = 2;
		add(doneLabel, gbc_doneLabel);
	}
}
