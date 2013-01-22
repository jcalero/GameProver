// File: Rule.java
// Author: Jakob Calero
// Date: 16/10/12
// 
// Enumerator representing rules available to apply to game objects.

package logic;

// [[A&~B] -> C] -> F

public enum Rule {
	AndAssum, RefGoal, AndGoal, ImpliesGoal,
	ImpliesAssum, CreateBubble, MergeBubble,
	Extract, Solve, SolveVar, MoveAssum, MoveAssumVar, MoveBubble,
	AddAssum, AddAssumVar, NotGoal, NotGoalSimplify,
	NotAssumToGoal, NotAssumSimplify,
	NotAssumContradiction, LogicalEqGoal, LogicalEqAssum, SwitchEqualsAssum, SwitchEqualsAssumVar, SwitchEqualsGoal, AssumImpliesGoalShortcut, AssumSimplify,
	SelectBubble, BubbleContext, Induction,
	CopyAssum, CopyAssumVar, CopyBubbleBox, DeleteAssum, DeleteAssumVar,
	DeleteBubble, RewriteAssum, RewriteGoal
}
