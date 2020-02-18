package misc.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;

public abstract class GuiUtils {
	
	public static void cPos(GridBagConstraints c, int x, int y, int w, int h) {
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = w;
		c.gridheight = h;
	}
	
	public static void cWeight(GridBagConstraints c, int x, int y) {
		c.weightx = x;
		c.weighty = y;
	}
	
	public static void cWeight(GridBagConstraints c, double x, double y) {
		c.weightx = x;
		c.weighty = y;
	}
	
	public static JMenuItem createMenuItem(String name, ActionListener actionListener) {
		JMenuItem m = new JMenuItem(name);
		m.addActionListener(actionListener);
		return m;
	}
	
	public static JButton createButton(String name, ActionListener actionListener, Dimension size) {
		JButton b = new JButton(name);
		b.addActionListener(actionListener);
		b.setPreferredSize(size);
		b.setMinimumSize(size);
		return b;
	}

	public static JLabel createLabel(String text) {
		return new JLabel(text);
	}
	
}
