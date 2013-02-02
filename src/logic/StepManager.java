/**
 * Manager for applying logic steps to the proof states,
 * recording the steps and handling any modification to 
 * the current sequence of steps.
 * 
 * Contains a static instance of the currently played
 * proof. Any new instances of StepManager will always
 * reference the same proof, hence it's important to 
 * clear it once finished with it and before starting a
 * new proof.
 */
package logic;

import game.Game;

public class StepManager {

	private Game window;
	private static SavedProof proof;

	/**
	 * Main constructor.
	 * 
	 * @param df
	 *            Reference to the main application window.
	 */
	public StepManager(Game df) {
		this.window = df;
	}

	/**
	 * Applies a logic step to the current proof. The logic step is defined in
	 * detail by the class LogicStep.
	 * 
	 * @param ls
	 *            - The logic step to apply
	 * @return Returns true if the rule application is possible. False
	 *         otherwise. Does not say anything about rule being applied
	 *         correctly, just that the pre-conditions say it's possible to
	 *         apply it.
	 */
	public boolean applyRule(LogicStep ls) {
		boolean consistentLogicStep = false;
		boolean success = false;
		// TODO: Get rid of this (dontRecord) once "SelectBubble" is not a rule
		// any more, that is, when the step-by-step display of selection of
		// bubbles is done in the new fancy replay window instead of simply
		// applying a "SelectBubble" step.
		boolean dontRecord = false;
		Rule rule = ls.getRule();
		int depth = ls.getDepth();
		int leftExpression = ls.getFirstExpressionIndex();
		int rightExpression = ls.getSecondExpressionIndex();
		int bubbleBoxIndex = ls.getBubbleBoxIndex();
		int targetDepth = ls.getTargetDepth();
		int complexIndex = ls.getComplexIndex();
		Expression newExpression = ls.getNewExpression();
		Expression rewrittenExpression = ls.getRewrittenExpression();
		String inductVariable = ls.getInductVariable();
		String bubbleContext = ls.getBubbleContext();

		if (rule == Rule.SelectBubble) {
			dontRecord = true;
		}

		System.out.println("[STEP MANAGER]: "
				+ (rule == null ? null : rule.toString())
				+ " - Absolute depth: " + depth);
		ProofState sourceState = new ProofState();
		ProofState targetState = new ProofState();
		sourceState = sourceState.getProofStateList().get(depth);
		targetState = targetState.getProofStateList().get(targetDepth);

		switch (rule) {
		case Solve:
			if (ls.isBinary()) {
				success = sourceState.solve(targetState, leftExpression,
						rightExpression);
				consistentLogicStep = true;
			}
			break;
		case SolveVar:
			if (ls.isBinary()) {
				success = sourceState.solveVar(targetState, leftExpression,
						rightExpression);
				consistentLogicStep = true;
			}
			break;
		case AndAssum:
			if (ls.isUnary()) {
				success = sourceState.andAssumRule(leftExpression);
				consistentLogicStep = true;
			}
			break;
		case AndGoal:
			if (ls.isUnary()) {
				success = sourceState.andGoalRule(leftExpression);
				consistentLogicStep = true;
			}
			break;
		case ImpliesAssum:
			if (ls.isUnary()) {
				success = sourceState.impliesAssumRule(leftExpression);
				consistentLogicStep = true;
			}
			break;
		case ImpliesGoal:
			if (ls.isUnary()) {
				success = sourceState.impliesGoalRule(leftExpression);
				consistentLogicStep = true;
			}
			break;
		case NotGoal:
			if (ls.isUnary()) {
				success = sourceState.notGoalRule(leftExpression);
				consistentLogicStep = true;
			}
			break;
		case NotGoalSimplify:
			if (ls.isUnary()) {
				success = sourceState.notGoalSimplifyRule(leftExpression);
				consistentLogicStep = true;
			}
			break;
		case NotAssumSimplify:
			if (ls.isUnary()) {
				success = sourceState.notAssumSimplifyRule(leftExpression);
				consistentLogicStep = true;
			}
			break;
		case NotAssumToGoal:
			if (ls.isUnary()) {
				success = sourceState.notAssumToGoalRule(leftExpression);
				consistentLogicStep = true;
			}
			break;
		case NotAssumContradiction:
			if (ls.isBinary()) {
				success = sourceState.notAssumContradictionRule(targetState,
						leftExpression, rightExpression);
				consistentLogicStep = true;
			}
			break;
		case LogicalEqAssum:
			if (ls.isUnary()) {
				success = sourceState.logicalEqAssum(leftExpression);
				consistentLogicStep = true;
			}
			break;
		case LogicalEqGoal:
			if (ls.isUnary()) {
				success = sourceState.logicalEqGoal(leftExpression);
				consistentLogicStep = true;
			}
			break;
		case SwitchEqualsGoal:
			if (ls.isUnary()) {
				success = sourceState.switchEqualsGoal(leftExpression);
				consistentLogicStep = true;
			}
			break;
		case SwitchEqualsAssum:
			if (ls.isUnary()) {
				success = sourceState.switchEqualsAssum(leftExpression);
				consistentLogicStep = true;
			}
			break;
		case SwitchEqualsAssumVar:
			if (ls.isUnary()) {
				success = sourceState.switchEqualsAssumVar(leftExpression);
				consistentLogicStep = true;
			}
			break;
		case AssumImpliesGoalShortcut:
			if (ls.isUnary()) {
				success = sourceState.assumImpliesGoalShortcut(targetState,
						leftExpression, rightExpression);
				consistentLogicStep = true;
			}
			break;
		case RefGoal:
			if (ls.isUnary()) {
				success = sourceState.refGoalRule(leftExpression);
				consistentLogicStep = true;
			}
			break;
		case CreateBubble:
			if (ls.isUnary()) {
				success = sourceState.createBubble(leftExpression);
				consistentLogicStep = true;
			}
			break;
		case MergeBubble:
			if (ls.isBinary()) {
				success = sourceState.merge(targetState, leftExpression,
						rightExpression);
				consistentLogicStep = true;
			}
			break;
		case Extract:
			if (ls.hasBubbleBoxIndex() && ls.isBinary()) {
				success = sourceState.extract(bubbleBoxIndex, leftExpression,
						rightExpression);
				consistentLogicStep = true;
			}
			break;
		case MoveAssum:
			if (ls.isBetweenStates() && ls.isUnary()) {
				success = sourceState.move(leftExpression, targetState);
				consistentLogicStep = true;
			}
			break;
		case MoveAssumVar:
			if (ls.isBetweenStates() && ls.isUnary()) {
				success = sourceState.moveVar(leftExpression, targetState);
				consistentLogicStep = true;
			}
			break;
		case MoveBubble:
			if (ls.isBetweenStates() && ls.isUnary()) {
				success = sourceState.movebb(leftExpression, targetState);
				consistentLogicStep = true;
			}
			break;
		case AddAssum:
			if (ls.hasNewExpression()) {
				success = sourceState.throwInAssum(newExpression);
				consistentLogicStep = true;
			}
			break;
		case AddAssumVar:
			if (ls.hasNewExpression()) {
				success = sourceState.throwInAssumVar(newExpression);
				consistentLogicStep = true;
			}

		case AssumSimplify:
			if (ls.isBinary()) {
				success = sourceState.assumSimplify(targetState,
						leftExpression, rightExpression, complexIndex);
				consistentLogicStep = true;
			}
			break;
		case CopyBubbleBox:
			if (ls.hasBubbleBoxIndex()) {
				success = sourceState.copyBubbleBox(bubbleBoxIndex);
				consistentLogicStep = true;
			}
			break;
		case SelectBubble:
			if (ls.isUnary() && ls.hasBubbleBoxIndex()) {
				success = sourceState.selectBubble(bubbleBoxIndex,
						leftExpression);
				consistentLogicStep = true;
			}
			break;
		case BubbleContext:
			if (ls.hasBubbleBoxIndex() && ls.hasBubbleContext()) {
				success = sourceState.applyBubbleContext(bubbleContext,
						bubbleBoxIndex);
				consistentLogicStep = true;
			}
			break;
		case Induction:
			if (ls.isUnary() && ls.hasInductVariable()) {
				success = sourceState.startInduction(leftExpression,
						inductVariable);
				consistentLogicStep = true;
			}
			break;
		case CopyAssum:
			if (ls.isUnary()) {
				success = sourceState.copyAssum(leftExpression);
				consistentLogicStep = true;
			}
			break;
		case CopyAssumVar:
			if (ls.isUnary()) {
				success = sourceState.copyAssumVar(leftExpression);
				consistentLogicStep = true;
			}
			break;
		case DeleteAssum:
			if (ls.isUnary()) {
				success = sourceState.deleteAssum(leftExpression);
				consistentLogicStep = true;
			}
			break;
		case DeleteAssumVar:
			if (ls.isUnary()) {
				success = true;
				success = sourceState.deleteAssumVar(leftExpression);
				consistentLogicStep = true;
			}
			break;
		case DeleteBubble:
			if (ls.hasBubbleBoxIndex()) {
				success = sourceState.deleteBubbleBox(bubbleBoxIndex);
				consistentLogicStep = true;
			}
			break;
		case RewriteAssum:
			if (ls.isUnary() && ls.hasNewExpression()
					&& ls.hasRewrittenExpression()) {
				success = sourceState.rewriteAssum(newExpression,
						rewrittenExpression, leftExpression);
				consistentLogicStep = true;
			}
			break;
		case RewriteGoal:
			if (ls.isUnary() && ls.hasNewExpression()
					&& ls.hasRewrittenExpression()) {
				success = sourceState.rewriteGoal(newExpression,
						rewrittenExpression, leftExpression);
				consistentLogicStep = true;
			}
			break;
		}

		// Don't continue if the logic step is not consistent with the rule to
		// apply. Print error.
		if (!consistentLogicStep) {
			printLogicStepError(ls);
			return consistentLogicStep;
		}

		// Don't continue if the rule can't be applied. Print message.
		if (!success) {
			printRuleError(rule);
			return success;
		}

		// If rule was applied and we're not replaying then record the LogicStep
		if (!window.isReplaying() && !dontRecord) {
			record(ls);
			dontRecord = false;
		}

		// Record the GameState if it was changed.
		window.getCurrentDisplayPanel().record();

		return consistentLogicStep && success;
	}

