package misc.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;

import misc.IndexedList;

public class DynamicListWindow extends Gui implements ActionListener{
	
	private JFrame frame;
	private JPanel panel;
	private JPanel buttonPanel;
	
	private JList<String> list;
	private DefaultListModel<String> listModel;
	
	private String lastButtonId = null;
	private IndexedList<JButton> buttons = new IndexedList<JButton>();
	private GuiListener listener;
	
	public DynamicListWindow(GuiListener listener, ArrayList<String> list) {
		this(listener ,(String[]) list.toArray());
	}
	
	public DynamicListWindow(GuiListener listener, String[] array) {
		frame = new JFrame();
		panel = new JPanel();
		buttonPanel = new JPanel();
		this.listener = listener;

		listModel = new DefaultListModel<String>();
		
		for(String ln : array) {
			listModel.addElement(ln);
		}
		list = new JList<String>(listModel);
		list.setBorder(Gui.defaultBorder);
		
		frame.pack();
		
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		c.insets = new Insets(5, 5, 0, 5);
		GuiUtils.cPos(c, 0, 0, 1, 1);
		GuiUtils.cWeight(c, 0, 1);
		panel.add(list,c);

		c.insets = new Insets(5, 5, 5, 5);
		GuiUtils.cPos(c, 0, 1, 1, 1);
		GuiUtils.cWeight(c, 1, 0);
		panel.add(buttonPanel,c);
		
		addCloseListener();
		
		frame.add(panel);
		frame.setLocationRelativeTo(null);
	}
	
	public void addButton(String id, String text) {
		JButton b = GuiUtils.createButton(text, this, Gui.defaultButtonSize);
		buttons.add(id,b);
		buttonPanel.add(b);
		frame.pack();
	}
	
	public JFrame getFrame() {
		return frame;
	}

	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton) e.getSource();
		String id = buttons.getIdOfObject(b);
		this.lastButtonId = id;
		
	}
	
	
	
}
