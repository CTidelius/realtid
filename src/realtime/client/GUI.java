package realtime.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUI extends JFrame {
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

		JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
		JButton camButton = new AddCameraButton("Add camera");
		buttonPanel.add(camButton, BorderLayout.SOUTH);
		JButton modeButton = new SetModeButton("Set mode movie/idle");
		buttonPanel.add(modeButton, BorderLayout.SOUTH);
		JButton synchButton = new SetSynchButton("Set synch on/off");
		buttonPanel.add(synchButton);
		add(buttonPanel, BorderLayout.SOUTH);

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

	private class SetModeButton extends JButton implements ActionListener {

		private SetModeButton(String name) {
			super(name);
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			// TODO: set movie/idle
		}
	}

	private class SetSynchButton extends JButton implements ActionListener {

		private SetSynchButton(String name) {
			super(name);
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			// TODO: set synch on/off
		}
	}

	private class AddCameraButton extends JButton implements ActionListener {

		private AddCameraButton(String name) {
			super(name);
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			addCamera();
		}

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
				setSize(340 * numCameras, 240); // images are 320x240 but
												// 340x240 makes sure there are
												// some space between
			} else {
				images.set(index, new ImageIcon(data).getImage());
			}
			repaint();
		}
	}
}
