package misc.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;

import misc.IndexedList;

public class DynamicInputWindow extends Gui implements ActionListener{
	
	private IndexedList<JTextComponent> textFields;
	
	private JFrame frame;
	private JPanel panel;
	private JPanel inputs;
	
	private JButton button;
	private GuiListener listener;
	
	private int currentLine = 0;
	private Border border = new JTextField().getBorder();
	private Border etchedBorder = BorderFactory.createEtchedBorder();
	
	public DynamicInputWindow(GuiListener listener, String buttonText, String description) {
		this.listener = listener;
		
		textFields = new IndexedList<JTextComponent>();
		
		frame = new JFrame();
		panel = new JPanel();
		inputs = new JPanel();
		button = GuiUtils.createButton(buttonText, this, new Dimension(80,25));
		
		inputs.setBorder(etchedBorder);
		
		panel.setLayout(new GridBagLayout());
		inputs.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.insets = new Insets(5, 5, 5, 5);
		c.fill = GridBagConstraints.BOTH;
		GuiUtils.cWeight(c, 0, 0);
		GuiUtils.cPos(c, 0, 0, 1, 1);
		panel.add(GuiUtils.createLabel(description),c);
		
		c.insets = new Insets(0, 5, 5, 5);
		c.fill = GridBagConstraints.BOTH;
		GuiUtils.cWeight(c, 1, 1);
		GuiUtils.cPos(c, 0, 1, 1, 1);
		panel.add(inputs,c);
		
		c.insets = new Insets(0, 5, 5, 5);
		c.fill = GridBagConstraints.NONE;
		GuiUtils.cWeight(c, 0, 0);
		GuiUtils.cPos(c, 0, 2, 1, 1);
		panel.add(button,c);
		
		frame.add(panel);
		
		addCloseListener();
		
		frame.setLocationRelativeTo(null);
		frame.setMinimumSize(new Dimension(320,240));
	}
	
	public void show() {
		frame.pack();
		frame.setVisible(true);
	}
	
	public void addInput(String id, String description, String value, boolean large) {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 5, 5, 5);
		if(large) {
			JTextArea ta = new JTextArea(value);
			ta.setBorder(border);
			textFields.add(id, ta);
			GuiUtils.cWeight(c, 1, 0);
			GuiUtils.cPos(c, 0, currentLine++, 2, 1);
			inputs.add(GuiUtils.createLabel(description),c);
			GuiUtils.cWeight(c, 1, 1);
			GuiUtils.cPos(c, 0, currentLine++, 2, 1);
			inputs.add(ta,c);
			
		}else {
			JTextField tf = new JTextField(value);
			textFields.add(id, tf);
			GuiUtils.cWeight(c, 1, 0);
			GuiUtils.cPos(c, 0, currentLine, 1, 1);
			inputs.add(GuiUtils.createLabel(description),c);
			GuiUtils.cWeight(c, 1, 0);
			GuiUtils.cPos(c, 1, currentLine++, 1, 1);
			inputs.add(tf,c);
		}
	}
	
	public JFrame getFrame() {
		return frame;
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == button) {
			listener.triggered(this);
		}
	}
	
	public String getText(String id) {
		return textFields.get(id).getText();
	}
	
	public void print() {
		int i=0;
		while(i<textFields.getSize()) {
			System.out.println("\""+textFields.getId(i)+"\""+":"+"\""+textFields.get(i).getText()+"\"");
			
			i++;
		}
	}
}
