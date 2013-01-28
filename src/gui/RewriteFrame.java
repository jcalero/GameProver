package gui;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import logic.Expression;
import logic.ProofState;

public class RewriteFrame extends JDialog {
	
	private static final long serialVersionUID = 1L;
	private RewritePanel rewritePanel;
	
	// TODO: Temporary variables for conversion. FIX:
	public int origExpIndex;
	public boolean isassum;
	public ProofState proofState;
	public MyLabel targetObject;
	private JPanel showboard;
	private JButton btnselect;
	private JButton btntypein;
	public JButton btnok;
	public JButton btncancel;

	private Expression origExp;
	
	public boolean select;
	public boolean selectedtar;
	private myEpanel selectedExp;
	private JTextField txtNewVar;
	private JLabel textwindow;
	private myEpanel dispExp;

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
	
	// TODO: Temporary methods for conversion. FIX:
	public void setsltExp(Expression e, String type) {
		this.invalidate();
		selectedExp.removeAll();
		selectedExp.setmyEpanel(e, this, type);
		this.validate();
	}
	
	// TODO: Temporary methods for conversion. FIX:	
	public void settextstr(String var) {
		txtNewVar.putClientProperty("original_variable", var);
		txtNewVar.setText(var);
		textwindow.setText("rewrit:" + var);
	}

	// TODO: Temporary methods for conversion. FIX:	
	public void setdispExp(Expression e, String type) {
		this.invalidate();
		showboard.removeAll();
		origExp = e;
		dispExp.removeAll();
		dispExp.setmyEpanel(e, this, type);
		showboard.add(dispExp);
		this.validate();

		btnselect.setEnabled(true);
		btntypein.setEnabled(true);
		btncancel.setEnabled(true);
		btnok.setEnabled(true);
		txtNewVar.setEnabled(true);
	}

	// TODO: Temporary methods for conversion. FIX:	
	public myEpanel getdispExp() {
		return dispExp;
	}

	// TODO: Temporary methods for conversion. FIX:	
	public myEpanel getsltExp() {
		return selectedExp;
	}

}
