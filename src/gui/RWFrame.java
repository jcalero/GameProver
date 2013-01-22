/**
 * File: RWFrame.java
 * Author: 
 * Date: 
 * 
 * Class for displaying and managing the "Rewrite Frame" at the
 * top of the window. Used for rewriting expressions.
 */
package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import logic.Expression;
import logic.LogicStep;
import logic.ProofState;
import logic.Rule;
import logic.StepManager;
import parser.MyExpressionParser;

@SuppressWarnings({ "unused", "serial" })
public class RWFrame extends JLabel implements MouseListener, ActionListener {
	public int origExpIndex;
	public boolean isassum;
	public ProofState proofState;
	public MyLabel targetObject;
	private myEpanel dispExp;
	private JPanel showboard;
	private JPanel selectboard;
	private myEpanel selectedExp;
	private JButton btnselect;
	private JButton btntypein;
	public JButton btnok;
	public JButton btncancel;
	private JButton btnexit;
	private JLabel textwindow;
	private JLabel textboard;
	private JTextField txtNewVar;
	public boolean selectedtar;
	public boolean select;

	private Expression origExp;
	private DisplayFrame parent;
	private Font font = new Font("SansSerif", 20, 18);
	private ImageIcon rwIcon = new ImageIcon(getClass().getResource(
			"images/rw.png"));

	public RWFrame(Expression e, DisplayFrame df) {

		isassum = false;
		textboard = new JLabel();
		textboard.setFont(font);
		parent = df;
		setOpaque(true);
		setBackground(Color.white);
		setBounds(200, 12, 410, 100);
		select = false;
		selectedtar = false;

		showboard = new JPanel();
		showboard.setBounds(10, 2, 395, 30);

		dispExp = new myEpanel();
		dispExp.setmyEpanel(origExp, this, "up");

		selectboard = new JPanel();
		selectboard.setBounds(10, 73, 395, 26);
		selectedExp = new myEpanel();
		selectedExp.setmyEpanel(origExp, this, "down");
		selectboard.add(selectedExp);

		txtNewVar = new JTextField();
		txtNewVar.setColumns(10);
		txtNewVar.setBounds(84, 41, 240, 30);
		txtNewVar.setForeground(new Color(0, 205, 0));
		txtNewVar.setFont(new Font("SansSerif", 20, 14));
		String var = new String();
		if (origExp == null) {
			var = "";
		} else {
			var = origExp.toString();
		}
		txtNewVar.putClientProperty("original_variable", var);
		txtNewVar.setText(var);
		txtNewVar.addActionListener(this);
		textwindow = new JLabel();
		textwindow.setText("rewriting: " + var);
		textwindow.setBounds(10, 82, 300, 15);
		btnselect = new JButton("Select");
		btnselect.setBounds(10, 41, 70, 15);
		btnselect.setMargin(new Insets(3, 3, 5, -2));

		btntypein = new JButton("Type");
		btntypein.setBounds(10, 57, 70, 15);

		btnok = new JButton("OK");
		btnok.setBounds(325, 41, 80, 15);
		btncancel = new JButton("Cancel");
		btncancel.setBounds(325, 57, 80, 15);

		btncancel.setMargin(new Insets(3, 1, 5, -2));
		btnexit = new JButton("Exit");
		btnexit.setBounds(410, 1, 80, 20);

		this.add(showboard);

		this.add(txtNewVar);
		this.add(btnselect);
		this.add(btntypein);
		this.add(btnok);
		this.add(btncancel);
		this.add(selectboard);

		btnselect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectHandler();
			}
		});

		btntypein.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				typeinHandler();
			}
		});

		btnok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				okHandler();
			}
		});

		btncancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancelHandler();
			}
		});

		btnselect.setEnabled(false);
		btntypein.setEnabled(false);
		btncancel.setEnabled(false);
		btnok.setEnabled(false);
		txtNewVar.setEnabled(false);

	}

	public void selectHandler() {
		btntypein.setEnabled(false);
	}

	public void settextstr(String var) {
		txtNewVar.putClientProperty("original_variable", var);
		txtNewVar.setText(var);
		textwindow.setText("rewrit:" + var);
	}

	private void typeinHandler() {
		btnselect.setEnabled(false);
		Expression tempexp = new Expression();

		tempexp = dispExp.getTarget();
		if (tempexp != null) {
			settextstr(tempexp.toString());
		} else {
			System.out.println("err");
		}
	}

	public myEpanel getdispExp() {
		return dispExp;
	}

	public myEpanel getsltExp() {
		return selectedExp;

	}

	public void okHandler() {
		this.invalidate();
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
			StepManager stepManager = new StepManager(parent);
			
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
			
			restart();
			setdisptext(origExp.toString());
			parent.updateFrame();

			setdisptext(null);
			selectedtar = false;
			targetObject.fullText.setForeground(Color.BLACK); // NULL POINTER EXCEPTION - Jakob
		}
		this.validate();

	}

	public void restart() {
		dispExp.removeAll();
		origExp = new Expression();
		selectedExp.removeAll();
		dispExp.setmyEpanel(origExp, this, "up");
		settextstr("N/A");
		select = false;
		selectedtar = false;
		btnselect.setEnabled(false);
		btntypein.setEnabled(false);
		btncancel.setEnabled(false);
		btnok.setEnabled(false);
		txtNewVar.setEnabled(false);
		parent.rwIn.setVisible(false);

	}

	public void cancelHandler() {
		this.invalidate();

		btnselect.setEnabled(true);
		btntypein.setEnabled(true);
		dispExp.removeAll();
		dispExp.setmyEpanel(origExp, this, "up");
		dispExp.reset();
		selectedExp.removeAll();
		settextstr("N/A");
		select = false;

		this.validate();

	}

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

	public void setsltExp(Expression e, String type) {
		this.invalidate();
		selectedExp.removeAll();
		selectedExp.setmyEpanel(e, this, type);
		this.validate();
	}

	public void setdisptext(String s) {
		this.invalidate();
		showboard.removeAll();
		textboard.setText(s);
		showboard.add(textboard);
		this.validate();
	}

	public Expression getorigExp() {
		return this.origExp;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		okHandler();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

}
