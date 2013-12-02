package realtime.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class GUI extends JFrame implements Observer {
	private Buffer buffer;
	private CameraDisplay display;
	private JLabel labelSync, labelMode;

	JPanel buttonPanel = new JPanel(new GridBagLayout());

	public GUI(Buffer buffer) {
		this.buffer = buffer;
		buffer.addObserver(this);
		setupGUI();
	}

	private void setupGUI() {
		getContentPane().setLayout(new BorderLayout());

		display = new CameraDisplay();
		add(display, BorderLayout.CENTER);

		setupButtonPanel();

		setPreferredSize(new Dimension(700, 400));
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
	}

	private void setupButtonPanel() {

		add(buttonPanel, BorderLayout.SOUTH);

		JButton camButton = new JButton("Add camera");
		camButton.addActionListener(new AddCameraListener());
		addToPanel(camButton, 0, 0, 4);

		labelMode = new JLabel("Mode (Idle)");
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

		labelSync = new JLabel("Sync (On)");
		addToPanel(labelSync, 2, 0);

		JRadioButton syncAuto = new JRadioButton("Auto");
		syncAuto.setActionCommand(String.valueOf(SetSynchListener.AUTO));
		syncAuto.setSelected(true);
		addToPanel(syncAuto, 2, 1);

		JRadioButton syncOn = new JRadioButton("On");
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

	public void update(Observable arg0, Object arg1) {
		String sync = buffer.getSync() == Buffer.SYNC_ON ? "On" : "Off";
		labelSync.setText("Sync (" + sync + ")");
		String mode = buffer.getMode() == Buffer.MODE_IDLE ? "Idle" : "Movie";
		labelMode.setText("Mode (" + mode + ")");
		display.setLastMotionIndex(buffer.getLastMotionIndex());
	}

	public void addCamera() {
		createInputFrame();

	}

	private void createInputFrame() {
		final JFrame frame = new JFrame("Enter host and port");

		JButton okButton = new JButton("OK");
		JButton cancelButton = new JButton("Cancel");

		final JTextField hostInput = new JTextField(15);
		final JTextField portInput = new JTextField(15);

		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				String host = hostInput.getText();
				int port = 0;
				try {
					port = Integer.parseInt(portInput.getText());
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Invalid port!");
					return;
				}
				buffer.addCamera(host, port);
				pack();
			}

		});

		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				frame.dispose();
			}

		});

		Container pane = frame.getContentPane();
		pane.setLayout(new GridLayout(3, 2));
		pane.add(new JLabel("Host:"));
		pane.add(hostInput);
		pane.add(new JLabel("Port:"));
		pane.add(portInput);
		pane.add(okButton);
		pane.add(cancelButton);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.pack();

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

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
				buffer.setGuiMode(Buffer.MODE_AUTO);
				break;
			case IDLE:
				buffer.setGuiMode(Buffer.MODE_IDLE);
				break;
			case MOVIE:
				buffer.setGuiMode(Buffer.MODE_MOVIE);
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
				buffer.setGuiSync(Buffer.SYNC_AUTO);
				break;
			case ON:
				buffer.setGuiSync(Buffer.SYNC_ON);
				break;
			case OFF:
				buffer.setGuiSync(Buffer.SYNC_OFF);
				break;
			}
		}
	}

	static class CameraDisplay extends JPanel {
		private int numCameras;
		private ArrayList<Image> images;
		private ArrayList<Long> delays;
		private int lastMotionIndex;

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
				g.setColor(Color.RED);
				if (i == lastMotionIndex) {
					g.drawRect(340 * i, 0, 320, 240);
					g.drawString("X", 340 * i + 160, 265);
				}
				g.setColor(Color.BLACK);
			}
		}

		public void setLastMotionIndex(int lastMotionIndex) {
			this.lastMotionIndex = lastMotionIndex;
		}

		public void setImage(RawImage rawImage) {
			int index = rawImage.getIndex();
			byte[] data = rawImage.getImage();
			if (index >= numCameras) {
				numCameras++;
				images.add(new ImageIcon(data).getImage());
				delays.add(rawImage.getDelay());
				setSize(340 * numCameras, 280);
			} else {
				images.set(index, new ImageIcon(data).getImage());
				delays.set(index, rawImage.getDelay());
			}
			repaint();
		}
	}
}
