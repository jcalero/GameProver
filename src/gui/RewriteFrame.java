package gui;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class RewriteFrame extends JDialog {
	
	private static final long serialVersionUID = 1L;
	private RewritePanel rewritePanel;

	/**
	 * Create the frame.
	 */
	public RewriteFrame(JFrame owner) {
		super(owner, false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		rewritePanel = new RewritePanel(this);
		setContentPane(rewritePanel);
		setSize(500, 220);
		setLocationRelativeTo(owner);
	}

}
