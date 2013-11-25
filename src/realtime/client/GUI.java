package realtime.client;

import java.awt.BorderLayout;
import realtime.server.OpCodes;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

@SuppressWarnings("serial")
public class GUI extends JFrame {
	private Buffer buffer;
	private CameraDisplay display;

	JPanel buttonPanel = new JPanel(new GridBagLayout());

	public GUI(Buffer buffer) {
		this.buffer = buffer;
		setupGUI();
	}

	private void setupGUI() {
		getContentPane().setLayout(new BorderLayout());

		display = new CameraDisplay();
		add(display, BorderLayout.CENTER);

		setupButtonPanel();

		setPreferredSize(new Dimension(800, 600));
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
	}

	private void setupButtonPanel() {

		add(buttonPanel, BorderLayout.SOUTH);

		JButton camButton = new JButton("Add camera");
		camButton.addActionListener(new AddCameraListener());
		addToPanel(camButton, 0, 0, 4);

		JLabel labelMode = new JLabel("Mode");
		addToPanel(labelMode, 1, 0);

		JRadioButton modeAuto = new JRadioButton("Auto");
		modeAuto.setActionCommand(String.valueOf(SetModeListener.AUTO));
		modeAuto.setSelected(true);
		addToPanel(modeAuto, 1, 1);

		JRadioButton modeIdle = new JRadioButton("Idle");
		modeIdle.setActionCommand(String.valueOf(SetModeListener.IDLE));
		addToPanel(modeIdle, 1, 2);

		JRadioButton modeMovie = new JRadioButton("Movie");
		modeMovie.setActionCommand(String.valueOf(SetModeListener.MOVIE));
		addToPanel(modeMovie, 1, 3);

		ButtonGroup modeGroup = new ButtonGroup();
		modeGroup.add(modeAuto);
		modeGroup.add(modeIdle);
		modeGroup.add(modeMovie);
		new SetModeListener(modeAuto, modeIdle, modeMovie);

		JLabel labelSync = new JLabel("Sync");
		addToPanel(labelSync, 2, 0);

		JRadioButton syncAuto = new JRadioButton("Auto");
		syncAuto.setActionCommand(String.valueOf(SetSynchListener.AUTO));
		addToPanel(syncAuto, 2, 1);

		JRadioButton syncOn = new JRadioButton("On");
		syncOn.setSelected(true);
		syncOn.setActionCommand(String.valueOf(SetSynchListener.ON));
		addToPanel(syncOn, 2, 2);

		JRadioButton syncOff = new JRadioButton("Off");
		syncOff.setActionCommand(String.valueOf(SetSynchListener.OFF));
		addToPanel(syncOff, 2, 3);

		ButtonGroup syncGroup = new ButtonGroup();
		syncGroup.add(syncAuto);
		syncGroup.add(syncOn);
		syncGroup.add(syncOff);
		new SetSynchListener(syncAuto, syncOn, syncOff);

	}

	private void addToPanel(Component comp, int x, int y) {
		addToPanel(comp, x, y, 1);
	}

	private void addToPanel(Component comp, int x, int y, int height) {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.PAGE_START;
		c.insets = new Insets(height, height, height, height);
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = 1;
		c.gridheight = height;
		buttonPanel.add(comp, c);
	}

	public void addCamera() {
		buffer.addCamera();
		pack();
	}

	public void refreshPanel(RawImage image) {
		display.setImage(image);
	}

	private class AddCameraListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			addCamera();
		}

	}

	private class SetModeListener implements ActionListener {
		public final static int AUTO = 0;
		public final static int IDLE = 1;
		public final static int MOVIE = 2;

		public SetModeListener(JRadioButton... buttons) {
			for (JRadioButton button : buttons) {
				button.addActionListener(this);
			}
		}

		public void actionPerformed(ActionEvent e) {
			int choice = Integer.parseInt(((JRadioButton) e.getSource()).getActionCommand());

			switch (choice) {
			case AUTO:
//				buffer.setMode(realtime.server.OpCodes.set);
				break;
			case IDLE:
				buffer.setMode(OpCodes.SET_IDLE);
				break;
			case MOVIE:
				buffer.setMode(OpCodes.SET_MOVIE);
				break;
			}
		}
	}

	private class SetSynchListener implements ActionListener {
		public final static int AUTO = 0;
		public final static int ON = 1;
		public final static int OFF = 2;

		public SetSynchListener(JRadioButton... buttons) {
			for (JRadioButton button : buttons) {
				button.addActionListener(this);
			}
		}

		public void actionPerformed(ActionEvent e) {
			int choice = Integer.parseInt(((JRadioButton) e.getSource()).getActionCommand());

			switch (choice) {
			case AUTO:
//				buffer.setSynch(Buffer.MODE_AUTO);
				break;
			case ON:
				buffer.setSynch(Buffer.MODE_SYNCH);
				break;
			case OFF:
				buffer.setSynch(Buffer.MODE_ASYNCH);
				break;
			}
		}
	}

	static class CameraDisplay extends JPanel {
		private int numCameras;
		private ArrayList<Image> images;
		private ArrayList<Long> delays;

		public CameraDisplay() {
			super();
			images = new ArrayList<Image>();
			delays = new ArrayList<Long>();
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			for (int i = 0; i < images.size(); i++) {
				g.drawImage(images.get(i), 340 * i, 0, null);
				g.drawString(delays.get(i).toString(), 340 * i + 160, 250);
			}
		}

		public void setImage(RawImage rawImage) {
			int index = rawImage.getIndex();
			byte[] data = rawImage.getImage();
			if(index >= numCameras) {
				numCameras++;
				images.add(new ImageIcon(data).getImage());
				delays.add(rawImage.getDelay());
				setSize(340 * numCameras, 260);
			} else {
				images.set(index, new ImageIcon(data).getImage());
				delays.set(index, rawImage.getDelay());
			}
			repaint();
		}
	}
}
