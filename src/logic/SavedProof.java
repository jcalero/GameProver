package logic;

import java.util.ArrayList;

public class SavedProof {
	
	private Expression expression;
	private ArrayList<LogicStep> stepList;
	
	/**
	 * Main constructor.
	 * @param exp - The expression this proof will be proving.
	 */
	public SavedProof(Expression exp) {
		this.expression = exp;
		this.stepList = new ArrayList<LogicStep>();
	}
	
	/**
	 * Copy constructor. Creates a deep copy of the SavedProof
	 * <i>proof</i>.
	 * 
	 * @param proof - The SavedProof to be copied.
	 */
	public SavedProof(SavedProof proof) {
		this.expression = proof.getExpression().clonexp();
		this.stepList = new ArrayList<LogicStep>();
		
		for(int i = 0; i < proof.size(); i++) {
			this.stepList.add(proof.get(i));
		}
	}
	
	/**
	 * Add a step to the proof. 
	 * @param step - The LogicStep to add at the end of the proof
	 * sequence. 
	 */
	public void add(LogicStep step) {
		stepList.add(step);
	}
	
	/**
	 * Return the i'th step in the proof.
	 * @param i - The index of the step to return.
	 * @return The LogicStep with index i.
	 */
	public LogicStep get(int i) {
		return stepList.get(i);
	}
	
	/**
	 * Removes the i'th step in the proof.
	 * @param i - The index of the step to remove.
	 * @return The LogicStep with index i.
	 */
	public LogicStep remove(int i) {
		return stepList.remove(i);
	}
	
	/**
	 * Return the expression this proof is proving.
	 * @return The expression.
	 */
	public Expression getExpression() {
		return expression;
	}
	
	/**
	 * Return the size of the list of proof steps
	 * in this proof.
	 * @return The number of proof steps.
	 */
	public int size() {
		return stepList.size();
	}
	
	/**
	 * Returns the string representing the expression of this proof.
	 * In other words, returns the output of calling "toString()" on
	 * the expression.
	 */
	@Override
	public String toString() {
		return expression.toString();
	}
}
