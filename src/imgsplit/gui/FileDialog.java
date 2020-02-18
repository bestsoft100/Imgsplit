package imgsplit.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FileDialog extends JFrame implements ListSelectionListener,ActionListener,KeyListener{
	
	private File currentFolder;
	private JLabel pathLabel;
	private JPanel panel;
	private JList<String> list;
	private JButton buttonCancel;
	private JButton buttonConfirm;
	private JScrollPane scrollPane;
	
	public FileDialog(String path) {
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		pathLabel = new JLabel();
		buttonCancel = new JButton("Cancel");
		buttonConfirm = new JButton("Confirm");
		scrollPane = new JScrollPane(list);
		list = new JList<String>();
		list.addListSelectionListener(this);
		list.addKeyListener(this);
		list.grabFocus();
		panel.setFocusable(false);
		
		buttonCancel.setFocusable(false);
		buttonConfirm.setFocusable(false);
		
		buttonCancel.addActionListener(this);
		buttonConfirm.addActionListener(this);
		
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(15, 10, 3, 10);
		Gui.c(c, 0, 0, 2, 1, 1, 0);
		panel.add(pathLabel,c);
		c.insets = new Insets(10, 10, 10, 10);
		Gui.c(c, 0, 1, 2, 1, 1, 1);
		panel.add(list,c);
		Gui.c(c, 0, 2, 1, 1, 1, 0);
		panel.add(buttonCancel,c);
		Gui.c(c, 1, 2, 1, 1, 1, 0);
		panel.add(buttonConfirm,c);
		
		add(panel);
		setSize(450,450);
		setLocationRelativeTo(null);
		setVisible(true);
		
		selectFolder(path);
	}
	
	private void selectFolder(String path) {
		currentFolder = new File(path).getAbsoluteFile();
		pathLabel.setText(currentFolder.getAbsolutePath());
		
		boolean hasParent = currentFolder.getParentFile() != null;
		System.out.println(hasParent);
		
		File[] files = currentFolder.listFiles();
		
		String[] names = new String[files.length + (hasParent?1:0)];
		names[0] = "..";
		int i=0;
		while(i<files.length) {
			names[i+(hasParent?1:0)] = files[i].getName();
			i++;
		}
		
		list.setListData(names);
	}
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		new FileDialog("");
	}
	
	public void valueChanged(ListSelectionEvent e) {
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == buttonCancel) {
			setVisible(false);
		}
		if(e.getSource() == buttonConfirm) {
			select();
		}
	}
	
	public void select() {
		int index = list.getSelectedIndex();
		String val = list.getSelectedValue();
		
		System.out.println("Select: "+list.getSelectedValue()+" at "+list.getSelectedIndex());
		if(val.equals("..")) {
			selectFolder(currentFolder.getParentFile().getAbsolutePath());
		}
	}

	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			select();
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}


}
