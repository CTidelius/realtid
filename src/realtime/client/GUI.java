package realtime.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUI extends JFrame {
	private ImagePanel leftPanel, rightPanel;

	public GUI() {
		setupGUI();
	}

	private void setupGUI() {
		add(leftPanel = new ImagePanel());
		add(rightPanel = new ImagePanel());
		
		setPreferredSize(new Dimension(500, 500));
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
	}

	public static void main(String[] args) {
		new GUI();
	}

	static class ImagePanel extends JPanel {
		ImageIcon icon;

		public ImagePanel() {
			super();
			icon = new ImageIcon();
			JLabel label = new JLabel(icon);
			add(label, BorderLayout.CENTER);
			this.setSize(200, 200);
		}

		public void refresh(byte[] data) {
			Image img = getToolkit().createImage(data);
			getToolkit().prepareImage(img, -1, -1, null);
			icon.setImage(img);
			icon.paintIcon(this, this.getGraphics(), 5, 5);
		}
	}
}
