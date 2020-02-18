package misc.gui;

import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.border.Border;

public abstract class Gui {

	public static final Border defaultBorder = new JTextField().getBorder();
	public static final Dimension defaultButtonSize = new Dimension(80,25);
	
	public abstract JFrame getFrame();
	
	protected void addCloseListener() {
		JFrame f = getFrame();
		f.addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent e) {
			}
			public void windowIconified(WindowEvent e) {
			}
			public void windowDeiconified(WindowEvent e) {
			}
			public void windowDeactivated(WindowEvent e) {
			}
			public void windowClosing(WindowEvent e) {
				f.setVisible(false);
				f.dispose();
			}
			public void windowClosed(WindowEvent e) {
			}
			public void windowActivated(WindowEvent e) {
			}
		});
		f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	public void show() {
		getFrame().setVisible(true);
	}
	
	public void dispose() {
		getFrame().setVisible(false);
		getFrame().dispose();
	}
	
}
