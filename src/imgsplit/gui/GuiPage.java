package imgsplit.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.management.RuntimeErrorException;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import imgsplit.func.Function;
import misc.IndexedList;
import misc.Properties;
import misc.StringUtils;

public class GuiPage{

	public static Insets paddingBig = new Insets(5,5,5,5);
	public static Insets paddingSmall = new Insets(1,1,1,1);
	
	private String name;
	private JPanel panel;
	private IndexedList<JTextField> stringInputs;
	private IndexedList<JToggleButton> booleanInputs;
	private IndexedList<ButtonGroup> buttonGroups;
	private GridBagConstraints c;
	private int y;
	private Function function;
	
	public GuiPage(String name, Function function) {
		this.name = name;
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		this.function = function;
		
		c = new GridBagConstraints();
		
		stringInputs = new IndexedList<JTextField>();
		booleanInputs = new IndexedList<JToggleButton>();
		buttonGroups = new IndexedList<ButtonGroup>();
	}
	
	public JPanel getPanel() {
		return panel;
	}
	
	public void addString(String description, String inputId) {
		if(existsOptionWithId(inputId)) {
			throw new RuntimeException("ID "+inputId+" already used");
		}
		JLabel l = new JLabel(description);
		JTextField t = new JTextField();

		c.insets = paddingBig;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		Gui.cWeight(c, 0, 0);
		Gui.cPos(c, 0, y, 1, 1);
		panel.add(l,c);
		Gui.cWeight(c, 1, 0);
		Gui.cPos(c, 1, y, 3, 1);
		panel.add(t,c);
		
		l.setToolTipText("Input-ID: "+inputId);
		
		stringInputs.add(inputId, t);
		
		y++;
	}
	
	public void addBoolean(String descritpion, String inputId) {
		if(existsOptionWithId(inputId)) {
			throw new RuntimeException("ID "+inputId+" already used");
		}
		JCheckBox b = new JCheckBox(descritpion);

		c.insets = paddingBig;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		
		Gui.cPos(c, 0, y, 4, 1);
		panel.add(b,c);

		b.setToolTipText("Input-ID: "+inputId);
		
		booleanInputs.add(inputId, b);
		y++;
	}
	
	public void addPanel(JPanel panel, boolean maxSize) {
		c.insets = paddingSmall;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		if(maxSize) {
			c.weighty = 1;
		}else {
			c.weighty = 0;
		}
		
		Gui.cPos(c, 0, y, 4, 1);
		this.panel.add(panel,c);
		
		y++;
	}
	
	public void addStrings(String description, String inputId, String description2, String inputId2) {
		if(existsOptionWithId(inputId)) {
			throw new RuntimeException("ID "+inputId+" already used");
		}
		if(existsOptionWithId(inputId2)) {
			throw new RuntimeException("ID "+inputId2+" already used");
		}
		JLabel l = new JLabel(description);
		JTextField t = new JTextField();
		JLabel l2 = new JLabel(description2);
		JTextField t2 = new JTextField();

		c.insets = paddingBig;
		c.fill = GridBagConstraints.HORIZONTAL;
		Gui.cWeight(c, 0, 0);
		Gui.cPos(c, 0, y, 1, 1);
		panel.add(l,c);
		Gui.cWeight(c, 1, 0);
		Gui.cPos(c, 1, y, 1, 1);
		panel.add(t,c);
		Gui.cWeight(c, 0, 0);
		Gui.cPos(c, 2, y, 1, 1);
		panel.add(l2,c);
		Gui.cWeight(c, 1, 0);
		Gui.cPos(c, 3, y, 1, 1);
		panel.add(t2,c);

		l.setToolTipText("Input-ID: "+inputId);
		l2.setToolTipText("Input-ID: "+inputId2);
		
		stringInputs.add(inputId, t);
		stringInputs.add(inputId2, t2);
		
		y++;
	}
	
	public void addRadioButtons(String description, String group, String[] valueNames, String[] valueIds) {
		if(valueIds.length != valueNames.length) {
			throw new RuntimeException();
		}
		int i=0;
		while(i<valueIds.length) {
			if(existsOptionWithId(valueIds[i])) {
				throw new RuntimeException("ID "+valueIds[i]+" already used");
			}
			i++;
		}
		createButtonGroup(group);
		ButtonGroup gr = buttonGroups.get(group);
		
		int count = valueIds.length;
		
		JPanel p = new JPanel();
		i=0;
		while(i<count) {
			JRadioButton b = new JRadioButton(valueNames[i]);
			gr.add(b);
			booleanInputs.add(valueIds[i], b);
			p.add(b);
			i++;
		}

		Gui.cWeight(c, 0, 0);
		Gui.cPos(c, 0, y, 1, 1);
		panel.add(new JLabel(description),c);
		Gui.cWeight(c, 1, 0);
		Gui.cPos(c, 1, y, 3, 1);
		panel.add(p,c);

		y++;
	}
	
	public void addSpace() {
		Gui.cWeight(c, 1, 1);
		Gui.cPos(c, 0, y, 4, 1);
		panel.add(new JPanel(),c);
		y++;
	}
	
	public void addText(String str) {
		String[] split = StringUtils.split(str, '\n');
		c.anchor = GridBagConstraints.WEST;
		for(String ln : split) {
			Gui.cWeight(c, 1, 0);
			Gui.cPos(c, 0, y, 4, 1);
			panel.add(new JLabel(ln),c);
			y++;
		}
	}
	
	public JTextField getStringInput(String inputId) {
		return stringInputs.get(inputId);
	}
	
	public void setString(String inputId, String value) {
		stringInputs.get(inputId).setText(value);
	}
	
	public String getString(String inputId) {
		return stringInputs.get(inputId).getText();
	}
	
	public String getName() {
		return name;
	}

	public Properties toProperties() {
		Properties p = new Properties();
		
		int i=0;
		while(i<stringInputs.getSize()) {
			p.set(stringInputs.getId(i), stringInputs.get(i).getText());
			i++;
		}
		
		i=0;
		while(i<booleanInputs.getSize()) {
			p.set(booleanInputs.getId(i), ""+booleanInputs.get(i).isSelected());
			i++;
		}
		
		return p;
	}
	
	public Function getFunction() {
		return function;
	}
	
	public boolean existsButtonGroup(String id) {
		int i=0;
		while(i<buttonGroups.getSize()) {
			if(buttonGroups.getId(i).equals(id)) {
				return true;
			}
			i++;
		}
		return false;
	}
	
	public void createButtonGroup(String id) {
		if(!existsButtonGroup(id)) {
			buttonGroups.add(id, new ButtonGroup());
		}
	}
	
	public boolean existsOptionWithId(String id) {
		int i=0;
		while(i<stringInputs.getSize()) {
			if(stringInputs.getId(i).equals(id)) {
				return true;
			}
			i++;
		}
		i=0;
		while(i<booleanInputs.getSize()) {
			if(booleanInputs.getId(i).equals(id)) {
				return true;
			}
			i++;
		}
		return false;
	}
	
}
