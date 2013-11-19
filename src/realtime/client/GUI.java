package realtime.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GUI extends JFrame implements ActionListener{
	private ArrayList<ImagePanel> panels;
	private JPanel topPanel;
	private Buffer buffer;

	public GUI(Buffer buffer) {
		this.buffer = buffer;
		setupGUI();
	}
	
	
	private void setupGUI() {
		panels = new ArrayList<ImagePanel>();
		
		getContentPane().setLayout(new BorderLayout());
		
		topPanel = new JPanel(new FlowLayout());
		add(topPanel, BorderLayout.NORTH);
		
		JButton button = new JButton("Add camera");
		button.addActionListener(this);
		add(button, BorderLayout.SOUTH);
		
		setPreferredSize(new Dimension(500, 500));
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
	}
	
	public void addCamera(){
		ImagePanel panel = new ImagePanel();
		panels.add(panel);
		topPanel.add(panel);
		buffer.addCamera();
		new CameraConnection(buffer);
		pack();
	}
	
	public void refreshPanel(Image image){
		panels.get(image.getIndex()).refresh(image.getImage());
	}
	
	public void actionPerformed(ActionEvent e) {
		addCamera();
		JOptionPane.showMessageDialog(null, "Camera added!");
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
			java.awt.Image img = getToolkit().createImage(data);
			getToolkit().prepareImage(img, -1, -1, null);
			icon.setImage(img);
			icon.paintIcon(this, this.getGraphics(), 5, 5);
		}
	}
}
