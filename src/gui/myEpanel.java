package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import logic.Expression;

@SuppressWarnings({ "serial", "unused" })
public class myEpanel extends JPanel implements MouseListener {

	private myEpanel left;
	private myEpanel right;
	private JLabel node;
	public RewriteFrame parent;
	private boolean hassub;
	private boolean chosen;
	private static boolean find;
	public static Expression finaltarget;
	private HashMap<JLabel, Expression> RWExpressions = new HashMap<JLabel, Expression>();
	private Font font = new Font("SansSerif", 20, 18);
	private Expression origExp;
	private HashMap<String, Expression> ExpTree;
	private Expression target;
	private String type;

	public myEpanel() {
		super();
		setBorder(BorderFactory.createEmptyBorder(-5, -5, -5, -5));
		setOpaque(true);
		setBackground(Color.white);
		hassub = false;
		chosen = false;
		find = false;
		origExp = null;
	}

	public myEpanel(JLabel n, RewriteFrame pf) {
		parent = pf;
		node = n;
		add(node);
	}

	public myEpanel(myEpanel left, JLabel s, myEpanel right) {
		this.left = new myEpanel(left);
		node = s;
		this.right = new myEpanel(right);
		add(this.left);
		add(this.node);
		add(this.right);
	}

	public myEpanel(myEpanel e) {
		node = e.getNode();
		left = e.getLeft();
		right = e.getRight();
		add(node, BorderLayout.CENTER);
		add(left, BorderLayout.WEST);
		add(right, BorderLayout.EAST);
	}

	public myEpanel setmyEpanel(Expression e, RewriteFrame pf, String type) {
		parent = pf;
		this.removeAll();
		origExp = e;
		setType(type);

		if (e == null) {
		} else if (e.isTermVar() || e.isTermConstant()) {
			JLabel lb = new JLabel(e.toString());
			lb.setIconTextGap(1);
			node = lb;
			lb.setFont(font);
			lb.addMouseListener(this);
			this.add(lb, BorderLayout.CENTER);
			RWExpressions.put(lb, e);
			showRWEXPS();
		} else {

			this.hassub = true;
			JLabel nb = new JLabel(e.getNode().toString());
			JLabel lc = new JLabel("(");
			JLabel rc = new JLabel(")");
			nb.setIconTextGap(1);
			lc.setIconTextGap(1);
			rc.setIconTextGap(1);

			lc.setFont(font);
			rc.setFont(font);
			nb.addMouseListener(this);
			myEpanel l = new myEpanel();
			myEpanel r = new myEpanel();
			this.setLeft(l.setmyEpanel(e.getLeft(), pf, type));
			this.setRight(r.setmyEpanel(e.getRight(), pf, type));
			l.setType(type);
			r.setType(type);
			this.setNode(nb);
			nb.setFont(font);
			RWExpressions.put(nb, e);
			showRWEXPS();
			this.add(lc, BorderLayout.WEST);
			this.add(this.left, BorderLayout.WEST);
			this.add(this.node, BorderLayout.CENTER);
			this.add(this.right, BorderLayout.EAST);
			this.add(rc, BorderLayout.EAST);
		}
		repaint();
		return this;

	}

	public void showRWEXPS() {
		Iterator<JLabel> print = RWExpressions.keySet().iterator();
		while (print.hasNext()) {
			Expression e = RWExpressions.get(print.next());
		}

	}

	public void setchosen(boolean b) {
		this.chosen = b;

	}

	public Expression getOrigExp() {
		return this.origExp;
	}

	public boolean getsub() {
		return this.hassub;
	}

	public myEpanel getLeft() {
		return left;
	}

	public myEpanel getRight() {
		return right;
	}

	public JLabel getNode() {
		return node;
	}

	public void setLeft(myEpanel l) {
		this.left = l;
	}

	public void setRight(myEpanel r) {
		this.right = r;
	}

	public void setNode(JLabel n) {
		this.node = n;
	}

	public void setTarget(Expression e) {
		this.target = e;
	}

	public Expression getTarget() {
		if (this.getchosen()) {
			find = true;
			finaltarget = this.target;
		} else if (!this.chosen && !find && this.hassub) {
			this.getRight().getTarget();// }
			this.getLeft().getTarget();
		}
		return finaltarget;
	}

	public boolean getchosen() {
		return this.chosen;
	}

	public HashMap<JLabel, Expression> getRWExprssions() {
		return this.RWExpressions;
	}

	public void changecolor(Color c) {
		if (!this.getsub()) {
			this.getNode().setForeground(c);
		} else {
			this.getRight().changecolor(c);
			this.getLeft().changecolor(c);
			this.getNode().setForeground(c);
		}
	}

	public Expression findtarget() {
		boolean find = false;
		if (this.chosen = true) {
			find = true;
			return this.getOrigExp();

		} else if (!find) {
			this.getLeft().findtarget();

		} else {
			this.getRight().findtarget();
		}

		return null;
	}

	public void replaceAll(Object old, Object nnew, Expression orig) {

	}

	public void mouseClicked(MouseEvent n) {
		JLabel clicked = (JLabel) n.getSource();
		this.reset();
		if (RWExpressions.containsKey(clicked)) {
			this.chosen = true;
			target = this.origExp;
			parent.settextstr(target.toString());
			parent.selectedtar = true;
			if (this.getType().equals("up")) {
				parent.getdispExp().changecolor(Color.black);
				this.changecolor(Color.red);
				parent.select = true;
			} else if (parent.select && this.getType().equals("down")) {
				parent.getsltExp().changecolor(Color.black);
				this.changecolor(new Color(0, 205, 0));// dark green RGB COLOR
			}
			repaint();
		}

	}

	public String getType() {
		return this.type;
	}

	public void setType(String t) {
		this.type = t;
	}

	@SuppressWarnings("static-access")
	public void reset() {
		this.finaltarget = null;
		this.changecolor(Color.black);
		this.chosen = false;
		if (this.hassub) {
			this.left.reset();
			this.right.reset();
		}
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