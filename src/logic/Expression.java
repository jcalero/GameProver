package logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import parser.MyExpressionParser;

public class Expression implements Cloneable {

	private String node;
	private Expression left; 
	private Expression right;
	
	// This shouldn't be global really. I'll find a way to make it better
	ArrayList<String> expressionAsList = new ArrayList<String>();
	
	public Expression() {
		node = "";
		left = null;
		right = null;
	}

	
	public Expression(String s) {
		node = s;
	}    // JRL changed:
	
    public Expression(Expression left, String s, Expression right) {
        // check on top-level symbol removed
        this.left = left ;
        node = s;
        this.right = right ;
    }

// JRL changed:
    public Expression(Expression e) {
        if (e != null) {
            node = e.getNode();
            left = e.getLeft();
            right = e.getRight();
        }
    }

// JRL changed:
    public Expression clonexp() {
        Expression c = new Expression(this.node) ;
        if (this.left != null) {
            c.left = this.left.clonexp() ;
        }
        if (this.right != null) {
            c.right = this.right.clonexp() ;
        }
        return c ;
    }
	
	public Expression getLeft() {
		return left;
	}

	public void setLeft(Expression left) {
		this.left = left;
	}

	public Expression getRight() {
		return right;
	}

	public void setRight(Expression right) {
		this.right = right;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}
	
	public ArrayList<String> getExpressionAsList() {
		setExpressionAsList();
		return expressionAsList;
	}
	
	public void setExpressionAsList() {
		resetExpressionAsList();
		inOrderTraversal(this);
	}
	
	public void resetExpressionAsList() {
		this.expressionAsList = new ArrayList<String>();
		
	}

	// Used to display expression below image
	public boolean isComplex() {
		return node.equals("->") || node.equals("&") || node.equals("~") || node.equals("<->");
	}

	public boolean isNot() {
		return node.equals("~");
	}

	public boolean isLogicVar() {
		return node.matches("A-Z");
	}

	public boolean isEqualsExp() {
		return node.equals("=");
	}

	public boolean isPlusExp() {
		return node.equals("+");
	}

	public boolean isTimesExp() {
		return node.equals("*");
	}

	public boolean isTermVar() {
		return node.matches("[a-z]*");
	}
	
	public boolean containsTermVars() {
		for (String s : getExpressionAsList()) {
			if (s.matches("[a-z]*")) {
				return true;
			}
		}

		return false;
	}

	public boolean isTermConstant() {
		return node.matches("1") || node.matches("0");
	}

	public void PartReplaceExp(String n, Expression e) {
		if (this.getNode() == n) {
			this.replaceWith(e);
			return;
		} else {
			this.getLeft().PartReplaceExp(n, e);
			this.getRight().PartReplaceExp(n, e);
		}
	}

	public boolean hasnode() {
		boolean has = true;
		if (!(this.isTermVar() || this.isTermConstant())) {
			has = false;
		}
		return has;
	}
	
	// Builds the expression into a human readable form to display
	public String toString() {
		if (left == null) {
			return node;
		} else if (node.equals("~")) {
			return "[" + node + left.toString() + "]";
		} else if (node.equals("&") || node.equals("->") || node.equals("<->")) {
			return "[" + left.toString() + " " + node + " "
					+ right.toString() + "]";
		} else if (node.equals("=")) {
			return left.termToString() + "=" + right.termToString();
		} else {
			return termToString();
		}
	}
	
	public String termToString() {
		if (left == null) {
			return node;
		} else {
			return "(" + left.termToString() + node + right.termToString()
					+ ")";
		}
	}


	public ArrayList<String> getTermVars() {
		ArrayList<String> result = new ArrayList<String>();

		if (isTermVar())
			result.add(node);
		else if (left != null && right != null) {
			result.addAll(left.getTermVars());
			result.addAll(right.getTermVars());
		} else if (left != null && right == null) {
			result.addAll(left.getTermVars());
		}

		return result;
	}

	public void replaceWith(Expression e) {
		this.node = e.node;
		this.left = e.left;
		this.right = e.right;
	}

	public static Expression replaceAll(Object[] old, Object[] nnew, Expression orig) {
		Expression e = new Expression();
		for (int i = 0; i < old.length; i++) {
			if (orig.node.equals(old[i])) {
				Expression newE = new Expression();
				try {
					newE = MyExpressionParser.parse((String)nnew[i]);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				e.node = newE.node;
				e.left = newE.left;
				e.right = newE.right;
				break;
			}
			else
				e.node = orig.node;
		}
		if (orig.left != null)
			e.left = replaceAll(old, nnew, orig.left);
		if (orig.right != null)
			e.right = replaceAll(old, nnew, orig.right);
		return e;
	}

	public static Expression replaceAll(Object old, Object nnew, Expression orig) {
		Expression e = new Expression();
		if (orig.node.equals(old)) {
			Expression newE = new Expression();
			try {
				newE = MyExpressionParser.parse((String)nnew);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.node = newE.node;
			e.left = newE.left;
			e.right = newE.right;
		}
		else
			e.node = orig.node;

		if (orig.left != null)
			e.left = replaceAll(old, nnew, orig.left);
		if (orig.right != null)
			e.right = replaceAll(old, nnew, orig.right);
		return e;
	}
	
	public void replacePart(Expression olde, Expression newe,Expression orig) {
	String on=olde.getNode();
	//String nn=newe.getNode();
	String orign=orig.getNode();
	
	if (on==orign){
	    // System.out.println("replace "+orig.toString()+" to "+newe);
		orig.replaceWith(newe);
}
	else if(orig.hasnode()){
		 orig.getLeft().replacePart(olde,newe,orig.getLeft());
	     orig.getRight().replacePart(olde,newe,orig.getRight());
	    }
	
	}
	
	public ArrayList<String> inOrderTraversal(Expression e) {
		if (e != null) {
			inOrderTraversal(e.getLeft());
			expressionAsList.add(e.getNode().toString());
			inOrderTraversal(e.getRight());
		}

		return expressionAsList;
	}
	
	public static ArrayList<String> levelorder(Expression e) {
		ArrayList<String> asText = new ArrayList<String>();
		Queue<Expression> nodequeue = new LinkedList<Expression>();
		if (e != null) {
			nodequeue.add(e);
		}
		while (!nodequeue.isEmpty()) {
			Expression next = nodequeue.remove();
			asText.add(next.toString());
			if (next.getLeft() != null) {
				nodequeue.add(next.getLeft());
			}

			if (next.getRight() != null) {
				nodequeue.add(next.getRight());
			}
		}
		return asText;
	}

	public boolean canBeSpecialised(Expression e) {

		resetExpressionAsList();
		setExpressionAsList();
		System.out.println(getExpressionAsList() + " dskdmk");
		
		for (String part : expressionAsList) {
			if (part.contains("?")) {
				return true;
			}
		}
		return false;
	}
	
	
}

