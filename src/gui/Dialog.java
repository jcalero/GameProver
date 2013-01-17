package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public abstract class Dialog extends JDialog {

	/**
	 * Default serial version id.
	 */
	private static final long serialVersionUID = 1L;
	protected final JPanel contentPanel = new JPanel();

	private static final int minX = 200;
	private static final int minY = 100;

	/**
	 * Create the dialog.
	 */
	public Dialog(Frame frame, boolean modal) {
		super(frame, modal);
		setDialogBounds();
		setLocationRelativeTo(null);
		setAlwaysOnTop(true);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
	}

	private void setDialogBounds() {
		// Container parent = getParent();
		// Rectangle parentBounds = parent.getBounds();
		//
		// int xPos = (int) parentBounds.getX();
		// int yPos = (int) parentBounds.getY();
		int xPos = 200;
		int yPos = 200;

		setBounds(xPos, yPos, minX, minY);
		setMinimumSize(new Dimension(minX, minY));
	}

	// TODO: Make these methods be forced on the children but also contain
	// default implementations. Like "MouseListener" etc..
	protected abstract void confirmAction();

	protected abstract void cancelAction();
}