	/**
	 * Save the last step taken to the proof.
	 * 
	 * @param step
	 *            - The step to be recorded.
	 */
	private void record(LogicStep step) {
		proof.add(step);
		window.setWasCleanup(false);
		System.out.println("[RECORDING]: (" + proof.size() + ") Added step: "
				+ step);
	}

	/**
	 * Removes the last step taken in the current proof.
	 */
	public void undo() {
		if (proof.size() > 0) {
			System.out.println("[RECORDING]: (" + proof.size()
					+ ") Removed step: " + proof.get(proof.size() - 1));
			proof.remove(proof.size() - 1);
		}
	}

	/**
	 * Saves the currently recorded proof to the local list of proofs and resets
	 * the old one.
	 */
	public void save() {
		SavedProof p = new SavedProof(proof);
		window.getSavedProofs().add(p);
		reset();
	}

	/**
	 * Clear the current proof.
	 */
	private void reset() {
		proof = null;
	}

	/**
	 * Returns the current unfinished proof sequence.
	 * 
	 * @return A SavedProof at a partially finished state before being saved to
	 *         the list of saved proofs (stored in GameManager).
	 */
	public SavedProof getCurrentProof() {
		return proof;
	}

	/**
	 * Initialises the expression <i>exp</i> to be used for a new proof.
	 * Constructs the necessary SavedProof.
	 * 
	 * @param exp
	 *            - The expression to be proved.
	 */
	public void start(Expression exp) {
		proof = new SavedProof(exp);
	}

