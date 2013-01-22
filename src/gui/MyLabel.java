package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import logic.Expression;

@SuppressWarnings("serial")
public class MyLabel extends JLabel {
	public static final String GOAL = "GOAL";
	public static final String ASSUM = "ASSUMPTION";

	private Color color = Color.RED;
	private Font font = new Font("SansSerif", 1, 26);

	public JLabel fullText;
	private Expression e;

	// private String type;
	public MyLabel() {
		super();
		fullText = null;
	}

	public MyLabel(Expression e, ImageIcon i) {
		this.e = e;
		setText(getTextToShow(e)); // top-level symbol of e

		if (e.toString() != null) {
			setToolTipText(e.toString());
		} else {
			setToolTipText("False");
		}
		setIcon(i);

		setHorizontalTextPosition(JLabel.CENTER);
		setForeground(color);
		setFont(font);

		fullText = new JLabel(e.toString());
		fullText.setHorizontalTextPosition(JLabel.CENTER);
		fullText.setBorder(BorderFactory.createEmptyBorder(-15, -1, -15, -1));
	}

	public void showFullText() {
		Rectangle labelBounds = getBounds();
		JLabel newimg = new JLabel();
		newimg.setBounds(labelBounds.x, labelBounds.y, labelBounds.width,
				labelBounds.height + 15);
		newimg.setBackground(Color.red);
		fullText.setBounds(labelBounds.x, labelBounds.y + labelBounds.height,
				100, 13);
		// this.setBounds(labelBounds.x,
		// labelBounds.y,labelBounds.width,labelBounds.height+);
		if (getParent() != null && e.isComplex() || e.isEqualsExp()// 2012
				|| e.isPlusExp() || e.isTimesExp())
			getParent().add(fullText);
	}

	public JLabel getFullText() {
		return fullText;
	}

	public String getTextToShow(Expression e) {
		return e.getNode();
	}

	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		if (fullText != null) {
			fullText.setBounds(x, y + getBounds().height, getToolTipText()
					.length() * 10, 13);
		}
	}

	public int getWidth() {
		return getIcon().getIconWidth() + 10;
	}

	public int getHeight() {
		return getIcon().getIconHeight();
	}

	public void changeText(Expression newText) {
		setText(getTextToShow(e));

		setToolTipText(e.toString());

		getParent().remove(fullText);

		fullText = new JLabel(e.toString());
		fullText.setHorizontalTextPosition(JLabel.CENTER);
		this.showFullText();
	}
}
