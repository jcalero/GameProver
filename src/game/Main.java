package game;

import java.awt.EventQueue;
import java.io.FileNotFoundException;

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
					SaveManager saveManager = new SaveManager();
					MainWindow window = new MainWindow(saveManager);
					try {
						saveManager.loadAutoSaveFile();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					window.getMainFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
