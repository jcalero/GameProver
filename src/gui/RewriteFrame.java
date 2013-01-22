package gui;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class RewriteFrame extends JDialog {
	
	private static final long serialVersionUID = 1L;
	private RewritePanel contentPane;

	/**
	 * Create the frame.
	 */
	public RewriteFrame(JFrame owner) {
		super(owner, false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		contentPane = new RewritePanel(this);
		setContentPane(contentPane);
		setSize(500, 220);
		setLocationRelativeTo(owner);
	}

}
