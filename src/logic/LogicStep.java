/**
 * Class containing the "recipe" for the StepManager to apply a rule
 * from. Contains all the necessary information a ProofState needs to
 * apply a certain rule 
 */
package logic;

public class LogicStep {

	// Base properties
	private Rule rule = null;
	private int depth = -1; // Depth is irrespective of cleanup operations
	private int firstExpIndex = -1;

	// Extra properties for advanced rules
	private int secondExpIndex = -1;
	private int bubbleBoxIndex = -1;
	private int targetDepth = -1;
	private int complexIndex = -1;
	private Expression newExpression = null;
	private Expression rewrittenExpression = null;
	private String bubbleContext = null;
	private String inductVariable = null;

	/**
	 * Base constructor. All LogicSteps need to contain at least an
	 * <code>Rule</code> and a index referencing what ProofState to apply the
	 * rule to. Normally more information is required but that is dependant on
	 * the specific rules.
	 * 
	 * @param rule
	 *            - The rule to apply. Check the Rule enum if it exists,
	 *            otherwise it needs to be implemented.
	 * @param depth
	 *            - The absolute depth of the ProofState the rule is being
	 *            applied to in the "tree" of ProofStates of the current proof.
	 *            Usually this is given by the call to
	 *            <code>getAbsoluteDepth</code> in ProofStatePanel or
	 *            <code>getDepth</code> in a ProofState.
	 */
	public LogicStep(Rule rule, int depth) {
		this.rule = rule;
		this.depth = depth;
		this.targetDepth = depth;
	}

	/**
	 * If the rule is a binary operation, that is, there are two objects being
	 * manipulated (as is the case when dragging one object onto an other, for
	 * example), this method specifies the index of the second expression the
	 * rule should be applied on. Typically this is the object being dropped ON.
	 * 
	 * @param expressionIndex
	 *            - The index of the second expression. Set to -1 to specify no
	 *            second expression.
	 */
	public void setSecondExpressionIndex(int expressionIndex) {
		this.secondExpIndex = expressionIndex;
	}

	/**
	 * If the rule is a unary operation, that is, only one object is being
	 * manipulated (as is the case when clicking on a single object, for example
	 * when expanding an implication goal), this method will specify the index
	 * of this expression.
	 * 
	 * In the case of a binary operation (two objects being manipulated, like
	 * with drag and drop) this usually refers to the index of the expression
	 * being dragged. Don't forget to specify the second expression if this is
	 * the case with <code>setSecondExpressionIndex</code>.
	 * 
	 * @param expressionIndex
	 *            - The index of the first (or only) expression. Set to -1 to
	 *            specify no first expression.
	 */
	public void setFirstExpressionIndex(int expressionIndex) {
		this.firstExpIndex = expressionIndex;
	}

	/**
	 * If a rule relates to multiple ProofStates, for example when moving an
	 * assumption from one ProofState to a sub-ProofState, this method specifies
	 * the depth of the second or target ProofState.
	 * 
	 * @param targetDepth
	 *            - The absolute depth of the target ProofState, usually
	 *            obtained by the call of <code>getAbsoluteDepth</code> in the
	 *            ProofStatePanel of the target. Set to -1 to specify no target.
	 */
	public void setTargetDepth(int targetDepth) {
		this.targetDepth = targetDepth;
	}

	/**
	 * If the rule requires a new expression to be used in some way this methods
	 * specifies that expression. This is the case in assumption introductions
	 * for example (throw-ins) and the entire expression needs to be specified.
	 * 
	 * @param exp
	 *            - The expression to be specified, set to <code>null</code> to
	 *            specify no new expression.
	 */
	public void setNewExpression(Expression exp) {
		this.newExpression = exp;
	}
	
	public void setRewrittenExpression(Expression exp) {
		this.rewrittenExpression = exp;
	}

	/**
	 * If the rule concerns the contents of a bubble-box, the index of that
	 * bubble-box needs to be specified with this method.
	 * 
	 * @param bubbleBoxIndex
	 *            - The index of the bubble box. Set to -1 to specify no bubble
	 *            box.
	 */
	public void setBubbleBoxIndex(int bubbleBoxIndex) {
		this.bubbleBoxIndex = bubbleBoxIndex;
	}

	public void setComplexIndex(int complexIndex) {
		this.complexIndex = complexIndex;
	}

	public void setBubbleContext(String bubbleContext) {
		this.bubbleContext = bubbleContext;
	}
	
	public void setInductVariable(String inductVariable) {
		this.inductVariable = inductVariable;
	}

	/**
	 * Returns the rule this LogicStep is concerned with.
	 * 
	 * @return The rule.
	 */
	public Rule getRule() {
		return this.rule;
	}

	/**
	 * Returns the index of the ProofState the step applies to in the tree/list
	 * of ProofStates in the current proof.
	 * 
	 * @return The absolute index of the ProofState
	 */
	public int getDepth() {
		return this.depth;
	}

	public int getSecondExpressionIndex() {
		return secondExpIndex;
	}

	public int getFirstExpressionIndex() {
		return firstExpIndex;
	}

	public int getBubbleBoxIndex() {
		return bubbleBoxIndex;
	}

	public int getTargetDepth() {
		return targetDepth;
	}

	public Expression getNewExpression() {
		return newExpression;
	}
	
	public Expression getRewrittenExpression() {
		return rewrittenExpression;
	}
	
	public int getComplexIndex() {
		return complexIndex;
	}

	public String getBubbleContext() {
		return bubbleContext;
	}
	
	public String getInductVariable() {
		return inductVariable;
	}

	public boolean isUnary() {
		boolean b = firstExpIndex != -1;
		return b;
	}

	public boolean isBinary() {
		boolean b1 = firstExpIndex != -1;
		boolean b2 = secondExpIndex != -1;
		return b1 && b2;
	}

	public boolean hasBubbleBoxIndex() {
		boolean b = bubbleBoxIndex != -1;
		return b;
	}

	/**
	 * Returns true if the logic step concerns more than one ProofState, such as
	 * when moving assumptions between states or applying a rule to a sub-state.
	 * 
	 * @return false if the logic step is contained within one ProofState, true
	 *         otherwise. In other words, false if targetDepth == depth.
	 */
	public boolean isBetweenStates() {
		boolean b = targetDepth != depth;
		return b;
	}

	public boolean hasNewExpression() {
		boolean b = newExpression != null;
		return b;
	}
	
	public boolean hasRewrittenExpression() {
		boolean b = rewrittenExpression != null;
		return b;
	}

	public boolean hasBubbleContext() {
		boolean b = bubbleContext != null;
		return b;
	}
	
	public boolean hasInductVariable() {
		boolean b = inductVariable != null;
		return b;
	}

	public String toString() {
		String output = rule + ";" + depth + ";" + targetDepth + ";"
				+ firstExpIndex + ";" + secondExpIndex + ";" + bubbleBoxIndex
				+ ";" + complexIndex + ";" + bubbleContext + ";"
				+ newExpression + ";" + inductVariable;

		return output;
	}

}
