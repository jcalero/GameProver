package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class RewritePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField txtx;
	private JDialog frame;

	/**
	 * Create the panel.
	 */
	public RewritePanel(JDialog frame) {
		this.frame = frame;
		setBorder(new LineBorder(new Color(0, 0, 0)));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{26, 0, 0, 25, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel selectSubtermLabel = new JLabel("Select subterm");
		GridBagConstraints gbc_selectSubtermLabel = new GridBagConstraints();
		gbc_selectSubtermLabel.anchor = GridBagConstraints.SOUTH;
		gbc_selectSubtermLabel.insets = new Insets(0, 0, 5, 5);
		gbc_selectSubtermLabel.gridx = 1;
		gbc_selectSubtermLabel.gridy = 0;
		add(selectSubtermLabel, gbc_selectSubtermLabel);
		
		JLabel throwInLabel = new JLabel("");
		throwInLabel.setBorder(new LineBorder(new Color(0, 0, 0)));
		throwInLabel.setIcon(new ImageIcon(RewritePanel.class.getResource("/gui/images/throw_in_small.png")));
		GridBagConstraints gbc_throwInLabel = new GridBagConstraints();
		gbc_throwInLabel.insets = new Insets(0, 0, 5, 0);
		gbc_throwInLabel.gridheight = 2;
		gbc_throwInLabel.anchor = GridBagConstraints.SOUTHEAST;
		gbc_throwInLabel.gridx = 3;
		gbc_throwInLabel.gridy = 0;
		add(throwInLabel, gbc_throwInLabel);
		
		JPanel oldExpressionPanel = new JPanel();
		oldExpressionPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		oldExpressionPanel.setBackground(Color.WHITE);
		GridBagConstraints gbc_oldExpressionPanel = new GridBagConstraints();
		gbc_oldExpressionPanel.gridwidth = 3;
		gbc_oldExpressionPanel.insets = new Insets(0, 0, 5, 5);
		gbc_oldExpressionPanel.fill = GridBagConstraints.BOTH;
		gbc_oldExpressionPanel.gridx = 0;
		gbc_oldExpressionPanel.gridy = 1;
		add(oldExpressionPanel, gbc_oldExpressionPanel);
		GridBagLayout gbl_oldExpressionPanel = new GridBagLayout();
		gbl_oldExpressionPanel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_oldExpressionPanel.rowHeights = new int[]{0, 0, 0, 0};
		gbl_oldExpressionPanel.columnWeights = new double[]{1.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_oldExpressionPanel.rowWeights = new double[]{1.0, 0.0, 1.0, Double.MIN_VALUE};
		oldExpressionPanel.setLayout(gbl_oldExpressionPanel);
		
		JLabel tempExpressionLabel = new JLabel("(x+1)*x->(x+x)");
		tempExpressionLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 18));
		GridBagConstraints gbc_tempExpressionLabel = new GridBagConstraints();
		gbc_tempExpressionLabel.insets = new Insets(0, 0, 5, 5);
		gbc_tempExpressionLabel.gridx = 1;
		gbc_tempExpressionLabel.gridy = 1;
		oldExpressionPanel.add(tempExpressionLabel, gbc_tempExpressionLabel);
		
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
		gbl_rewriteToPanel.columnWidths = new int[]{0, 0, 0, 32, 0, 0, 0, 0, 0, 0, 0};
		gbl_rewriteToPanel.rowHeights = new int[]{8, 0, 4, 0};
		gbl_rewriteToPanel.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_rewriteToPanel.rowWeights = new double[]{1.0, 0.0, 1.0, Double.MIN_VALUE};
		rewriteToPanel.setLayout(gbl_rewriteToPanel);
		
		JLabel lblNewLabel = new JLabel("Rewrite:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 1;
		rewriteToPanel.add(lblNewLabel, gbc_lblNewLabel);
		
		JLabel lblx = new JLabel("(x+1)");
		lblx.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblx.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblx.setOpaque(true);
		lblx.setHorizontalAlignment(SwingConstants.CENTER);
		lblx.setBorder(new LineBorder(new Color(0, 0, 0)));
		lblx.setBackground(Color.WHITE);
		GridBagConstraints gbc_lblx = new GridBagConstraints();
		gbc_lblx.ipady = 5;
		gbc_lblx.ipadx = 5;
		gbc_lblx.insets = new Insets(0, 0, 5, 5);
		gbc_lblx.gridx = 2;
		gbc_lblx.gridy = 1;
		rewriteToPanel.add(lblx, gbc_lblx);
		
		JLabel lblTo = new JLabel("to:");
		lblTo.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lblTo = new GridBagConstraints();
		gbc_lblTo.anchor = GridBagConstraints.EAST;
		gbc_lblTo.insets = new Insets(0, 0, 5, 5);
		gbc_lblTo.gridx = 3;
		gbc_lblTo.gridy = 1;
		rewriteToPanel.add(lblTo, gbc_lblTo);
		
		txtx = new JTextField();
		txtx.setText("((x*1)+1)");
		GridBagConstraints gbc_txtx = new GridBagConstraints();
		gbc_txtx.insets = new Insets(0, 0, 5, 5);
		gbc_txtx.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtx.gridx = 4;
		gbc_txtx.gridy = 1;
		rewriteToPanel.add(txtx, gbc_txtx);
		txtx.setColumns(10);
		
		JLabel lblResult = new JLabel("-> Result:");
		lblResult.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lblResult = new GridBagConstraints();
		gbc_lblResult.insets = new Insets(0, 0, 5, 5);
		gbc_lblResult.gridx = 6;
		gbc_lblResult.gridy = 1;
		rewriteToPanel.add(lblResult, gbc_lblResult);
		
		JLabel lblxxxx = new JLabel("((x*1)+1)*x->(x+x)");
		lblxxxx.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		GridBagConstraints gbc_lblxxxx = new GridBagConstraints();
		gbc_lblxxxx.anchor = GridBagConstraints.BASELINE;
		gbc_lblxxxx.insets = new Insets(0, 0, 5, 5);
		gbc_lblxxxx.gridx = 8;
		gbc_lblxxxx.gridy = 1;
		rewriteToPanel.add(lblxxxx, gbc_lblxxxx);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 4;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 3;
		add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 20, 0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{4, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JButton btnNewButton = new JButton("Select...");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 1;
		panel.add(btnNewButton, gbc_btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Match...");
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton_1.gridx = 2;
		gbc_btnNewButton_1.gridy = 1;
		panel.add(btnNewButton_1, gbc_btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Finish!");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				finishAction();
			}
		});
		btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 12));
		GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
		gbc_btnNewButton_2.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton_2.gridx = 4;
		gbc_btnNewButton_2.gridy = 1;
		panel.add(btnNewButton_2, gbc_btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("Cancel");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cancelAction();
			}
		});
		GridBagConstraints gbc_btnNewButton_3 = new GridBagConstraints();
		gbc_btnNewButton_3.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton_3.gridx = 5;
		gbc_btnNewButton_3.gridy = 1;
		panel.add(btnNewButton_3, gbc_btnNewButton_3);

	}
	
	private void cancelAction() {
		frame.setVisible(false);
	}
	
	private void finishAction() {
		frame.setVisible(false);
	}

}
