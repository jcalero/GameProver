package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

@SuppressWarnings({ "serial" })
public class DonePanel extends ProofStatePanel {

	private boolean added = false;
	private JLabel lblCongrats;
	private JButton btnNewButton;

	public DonePanel(final DisplayFrame df) {
		setOpaque(false);// or shadow will show after repaint
		parent = df;

		setBounds(10, 130, 880, 630);
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		setLayout(null);

		lblCongrats = new JLabel(
				"<html><center><b>Congratulations! You have proved the theorem!</b><br /><br /><br />Would you like to add it to your list of axioms?</center></html>");
		lblCongrats.setBounds(355, 151, 165, 200);

		btnNewButton = new JButton(
				"<html><center>Yes - add to axioms</center></html>");
		btnNewButton.setSize(100, 30);
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!added) {
					df.addToList();
					added = true;
					lblCongrats
							.setText("<html><center>Now you can try and tackle a more complex theorem!</center></html>");
					btnNewButton.setText("OK");
				} else {
					parent.scrollPanel.setViewportView(null);
				}
			}
		});
		btnNewButton.setBounds(355, 368, 165, 23);

		add(lblCongrats);
		add(btnNewButton);

		JLabel lblYay = new JLabel("YAY!!");
		lblYay.setHorizontalAlignment(SwingConstants.CENTER);
		lblYay.setFont(new Font("Tahoma", Font.PLAIN, 42));
		lblYay.setBounds(355, 45, 165, 70);
		add(lblYay);
		// repaint();
		// paintComponent(g);
	}

	@Override
	protected void paintComponent(Graphics g) {

		// The following worked on Wenhe's installation - why not on mine??
		//
		// try {
		// BufferedImage img=ImageIO.read(new
		// File(this.getClass().getResource("images/win.png").getPath()));
		// g.drawImage(img, 0, 0, 880, 630,null);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

	}

}