	/**
	 * Prints a formatted error message with information about the LogicStep.
	 * Handy for knowing if a rule that is being applied contains all the
	 * necessary information.
	 * 
	 * @param ls
	 *            - The LogicStep to print the information from.
	 */
	private void printLogicStepError(LogicStep ls) {
		String error = "[STEP MANAGER]: Could not apply rule: \""
				+ ls.getRule()
				+ "\"\nDoes the LogicStep contain the correct information?"
				+ "\n---------------------------------------" + "\ndepth: "
				+ ls.getDepth() + "\nleftExpression: "
				+ ls.getFirstExpressionIndex() + "\nrightExpression: "
				+ ls.getSecondExpressionIndex() + "\nbubbleBoxIndex: "
				+ ls.getBubbleBoxIndex() + "\ntargetDepth: "
				+ ls.getTargetDepth() + "\nnewExpression: "
				+ (ls.getNewExpression() == null ? "null" : "exists")
				+ "\n---------------------------------------";
		System.err.println(error);
	}

	/**
	 * Prints a formatted error message with information about the Rule.
	 * 
	 * @param rule
	 *            - The Rule the error message should specify.
	 */
	private void printRuleError(Rule rule) {
		String error = "[STEP MANAGER]: Could not apply rule \"" + rule
				+ "\", conditions not met.";
		System.out.println(error);
	}
}
