package realtime.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUI extends JFrame implements ActionListener {
	private Buffer buffer;
	private CameraDisplay display;

	public GUI(Buffer buffer) {
		this.buffer = buffer;
		setupGUI();
	}

	private void setupGUI() {
		getContentPane().setLayout(new BorderLayout());

		display = new CameraDisplay();
		add(display, BorderLayout.CENTER);

		JButton button = new JButton("Add camera");
		button.addActionListener(this);
		add(button, BorderLayout.SOUTH);

		setPreferredSize(new Dimension(800, 600));
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
	}

	public void addCamera() {
		buffer.addCamera();
		new CameraConnection(buffer);
		pack();
	}

	public void refreshPanel(RawImage image) {
		display.setImage(image.getImage(), image.getIndex());
	}

	public void actionPerformed(ActionEvent e) {
		addCamera();
	}

	static class CameraDisplay extends JPanel {
		private int numCameras;
		private ArrayList<Image> images;

		public CameraDisplay() {
			super();
			images = new ArrayList<Image>();
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			for (int i = 0; i < images.size(); i++)
				g.drawImage(images.get(i), 340 * i, 0, null);
		}

		public void setImage(byte[] data, int index) {
			if (index >= numCameras) {
				numCameras++;
				images.add(new ImageIcon(data).getImage());
				setSize(340 * numCameras, 240); //images are 320x240 but 340x240 makes sure there are some space between
			} else {
				images.set(index, new ImageIcon(data).getImage());
			}
			repaint();
		}
	}
}
