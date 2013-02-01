package gui;

import game.Game;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import parser.MyExpressionParser;

import logic.Expression;
import logic.LogicStep;
import logic.ProofState;
import logic.Rule;
import logic.StepManager;

public class RewriteFrame extends JDialog {
	
	private static final long serialVersionUID = 1L;
	private RewritePanel rewritePanel;
//	private Game game;
	private GamePanel gamePanel;
	
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
//	private JLabel textwindow;
	private myEpanel dispExp;
	private JLabel selectedExpLabel;

	/**
	 * Create the frame.
	 */
	public RewriteFrame(JFrame owner, GamePanel gamePanel) {
		super(owner, false);
		this.gamePanel = gamePanel;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		rewritePanel = new RewritePanel(this);
		setContentPane(rewritePanel);
		setSize(500, 220);
		setLocationRelativeTo(owner);
		
		dispExp = new myEpanel();
		dispExp.setmyEpanel(origExp, this, "up");
		
		showboard = rewritePanel.getShowBoardPanel();
		txtNewVar = rewritePanel.getRewriteTextField();
		selectedExpLabel = rewritePanel.getSelectedExpLabel();
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
		selectedExpLabel.setText(var);
		selectedExpLabel.setToolTipText(var);
		
//		textwindow.setText("rewrit:" + var);
	}

	// TODO: Temporary methods for conversion. FIX:	
	public void setdispExp(Expression e, String type) {
		this.invalidate();
		showboard.removeAll();
		origExp = e;
		dispExp.removeAll();
		dispExp.setmyEpanel(e, this, type);
		rewritePanel.loadShowBoard(dispExp);
		this.validate();

//		btnselect.setEnabled(true);
//		btntypein.setEnabled(true);
//		btncancel.setEnabled(true);
//		btnok.setEnabled(true);
//		txtNewVar.setEnabled(true);
	}

	// TODO: Temporary methods for conversion. FIX:	
	public myEpanel getdispExp() {
		return dispExp;
	}

	// TODO: Temporary methods for conversion. FIX:	
	public myEpanel getsltExp() {
		return selectedExp;
	}
	
	public void okHandler() {
//		this.invalidate();
		if (selectedtar) {

			String s = txtNewVar.getText();
			Expression exp = new Expression();
			if (s == null)
				return;
			try {
				exp = MyExpressionParser.parse(s);
			} catch (Exception e) {
				e.printStackTrace();
			}

			Expression newe = exp.clonexp();
			Expression olde = dispExp.getTarget().clonexp();
			
			Expression newGoal = new Expression();
			newGoal.setNode("=");
			newGoal.setLeft(olde);
			newGoal.setRight(newe);
			
			dispExp.getTarget().replaceWith(exp);
			
			Expression rewrittenExp = origExp.clonexp();
			
			System.out.println("game: " + gamePanel.getGame());
			StepManager stepManager = new StepManager(gamePanel.getGame());
			
			Rule rule = null;
			
			if (isassum) {
				rule = Rule.RewriteAssum;
			} else if (!isassum) {
				rule = Rule.RewriteGoal;
			}
			
			LogicStep ls = new LogicStep(rule, proofState.getDepth());
			ls.setFirstExpressionIndex(origExpIndex);
			ls.setNewExpression(newGoal);
			ls.setRewrittenExpression(rewrittenExp);
			stepManager.applyRule(ls);
			
			reset();
//			setdisptext(origExp.toString());
//			parent.updateFrame();

//			setdisptext(null);
			selectedtar = false;
			targetObject.fullText.setForeground(Color.BLACK); // NULL POINTER EXCEPTION? - Jakob
		}
//		this.validate();
		gamePanel.getGame().updateFrame();
		setVisible(false);

	}
	
	public void reset() {
		dispExp.removeAll();
		origExp = new Expression();
//		selectedExp.removeAll();
//		dispExp.setmyEpanel(origExp, this, "up");
		settextstr("N/A");
		select = false;
		selectedtar = false;
//		btnselect.setEnabled(false);
//		btntypein.setEnabled(false);
//		btncancel.setEnabled(false);
//		btnok.setEnabled(false);
//		txtNewVar.setEnabled(false);
//		parent.rwIn.setVisible(false);

	}

}
