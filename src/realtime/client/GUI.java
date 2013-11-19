package realtime.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUI extends JFrame {
	private ArrayList<ImagePanel> panels;

	public GUI() {
		setupGUI();
	}

	private void setupGUI() {
		panels = new ArrayList<ImagePanel>();
		
		ImagePanel panel = new ImagePanel();
		panels.add(panel);
		add(panel);
		
		setPreferredSize(new Dimension(500, 500));
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
	}
	
	public void refreshPanel(ImageStruct image){
		panels.get(image.getIndex()).refresh(image.getImage());
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
