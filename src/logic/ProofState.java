package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import parser.MyExpressionParser;

public class ProofState implements Cloneable {

	private ArrayList<Expression> assums;
	private ArrayList<ArrayList<Expression>> assumbubbles;
	private ArrayList<Expression> assumVars;
	private ArrayList<Expression> goals;
	private ArrayList<ProofState> substates;
	private ProofState parent;

	private Expression selectedBubbleL = null;
	private Expression selectedBubbleR = null;
	private int lastBubbleBox = -1;

	private static ArrayList<ProofState> proofStateList = new ArrayList<ProofState>();

	private boolean hide; // Flag to let the display frame (application window)
							// know if it should show this proofstate or not.

	public ProofState() {
		assums = new ArrayList<Expression>();
		assumbubbles = new ArrayList<ArrayList<Expression>>();
		assumVars = new ArrayList<Expression>();
		goals = new ArrayList<Expression>();
		substates = new ArrayList<ProofState>();
		selectedBubbleL = null;
		selectedBubbleR = null;
		parent = null;
	}

	public ProofState(
			// ArrayList<Expression> assums,
			ArrayList<Expression> assums, ArrayList<Expression> goals,
			ArrayList<ProofState> substates) {
		this.assums = assums;
		this.goals = goals;
		this.substates = substates;
		this.selectedBubbleL = null;
		this.selectedBubbleR = null;
		parent = null;
		Iterator<ProofState> i = substates.iterator();
		while (i.hasNext()) {
			i.next().setParent(this);
		}
	}
	
	/*
	 * Create a new ProofState based on non-parsed string. The string will
	 * be parsed and, if successful, will create the ProofState with the
	 * now parsed expression as it's goal. 
	 */
	public ProofState( String expressionString ) throws Exception {
		this();
		this.goals.add(MyExpressionParser.parse(expressionString));
	}

	public Object cloneps() {
		ProofState o = null;
		try {
			o = (ProofState) super.clone();
		} catch (CloneNotSupportedException e) {
			System.out.println(e.toString());
		}
		o.assums = CloneExpAL(assums);
		o.assumVars = CloneExpAL(assumVars);
		o.goals = CloneExpAL(goals);
		o.substates = ClonePrSAL(substates);
		o.assumbubbles = DeepCloneAL(assumbubbles);

		return o;
	}

	public ArrayList<Expression> CloneExpAL(ArrayList<Expression> src) {

		ArrayList<Expression> dest = new ArrayList<Expression>();

		for (int i = 0; i < src.size(); i++) {
			Expression e = src.get(i);
			dest.add((Expression) e.clonexp());
		}

		return dest;
	}

