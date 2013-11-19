package realtime.client;

import java.awt.Dimension;

import javax.swing.JFrame;

public class GUI extends JFrame {

	public GUI() {
		setupGUI();
	}

	private void setupGUI() {
		setPreferredSize(new Dimension(500, 500));
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
	}

	public static void main(String[] args) {
		new GUI();
	}
}
