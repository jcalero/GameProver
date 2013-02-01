package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class RewritePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField rewriteTextField;
	private RewriteFrame frame;

	private JPanel showBoardPanel;
	private JLabel selectedExpLabel;

	/**
	 * Create the panel.
	 */
	public RewritePanel(RewriteFrame frame) {
		this.frame = frame;
		ActionListener finishListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				finishAction();
			}
		};
		
		setBorder(new LineBorder(new Color(0, 0, 0)));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 26, 0, 0, 25, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0,
				Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel selectSubtermTextLabel = new JLabel("Select subterm");
		GridBagConstraints gbc_selectSubtermTextLabel = new GridBagConstraints();
		gbc_selectSubtermTextLabel.anchor = GridBagConstraints.SOUTH;
		gbc_selectSubtermTextLabel.insets = new Insets(0, 0, 5, 5);
		gbc_selectSubtermTextLabel.gridx = 1;
		gbc_selectSubtermTextLabel.gridy = 0;
		add(selectSubtermTextLabel, gbc_selectSubtermTextLabel);

		JLabel throwInImageLabel = new JLabel("");
		throwInImageLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		ImageIcon throwInIcon = new ImageIcon(
				RewritePanel.class.getResource("/gui/images/rw_small.png"));
		throwInImageLabel.setIcon(throwInIcon);
		GridBagConstraints gbc_throwInImageLabel = new GridBagConstraints();
		gbc_throwInImageLabel.insets = new Insets(0, 0, 5, 0);
		gbc_throwInImageLabel.gridheight = 2;
		gbc_throwInImageLabel.anchor = GridBagConstraints.SOUTHEAST;
		gbc_throwInImageLabel.gridx = 3;
		gbc_throwInImageLabel.gridy = 0;
		add(throwInImageLabel, gbc_throwInImageLabel);

		showBoardPanel = new JPanel();
		showBoardPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		showBoardPanel.setBackground(Color.WHITE);
		GridBagConstraints gbc_showBoardPanel = new GridBagConstraints();
		gbc_showBoardPanel.gridwidth = 3;
		gbc_showBoardPanel.insets = new Insets(0, 0, 5, 5);
		gbc_showBoardPanel.fill = GridBagConstraints.BOTH;
		gbc_showBoardPanel.gridx = 0;
		gbc_showBoardPanel.gridy = 1;
		add(showBoardPanel, gbc_showBoardPanel);
		GridBagLayout gbl_showBoardPanel = new GridBagLayout();
		gbl_showBoardPanel.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_showBoardPanel.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_showBoardPanel.columnWeights = new double[] { 1.0, 0.0, 1.0,
				Double.MIN_VALUE };
		gbl_showBoardPanel.rowWeights = new double[] { 1.0, 0.0, 1.0,
				Double.MIN_VALUE };
		showBoardPanel.setLayout(gbl_showBoardPanel);

		JLabel tempExpressionLabel = new JLabel("(x+1)*x->(x+x)");
		tempExpressionLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN,
				18));
		GridBagConstraints gbc_tempExpressionLabel = new GridBagConstraints();
		gbc_tempExpressionLabel.insets = new Insets(0, 0, 5, 5);
		gbc_tempExpressionLabel.gridx = 1;
		gbc_tempExpressionLabel.gridy = 1;
		showBoardPanel.add(tempExpressionLabel, gbc_tempExpressionLabel);

		JPanel rewriteToPanel = new JPanel();
		rewriteToPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		GridBagConstraints gbc_rewriteToPanel = new GridBagConstraints();
		gbc_rewriteToPanel.gridwidth = 4;
		gbc_rewriteToPanel.insets = new Insets(0, 0, 5, 0);
		gbc_rewriteToPanel.fill = GridBagConstraints.BOTH;
		gbc_rewriteToPanel.gridx = 0;
		gbc_rewriteToPanel.gridy = 2;
		add(rewriteToPanel, gbc_rewriteToPanel);
		GridBagLayout gbl_rewriteToPanel = new GridBagLayout();
		gbl_rewriteToPanel.columnWidths = new int[] { 0, 0, 120, 32, 0, 0, 0 };
		gbl_rewriteToPanel.rowHeights = new int[] { 8, 0, 4, 0 };
		gbl_rewriteToPanel.columnWeights = new double[] { 1.0, 0.0, 0.0, 0.0,
				0.0, 1.0, Double.MIN_VALUE };
		gbl_rewriteToPanel.rowWeights = new double[] { 1.0, 0.0, 1.0,
				Double.MIN_VALUE };
		rewriteToPanel.setLayout(gbl_rewriteToPanel);

		JLabel rewriteTextLabel = new JLabel("Rewrite:");
		rewriteTextLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_rewriteTextLabel = new GridBagConstraints();
		gbc_rewriteTextLabel.fill = GridBagConstraints.VERTICAL;
		gbc_rewriteTextLabel.insets = new Insets(0, 0, 5, 5);
		gbc_rewriteTextLabel.gridx = 1;
		gbc_rewriteTextLabel.gridy = 1;
		rewriteToPanel.add(rewriteTextLabel, gbc_rewriteTextLabel);

		selectedExpLabel = new JLabel("");
		selectedExpLabel.setPreferredSize(new Dimension(120, 0));
		selectedExpLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		selectedExpLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		selectedExpLabel.setOpaque(true);
		selectedExpLabel.setHorizontalAlignment(SwingConstants.CENTER);
		selectedExpLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		selectedExpLabel.setBackground(Color.WHITE);
		GridBagConstraints gbc_selectedExpLabel = new GridBagConstraints();
		gbc_selectedExpLabel.insets = new Insets(0, 0, 5, 5);
		gbc_selectedExpLabel.fill = GridBagConstraints.BOTH;
		gbc_selectedExpLabel.gridx = 2;
		gbc_selectedExpLabel.gridy = 1;
		rewriteToPanel.add(selectedExpLabel, gbc_selectedExpLabel);

		JLabel toTextLabel = new JLabel("to:");
		toTextLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_toTextLabel = new GridBagConstraints();
		gbc_toTextLabel.anchor = GridBagConstraints.ABOVE_BASELINE_TRAILING;
		gbc_toTextLabel.insets = new Insets(0, 0, 5, 5);
		gbc_toTextLabel.gridx = 3;
		gbc_toTextLabel.gridy = 1;
		rewriteToPanel.add(toTextLabel, gbc_toTextLabel);

		rewriteTextField = new JTextField();
		GridBagConstraints gbc_rewriteTextField = new GridBagConstraints();
		gbc_rewriteTextField.insets = new Insets(0, 0, 5, 5);
		gbc_rewriteTextField.fill = GridBagConstraints.BOTH;
		gbc_rewriteTextField.gridx = 4;
		gbc_rewriteTextField.gridy = 1;
		rewriteToPanel.add(rewriteTextField, gbc_rewriteTextField);
		rewriteTextField.setColumns(15);
		rewriteTextField.addActionListener(finishListener);

		JPanel buttonPanel = new JPanel();
		GridBagConstraints gbc_buttonPanel = new GridBagConstraints();
		gbc_buttonPanel.gridwidth = 4;
		gbc_buttonPanel.insets = new Insets(0, 0, 5, 0);
		gbc_buttonPanel.fill = GridBagConstraints.BOTH;
		gbc_buttonPanel.gridx = 0;
		gbc_buttonPanel.gridy = 3;
		add(buttonPanel, gbc_buttonPanel);
		GridBagLayout gbl_buttonPanel = new GridBagLayout();
		gbl_buttonPanel.columnWidths = new int[] { 0, 0, 0, 20, 0, 0, 0, 0 };
		gbl_buttonPanel.rowHeights = new int[] { 4, 0, 0 };
		gbl_buttonPanel.columnWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 1.0, Double.MIN_VALUE };
		gbl_buttonPanel.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		buttonPanel.setLayout(gbl_buttonPanel);

		JButton selectButton = new JButton("Select...");
		selectButton.setEnabled(false);
		GridBagConstraints gbc_selectButton = new GridBagConstraints();
		gbc_selectButton.insets = new Insets(0, 0, 0, 5);
		gbc_selectButton.gridx = 1;
		gbc_selectButton.gridy = 1;
		buttonPanel.add(selectButton, gbc_selectButton);

		JButton matchButton = new JButton("Match...");
		matchButton.setEnabled(false);
		GridBagConstraints gbc_matchButton = new GridBagConstraints();
		gbc_matchButton.insets = new Insets(0, 0, 0, 5);
		gbc_matchButton.gridx = 2;
		gbc_matchButton.gridy = 1;
		buttonPanel.add(matchButton, gbc_matchButton);

		JButton finishButton = new JButton("Finish!");
		finishButton.addActionListener(finishListener);
		finishButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_finishButton = new GridBagConstraints();
		gbc_finishButton.insets = new Insets(0, 0, 0, 5);
		gbc_finishButton.gridx = 4;
		gbc_finishButton.gridy = 1;
		buttonPanel.add(finishButton, gbc_finishButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cancelAction();
			}
		});
		GridBagConstraints gbc_cancelButton = new GridBagConstraints();
		gbc_cancelButton.insets = new Insets(0, 0, 0, 5);
		gbc_cancelButton.gridx = 5;
		gbc_cancelButton.gridy = 1;
		buttonPanel.add(cancelButton, gbc_cancelButton);
		
		frame.getRootPane().setDefaultButton(finishButton);
	}

	private void cancelAction() {
		frame.reset();
		frame.setVisible(false);
	}

	private void finishAction() {
		frame.okHandler();
	}

	protected JPanel getShowBoardPanel() {
		return showBoardPanel;
	}

	protected JTextField getRewriteTextField() {
		return rewriteTextField;
	}

	protected JLabel getSelectedExpLabel() {
		return selectedExpLabel;
	}

	protected void loadShowBoard(myEpanel expPanel) {
		GridBagConstraints gbc_expPanel = new GridBagConstraints();
		gbc_expPanel.insets = new Insets(0, 0, 5, 5);
		gbc_expPanel.gridx = 1;
		gbc_expPanel.gridy = 1;
		showBoardPanel.add(expPanel, gbc_expPanel);
	}

}
