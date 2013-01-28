package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import logic.Expression;
import parser.MyExpressionParser;
import javax.swing.SwingConstants;

public class NewProofDialog extends JDialog {


	private static final long serialVersionUID = 1L;
	
	protected final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private MainWindow mainWindow;
	private JLabel errorLabel;
	
	// Constraints
	private static final int minX = 350;
	private static final int minY = 175;
	private static final int WIDTH = 350;
	private static final int HEIGHT = 175;

	/**
	 * Create the dialog.
	 */
	public NewProofDialog(MainWindow mainWindow, Frame owner) {
		super(owner, true);
		this.mainWindow = mainWindow;
		setDialogBounds();
		setLocationRelativeTo(owner);
		setAlwaysOnTop(true);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		setTitle("Start a new proof");
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{20, 0, 90, 0, 20, 0};
		gbl_contentPanel.rowHeights = new int[]{15, 20, 0, 20, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{1.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPanel.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel newProofInfoLabel = new JLabel("<html><center>Write the expression you want to prove.</center></html>");
		newProofInfoLabel.setMinimumSize(new Dimension(130, 40));
		newProofInfoLabel.setMaximumSize(new Dimension(120, 14));
		GridBagConstraints gbc_newProofInfoLabel = new GridBagConstraints();
		gbc_newProofInfoLabel.gridwidth = 3;
		gbc_newProofInfoLabel.insets = new Insets(0, 0, 5, 5);
		gbc_newProofInfoLabel.gridx = 1;
		gbc_newProofInfoLabel.gridy = 1;
		contentPanel.add(newProofInfoLabel, gbc_newProofInfoLabel);
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.gridwidth = 3;
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 2;
		contentPanel.add(textField, gbc_textField);
		textField.setColumns(10);
		
		errorLabel = new JLabel("");
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		errorLabel.setForeground(Color.red);
		GridBagConstraints gbc_errorLabel = new GridBagConstraints();
		gbc_errorLabel.gridwidth = 5;
		gbc_errorLabel.insets = new Insets(0, 0, 5, 5);
		gbc_errorLabel.gridx = 0;
		gbc_errorLabel.gridy = 3;
		contentPanel.add(errorLabel, gbc_errorLabel);

		JPanel buttonPane = new JPanel();
		FlowLayout fl_buttonPane = new FlowLayout(FlowLayout.CENTER);
		buttonPane.setLayout(fl_buttonPane);
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				confirmAction();
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

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
	
	private void confirmAction() {
		String proofString = textField.getText();
		Expression expression = null;
		boolean parseSuccess = false;
		
		try {
			expression = MyExpressionParser.parse(proofString);
			parseSuccess = true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			parseSuccess = false;
		}
		
		if (parseSuccess) {
			mainWindow.loadGamePanel(expression);
			setVisible(false);
		} else {
			setErrorText("Couldn't parse expression, please try again.");
		}
	}
	
	private void cancelAction() {
		setVisible(false);
	}
	
	private void setErrorText(String text) {
		errorLabel.setText("ERROR: " + text);
	}
	
	public void reset() {
		errorLabel.setText("");
	}
}
