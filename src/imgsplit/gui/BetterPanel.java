package imgsplit.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

public class BetterPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private GridBagLayout gridBagLayout;
	private GridBagConstraints gridBagConstraints;
	
	public BetterPanel() {
		gridBagLayout = new GridBagLayout();
		gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		
		setLayout(gridBagLayout);
		
	}
	
	public void add(Component comp, int x, int y, int width, int height) {
		gridBagConstraints.gridx = x;
		gridBagConstraints.gridy = y;
		gridBagConstraints.gridwidth = width;
		gridBagConstraints.gridheight = height;
		add(comp,gridBagConstraints);
	}
	
	public void add(Component comp, int x, int y, int width, int height, double weightx, double weighty) {
		gridBagConstraints.weightx = weightx;
		gridBagConstraints.weighty = weighty;
		add(comp, x, y, width, height);
	}
	
	public void setPadding(int padding) {
		gridBagConstraints.insets = new Insets(padding, padding, padding, padding);
	}
	
}
