package game;

import java.awt.EventQueue;

import gui.MainWindow;

/**
 * @author Jakob Calero
 * 
 */
public class Main {

	/**
	 * Launches the game. Initialises the main game window and shows the start
	 * panel.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
