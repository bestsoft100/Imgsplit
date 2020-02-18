package imgsplit.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractButton;
import javax.swing.JTextField;

public class BetterTextField extends JTextField{
	private static final long serialVersionUID = 1L;
	
	private AbstractButton button;
	private KeyListener enterListener = new KeyListener() {
		public void keyTyped(KeyEvent e) {}
		public void keyReleased(KeyEvent e) {}
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				if(button != null) {
					button.doClick();
				}
			}
		}
	};
	
	public BetterTextField() {
		addKeyListener(enterListener);
	}
	
	public void setButton(AbstractButton button) {
		this.button = button;
	}
	
	public AbstractButton getButton() {
		return button;
	}
	
}
