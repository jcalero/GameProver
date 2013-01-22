package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import logic.Expression;
import logic.GameState;
import logic.LogicStep;
import logic.ProofState;
import logic.Rule;
import logic.StepManager;

@SuppressWarnings({ "serial" })
public class ProofStatePanel extends JPanel implements MouseListener,
		MouseMotionListener, Cloneable {
	// private URL win = getClass().getResource("sounds/win.wav");
	private URL cheer = getClass().getResource("sounds/cheer.wav");
	private URL wrong = getClass().getResource("sounds/wrong.wav");
	private URL kick = getClass().getResource("sounds/kick.wav");

	public DisplayFrame parent;
	public ProofState logicState;
	public StepManager stepManager;
	private int assumStartX = 10;
	private int assumStartY = 10;
	private int assumIntervalX = 120;

	private int goalStartX = assumStartX;
	private int goalStartY = assumStartY + 50;
	private int goalIntervalX = 120;

	private int sideOffset = 10;
	private int subStateStartY = 250;

	// 2012
	private int bbStartX = 10;
	private int bbStartY = goalStartY + 70;
	private int bbIntervalX = 130;

	private ImageIcon footballIcon = new ImageIcon(getClass().getResource(
			"images/football.png"));
	private ImageIcon flyingFootballIcon = new ImageIcon(getClass()
			.getResource(
					"images/flyingFootball.png"));

	private ImageIcon goalIcon = new ImageIcon(getClass().getResource(
			"images/goal.png"));
	private ImageIcon falseIcon = new ImageIcon(getClass().getResource(
			"images/false.png"));
	private Font font = new Font("SansSerif", 1, 26);
	private ImageIcon bbIconOriginal = new ImageIcon(getClass().getResource(
			"images/bubble.png"));
	private Image smallerBubbleImage = bbIconOriginal.getImage()
			.getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH);
	private ImageIcon bbIcon = new ImageIcon(smallerBubbleImage);
	private ImageIcon helpmark = new ImageIcon(getClass().getResource(
			"images/helpmark.png"));
	private ImageIcon bbs = new ImageIcon(getClass().getResource(
			"images/bbs.png"));
	private ImageIcon sbb = new ImageIcon(getClass().getResource(
			"images/sbb.png"));
	// 2012
	private boolean isDragged = false;
	private int xOffset = 0, yOffset = 0;
	private Rectangle prevBounds = null;

	private HashMap<MyLabel, Expression> assumExpressions = new HashMap<MyLabel, Expression>();
	private HashMap<MyLabel, Expression> assumVariablesExpressions = new HashMap<MyLabel, Expression>();
	private HashMap<MyLabel, Expression> goalExpressions = new HashMap<MyLabel, Expression>();
	private HashMap<ProofStatePanel, ProofState> subPanels = new HashMap<ProofStatePanel, ProofState>();
	// 2012 bubbles in assumbubbles' list
	//
	private HashMap<MyLabel, Expression> bbExpressions = new HashMap<MyLabel, Expression>();
	private HashMap<JLabel, ArrayList<Expression>> bblstExpressions = new HashMap<JLabel, ArrayList<Expression>>();// 2012*
	// 2012

	private HashMap<String, Rule> rulesToString = new HashMap<String, Rule>();
	private int rightClickAssumIndex = -1;
	private int rightClickAssumVarIndex = -1;
	private int rightClickGoalIndex = -1;

	// can only store 6 of each per panel
	// private JLabel[] assumLabelPositions = new JLabel[200];
	private ArrayList<JLabel> assumLabelPositions = new ArrayList<JLabel>();
	private ArrayList<JLabel> assumVarLabelPositions = new ArrayList<JLabel>();
	private ArrayList<JLabel> goalLabelPositions = new ArrayList<JLabel>();
	private JLabel[] bblistPositions = new JLabel[6];
	private JLabel[] bbPositions = new JLabel[10];

	public JPopupMenu popup;

	private enum Type {
		NonLogical, Panel, Assum, AssumVar, Goal, Bubble, BubbleBox
	}

	private enum Modifier {
		None, Shift, Ctrl, ShiftAndCtrl, Alt, AltGr
	}

	private enum MouseButton {
		Left, Right, Middle
	}

	// 2012 clone
	public ProofStatePanel() {

	}

	// 2012
	public ProofStatePanel(DisplayFrame df) {
		parent = df;
	}

	public ProofStatePanel(DisplayFrame df, ProofState pf) {
		buildRulesToString();

		addMouseListener(this);// 2012 08 for help tips
		this.invalidate();
		parent = df;
		logicState = pf;
		stepManager = new StepManager(parent);

		popup = new JPopupMenu();

	//	Color background = new Color(102,255,51);
	//	setBackground(background);
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		setLayout(null);

		// these are the default bounds of the outer-most proofpanel
		// subpanels are resized in the update method
		setBounds(sideOffset, subStateStartY, 880, 630);

		// add assums
		for (int i = 0; i < logicState.getAssums().size(); i++) {
			Expression assum = logicState.getAssum(i);
			MyLabel assumLabel = new MyLabel(assum, footballIcon/* ,"assum" */);

			assumLabel.setBounds(assumStartX + i * assumIntervalX, assumStartY,
					footballIcon.getIconWidth() + 10,
					footballIcon.getIconHeight());

			assumLabel.addMouseListener(this);
			assumLabel.addMouseMotionListener(this);

			assumExpressions.put(assumLabel, assum);
			assumLabelPositions.add(assumLabel);

			// assumLabel.setOpaque(true);
			// assumLabel.setBackground(Color.yellow);
			add(assumLabel);
			assumLabel.showFullText();
		}

		// add goals
		for (int i = 0; i < logicState.getGoals().size(); i++) {
			Expression goal = logicState.getGoal(i);
			MyLabel goalLabel = new MyLabel(goal, goalIcon/* ,"goal" */);
			MyLabel falseLabel = new MyLabel(goal, falseIcon/* ,"goal" */);

			goalLabel.addMouseListener(this);
			// goalLabel.addMouseMotionListener(this);//testing

			if (goal.getNode() == null) {

				falseLabel.setBounds(goalStartX + i * goalIntervalX,
						goalStartY, goalIcon.getIconWidth(),
						goalIcon.getIconHeight());
				goalExpressions.put(falseLabel, goal);
				this.add(falseLabel);
			} else {

				goalLabel.setBounds(goalStartX + i * goalIntervalX, goalStartY,
						goalIcon.getIconWidth(), goalIcon.getIconHeight());
				goalExpressions.put(goalLabel, goal);
				goalLabelPositions.add(goalLabel);
				this.add(goalLabel);
				goalLabel.showFullText();
			}
		}

		// add bubbles
		for (int i = 0; i < logicState.getAssumbubbles().size(); i++) {

			JLabel BubblePanel = new JLabel();
			ArrayList<Expression> Bubblelst = logicState.getAssumbubbles().get(
					i);
			JLabel b = new JLabel("=", JLabel.CENTER);
			b.setHorizontalTextPosition(JLabel.CENTER);
			b.setForeground(Color.RED);
			b.setFont(font);
			// b.setIcon(goal4bubIcon);
			b.setIcon(footballIcon);

			BubblePanel.setLayout(new GridLayout(2, 0));
			BubblePanel.add(b);
			// System.out.println("add bubblelst to pic");

			for (int j = 0; j < logicState.getAssumbubbles().get(i).size(); j++) {
				// Expression goal = logicState.getGoal(i);

				Expression bubble = logicState.getAssumbubbles().get(i).get(j);
				MyLabel bbLabel = new MyLabel(bubble, bbIcon/* ,"bubble" */);
				// bbLabel.showFullText();//tip
				bbLabel.addMouseListener(this);
				// bbLabel.addMouseMotionListener(this);
				// bbLabel.setLocation(j,j+1);

				bbExpressions.put(bbLabel, bubble);
				bbPositions[j] = bbLabel;

				JPanel bbparent = new JPanel();
				bbparent.add(bbLabel);
				bbLabel.showFullText();
				BubblePanel.add(bbparent);
				// System.out.println("add bubble to pic"+j+" "+bubble.toString());
			}

			int s = 2;
			BubblePanel.setBounds(bbStartX + i * bbIntervalX, bbStartY,
					bbIcon.getIconWidth() * (s + 1),
					bbIcon.getIconHeight() * 2 + 30);
			// p.setBounds(bbStartX + i * bbIntervalX, bbStartY,
			// bbIcon.getIconWidth()*(s+1), bbIcon.getIconHeight()*2+1);
			// BubblePanel.setOpaque(true);
			// BubblePanel.setBackground(Color.blue);
			BubblePanel.setBorder(BorderFactory.createLineBorder(Color.RED));
			BubblePanel.addMouseListener(this);
			BubblePanel.addMouseMotionListener(this);
			bblstExpressions.put(BubblePanel, Bubblelst);
			bblistPositions[i] = BubblePanel;
			this.invalidate();
			add(BubblePanel);
			this.validate();
		}

		addSubStates(logicState);
		this.validate();
	}

	// 2012
	@Override
	public Object clone() {
		ProofStatePanel o = null;
		try {
			o = (ProofStatePanel) super.clone();

		} catch (CloneNotSupportedException e) {
			System.out.println(e.toString());
		}
		o.logicState = (ProofState) logicState.cloneps();
		// o.parent=(DisplayFrame) parent.clone();
		return o;
	}

	private void addSubStates(ProofState proofState) {
		for (int i = 0; i < proofState.getSubstates().size(); i++) {
			System.out.println("addSubStates loop");
			ProofState state = proofState.getSubstate(i);
			if (state.getHideFlag() && state.getSubstates().size() > 0) {
				addSubStates(state);
				return;
			}
			ProofStatePanel p = new ProofStatePanel(parent, state);
			subPanels.put(p, state);
			this.add(p);
		}
	}

	public void buildRulesToString() {
		rulesToString.put("Simplify Imp. Assumption", Rule.ImpliesAssum);
		rulesToString.put("Simplify Imp. Goal", Rule.ImpliesGoal);
		rulesToString.put("Simplify And Assumption", Rule.AndAssum);
		rulesToString.put("Simplify And Goal", Rule.AndGoal);
		rulesToString.put("Try to Prove Contradiction", Rule.NotAssumToGoal);
		rulesToString.put("Simplify Negations", Rule.NotAssumSimplify);
		rulesToString.put("Simplify Goal Negations", Rule.NotGoalSimplify);
		rulesToString.put("Falsify Goal",
				Rule.NotGoal);
		rulesToString.put("Switch Over Assumption", Rule.SwitchEqualsAssum);
		rulesToString.put("Switch Over AssumptionVar",
				Rule.SwitchEqualsAssumVar);
		rulesToString.put("Copy Assumption", Rule.CopyAssum);
		rulesToString.put("Copy AssumptionVar", Rule.CopyAssumVar);
		rulesToString.put("Start Induction", Rule.Induction);
		rulesToString.put("Rewrite Assumption", Rule.RewriteAssum);
		rulesToString.put("Rewrite Goal", Rule.RewriteGoal);
		rulesToString.put("Start Bubbling", Rule.CreateBubble);
	}

	// boolean recorded=false;
	// @SuppressWarnings("static-access")
	public void update() {
		parent.updateUndoButton();
		this.invalidate();

		// remove assums no longer in the state
		// NOTE Currently removes all assumptions (uncomment if statement to
		// change back to old way)
		Iterator<MyLabel> assumLabelIterator = assumExpressions.keySet()
				.iterator();
		while (assumLabelIterator.hasNext()) {
			MyLabel assumLabel = (MyLabel) assumLabelIterator.next();
			Expression assum = assumExpressions.get(assumLabel);

			// if (!logicState.getAssums().contains(assum)) {
			this.remove(assumLabel);
			this.remove(assumLabel.getFullText());
			assumLabelIterator.remove();
			for (int i = 0; i < assumLabelPositions.size(); i++) {
				if (assumLabelPositions.get(i) == assumLabel)
					assumLabelPositions.remove(i);
			}
			// }
		}

		// // add new assums (if necessary)
		// Iterator<Expression> assumIterator =
		// logicState.getAssums().iterator();
		// while (assumIterator.hasNext()) {
		// boolean hasAssum = false;
		// Expression e = assumIterator.next();
		// Iterator<MyLabel> assumLabelIterator2 = assumExpressions.keySet()
		// .iterator();
		//
		// while (assumLabelIterator2.hasNext()) {
		// MyLabel key = (MyLabel) assumLabelIterator2.next();
		// Expression a = assumExpressions.get(key);
		// if (a == e) {
		// hasAssum = true;
		// break;
		// }
		// }
		// if (!hasAssum) {
		// for (int i = 0; i < assumLabelPositions.size(); i++) {
		// if (assumLabelPositions.get(i) == null) {
		// MyLabel assumLabel = new MyLabel(e, footballIcon/*
		// * ,"assum"
		// */);
		//
		// assumLabel.setBounds(assumStartX + i * assumIntervalX
		// + (assumIntervalX * assumVariablesExpressions
		// .size()),
		// assumStartY, footballIcon.getIconWidth() + 10,
		// footballIcon.getIconHeight());
		//
		// assumLabel.addMouseListener(this);
		// assumLabel.addMouseMotionListener(this);
		//
		// assumExpressions.put(assumLabel, e);
		// assumLabelPositions.add(assumLabel);
		// add(assumLabel);
		// assumLabel.showFullText();
		// break;
		// }
		// }
		// }
		// }

		// add assums
		for (int i = 0; i < logicState.getAssums().size(); i++) {
			Expression assum = logicState.getAssum(i);
			MyLabel assumLabel = new MyLabel(assum, footballIcon/* ,"assum" */);

			assumLabel.setBounds(assumStartX + i * assumIntervalX, assumStartY,
					footballIcon.getIconWidth() + 10,
					footballIcon.getIconHeight());

			assumLabel.addMouseListener(this);
			assumLabel.addMouseMotionListener(this);

			assumExpressions.put(assumLabel, assum);
			assumLabelPositions.add(assumLabel);

			// assumLabel.setOpaque(true);
			// assumLabel.setBackground(Color.yellow);
			add(assumLabel);
			assumLabel.showFullText();
		}

		// remove assum vars no longer in the state
		// NOTE Currently removes all assumptionVars (uncomment if statement to
		// change back to old way)
		Iterator<MyLabel> assumVarLabelIterator = assumVariablesExpressions
				.keySet()
				.iterator();
		while (assumVarLabelIterator.hasNext()) {
			MyLabel assumVarLabel = (MyLabel) assumVarLabelIterator.next();
			Expression assumVar = assumVariablesExpressions.get(assumVarLabel);

			// if (!logicState.getAssumVars().contains(assumVar)) {
			this.remove(assumVarLabel);
			this.remove(assumVarLabel.getFullText());
			assumVarLabelIterator.remove();
			for (int i = 0; i < assumVarLabelPositions.size(); i++) {
				if (assumVarLabelPositions.get(i) == assumVarLabel)
					assumVarLabelPositions.remove(i);
			}
			// }
		}

		// add new assum vars (if necessary)
		// Iterator<Expression> assumVarsIterator = logicState.getAssumVars()
		// .iterator();
		// while (assumVarsIterator.hasNext()) {
		// boolean hasAssumVar = false;
		// Expression e = assumVarsIterator.next();
		// Iterator<MyLabel> assumVarLabelIterator2 = assumVariablesExpressions
		// .keySet()
		// .iterator();
		//
		// while (assumVarLabelIterator2.hasNext()) {
		// MyLabel key = (MyLabel) assumVarLabelIterator2.next();
		// Expression a = assumVariablesExpressions.get(key);
		// if (a == e) {
		// hasAssumVar = true;
		// break;
		// }
		// }
		// if (!hasAssumVar) {
		// for (int i = 0; i < assumVarLabelPositions.size(); i++) {
		// if (assumVarLabelPositions.get(i) == null) {
		// MyLabel assumVarLabel = new MyLabel(e,
		// flyingFootballIcon/*
		// * ,"assum"
		// */);
		//
		// assumVarLabel.setBounds((assumStartX + i
		// * assumIntervalX)
		// + (assumIntervalX * assumExpressions.size()),
		// assumStartY,
		// flyingFootballIcon.getIconWidth() + 10,
		// flyingFootballIcon.getIconHeight());
		//
		// assumVarLabel.addMouseListener(this);
		// assumVarLabel.addMouseMotionListener(this);
		//
		// assumVariablesExpressions.put(assumVarLabel, e);
		// assumVarLabelPositions.add(assumVarLabel);
		// add(assumVarLabel);
		// assumVarLabel.showFullText();
		// break;
		// }
		// }
		// }
		// }

		// add assums vars
		for (int i = 0; i < logicState.getAssumVars().size(); i++) {
			Expression assumVar = logicState.getAssumVar(i);
			MyLabel assumVarLabel = new MyLabel(assumVar, flyingFootballIcon);

			assumVarLabel.setBounds((assumStartX + i
					* assumIntervalX)
					+ (assumIntervalX * assumExpressions.size()),
					assumStartY,
					flyingFootballIcon.getIconWidth() + 10,
					flyingFootballIcon.getIconHeight());

			assumVarLabel.addMouseListener(this);
			assumVarLabel.addMouseMotionListener(this);

			assumVariablesExpressions.put(assumVarLabel, assumVar);
			assumLabelPositions.add(assumVarLabel);

			// assumLabel.setOpaque(true);
			// assumLabel.setBackground(Color.yellow);
			add(assumVarLabel);
			assumVarLabel.showFullText();
		}

		// remove goals no longer in the state
		Iterator<MyLabel> goalLabelIterator = goalExpressions.keySet()
				.iterator();
		while (goalLabelIterator.hasNext()) {
			MyLabel key = (MyLabel) goalLabelIterator.next();
			Expression g = goalExpressions.get(key);
			// if (!logicState.getGoals().contains(g)) {
			this.remove(key);
			this.remove(key.getFullText());
			goalLabelIterator.remove();
			for (int i = 0; i < goalLabelPositions.size(); i++) {
				if (goalLabelPositions.get(i) == key)
					goalLabelPositions.remove(i);
			}
			// }
		}

		// add new goals (if necessary)
		// Iterator<Expression> goalIterator = logicState.getGoals().iterator();
		// while (goalIterator.hasNext()) {
		// boolean hasGoal = false;
		// Expression e = goalIterator.next();
		// Iterator<MyLabel> goalLabelIterator2 = goalExpressions.keySet()
		// .iterator();
		// while (goalLabelIterator2.hasNext()) {
		// JLabel key = (JLabel) goalLabelIterator2.next();
		// Expression g = goalExpressions.get(key);
		// if (g == e) {
		// hasGoal = true;
		// break;
		// }
		// }
		// if (!hasGoal) {
		// for (int i = 0; i < goalLabelPositions.size(); i++) {
		// if (goalLabelPositions.get(i) == null) {
		// MyLabel goalLabel = new MyLabel(e, goalIcon/* ,"goal" */);
		//
		// goalLabel.addMouseListener(this);
		//
		// goalLabel.setBounds(goalStartX + i * goalIntervalX,
		// goalStartY, goalIcon.getIconWidth(),
		// goalIcon.getIconHeight());
		//
		// goalExpressions.put(goalLabel, e);
		// goalLabelPositions.add(goalLabel);
		// this.add(goalLabel);
		// goalLabel.showFullText();
		// break;
		// }
		// }
		// }
		// }

		for (int i = 0; i < logicState.getGoals().size(); i++) {
			Expression goal = logicState.getGoal(i);
			MyLabel goalLabel = new MyLabel(goal, goalIcon/* ,"goal" */);
			MyLabel falseLabel = new MyLabel(goal, falseIcon/* ,"goal" */);

			goalLabel.addMouseListener(this);
			// goalLabel.addMouseMotionListener(this);//testing

			if (goal.getNode() == null) {

				falseLabel.setBounds(goalStartX + i * goalIntervalX,
						goalStartY, goalIcon.getIconWidth(),
						goalIcon.getIconHeight());
				goalExpressions.put(falseLabel, goal);
				this.add(falseLabel);
			} else {

				goalLabel.setBounds(goalStartX + i * goalIntervalX, goalStartY,
						goalIcon.getIconWidth(), goalIcon.getIconHeight());
				goalExpressions.put(goalLabel, goal);
				goalLabelPositions.add(goalLabel);
				this.add(goalLabel);
				goalLabel.showFullText();
			}
		}

		// remove bubbles
		Iterator<JLabel> bbpanelIterator = bblstExpressions.keySet().iterator();
		while (bbpanelIterator.hasNext()) {

			JLabel bbpanel = (JLabel) bbpanelIterator.next();
			ArrayList<Expression> bubbles = bblstExpressions.get(bbpanel);
			if (!logicState.getAssumbubbles().contains(bubbles)) {
				this.remove(bbpanel);
				// this.remove(bbpanel.getFullText());
				bbpanelIterator.remove();
				for (int i = 0; i < bblistPositions.length; i++) {
					if (bblistPositions[i] == bbpanel)
						bblistPositions[i] = null;
				}
			}
		}

		// add bubbles
		Iterator<ArrayList<Expression>> bblstIterator = logicState
				.getAssumbubbles().iterator();
		while (bblstIterator.hasNext()) {
			// System.out.println("adding-repainting");
			boolean hasbubble = false;
			ArrayList<Expression> e = bblstIterator.next();
			Iterator<JLabel> bbpanelIterator2 = bblstExpressions.keySet()
					.iterator();

			while (bbpanelIterator2.hasNext()) {
				JLabel key = (JLabel) bbpanelIterator2.next();
				ArrayList<Expression> a = bblstExpressions.get(key);
				if (a == e) {
					hasbubble = true;
					break;
				}
			}

			// Check if bubbles are selected
			Iterator<MyLabel> bubbleIterator = bbExpressions.keySet()
					.iterator();
			while (bubbleIterator.hasNext()) {
				MyLabel bubble = bubbleIterator.next();
				Expression bubbleExpression = bbExpressions.get(bubble);
				if (logicState.isSelectedBubble(bubbleExpression)) {
					bubble.setForeground(Color.blue);
				} else {
					bubble.setForeground(Color.red);
				}
			}

			if (!hasbubble) {
				for (int i = 0; i < bblistPositions.length; i++) {
					if (bblistPositions[i] == null) {
						// 201207
						JLabel BubblePanel2 = new JLabel();
						BubblePanel2.setLayout(new GridLayout(2, 3));

						JLabel b2 = new JLabel("=", JLabel.CENTER);
						b2.setHorizontalTextPosition(JLabel.CENTER);
						b2.setForeground(Color.RED);
						b2.setFont(font);
						b2.setIcon(footballIcon);
						BubblePanel2.add(b2);

						for (int j = 0; j < e.size(); j++) {
							Expression bubble = e.get(j);
							MyLabel bbLabel = new MyLabel(bubble, bbIcon/* ,"bb" */);
							bbLabel.addMouseListener(this);

							bbExpressions.put(bbLabel, bubble);
							bbPositions[j] = bbLabel;
							JPanel bbparent = new JPanel();
							bbLabel.setForeground(Color.red);
							bbparent.add(bbLabel);
							bbLabel.showFullText();

							BubblePanel2.add(bbparent);
							repaint();
						}

						// int s= e.size();
						int s = 2;
						// BubblePanel2.setBounds(10,10,10,10);
						BubblePanel2.setBounds(bbStartX + i * bbIntervalX,
								bbStartY, bbIcon.getIconWidth() * (s + 1),
								bbIcon.getIconHeight() * s + 50);
						// BubblePanel2.setOpaque(true);
						// BubblePanel2.setBackground(Color.blue);
						BubblePanel2.setBorder(BorderFactory
								.createLineBorder(Color.red));
						BubblePanel2.addMouseListener(this);
						BubblePanel2.addMouseMotionListener(this);

						bblstExpressions.put(BubblePanel2, e);
						bblistPositions[i] = BubblePanel2;
						this.invalidate();
						add(BubblePanel2);
						this.validate();
						// System.out.println("update bubbles");
						break;
					}
				}
			}

		}

		// int i=0;
		// update all subpanels and remove ones no longer in the proofstate
		Iterator<ProofStatePanel> panelIterator = subPanels.keySet().iterator();
		while (panelIterator.hasNext()) {
			ProofStatePanel subPanel = (ProofStatePanel) panelIterator.next();
			ProofState state = subPanels.get(subPanel);
			if (!logicState.getSubstates().contains(state)
					|| state.getHideFlag()) {
				remove(subPanel);
				panelIterator.remove();
			} else
				// subPanel.recordable=false;
				subPanel.update();// i++;
			// parent.cleanup();
			// parent.UndoHandler();
			// System.out.println("unwanted----------");
			// 201206
		}
		// if(i>=1){parent.undolist.remove(parent.undolist.size()-(i+1));}
		// int j=0;
		// add new panels if necessary
		reAddSubStates(logicState);

		// A -> B -> C -> D
		resizepsp();

		// If two bubbles have been selected, show selection dialogue and
		// extract the new assumption if required
		if (!parent.isReplaying() && logicState.hasSelectedTwoBubbles()) {
			Expression[] selectedBubbles = logicState.getSelectedBubbles();
			boolean extract = showExtractBubbleDialog(selectedBubbles[0],
					selectedBubbles[1]);
			if (extract) {
				int bubbleBoxIndex = logicState.getSelectedBubbleBox();
				ArrayList<Expression> bubbleBox = logicState
						.getBubbleBox(bubbleBoxIndex);
				int leftBubble = bubbleBox.indexOf(selectedBubbles[0]);
				int rightBubble = bubbleBox.indexOf(selectedBubbles[1]);
				LogicStep ls = new LogicStep(Rule.Extract, getAbsoluteDepth());
				ls.setBubbleBoxIndex(bubbleBoxIndex);
				ls.setFirstExpressionIndex(leftBubble);
				ls.setSecondExpressionIndex(rightBubble);
				stepManager.applyRule(ls);
				logicState.clearBubbleSelection();
			} else {
				logicState.clearBubbleSelection();
			}

			parent.updateFrame();
		}
	}

	private void reAddSubStates(ProofState proofState) {
		Iterator<ProofState> SubstateIterator = proofState.getSubstates()
				.iterator();
		while (SubstateIterator.hasNext()) {
			boolean hasSubPanel = false;
			ProofState pf = SubstateIterator.next();
			Iterator<ProofState> subPanelsIterator = subPanels.values()
					.iterator();
			while (subPanelsIterator.hasNext()) {
				ProofState pfShown = (ProofState) subPanelsIterator.next();
				if (pf == pfShown) {
					hasSubPanel = true;
					break;
				}
			}
			if (!hasSubPanel) {
				if (pf.getHideFlag()) {
					reAddSubStates(pf);
					return;
				}

				ProofStatePanel p = new ProofStatePanel(parent, pf);
				subPanels.put(p, pf);
				this.add(p);
				this.validate();
				p.update();
			}
		}
	}

	public ProofStatePanel findParent() {
		ProofStatePanel temps = new ProofStatePanel();
		if (this.getParent() != null && getParent() instanceof ProofStatePanel) {
			temps = ((ProofStatePanel) getParent()).findParent();
		} else if (this instanceof ProofStatePanel) {
			temps = (ProofStatePanel) this;
		}
		return temps;
	}

	public void resizepsp() {
		if (getParent() != null && getParent() instanceof ProofStatePanel) {// not
																			// outer-most
																			// one
			setBounds(sideOffset, subStateStartY, getParent().getWidth() - 20,
					getParent().getHeight());
			getParent().setBounds(sideOffset, subStateStartY - 250,
					getParent().getWidth(), getHeight() + (200 * subPanels.size()));
			ProofStatePanel temps = (ProofStatePanel) getParent();
			temps.resizepsp();
		} else {
			setBounds(sideOffset, subStateStartY - 250, getWidth(),
					getHeight() + (200 * subPanels.size()));
		}

	}

	public int getHsize() {
		int i = this.findParent().getHeight();
		return i;
	}

	public int getWsize() {
		int i = this.findParent().getWidth();
		return i;
	}

	/*
	 * Recursive method for fetching the depth of this proof state panel
	 * relative to the top level proof state.
	 */
	public int getDepth() {
		return getDepth(this, 0);
	}

	public int getDepth(ProofStatePanel p, int counter) {
		if (p.getParent() == null
				|| !(p.getParent() instanceof ProofStatePanel))
			return counter;
		else {
			counter++;
			return getDepth((ProofStatePanel) p.getParent(), counter);
		}
	}

	/*
	 * Gets the absolute depth of the state panel irrespective of cleanup
	 * operations. In other words, gives the depth of the panel as if no cleanup
	 * operations were ever done.
	 */
	public int getAbsoluteDepth() {
		return logicState.getDepth();
	}

	public HashMap<ProofStatePanel, ProofState> getSubPanels() {
		return subPanels;
	}

	/**
	 * Finds the first component at (x, y) that is not a ProofStatePanel. If no
	 * component can be found returns the last (deepest) ProofStatePanel found.
	 * 
	 * @param x
	 *            - The x coordinate where the object is to be found.
	 * @param y
	 *            - The y coordinate where the object is to be found.
	 * @return The first component found at x,y or the last ProofStatePanel
	 *         found if no component can be found.
	 */
	public Component getObjectAtPoint(int x, int y) {
		Component thisComponent = getComponentAt(x, y);
		int droppedX = x;
		int droppedY = y;
		Component prevComponent = null;
		while (thisComponent instanceof ProofStatePanel
				&& thisComponent != prevComponent) {
			prevComponent = thisComponent;
			droppedX = droppedX - thisComponent.getBounds().x;
			droppedY = droppedY - thisComponent.getBounds().y;
			thisComponent = thisComponent.getComponentAt(droppedX, droppedY);
			thisComponent = thisComponent == null ? prevComponent
					: thisComponent;
		}
		return thisComponent;
	}

	public void mouseClicked(MouseEvent e) {
		MouseButton button = getMouseButton(e);
		// Middle mouse button
		if (button == MouseButton.Middle) {
			printDebug();
			return;
		}

		// If replaying, don't allow anything other than the middle mouse button
		// for debug.
		if (parent.isReplaying()) {
			parent.setHelptxt("Help: Not allowed while replaying, press the Replay button or start a new proof.");
			return;
		}

		// If the application of the rule is done elsewhere, like for example,
		// when clicking "OK" after rewriting an expression, then set the noRule
		// boolean to true in the specific part of the switch case below.
		boolean noRule = false;

		// Collect the clicked source and return if it's
		// not a JLabel (might have to be modified if we
		// want to click on other things than JLabels.
		Object source = (Object) e.getSource();
		JLabel clicked = null;
		if (source instanceof JLabel) {
			clicked = (JLabel) source;
		} else {
			return;
		}

		// Get the logical type of the source
		Type type = getComponentType(clicked);
		Modifier modifier = getModifier(e);

		// Exit if the click was a right-mouse click on something other than an
		// assumption or bubble box
		if (button == MouseButton.Right && type != Type.Assum
				&& type != Type.BubbleBox) {
			return;
		}

		// Initialise all the info needed to apply rules
		// and set default values for any information we
		// don't know yet.
		ProofState state = logicState;
		int depth = getAbsoluteDepth();
		int index = -1;
		int bubbleBoxIndex = -1;
		Expression exp = null;
		Expression newExp = null;
		Expression bubble = null;
		ArrayList<Expression> bubbleBox = null;
		String bubbleContext = null;
		String inductVariable = null;
		LogicStep ls = new LogicStep(null, -1);
		Rule rule = null;
		String node = null;

		// Select which rule to apply and gather the remaining
		// information needed to apply it. Main switch case is
		// based on what kind of logical object you clicked on.
		if (button == MouseButton.Left) {

			switch (type) {
			case Assum:
				exp = assumExpressions.get(clicked);
				index = state.getAssums().indexOf(exp);
				node = exp.getNode();

				// Special right-click case
				// if (button == MouseButton.Right) {
				// rule = Rule.DeleteAssum;
				// break;
				// }

				switch (modifier) {
				case Shift:
					rule = Rule.CopyAssum;
					break;
				case Ctrl:
					startRewriteAssum(index, clicked);
					noRule = true;
					break;
				case ShiftAndCtrl:
					rule = Rule.NotAssumSimplify;
					break;
				case AltGr:
					rule = Rule.SwitchEqualsAssum;
					break;
				default:
					if (node.equals("&")) {
						rule = Rule.AndAssum;
					} else if (node.equals("->")) {
						rule = Rule.ImpliesAssum;
					} else if (node.equals("=")) {
						rule = Rule.CreateBubble;
					} else if (node.equals("~")) {
						rule = Rule.NotAssumToGoal;
					} else if (node.equals("<->")) {
						rule = Rule.LogicalEqAssum;
					}
					break;
				}
				break;
			case AssumVar:
				// Special right-click case
				// if (button == MouseButton.Right) {
				// rule = Rule.DeleteAssumVar;
				// break;
				// }
				exp = assumVariablesExpressions.get(clicked);
				index = state.getAssumVars().indexOf(exp);
				node = exp.getNode();
				switch (modifier) {
				case AltGr:
					rule = Rule.SwitchEqualsAssumVar;
					break;
				default:
					SpecialiseDialog sd = new SpecialiseDialog(exp,
							this);
					break;
				}
				break;
			case Goal:
				exp = goalExpressions.get(clicked);
				index = state.getGoals().indexOf(exp);
				node = exp.getNode();
				switch (modifier) {
				case Shift:
					inductVariable = showInductDialog(exp);
					rule = Rule.Induction;
					break;
				case Ctrl:
					startRewriteGoal(index, clicked);
					noRule = true;
					break;
				case ShiftAndCtrl:
					rule = Rule.NotGoalSimplify;
					break;
				case AltGr:
					rule = Rule.SwitchEqualsGoal;
					break;
				default:
					if (node.equals("&")) {
						rule = Rule.AndGoal;
					} else if (node.equals("->")) {
						rule = Rule.ImpliesGoal;
					} else if (node.equals("=")) {
						rule = Rule.RefGoal;
					} else if (node.equals("~")) {
						rule = Rule.NotGoal;
					} else if (node.equals("<->")) {
						rule = Rule.LogicalEqGoal;
					}
					break;
				}
				break;
			case BubbleBox:
				bubbleBox = bblstExpressions.get(clicked);
				bubbleBoxIndex = state.getAssumbubbles().indexOf(bubbleBox);

				// Special right-click case
				if (button == MouseButton.Right) {
					rule = Rule.DeleteBubble;
					break;
				}

				switch (modifier) {
				case Shift:
					rule = Rule.CopyBubbleBox;
					break;
				case Ctrl:
					bubbleContext = showBubbleContextDialog();
					rule = Rule.BubbleContext;
					break;
				default:
					break;
				}
				break;
			case Bubble:
				// SELECT BUBBLE (EXTRACT IS DONE IN update())
				bubble = bbExpressions.get(clicked);
				JLabel bubbleBoxLabel = (JLabel) clicked.getParent()
						.getParent();
				bubbleBox = bblstExpressions.get(bubbleBoxLabel);
				index = bubbleBox.indexOf(bubble);
				bubbleBoxIndex = state.getAssumbubbles().indexOf(bubbleBox);
				rule = Rule.SelectBubble;
				break;
			default:
				System.out.println(">>>> DEBUG: THIS SHOULD NOT HAPPEN");
				rule = null;
				break;
			}
		}

		if (noRule) {
			noRule = false;
			parent.updateFrame();
		}

		// The final information has been gathered and a rule can be applied,
		// assuming that a rule has been assigned. If no rule can be applied
		// nothing will happen.
		if (rule == null) {
			parent.play(wrong);
		} else {
			if (rule != Rule.SelectBubble)
				logicState.clearBubbleSelection();
			ls = new LogicStep(rule, depth);
			ls.setFirstExpressionIndex(index);
			ls.setBubbleBoxIndex(bubbleBoxIndex);
			ls.setNewExpression(newExp);
			ls.setBubbleContext(bubbleContext);
			ls.setInductVariable(inductVariable);
			System.out.println("Rule = " + rule.toString());
			stepManager.applyRule(ls);
			parent.play(kick);
			parent.updateFrame();
		}
	}

	public void mouseDragged(MouseEvent e) {
		// Ignore event if replaying
		if (parent.isReplaying()) {
			parent.setHelptxt("Help: Not allowed while replaying, press the Replay button or start a new proof.");
			return;
		}
		isDragged = true;
		JLabel dragged = (JLabel) e.getSource();

		int newX = (e.getX() + dragged.getBounds().x - xOffset < 0) ? 0 : e
				.getX() + dragged.getBounds().x - xOffset;
		int newY = (e.getY() + dragged.getBounds().y - yOffset < 0) ? 0 : e
				.getY() + dragged.getBounds().y - yOffset;

		dragged.setLocation(newX, newY);
	}

	public void mousePressed(MouseEvent e) {
		// Ignore event if replaying
		if (parent.isReplaying()) {
			parent.setHelptxt("Help: Not allowed while replaying, press the Replay button or start a new proof.");
			return;
		}

		rightClickAssumIndex = -1;
		rightClickAssumVarIndex = -1;
		rightClickGoalIndex = -1;

		// Collect the clicked source and return if it's
		// not a JLabel (might have to be modified if we
		// want to click on other things than JLabels.
		Object source = (Object) e.getSource();

		if (source instanceof JLabel) {
			JLabel jlb = (JLabel) e.getSource();
			prevBounds = jlb.getBounds();
			xOffset = e.getX();
			yOffset = e.getY();
			((JLabel) e.getSource()).getParent().setComponentZOrder(
					(JLabel) e.getSource(), 0);
		}
		JLabel clicked = null;
		if (source instanceof JLabel) {
			clicked = (JLabel) source;
		} else {
			return;
		}

		// Get the logical type of the source
		Type type = getComponentType(clicked);

		// Initialise all the info needed to apply rules
		// and set default values for any information we
		// don't know yet.
		ProofState state = logicState;
		int depth = getAbsoluteDepth();
		int index = -1;
		int bubbleBoxIndex = -1;
		Expression exp = null;
		Expression newExp = null;
		Expression bubble = null;
		ArrayList<Expression> bubbleBox = null;
		String bubbleContext = null;
		String inductVariable = null;
		LogicStep ls = new LogicStep(null, -1);
		Rule rule = null;
		String node = null;

		if (e.isPopupTrigger()) {
			switch (type) {
			case Assum:
				exp = assumExpressions.get(clicked);
				index = state.getAssums().indexOf(exp);
				rightClickAssumIndex = index;
				node = exp.getNode();
				populateAssumPopupMenu(exp, type);
				break;
			case AssumVar:
				exp = assumVariablesExpressions.get(clicked);
				index = state.getAssumVars().indexOf(exp);
				rightClickAssumVarIndex = index;
				node = exp.getNode();
				populateAssumVarPopupMenu(exp, type);
				break;
			case Goal:
				exp = goalExpressions.get(clicked);
				index = state.getGoals().indexOf(exp);
				rightClickGoalIndex = index;
				node = exp.getNode();
				populateGoalPopupMenu(exp, type);
				break;
			}

			popup.show(e.getComponent(), e.getX(), e.getY());
			System.out.println("Past the popup");

		}
	}

	public void mouseReleased(MouseEvent e) {
		// Ignore event if replaying
		if (parent.isReplaying()) {
			parent.setHelptxt("Help: Not allowed while replaying, press the Replay button or start a new proof.");
			return;
		}
		if (!isDragged) {
			return;
		}
		isDragged = false;
		xOffset = yOffset = 0;

		// The object being dragged
		JLabel source = (JLabel) e.getSource();

		// Saving position dropped on and hiding source object
		int droppedX = source.getBounds().x + e.getX();
		int droppedY = source.getBounds().y + e.getY();
		source.setBounds(0, 0, 0, 0);

		// Initialisation of panels and types
		ProofStatePanel fromPanel = (ProofStatePanel) source.getParent();
		ProofStatePanel toPanel = fromPanel;
		Type targetType = null;
		Type sourceType = null;

		// Get the target panel and target object dropped on
		Component droppedOn = getObjectAtPoint(droppedX, droppedY);
		if (droppedOn instanceof ProofStatePanel) {
			toPanel = (ProofStatePanel) droppedOn;
		} else if (droppedOn == null) {
			droppedOn = fromPanel;
		} else {
			toPanel = (ProofStatePanel) droppedOn.getParent();
		}

		// Get the logical types of the target and source
		targetType = toPanel.getComponentType(droppedOn);
		sourceType = fromPanel.getComponentType(source);

		// Initialise all the info needed to apply rules
		// and set default values for any information we
		// don't know yet.
		ProofState sourceState = fromPanel.logicState;
		ProofState targetState = toPanel.logicState;
		int sourceDepth = fromPanel.getAbsoluteDepth();
		int targetDepth = toPanel.getAbsoluteDepth();
		int sourceIndex = -1;
		int targetIndex = -1;
		Expression sourceExp = null;
		Expression targetExp = null;
		ArrayList<Expression> sourceBbl = null;
		ArrayList<Expression> targetBbl = null;
		LogicStep ls = new LogicStep(null, -1);
		Rule rule = null;
		int complexExpIndex = -1;

		// Now we know what the target and source panel and object types we need
		// to decide what to do with them
		switch (targetType) {
		case Assum:
			targetExp = toPanel.assumExpressions.get(droppedOn);
			targetIndex = targetState.getAssums().indexOf(targetExp);

			// Assum -> Assum
			if (sourceType.equals(Type.Assum)) {
				sourceExp = fromPanel.assumExpressions.get(source);
				sourceIndex = sourceState.getAssums().indexOf(sourceExp);
				complexExpIndex = sourceState.isSimplifiable(targetState,
						sourceIndex, targetIndex);

				// Simplify (e.g. [A->B] dropped on [A] becomes [B])
				if (complexExpIndex > 0) {
					rule = Rule.AssumSimplify;
				}
				// Contradiction, (e.g. [~A] dropped on [A] becomes [FALSE])
				else if (sourceState.assumContradictionApplies(sourceIndex,
						targetIndex)) {
					rule = Rule.NotAssumContradiction;
				}
			}
			break;
		case Goal:
			targetExp = toPanel.goalExpressions.get(droppedOn);
			targetIndex = targetState.getGoals().indexOf(targetExp);

			// Assum -> Goal
			if (sourceType.equals(Type.Assum)) {
				sourceExp = fromPanel.assumExpressions.get(source);
				sourceIndex = sourceState.getAssums().indexOf(sourceExp);
				if (sourceState.isSolvable(targetState, sourceIndex,
						targetIndex)) {
					rule = Rule.Solve;
					// Simplify (e.g. [A->B] dropped on [B] becomes Goal [A])
				} else if (sourceState.assumImpliesGoalShortcutApplies(
						targetState, sourceIndex, targetIndex)) {
					rule = Rule.AssumImpliesGoalShortcut;
				}
			} else if (sourceType.equals(Type.AssumVar)) {
				sourceExp = fromPanel.assumVariablesExpressions.get(source);
				sourceIndex = sourceState.getAssumVars().indexOf(sourceExp);
				if (sourceExp.canBeSpecialised(sourceExp)
						&& logicState
								.canSpecialiseThrowIn(sourceIndex, targetIndex)) {
					rule = Rule.SolveVar;
				}
			}
			break;
		case BubbleBox:
			targetBbl = toPanel.bblstExpressions.get(droppedOn);
			targetIndex = targetState.getAssumbubbles().indexOf(targetBbl);

			// Bubble -> Bubble
			if (sourceType.equals(Type.BubbleBox)) {
				sourceBbl = fromPanel.bblstExpressions.get(source);
				sourceIndex = sourceState.getAssumbubbles().indexOf(sourceBbl);
				if (sourceState.isMergeable(targetState, sourceIndex,
						targetIndex)) {
					rule = Rule.MergeBubble;
				}
			}
			break;
		case Panel:
			if (targetDepth == sourceDepth) {
				break;
			}

			// Assum -> Panel
			if (sourceType.equals(Type.Assum)) {
				sourceExp = fromPanel.assumExpressions.get(source);
				sourceIndex = sourceState.getAssums().indexOf(sourceExp);
				rule = Rule.MoveAssum;
			} else if (sourceType.equals(Type.AssumVar)) {
				sourceExp = fromPanel.assumVariablesExpressions.get(source);
				sourceIndex = sourceState.getAssumVars().indexOf(sourceExp);
				rule = Rule.MoveAssumVar;
			}
			// Bubble -> Panel
			else if (sourceType.equals(Type.BubbleBox)) {
				sourceBbl = fromPanel.bblstExpressions.get(source);
				sourceIndex = sourceState.getAssumbubbles().indexOf(sourceBbl);
				rule = Rule.MoveBubble;
			}
			break;
		default:
			rule = null;
			break;
		}

		sourceState.clearBubbleSelection();
		targetState.clearBubbleSelection();

		// The final information has been gathered and a rule can be applied,
		// assuming that a rule has been assigned. If no rule can be applied
		// the object will reset to its old position.
		if (rule == null) {
			source.setBounds(prevBounds);
			parent.play(wrong);
		} else {
			ls = new LogicStep(rule, sourceDepth);
			ls.setTargetDepth(targetDepth);
			ls.setFirstExpressionIndex(sourceIndex);
			ls.setSecondExpressionIndex(targetIndex);
			ls.setComplexIndex(complexExpIndex);
			stepManager.applyRule(ls);
			parent.play(cheer);
			parent.updateFrame();
		}

	}

	private boolean showExtractBubbleDialog(Expression leftBubble,
			Expression rightBubble) {
		int n = JOptionPane.showConfirmDialog(null, "Create Equation:  "
				+ leftBubble.toString() + "=" + rightBubble.toString() + "  ?",
				"Extract Equation", JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.YES_OPTION) {
			return true;
		} else if (n == JOptionPane.NO_OPTION) {
			return false;
		}
		return false;
	}

	private String showBubbleContextDialog() {
		String s = (String) JOptionPane.showInputDialog(this,
				"Please input Expressioin using ? for hole",
				"Apply context to bubble", JOptionPane.PLAIN_MESSAGE, null,
				null, null);
		return s;
	}

	private String showInductDialog(Expression clickedGoal) {
		System.out.println(clickedGoal);
		ArrayList<String> expVars = clickedGoal.getTermVars();
		if (expVars.isEmpty())
			return null;
		ArrayList<String> expVarsNoDup = new ArrayList<String>();

		// remove duplicates
		for (int i = 0; i < expVars.size(); i++) {
			String str = expVars.get(i);
			if (!expVarsNoDup.contains(str))
				expVarsNoDup.add(str);
		}

		Object selectedValue;
		if (expVarsNoDup.size() > 1) {
			selectedValue = JOptionPane.showInputDialog(null,
					"Choose the variable to perform induction on",
					"Performing induction", JOptionPane.INFORMATION_MESSAGE,
					null, expVarsNoDup.toArray(), null);
		}

		else
			selectedValue = expVarsNoDup.get(0);
		return (String) selectedValue;
	}

	private void startRewriteAssum(int index, JLabel clicked) {
		int assumNum = index;
		// RW-select AlterExp
		if (parent.getrwframe().select) {

			Expression rwexp = logicState.getAssum(assumNum);
			parent.getrwframe().setsltExp(rwexp, "down");

		}
		// RW-select TargetExp
		else {
			MyLabel myclicked = (MyLabel) clicked;
			myclicked.fullText.setForeground(Color.BLUE);
			parent.rwIn.setVisible(true);
			Expression rwexp = logicState.getAssum(assumNum);
			parent.setRWexp(rwexp, "up");
			parent.getrwframe().isassum = true;
			parent.getrwframe().origExpIndex = assumNum;
			parent.getrwframe().proofState = logicState;
			parent.getrwframe().targetObject = myclicked;
		}
	}

	private void startRewriteGoal(int index, JLabel clicked) {
		int goalNum = index;
		// RW-select AlterExp
		if (parent.getrwframe().select) {
			Expression rwexp = logicState.getGoal(goalNum);
			parent.getrwframe().setsltExp(rwexp, "down");
		}
		// RW-select TargetExp
		else {
			MyLabel myclicked = (MyLabel) clicked;
			myclicked.fullText.setForeground(Color.BLUE);
			parent.rwIn.setVisible(true);
			parent.getrwframe().isassum = false;
			parent.getrwframe().origExpIndex = goalNum;
			parent.getrwframe().proofState = logicState;
			parent.getrwframe().targetObject = myclicked;
			Expression rwexp = logicState.getGoal(goalNum);
			parent.setRWexp(rwexp, "up");
		}
	}

	private void printDebug() {
		System.out.println("+++++++++++++++++++++++++++");
		System.out.print("Substate index: " + getDepth(this, 0));
		if (logicState != null && parent.getGameList().size() > 0) {
			System.out.print(", ProofState index: " + logicState.getDepth());
			System.out.print(", ProofStateList: "
					+ logicState.getProofStateList().size());
			System.out.println(", GameList: " + parent.getGameList().size());
			System.out.println("+++++++++++++++++++++++++++");
			System.out.println(" +++ logicState: ");
			System.out.println(logicState);
			System.out.println("+++++++++++++++++++++++++++");
			System.out.println(logicState.getProofStateList().contains(
					parent.getCurrentDisplayPanel().logicState));
			System.out.println("+++++++++++++++++++++++++++");

			// System.out.println(" +++ Currently shown state:");
			// System.out.println(parent.getCurrentlyShownState().logicState);
			System.out.println(" +++ ProofStateList:");
			System.out.println(logicState.getProofStateList());
			System.out.println("+++++++++++++++++++++++++++");
			System.out.println(logicState.debugBubbleSelection());
			System.out.println("+++++++++++++++++++++++++++");
			System.out.println(" +++ Hide flags:");
			for (int i = 0; i < logicState.getProofStateList().size() - 1; i++) {
				System.out.print(logicState.getProofStateList().get(i)
						.getHideFlag()
						+ ", ");
			}
			System.out.print(logicState.getProofStateList()
					.get(logicState.getProofStateList().size() - 1)
					.getHideFlag());
			System.out.println();

		}
		System.out.println("+++++++++++++++++++++++++++");
	}

	/**
	 * Records the current state of the proof. Used for undo.
	 */
	public void record() {
		GameState gameState = new GameState();
		ArrayList<ProofState> proofStateList = logicState.getProofStateList();
		ProofState displayState = new ProofState();
		displayState = logicState;

		ArrayList<GameState> gameList = parent.getGameList();
		GameState lastStep = gameList.get(gameList.size() - 1);
		if (lastStep.getDisplayState().toString()
				.equals(displayState.toString())
				&& lastStep.getProofStateList().toString()
						.equals(proofStateList.toString())) {
			System.out.println("[GAMESTATE]: No recording, identical step.");
			return;
		}
		System.out.println("[GAMESTATE]: Recording step.");

		gameState.setProofStateList(proofStateList);
		gameState.setDisplayStateIndex(logicState.getDepth());
		parent.getGameList().add(gameState);
	}

	public String getTextToShow(Expression e) {
		return e.getNode();
	}

	private Type getComponentType(Component component) {
		Type type = null;
		if (assumExpressions.containsKey(component)) {
			type = Type.Assum;
		} else if (assumVariablesExpressions.containsKey(component)) {
			type = Type.AssumVar;
		} else if (goalExpressions.containsKey(component)) {
			type = Type.Goal;
		} else if (bblstExpressions.containsKey(component)) {
			type = Type.BubbleBox;
		} else if (bbExpressions.containsKey(component)) {
			type = Type.Bubble;
		} else if (component instanceof ProofStatePanel) {
			type = Type.Panel;
		} else {
			if (component != null)
				System.out.println(">>> DEBUG : component is a "
						+ component.getClass());
			else
				System.err.println(">>> DEBUG : component is null");
			type = Type.NonLogical;
		}
		return type;
	}
	
	public int getAssumSize() {
		return assumLabelPositions.size();
	}
	
	public int getAssumVarSize() {
		return assumVarLabelPositions.size();
	}
	
	public int getGoalSize() {
		return goalLabelPositions.size();
	}

	private Modifier getModifier(MouseEvent e) {
		if (e.isShiftDown() && !e.isControlDown()) {
			return Modifier.Shift;
		} else if (e.isControlDown() && !e.isShiftDown()) {
			return Modifier.Ctrl;
		} else if (e.isControlDown() && e.isShiftDown()) {
			return Modifier.ShiftAndCtrl;
		} else if (e.isAltGraphDown()) {
			return Modifier.AltGr;
		} else {
			return Modifier.None;
		}
	}

	private MouseButton getMouseButton(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			return MouseButton.Left;
		} else if (e.getButton() == MouseEvent.BUTTON2) {
			return MouseButton.Middle;
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			return MouseButton.Right;
		}
		return null;
	}

	public boolean isDone() {
		boolean result = logicState.getGoals().isEmpty();

		if (!subPanels.isEmpty()) {
			Iterator<ProofStatePanel> subPanelss = subPanels.keySet()
					.iterator();
			while (subPanelss.hasNext() && subPanelss != null) {
				ProofStatePanel p = subPanelss.next();
				result = result && p.isDone();
			}
		}
		return result;
	}

	public boolean noGoalsAndNoSubstates() {
		return logicState.getGoals().isEmpty()
				&& logicState.getSubstates().isEmpty();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
//		Object clicked1 = (Object) e.getSource();
//		if (clicked1 instanceof JLabel) {
//			JLabel clicked = (JLabel) e.getSource();
//			if (goalExpressions.containsKey(clicked)) {
//				parent.sethelpmark(goalIcon);
//				if (parent.getrwframe().select) {
//					// parent.setHelpIco();
//					parent.setHelptxt("Help: [Ctrl-Click]: Select Alter Expression [Shift-Click]: Induction ");
//
//				} else {
//					parent.setHelptxt("Help: [Ctrl-Click]: Select ReWriting Target [Shift-Click]: Induction ");
//				}
//
//			} else if (assumExpressions.containsKey(clicked)) {
//				parent.sethelpmark(footballIcon);
//				if (parent.getrwframe().select) {
//					parent.setHelptxt("Help: [Ctrl-Click]: Select Alter Expression [Shift-Click]: Copy ");
//				} else {
//					parent.setHelptxt("Help: [Ctrl-Click]: Select ReWriting Target [Shift-Click]: Copy ");
//				}
//			} else if (bbExpressions.containsKey(clicked)) {
//				parent.sethelpmark(sbb);
//				parent.setHelptxt("Help: [Ctrl-Click]: Extract-Equation Choosing  ");
//			} else if (bblstExpressions.containsKey(clicked)) {
//				parent.sethelpmark(bbs);
//				parent.setHelptxt("Help: [Ctrl-Click]: Context Application [Shift-Click]:Induction [Drag]: Merge/Move");
//			}
//		} else if (clicked1 instanceof ProofStatePanel) {
//			parent.sethelpmark(helpmark);
//			parent.setHelptxt("Help: move mouse over objects see allowed operation");
//		} else {
//			parent.sethelpmark(helpmark);
//			parent.setHelptxt("Help: move mouse over objects see allowed operation");
//		}
	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	public String toString() {
		return logicState.toString();
	}

	/**
	 * Reverts to the last game state stored with record(). The gamestate list
	 * is stored in the DisplayFrame.
	 * 
	 * @return The top level (visible) ProofState as specified in the new
	 *         (previous) GameState.
	 */
	public ProofState undo() {
		ProofState newState = new ProofState();
		newState = logicState;

		ArrayList<GameState> gameList = parent.getGameList();
		if (gameList.size() >= 2) {
			GameState newGameState = gameList.get(gameList.size() - 2);
			int i = newGameState.getDisplayStateIndex();
			newState.setProofStateList(newGameState.getProofStateList());
			newState = newState.getProofStateList().get(i);
			gameList.remove(gameList.size() - 1);
		}
		return newState;
	}

	public boolean arequal(ProofState a, ProofState b) {
		if (a.toString().equals(b.toString())) {
			return true;
		} else {
			return false;
		}
	}

	public void populateAssumPopupMenu(Expression e, Type type) {
		popup = new JPopupMenu();
		ActionListener actionListener = new AssumPopupActionListener();

		if (e.getNode().equals("->")) {
			JMenuItem menu1Item = new JMenuItem("Simplify Imp. Assumption");
			menu1Item.addActionListener(actionListener);
			popup.add(menu1Item);
		} else if (e.getNode().equals("&")) {
			JMenuItem menu2Item = new JMenuItem("Simplify And Assumption");
			menu2Item.addActionListener(actionListener);
			popup.add(menu2Item);
		} else if (e.getNode().equals("~")) {
			JMenuItem menu3Item = new JMenuItem("Try to Prove Contradiction");
			menu3Item.addActionListener(actionListener);
			popup.add(menu3Item);
			JMenuItem menu4Item = new JMenuItem("Simplify Negations");
			menu4Item.addActionListener(actionListener);
			popup.add(menu4Item);
		} else if (e.getNode().equals("=")) {
			JMenuItem menu6Item = new JMenuItem("Switch Over Assumption");
			menu6Item.addActionListener(actionListener);
			popup.add(menu6Item);
			JMenuItem menu8Item = new JMenuItem("Start Bubbling");
			menu8Item.addActionListener(actionListener);
			popup.add(menu8Item);
		} else if (e.getNode().equals("+")) {

		}

		popup.addSeparator();

		JMenuItem menu7Item = new JMenuItem("Copy Assumption");
		menu7Item.addActionListener(actionListener);
		popup.add(menu7Item);
		JMenuItem menu9Item = new JMenuItem("Rewrite Assumption");
		menu9Item.addActionListener(actionListener);
		popup.add(menu9Item);

	}

	public void populateAssumVarPopupMenu(Expression e, Type type) {

		popup = new JPopupMenu();

		ActionListener actionListener = new AssumVarPopupActionListener();
		if (e.getNode().equals("->")) {

		} else if (e.getNode().equals("&")) {

		} else if (e.getNode().equals("~")) {

		} else if (e.getNode().equals("=")) {
			JMenuItem menu6Item = new JMenuItem("Switch Over AssumptionVar");
			menu6Item.addActionListener(actionListener);
			popup.add(menu6Item);
		} else if (e.getNode().equals("+")) {

		}

		JMenuItem menu6Item = new JMenuItem("Manually Specialise Variables");
		menu6Item.addActionListener(actionListener);
		popup.add(menu6Item);

		popup.addSeparator();

		JMenuItem menu7Item = new JMenuItem("Copy AssumptionVar");
		menu7Item.addActionListener(actionListener);
		popup.add(menu7Item);
	}

	public void populateGoalPopupMenu(Expression e, Type type) {
		popup = new JPopupMenu();
		ActionListener actionListener = new GoalPopupActionListener();

		if (e.getNode().equals("->")) {
			JMenuItem menu1Item = new JMenuItem("Simplify Imp. Goal");
			menu1Item.addActionListener(actionListener);
			popup.add(menu1Item);
		} else if (e.getNode().equals("&")) {
			JMenuItem menu2Item = new JMenuItem("Simplify And Goal");
			menu2Item.addActionListener(actionListener);
			popup.add(menu2Item);
		} else if (e.getNode().equals("~")) {
			JMenuItem menu3Item = new JMenuItem("Falsify Goal");
			menu3Item.addActionListener(actionListener);
			popup.add(menu3Item);
			JMenuItem menu4Item = new JMenuItem("Simplify Goal Negations");
			menu4Item.addActionListener(actionListener);
			popup.add(menu4Item);
		} else if (e.getNode().equals("=")) {
			JMenuItem menu6Item = new JMenuItem("Equals Switch Goal Rule");
			menu6Item.addActionListener(actionListener);
			popup.add(menu6Item);
		} else if (e.getNode().equals("+")) {

		}

		popup.addSeparator();

		JMenuItem menu8Item = new JMenuItem("Start Induction");
		menu8Item.addActionListener(actionListener);
		popup.add(menu8Item);
		JMenuItem menu9Item = new JMenuItem("Rewrite Goal");
		menu9Item.addActionListener(actionListener);
		popup.add(menu9Item);

	}

	class AssumPopupActionListener implements ActionListener {
		public void actionPerformed(ActionEvent actionEvent) {
			System.out.println("Selected: " + actionEvent.getActionCommand());

			ProofState state = logicState;
			int depth = getAbsoluteDepth();
			int index = rightClickAssumIndex;
			int bubbleBoxIndex = -1;
			Expression newExp = null;
			Expression bubble = null;
			ArrayList<Expression> bubbleBox = null;
			String bubbleContext = null;
			String inductVariable = null;
			LogicStep ls = new LogicStep(null, -1);
			Rule rule = rulesToString.get(actionEvent.getActionCommand());
			String node = null;

			if (rightClickAssumIndex != -1) {
				ls = new LogicStep(rule, depth);
				ls.setFirstExpressionIndex(index);
				ls.setBubbleBoxIndex(bubbleBoxIndex);
				ls.setNewExpression(newExp);
				ls.setBubbleContext(bubbleContext);
				ls.setInductVariable(inductVariable);
				stepManager.applyRule(ls);
				parent.play(kick);
				parent.updateFrame();
			}
		}
	}

	class AssumVarPopupActionListener implements ActionListener {
		public void actionPerformed(ActionEvent actionEvent) {
			System.out.println("Selected: " + actionEvent.getActionCommand());

			if (actionEvent.getActionCommand().equals(
					"Manually Specialise Variables")) {
				// SpecialiseDialog sd = new
				// SpecialiseDialog(assumVariablesExpressions.get(rightClickAssumVarIndex),
				// logicState
				// );
			} else {

				ProofState state = logicState;
				int depth = getAbsoluteDepth();
				int index = rightClickAssumVarIndex;
				int bubbleBoxIndex = -1;
				Expression newExp = null;
				Expression bubble = null;
				ArrayList<Expression> bubbleBox = null;
				String bubbleContext = null;
				String inductVariable = null;
				LogicStep ls = new LogicStep(null, -1);
				Rule rule = rulesToString.get(actionEvent.getActionCommand());
				String node = null;

				if (rightClickAssumVarIndex != -1) {
					ls = new LogicStep(rule, depth);
					ls.setFirstExpressionIndex(index);
					ls.setBubbleBoxIndex(bubbleBoxIndex);
					ls.setNewExpression(newExp);
					ls.setBubbleContext(bubbleContext);
					ls.setInductVariable(inductVariable);
					stepManager.applyRule(ls);
					parent.play(kick);
					parent.updateFrame();
				}
			}
		}
	}

	class GoalPopupActionListener implements ActionListener {
		public void actionPerformed(ActionEvent actionEvent) {
			System.out.println("Selected: " + actionEvent.getActionCommand());

			ProofState state = logicState;
			int depth = getAbsoluteDepth();
			int index = rightClickGoalIndex;
			int bubbleBoxIndex = -1;
			Expression newExp = null;
			Expression bubble = null;
			ArrayList<Expression> bubbleBox = null;
			String bubbleContext = null;
			String inductVariable = null;
			LogicStep ls = new LogicStep(null, -1);
			Rule rule = rulesToString.get(actionEvent.getActionCommand());
			String node = null;

			System.out.println(rule);
			if (rule.toString().equals(Rule.Induction.toString())) {
				inductVariable = showInductDialog(state.getGoals().get(
						rightClickGoalIndex));
				System.out.println("whiiiiti");
			}

			if (rightClickGoalIndex != -1) {
				ls = new LogicStep(rule, depth);
				ls.setFirstExpressionIndex(index);
				ls.setBubbleBoxIndex(bubbleBoxIndex);
				ls.setNewExpression(newExp);
				ls.setBubbleContext(bubbleContext);
				ls.setInductVariable(inductVariable);
				stepManager.applyRule(ls);
				parent.play(kick);
				parent.updateFrame();
			}
		}
	}

}