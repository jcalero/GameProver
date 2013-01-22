package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import logic.Expression;

@SuppressWarnings({ "serial" })
public class SpecialiseDialog extends JDialog {

	private final JPanel pnlSpecialise = new JPanel();
	private Expression origExp;
	private Expression exp;
	private JLabel lblOrigExp;
	private ProofStatePanel panelToAddTo;
	/**
	 * Create the dialog.
	 */
	public SpecialiseDialog() {
		exp = new Expression();
		origExp = new Expression();
		setTitle("Please specialise the theorem...");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		pnlSpecialise.setBorder(new EmptyBorder(5, 5, 5, 5));

		getContentPane().add(pnlSpecialise, BorderLayout.CENTER);
		pnlSpecialise.setLayout(new BoxLayout(pnlSpecialise, BoxLayout.Y_AXIS));

		JPanel origExpPanel = new JPanel();
		pnlSpecialise.add(origExpPanel);

		lblOrigExp = new JLabel("N/A");
		origExpPanel.add(lblOrigExp);
		lblOrigExp.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblOrigExp.setHorizontalAlignment(SwingConstants.CENTER);

		JPanel pnlButtons = new JPanel();
		pnlButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(pnlButtons, BorderLayout.SOUTH);

		JButton btnOK = new JButton("OK");
		btnOK.setActionCommand("OK");
		btnOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				okHandler();
			}
		});
		pnlButtons.add(btnOK);
		getRootPane().setDefaultButton(btnOK);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setActionCommand("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancelHandler();
			}
		});
		pnlButtons.add(btnCancel);

		setVisible(true);
	}

	public SpecialiseDialog(Expression e) {
		this();
		exp = e;
		origExp = e;
		lblOrigExp.setText(exp.toString());

		ArrayList<String> expVars = exp.getExpressionAsList();
		ArrayList<String> expVarsNoDup = new ArrayList<String>();
		Iterator<String> expVarsIt = expVars.iterator();
		System.out.println("expVars = " + expVars);

		//remove duplicates
		for (int i = 0; i < expVars.size(); i++) {
			String str = expVars.get(i);
			if (!expVarsNoDup.contains(str) && str.contains("?"))
				expVarsNoDup.add(str);
		}
		System.out.println("expVarsNoDup = " + expVarsNoDup);

		expVarsIt = expVarsNoDup.iterator();
		while (expVarsIt.hasNext()) {	

			final String var = expVarsIt.next();
			JLabel lblVar= new JLabel("'" + var + "' turns into");

			JTextField txtNewVar = new JTextField();
			txtNewVar.setColumns(10);
			txtNewVar.putClientProperty("original_variable", var);
            //txtNewVar.setText(var);
			JPanel varPanel = new JPanel();
			varPanel.add(lblVar);
			varPanel.add(txtNewVar);
			pnlSpecialise.add(varPanel);
		}
	}

	public SpecialiseDialog(Expression e, ProofStatePanel ps) {
		this(e);
		panelToAddTo = ps;
	}

	public void subVars() {
		HashMap<String, String> hm = new HashMap <String, String>();

		JPanel specialise = (JPanel)getContentPane().getComponent(0);
		Component[] comps = specialise.getComponents();

		for (int i = 1; i < comps.length; i++) {
			JPanel var = (JPanel)comps[i];
			if (var.getComponent(1) instanceof JTextField) {
				JTextField tf = (JTextField) var.getComponent(1);
				if (!tf.getText().isEmpty()) {
					hm.put((String)tf.getClientProperty("original_variable"), tf.getText());
				}
			}
		}

		exp = Expression.replaceAll(hm.keySet().toArray(), hm.values().toArray(), origExp);
	}

	public void okHandler() {
		panelToAddTo.parent.throwIn.setVisible(false);
		subVars();
		
		if (exp.toString().contains("?")) {
			panelToAddTo.logicState.addAssumVar
			(exp);			
		} else {
			panelToAddTo.logicState.addAssum(exp);
		}
		panelToAddTo.logicState.removeAssumVar(origExp);
		panelToAddTo.parent.updateFrame();
		dispose();
	}

	public void cancelHandler() {
		dispose();
	}
}