	public ArrayList<ProofState> ClonePrSAL(ArrayList<ProofState> src) {
		ArrayList<ProofState> dest = new ArrayList<ProofState>();

		for (int i = 0; i < src.size(); i++) {
			ProofState p = src.get(i);
			dest.add((ProofState) p.cloneps());// dest.set(i,p);
		}

		return dest;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<ArrayList<Expression>> DeepCloneAL(
			ArrayList<ArrayList<Expression>> src) {
		ArrayList dest = new ArrayList<ArrayList<Expression>>();

		for (int i = 0; i < src.size(); i++) {
			dest.add(CloneExpAL(src.get(i)));
		}

		return dest;
	}

	public void setParent(ProofState parent) {
		this.parent = parent;
	}

	public ProofState getParent() {
		return parent;
	}

	public void addAssum(Expression e) {
		assums.add(e);
	}

	public void removeAssum(Expression e) {
		assums.remove(e);
	}

	public void addAssumBubble(ArrayList<Expression> e) {
		assumbubbles.add(e);
	}

	public void removeAssumBubble(ArrayList<Expression> e) {
		assumbubbles.remove(e);
	}

	public void setAssumBubble(ArrayList<ArrayList<Expression>> assumbubble) {
		this.assumbubbles = assumbubble;
	}

	public boolean movebb(int bubbleIndex, ProofState p) {
		ArrayList<Expression> a = assumbubbles.get(bubbleIndex);
		removeAssumBubble(a);
		p.addAssumBubble(a);
		return true;
	}

	public void addGoal(Expression e) {
		goals.add(e);
	}

	public void removeGoal(Expression e) {
		goals.remove(e);
	}

	private void addSubstate(ProofState p) {
		p.setParent(this);
		substates.add(p);
		proofStateList.add(p);
	}

	public void removeSubstate(ProofState p) {
		substates.remove(p);
	}

	public ArrayList<Expression> getAssums() {
		return assums;
	}

	public void setAssums(ArrayList<Expression> assums) {
		this.assums = assums;
	}

	public Expression getAssum(int i) {
		return assums.get(i);
	}

	public ArrayList<Expression> getGoals() {
		return goals;
	}

	public void setGoals(ArrayList<Expression> goals) {
		this.goals = goals;
	}

	public Expression getGoal(int i) {
		return goals.get(i);
	}

	public ArrayList<ProofState> getSubstates() {
		return substates;
	}

	public void setSubstates(ArrayList<ProofState> substates) {
		this.substates = substates;
	}

	public ProofState getSubstate(int i) {
		return substates.get(i);
	}

	public boolean copyAssum(int i) {
		Expression newAssum = new Expression(assums.get(i).clonexp());
		addAssum(newAssum);
		return true;
	}
	

	public boolean throwInAssum(Expression assum) {
		addAssum(assum);
		return true;
	}

	public boolean throwInAssumVar(Expression assumVar) {
		addAssumVar(assumVar);
		return true;
	}

	public boolean copyAssumVar(int i) {
		Expression newAssumVar = new Expression(assumVars.get(i).clonexp());
		addAssumVar(newAssumVar);
		return true;
	}

	// Thrown-in assumptions stuff
	public void addAssumVar(Expression e) {
		assumVars.add(e);
	}

	public void removeAssumVar(Expression e) {
		assumVars.remove(e);
	}

	public ArrayList<Expression> getAssumVars() {
		return assumVars;
	}

	public Expression getAssumVar(int i) {
		return assumVars.get(i);
	}

	public void setAssumVars(ArrayList<Expression> assumVars) {
		this.assumVars = assumVars;
	}

	public void resetAssumVars() {
		assumVars = new ArrayList<Expression>();
	}

	public boolean solve(ProofState target, int i, int j) {
		removeAssum(assums.get(i));
		target.removeGoal(target.goals.get(j));
		return true;
	}

	public boolean solveVar(ProofState target, int i, int j) {
		removeAssumVar(assumVars.get(i));
		target.removeGoal(target.goals.get(j));
		return true;
	}

	/**
	 * Used to determine if a given goal is satisfied with a given assumption.
	 * The goal is satisfied if the assumption matches the goal exactly or the
	 * assumption is a false assumption (!F)
	 * 
	 * @param target
	 *            The proofstate the rule is applied within
	 * @param i
	 *            The position of the assumption in the assumption arraylist
	 * @param j
	 *            The position of the goal in the goal arraylist
	 * @return True if the assumption matches the goal or the assumption equals
	 *         '!F'
	 */
	public boolean isSolvable(ProofState target, int i, int j) {
		Expression assum = assums.get(i);
		Expression goal = target.goals.get(j);
		boolean isFalse = assum.toString().equals("!F");
		boolean isMatch = assum.toString().equals(goal.toString());
		return isFalse || isMatch;
	}

	/**
	 * Splits A & B assumption into A assumption and B assumption
	 * 
	 * @param i
	 *            Index of assumption
	 */
	public boolean andAssumRule(int i) {
		if (i >= getAssums().size()) {
			return false;
		}

		Expression theAssum = this.getAssum(i);
		if (theAssum.getNode().equals("&")) {
			removeAssum(theAssum);
			addAssum(theAssum.getLeft());
			addAssum(theAssum.getRight());
			return true;
		}
		return false;
	}

	/**
	 * Splits A & B goal into A goal and B goal
	 * 
	 * @param i
	 *            Index of goal
	 */
	public boolean andGoalRule(int i) {
		if (i >= getGoals().size()) {
			return false;
		}

		Expression theGoal = getGoal(i);
		if (theGoal.getNode().equals("&")) {
			removeGoal(theGoal);
			addGoal(theGoal.getLeft());
			addGoal(theGoal.getRight());
			return true;
		}
		return false;
	}

	public boolean impliesAssumRule(int i) {
		if (i >= getAssums().size()) {
			return false;
		}

		Expression theAssum = this.getAssum(i);
		if (theAssum.getNode().equals("->")) {
			removeAssum(theAssum);
			ProofState newState = new ProofState();
			// proofStateList.add(newState);
			newState.addAssum(theAssum.getRight());
			newState.setGoals(this.getGoals());
			newState.setSubstates(this.getSubstates());
			resetGoals();
			addGoal(theAssum.getLeft());
			setSubstates(getSubstates());
			resetSubstates();
			addSubstate(newState);
			return true;
		}
		return false;
	}

	public boolean impliesGoalRule(int i) {
		if (i >= getGoals().size()) {
			return false;
		}

		Expression theGoal = getGoal(i);
		if (theGoal.getNode().equals("->") && substates.isEmpty()) {
			removeGoal(theGoal);
			ProofState newState = new ProofState();
			newState.addAssum(theGoal.getLeft());
			newState.addGoal(theGoal.getRight());
			addSubstate(newState);
			return true;
		}
		return false;
	}

	public boolean refGoalRule(int i) {
		if (i >= getGoals().size()) {
			return false;
		}

		Expression theGoal = getGoal(i);
		if (theGoal.getNode().equals("=")
				&& theGoal.getLeft().toString()
						.equals(theGoal.getRight().toString())) {
			removeGoal(theGoal);
			return true;
		}
		return false;
	}

	/**
	 * Switches over goal with equals as its node ie 'a=b' becomes 'b=a'
	 * 
	 * @param Index
	 *            of goal in goals array list
	 * @return true if rule is successful, false otherwise
	 */
	public boolean switchEqualsGoal(int i) {
		if (i >= getGoals().size()) {
			return false;
		}
		Expression goal = getGoal(i);
		if (goal.getNode().equals("=")) {
			Expression newGoal = new Expression(goal.getRight(), "=",
					goal.getLeft());
			goals.remove(goal);
			goals.add(newGoal);
			return true;
		}
		return false;
	}

	/**
	 * Switches over assumption with equals as its node ie 'a=b' becomes 'b=a'
	 * 
	 * @param Index
	 *            of assumption in assumption array list
	 * @return true if rule is successful, false otherwise
	 */
	public boolean switchEqualsAssum(int i) {
		if (i >= getAssums().size()) {
			return false;
		}
		Expression assum = getAssum(i);
		if (assum.getNode().equals("=")) {
			Expression newAssum = new Expression(assum.getRight(), "=",
					assum.getLeft());
			assums.remove(assum);
			assums.add(newAssum);
			return true;
		}
		return false;
	}

	/**
	 * Switches over assumptionVariable with equals as its node ie 'a=b' becomes
	 * 'b=a'
	 * 
	 * @param Index
	 *            of assumptionVariable in assumptionVariable array list
	 * @return true if rule is successful, false otherwise
	 */
	public boolean switchEqualsAssumVar(int i) {
		if (i >= getAssumVars().size()) {
			return false;
		}
		Expression assumVar = getAssumVar(i);
		if (assumVar.getNode().equals("=")) {
			Expression newAssumVar = new Expression(assumVar.getRight(), "=",
					assumVar.getLeft());
			assumVars.remove(assumVar);
			assumVars.add(newAssumVar);
			return true;
		}
		return false;
	}

	/**
	 * When an assumption is dropped on a matching goal
	 * 
	 * @param i
	 *            index of assumption in the assums arraylist
	 * @param j
	 *            index of goal in the goals arraylist
	 */
	// public void goalAssumSatisfyRule(int i, int j) {
	// if (i >= getAssums().size() || j >= getGoals().size()) {
	// return;
	// }
	//
	// Expression assum = getAssum(i);
	// Expression goal = getGoal(j);
	//
	// removeAssum(assum);
	// removeGoal(goal);
	//
	// }

	/**
	 * Removes excess NOT rules from assumptions (eg simplifies ~(~(A)) to A)
	 * 
	 * @param Position
	 *            of assumption in assumption arraylist
	 */
	public boolean notAssumSimplifyRule(int i) {
		if (i >= getAssums().size()) {
			return false;
		}

		Expression theAssum = getAssum(i);
		Expression finalAssumption;
		int firstCount = 0;

		while (theAssum.getNode().equals("~")) {
			firstCount++;
			theAssum = theAssum.getLeft();
		}

		if (firstCount > 1) {
			if (firstCount % 2 == 0) {
				finalAssumption = theAssum;
			} else {
				finalAssumption = new Expression(theAssum, "~", null);
			}
			removeAssum(getAssum(i));
			addAssum(finalAssumption);
			return true;
		}
		return false;
	}

	/**
	 * Removes excess NOT rules from goals (eg simplifies ~(~(A)) to A)
	 * 
	 * @param Position
	 *            of goal in goal arraylist
	 */
	public boolean notGoalSimplifyRule(int i) {
		if (i >= getGoals().size()) {
			return false;
		}

		Expression theGoal = getGoal(i);
		Expression finalGoal;
		int firstCount = 0;

		while (theGoal.getNode().equals("~")) {
			firstCount++;
			theGoal = theGoal.getLeft();
		}

		if (firstCount > 1) {
			if (firstCount % 2 == 0) {
				finalGoal = theGoal;
			} else {
				finalGoal = new Expression(theGoal, "~", null);
			}
			removeGoal(getGoal(i));
			addGoal(finalGoal);
			return true;
		}
		return false;

	}

	/**
	 * if you drag an assumption and its negation together, you cause a
	 * contradiction. This produces a 'False' assumption which can be used to
	 * satisfy any goal
	 * 
	 * @Param targetState the target state the assumption was dropped on
	 * @Param i index of dragged assumption in assums arraylist
	 * @Param j index of dropped on assumption in assums arraylist
	 */
	public boolean notAssumContradictionRule(ProofState targetState, int i,
			int j) {
		if (i >= getAssums().size() || j >= targetState.getAssums().size()) {
			return false;
		}

		Expression firstAssum = getAssum(i);
		Expression secondAssum = targetState.getAssum(j);
		Expression falseAssum = new Expression("!F");

		removeAssum(firstAssum);
		targetState.removeAssum(secondAssum);
		targetState.addAssum(falseAssum);
		return true;
	}

	/**
	 * Negation of an assumption- assumption becomes a goal. All substates are
	 * removed
	 * 
	 * @param Position
	 *            of assumption in assumption arraylist
	 */
	public boolean notAssumToGoalRule(int i) {
		if (i >= getAssums().size()) {
			return false;
		}
		Expression theAssum = getAssum(i);
		Expression left = theAssum.getLeft();
		if (theAssum.getNode().equals("~")) {
			Expression newGoal = new Expression(left);
			removeAssum(theAssum);
			resetGoals();
			resetSubstates();
			addGoal(newGoal);
			return true;
		}
		return false;
	}

	/**
	 * A NOT A goal becomes an A assumption and a false Goal
	 * 
	 * @param Position
	 *            of goal in goal array list
	 */
	public boolean notGoalRule(int i) {
		if (i >= getGoals().size()) {
			return false;
		}
		Expression theGoal = getGoal(i);

		if (theGoal.getNode().equals("~") && substates.isEmpty()) {
			removeGoal(theGoal);
			ProofState newState = new ProofState();
			newState.addAssum(theGoal.getLeft());
			Expression falseGoal = new Expression(null, "False", null);
			newState.addGoal(falseGoal);
			addSubstate(newState);
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @return returns true if the assumption contradiction rule applies. ie one
	 *         expression contains an even number of ~ and one contains an odd
	 *         number. NOTE: Only works when both expressions are in the same
	 *         substate (Relies on index of assums arraylist of a specific
	 *         state)
	 */

	public boolean assumContradictionApplies(int i, int j) {
		Expression firstAssum = getAssum(i);
		String firstBase = firstAssum.getNode();
		Expression secondAssum = getAssum(j);
		String secondBase = secondAssum.getNode();
		int firstCount = 0;
		int secondCount = 0;

		while (firstAssum.getNode().equals("~")) {
			firstCount++;
			firstAssum = firstAssum.getLeft();
			firstBase = firstAssum.getNode().toString();
		}
		while (secondAssum.getNode().equals("~")) {
			secondCount++;
			secondAssum = secondAssum.getLeft();
			secondBase = secondAssum.getNode().toString();
		}

		if (firstBase.equals(secondBase)) {
			return ((firstCount % 2 == 0) && (secondCount % 2 != 0))
					|| ((firstCount % 2 != 0) && (secondCount % 2 == 0));
		} else {
			return false;

		}
	}

	/**
	 * 
	 * @return returns true if the assumption contradiction rule applies. ie one
	 *         expressions contains an even number of ~ and one contains an odd
	 *         number. NOTE: Also works when expressions are in different states
	 */
	// public boolean assumContradictionApplies(Expression e1, Expression e2) {
	// // Expression firstAssum = e;
	// Expression firstAssum = e1;
	// String firstBase = e1.getNode();
	// Expression secondAssum = e2;
	// String secondBase = e2.getNode();
	// int firstCount = 0;
	// int secondCount = 0;
	//
	// while (firstAssum.getNode().equals("~")) {
	// firstCount++;
	// firstAssum = firstAssum.getLeft();
	// firstBase = firstAssum.getNode().toString();
	// }
	// System.out.println("first base = " + firstBase);
	// while (secondAssum.getNode().equals("~")) {
	// secondCount++;
	// secondAssum = secondAssum.getLeft();
	// secondBase = secondAssum.getNode().toString();
	// }
	//
	// System.out.println("second base = " + secondBase);
	// if (firstBase.equals(secondBase)) {
	// System.out.println("TRUE");
	// System.out
	// .println(((firstCount % 2 == 0) && (secondCount % 2 != 0))
	// || ((firstCount % 2 != 0) && (secondCount % 2 == 0)));
	// return ((firstCount % 2 == 0) && (secondCount % 2 != 0))
	// || ((firstCount % 2 != 0) && (secondCount % 2 == 0));
	// } else {
	// return false;
	//
	// }
	// }

	/**
	 * When a false assumption is clicked on, causes a contradiction- remove all
	 * goals, assumptions and substates from the current state
	 */
	// public void falseAssumRule() {
	// resetAssums();
	// resetGoals();
	// resetSubstates();
	// }

	/**
	 * Splits a logical equivalence goal into the corresponding implies goals
	 * 
	 * @param i
	 *            Index of the goal in the goals arraylist
	 * @return true if the rule is successful, false otherwise
	 */
	public boolean logicalEqGoal(int i) {
		if (i >= getGoals().size()) {
			return false;
		}
		Expression goal = goals.get(i);
		if (goal.getNode().equals("<->")) {
			Expression left = new Expression(goal.getLeft(), "->",
					goal.getRight());
			Expression right = new Expression(goal.getRight(), "->",
					goal.getLeft());

			addGoal(left);
			addGoal(right);
			removeGoal(goal);
			return true;
		}
		return false;
	}

	/**
	 * Splits a logical equivalence assumption into the corresponding implies
	 * assumptions
	 * 
	 * @param i
	 *            Index of the goal in the goals arraylist
	 * @return true
	 */
	public boolean logicalEqAssum(int i) {
		if (i >= getAssums().size()) {
			return false;
		}

		Expression assum = assums.get(i);

		if (assum.getNode().equals("<->")) {
			Expression left = new Expression(assum.getLeft(), "->",
					assum.getRight());
			Expression right = new Expression(assum.getRight(), "->",
					assum.getLeft());

			addAssum(left);
			addAssum(right);
			removeAssum(assum);
			return true;
		}
		return false;
	}

	public boolean assumSimplify(ProofState target, int i, int j, int complex) {
		Expression e1 = assums.get(i);
		Expression e2 = target.assums.get(j);
		Expression newExp;
		newExp = complex == 1 ? e1.getRight() : e2.getRight();

		if (i < j) {
			target.assums.remove(j);
			assums.remove(i);
		} else {
			assums.remove(i);
			target.assums.remove(j);
		}
		target.addAssum(newExp);
		return true;
	}

	public int isSimplifiable(ProofState target, int i, int j) {
		Expression e1 = assums.get(i);
		Expression e2 = target.assums.get(j);
		Expression e1Left = e1.getLeft();
		Expression e2Left = e2.getLeft();

		if (e1.isComplex() && e1.getRight() != null
				&& e1Left.toString().equals(e2.toString())) {
			return 1;
		} else if (e2.isComplex() && e2.getRight() != null
				&& e2Left.toString().equals(e1.toString())) {
			return 2;
		}
		return -1;
	}

	/**
	 * Shortcut when an implication assumption is dropped on a goal, where the
	 * goal equals the right hand side of the assumption. Shortcut as:
	 * Assumption A -> B, Goal B. Clicking assumption results in Goal A,
	 * assumption B, and the previous Goal B, This simplifes to Goal A This
	 * checks if all this can be done in one go
	 * 
	 * @param target
	 *            Proofstate the rule rule is applied in
	 * @param i
	 *            Index of the assumption in the assumption index
	 * @param j
	 *            Index of the goal in the goal index
	 * @return true if the simplification rule applies. False if not
	 */
	public boolean assumImpliesGoalShortcutApplies(ProofState target, int i,
			int j) {
		Expression e1 = assums.get(i);
		Expression e2 = target.goals.get(j);
		if (e1.getNode().equals("->")
				&& e1.getRight().toString().equals(e2.toString())) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Used if 'assumImpliesGoalShortcutApplies' returns true. Applies the
	 * shortcut rule.
	 * 
	 * @param target
	 *            Proofstate the rule rule is applied in
	 * @param i
	 *            Index of the assumption in the assumption index
	 * @param j
	 *            Index of the goal in the goal index
	 */
	public boolean assumImpliesGoalShortcut(ProofState target, int i, int j) {
		if (i >= getAssums().size() || j >= getGoals().size()) {
			return false;
		}

		Expression assum = assums.get(i);
		if (assum.getNode().equals("->")) {
			target.addGoal(assum.getLeft());
			removeAssum(assums.get(i));
			target.removeGoal(target.goals.get(j));
			return true;
		}
		return false;
	}

	// [B -> [A->B]] -> [B&L]
	/**
	 * If an assumption contains expressions that can be specialised, (ie with a
	 * leading '?') this method attempts to match the assumption to the goal. If
	 * successful, the goal is satisfied.
	 * 
	 * @param a
	 *            Thrown in assumption that is being specialised
	 * @param g
	 *            Goal that is being satisfied
	 * 
	 */

	public boolean canSpecialiseThrowIn(int i, int j) {
		if (i >= getAssumVars().size() || j >= getGoals().size()) {
			System.out.println("Oh dear");
			return false;
		}
		ArrayList<String> specialisable = new ArrayList<String>();
		Expression assum = assumVars.get(i);
		Expression goal = goals.get(j);

		// Finds which variables are in the assumption to be specialised
		for (String part : assum.getExpressionAsList()) {
			if (part.contains("?") && !specialisable.contains(part)) {
				specialisable.add(part);
			}
		}
		goal.setExpressionAsList();

		// Finds the level-order positions of each variable in the assumption
		// tree
		HashMap<String, ArrayList<Integer>> variablePositions = levelOrderTraversalOfAssum(assum);
		// For each variable, finds the first instance of what it should be
		// matched to.
		// i.e the expression at the equivalent position in the goal
		// (This should be consistent through out the specialisation so we only
		// consider the first)
		HashMap<String, Expression> specialisations = new HashMap<String, Expression>();

		// for each variable in the variablePositions hashMap...
		for (Map.Entry<String, ArrayList<Integer>> entry : variablePositions
				.entrySet()) {
			// ... Find the expression the variable should be specialised to
			// ( We only use entry.getKey()).get(0) because the variable should
			// be specialised to
			// the same expression at each occurrence, so we only worry about
			// the first occurrence)
			Expression e = getExpressionToSpecialise(goal,
					(String) entry.getKey(),
					variablePositions.get(entry.getKey()).get(0),
					variablePositions, specialisations);

			specialisations.put(entry.getKey(), e);
		}
		System.out.println("_______________________________________");
		System.out.println("Variables and the specialisations:");
		for (Map.Entry<String, Expression> entry : specialisations.entrySet()) {
			System.out.println(entry.getKey() + " " + entry.getValue());
		}
		System.out.println("_______________________________________");

		// If any are null, no point continuing
		for (Map.Entry<String, Expression> entry : specialisations.entrySet()) {
			if (entry.getValue() == null) {
				return false;
			}
		}

		ArrayList<String> assumAsString = assum.getExpressionAsList();
		String newAssumString = "";

		for (String part : assumAsString) {
			if (specialisable.contains(part)
					&& specialisations.get(part) != null) {
				newAssumString += specialisations.get(part).toString();
			} else {
				newAssumString += part;
			}
		}

		// Removes brackets and white space from assumption/goal
		// This could potentially lead to problems
		String nAssumStripped = newAssumString.replaceAll("\\(|\\)|\\s+", "");
		System.out.println("Pattern matched assum = " + nAssumStripped);
		String goalString = goal.toString().replaceAll("\\(|\\)|\\s+", "");
		System.out.println("Original goal = " + goalString);

		if (nAssumStripped.equals(goalString)) {
			System.out.println("Specialised assumption matches goal");
		} else {
			System.out.println("Specialised assumption does not match goal");
		}
		return nAssumStripped.equals(goalString);
	}

	/**
	 * Returns a hashMap mapping each variable (?x etc) to its level order
	 * positions in the assumption
	 * 
	 * @param e
	 *            Assumption being considered
	 * @return hashMap of each variable, and its level-order positions in the
	 *         assumption
	 */
	public HashMap<String, ArrayList<Integer>> levelOrderTraversalOfAssum(
			Expression e) {
		HashMap<String, ArrayList<Integer>> map = new HashMap<String, ArrayList<Integer>>();
		int spec = 0;

		Queue<Expression> nodequeue = new LinkedList<Expression>();

		if (e != null) {
			nodequeue.add(e);
		}

		while (!nodequeue.isEmpty()) {
			spec++;
			Expression next = nodequeue.remove();

			if (next.toString().contains("?") && next.toString().length() == 2) {
				if (map.containsKey(next.toString())) {
					map.get(next.toString()).add(spec);
				} else {
					ArrayList<Integer> list = new ArrayList<Integer>();
					list.add(spec);
					map.put(next.toString(), list);
				}
			}
			if (next.getLeft() != null) {
				nodequeue.add(next.getLeft());
			}
			if (next.getRight() != null) {
				nodequeue.add(next.getRight());
			}
		}

		// System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		// System.out.println("Variables and their level-order position");
		// for (Map.Entry<String, ArrayList<Integer>> entry : map.entrySet()) {
		// System.out.println(entry.getKey() + " " + entry.getValue());
		// }
		//
		// System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		return map;
	}

	/**
	 * ---This is a total mess. Just accept it works till I manage to wade
	 * through it ---
	 * 
	 * For each variable in the assumption, gets the expression at the
	 * corresponding level-order-position in the goal
	 * 
	 * BUT if preceding variables are complex, the positions shift, so an offset
	 * must be added, which is where it gets messy.
	 * 
	 * I can probably reduce the amount of parameters here too
	 * 
	 * @param goal
	 *            The goal that the assumption is trying to be matched to
	 * @param variable
	 *            The variable that we are looking to match
	 * @param position
	 *            The position that variable occurs
	 * @param variablePositions
	 *            The hashMap of variables to all their positions
	 * @param specialisations
	 *            The hashMap of variables to their specialised expression
	 */
	public Expression getExpressionToSpecialise(Expression goal,
			String variable, int position,
			HashMap<String, ArrayList<Integer>> variablePositions,
			HashMap<String, Expression> specialisations) {
		int spec = 0;

		int offset = 0;

		// This is the messy offset calculation
		//
		// Basically, if there's only one variable to specialise, everything's
		// ok
		// If not, for the first variable encountered, everything's still ok.
		// If the first variable is simple (ie just 1 letter), everythings's
		// still ok.
		// If it's not (eg (a+b)), the position of the second variable has
		// shifted.
		for (Map.Entry<String, ArrayList<Integer>> entry : variablePositions
				.entrySet()) {
			if (specialisations.containsKey(entry.getKey())) {

				// If anything is specialised to null, no point continuing
				if (specialisations.get(entry.getKey()) == null) {
					return null;
				}
				for (Integer pos : entry.getValue()) {
					if (pos < position
							&& (specialisations.get(entry.getKey()).toString()
									.replaceAll("\\+|=|\\*", "")).length() > 1) {
						offset += variablePositions.size();
					}
				}
			}
		}

		position += offset;
		Queue<Expression> nodequeue = new LinkedList<Expression>();

		if (goal != null) {
			nodequeue.add(goal);
		}

		while (!nodequeue.isEmpty()) {
			spec++;
			Expression next = nodequeue.remove();

			if (spec == position) {
				return next;
			}

			if (next.getLeft() != null) {
				nodequeue.add(next.getLeft());
			}
			if (next.getRight() != null) {
				nodequeue.add(next.getRight());
			}
		}
		return null;
	}

	public void resetGoals() {
		goals = new ArrayList<Expression>();
	}

	public void resetAssums() {
		assums = new ArrayList<Expression>();
	}

	public void resetSubstates() {
		substates = new ArrayList<ProofState>();
		int stateIndex = proofStateList.indexOf(this);
		proofStateList.subList(stateIndex, proofStateList.size() - 1).clear();
	}

	public void resetAssumbubbles() {
		assumbubbles = new ArrayList<ArrayList<Expression>>();
	}

	public void cleanup() {
		if (isEmptyState()) {
			this.setHideFlag(true);
		}
	}

	public String toString() {
		String sub = substates.toString();
		sub = sub.replace("\n", "\n\t");
		// String s = "{\nassums:\t" + assums + "\ngoals:\t" + goals
		// + "\nassumbubbles:\t" + assumbubbles + "\nselected bubbles: ["
		// + selectedBubbleL + ", " + selectedBubbleR + "] ("
		// + lastBubbleBox + ")" + "\nsubstates:\t" + sub + "\n}";
		String s = "{\nassums:\t" + assums + "\nassumVars:\t" + assumVars
				+ "\ngoals:\t" + goals + "\nassumbubbles:\t" + assumbubbles
				+ "\nsubstates:\t" + sub + "\n}";
		return s;
	}

	public boolean arequal(ProofState a, ProofState b) {
		if (a.toString().equals(b.toString())) {
			return true;
		} else {
			return false;
		}

	}

	public ArrayList<String> AE2AS(ArrayList<Expression> ae) {
		ArrayList<String> as = new ArrayList<String>();
		for (int m = 0; m < ae.size(); m++) {
			as.add(ae.get(m).toString());
		}
		return as;

	}

	public ArrayList<Expression> AS2AE(ArrayList<String> as) {
		ArrayList<Expression> ae = new ArrayList<Expression>();
		for (int m = 0; m < as.size(); m++) {
			String s = as.get(m);
			if (s == null)
				break;
			try {
				ae.add(MyExpressionParser.parse(s));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ae;
	}

	public boolean isMergeable(ProofState target, int i, int j) {
		ArrayList<Expression> x = assumbubbles.get(i);
		ArrayList<Expression> y = target.assumbubbles.get(j);
		boolean mergeable = false;
		ArrayList<String> xs = this.AE2AS(x);
		ArrayList<String> ys = this.AE2AS(y);
		for (int m = 0; m < xs.size(); m++) {
			for (int ysn = 0; ysn < ys.size(); ysn++) {
				if (ys.get(ysn).equals(xs.get(m))) {
					mergeable = true;
				}
			}
		}
		return mergeable;
	}

	public boolean merge(ProofState target, int i, int j) {
		ArrayList<Expression> x = assumbubbles.get(i);
		ArrayList<Expression> y = target.assumbubbles.get(j);
		@SuppressWarnings("unchecked")
		ArrayList<Expression> z = (ArrayList<Expression>) y.clone();

		ArrayList<String> xs = this.AE2AS(x);
		ArrayList<String> zs = this.AE2AS(z);

		for (int m = 0; m < xs.size(); m++) {
			if (!zs.contains(xs.get(m))) {
				zs.add(xs.get(m));
			}
		}
		System.out.println(zs);
		ArrayList<Expression> zz = this.AS2AE(zs);
		target.assumbubbles.set(i, zz);
		assumbubbles.remove(y);
		return true;
	}

	public boolean createBubble(int i) {
		if (i >= getAssums().size()) {
			return false;
		}

		Expression theAssum = this.getAssum(i);

		if (theAssum.getNode().equals("=")) {
			removeAssum(theAssum);
			ArrayList<Expression> temp = new ArrayList<Expression>();
			temp.add(theAssum.getLeft());
			if (!theAssum.getLeft().toString()
					.equals(theAssum.getRight().toString())) {
				temp.add(theAssum.getRight());
			}
			assumbubbles.add(temp);
			return true;
		}
		return false;
	}

	public boolean extract(int bubbleBoxIndex, int leftBubbleIndex,
			int rightBubbleIndex) {
		ArrayList<Expression> b = assumbubbles.get(bubbleBoxIndex);
		Expression tempExpression = new Expression();
		tempExpression.setLeft(b.get(leftBubbleIndex).clonexp());
		tempExpression.setRight(b.get(rightBubbleIndex).clonexp());
		tempExpression.setNode("=");
		addAssum(tempExpression);
		return true;
	}

	public boolean selectBubble(int bubbleBoxIndex, int bubbleIndex) {
		ArrayList<Expression> bubbleBox = assumbubbles.get(bubbleBoxIndex);
		if (selectedBubbleL == null) {
			lastBubbleBox = bubbleBoxIndex;
			selectedBubbleL = bubbleBox.get(bubbleIndex);
		} else if (lastBubbleBox == bubbleBoxIndex) {
			selectedBubbleR = bubbleBox.get(bubbleIndex);
		} else {
			int tempBubbleIndex = bubbleIndex;
			int tempBubbleBoxIndex = bubbleBoxIndex;
			clearBubbleSelection();
			selectBubble(tempBubbleBoxIndex, tempBubbleIndex);
		}
		return true;
	}

	public void clearBubbleSelection() {
		selectedBubbleL = null;
		selectedBubbleR = null;
		lastBubbleBox = -1;
	}

	/**
	 * Returns true if the bubble expression e is one of the selected bubbles in
	 * this proof state.
	 * 
	 * @param e
	 *            - The bubble expression to look for among selected bubbles
	 * @return
	 */
	public boolean isSelectedBubble(Expression e) {
		return e == selectedBubbleL || e == selectedBubbleR;
	}

	/**
	 * Returns true if the proof state has a total of two selected bubbles.
	 * 
	 * @return
	 */
	public boolean hasSelectedTwoBubbles() {
		return selectedBubbleL != null && selectedBubbleR != null;
	}

	public boolean hasSelectedAnyBubble() {
		return selectedBubbleL != null || selectedBubbleR != null;
	}

	public Expression[] getSelectedBubbles() {
		Expression[] bbls = { selectedBubbleL, selectedBubbleR };
		return bbls;
	}

	public int getSelectedBubbleBox() {
		return lastBubbleBox;
	}

	/**
	 * Simple debug method to print the currently selected bubbles and the
	 * current bubbleBoxIndex.
	 * 
	 * @return String output is in the form
	 *         "[selectedBubbleL, selectedBubbleR] (bubbleBoxIndex)"
	 */
	public String debugBubbleSelection() {
		return "[" + selectedBubbleL + ", " + selectedBubbleR + "]" + " ("
				+ lastBubbleBox + ")";
	}

	public boolean move(int expressionIndex, ProofState p) {
		Expression a = assums.get(expressionIndex);
		removeAssum(a);
		p.addAssum(a);
		return true;
	}

	public boolean moveVar(int expressionIndex, ProofState p) {
		Expression a = assumVars.get(expressionIndex);
		removeAssumVar(a);
		p.addAssumVar(a);
		return true;
	}

	public boolean copyBubbleBox(int i) {
		ArrayList<Expression> newBubbleBox = CloneExpAL(assumbubbles.get(i));
		addAssumBubble(newBubbleBox);
		return true;
	}

	public ArrayList<ArrayList<Expression>> getAssumbubbles() {

		return assumbubbles;
	}

	public ArrayList<Expression> getBubbleBox(int i) {
		return assumbubbles.get(i);
	}

	// 201208 rewrite a Assum: A->G to newG->(A'->G)
	public boolean rewriteAssum(Expression newGoal, Expression newAssum,
			int origAssumIndex) {
		Expression originalAssum = assums.get(origAssumIndex);
		removeAssum(originalAssum);
		ArrayList<Expression> newAssums = new ArrayList<Expression>();
		newAssums = this.getAssums();
		resetAssums();

		ArrayList<ArrayList<Expression>> newAssumbbs = new ArrayList<ArrayList<Expression>>();
		newAssumbbs = this.getAssumbubbles();
		resetAssumbubbles();

		ProofState newState = new ProofState();
		newState = (ProofState) this.cloneps();
		newState.addAssum(originalAssum.clonexp());
		resetSubstates();
		addSubstate(newState);

		setAssums(newAssums);
		setAssumBubble(newAssumbbs);
		resetGoals();
		addGoal(newGoal);
		return true;
	}

	// rewrite a Goal: A->G or G to A->(G', newG)
	public boolean rewriteGoal(Expression newGoal, Expression rewrittenGoal,
			int originalGoalIndex) {
		Expression originalGoal = goals.get(originalGoalIndex);

		removeGoal(originalGoal);
		addGoal(rewrittenGoal);
		addGoal(newGoal);
		return true;
	}

	public boolean applyBubbleContext(String exp, int b) {
		ArrayList<Expression> origBubble = new ArrayList<Expression>(
				assumbubbles.get(b));
		Iterator<Expression> bb = origBubble.iterator();
		ArrayList<Expression> newBubble = new ArrayList<Expression>();
		while (bb.hasNext()) {
			String i = bb.next().toString();
			String s = exp.replaceAll("\\?", i);
			if (s == null)
				return false;

			try {
				newBubble.add(MyExpressionParser.parse(s));
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		removeAssumBubble(origBubble);
		addAssumBubble(newBubble);
		return true;
	}

	public boolean startInduction(int goalIndex, String selectedValue) {
		Expression clickedGoal = goals.get(goalIndex);

		removeGoal(clickedGoal);

		Expression goal0 = Expression.replaceAll(selectedValue, "0",
				clickedGoal);
		Expression goalInd = new Expression();
		goalInd.setNode("->");
		goalInd.setLeft(clickedGoal);
		goalInd.setRight(Expression.replaceAll(selectedValue, selectedValue
				+ "+1", clickedGoal));

		addGoal(goal0);
		addGoal(goalInd);
		return true;
	}

	public boolean deleteAssum(int assumIndex) {
		if (goals.isEmpty()) {
			getAssums().remove(assumIndex);
			return true;
		}
		return false;
	}

	public boolean deleteAssumVar(int assumVarIndex) {
		if (goals.isEmpty()) {
			getAssumVars().remove(assumVarIndex);
			return true;
		}
		return false;
	}

	public boolean deleteBubbleBox(int bubbleBoxIndex) {
		if (goals.isEmpty()) {
			getAssumbubbles().remove(bubbleBoxIndex);
			return true;
		}
		return false;
	}

	public boolean isEmptyState() {
		boolean a = getGoals().isEmpty();
		boolean b = getAssums().isEmpty();
		boolean c = getAssumbubbles().isEmpty();
		return a && b && c;
	}

	public int getDepth() {
		return getDepth(this, 0);
	}

	public int getDepth(ProofState p, int counter) {
		if (p.getParent() == null || !(p.getParent() instanceof ProofState))
			return counter;
		else {
			counter++;
			return getDepth((ProofState) p.getParent(), counter);
		}
	}

	public void setHideFlag(boolean b) {
		hide = b;
	}

	public boolean getHideFlag() {
		return hide;
	}

	public ArrayList<ProofState> getProofStateList() {
		return proofStateList;
	}

	public void setProofStateList(ArrayList<ProofState> pfl) {
		ProofState topState = (ProofState) pfl.get(0).cloneps();
		ArrayList<ProofState> proofStateListClone = new ArrayList<ProofState>();
		ProofState lastPS = topState;
		for (int i = 0; i < pfl.size(); i++) {
			ProofState ps;
			if (i > 0) {
				ps = lastPS.getSubstate(0);
				lastPS = ps;
			} else {
				ps = topState;
			}
			proofStateListClone.add(ps);
		}
		proofStateList = proofStateListClone;
	}
}
