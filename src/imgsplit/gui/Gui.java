package imgsplit.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import imgsplit.Imgsplit;
import imgsplit.WorkThread;
import imgsplit.func.FunctionAdvancedSheet;
import imgsplit.func.FunctionCombine;
import imgsplit.func.FunctionCreateSheet;
import imgsplit.func.FunctionEdit;
import imgsplit.func.FunctionSplit;
import imgsplit.func.FunctionSplitMulti;
import misc.FileUtils;
import misc.Properties;

public class Gui implements ActionListener{
	
	private static Gui instance;
	
	private static Insets paddingOuter = new Insets(8,8,8,8);
	
	public static Gui getInstance() {
		return instance;
	}
	
	private JFrame frame;
	private JPanel panel;
	private JTabbedPane tabs;
	private GridBagLayout gridBagLayout;
	private GridBagConstraints c;
	private ArrayList<GuiPage> pages;
	private JLabel statusBar;
	private JProgressBar progressBar;
	private JButton buttonStart;
	private JButton buttonBatch;
	
	public Gui() {
		instance = this;
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch (Exception e) {}
		
		pages = new ArrayList<GuiPage>();
		
		frame = new JFrame("Imgsplit v"+Imgsplit.getVersion());
		panel = new JPanel();
		tabs = new JTabbedPane();
		gridBagLayout = new GridBagLayout();
		statusBar = new JLabel("Idle");
		progressBar = new JProgressBar();
		
		initFrame();
		
		GuiPage page = new GuiPage("Split Single", new FunctionSplit());
		
		page.addText("Split an image into images with a specific size");
		page.addString("Input Image", "in");
		page.addText("Read the names from a text file\nLeave empty to use create numbered images");
		page.addString("Namelist File", "names");
		page.addStrings("Width", "w", "Height", "h");
		page.addString("Output Folder", "out");
		page.addBoolean("Remove Duplicates", Imgsplit.cmdRemoveDuplicates);
		page.addSpace();
		
		page.setString("out", "Imgsplit Output");
		page.setString("w", "16");
		page.setString("h", "16");
		
		addPage(page);
		
		
		page = new GuiPage("Split Multi", new FunctionSplitMulti());
		
		page.addString("Input Folder", "folder");
		page.addString("Filter", "filter");
		page.addStrings("Width", "w", "Height", "h");
		page.addString("Output Folder", "out");
		page.setString("out", "Imgsplit Output");
		page.addSpace();
		
		page.setString("w", "16");
		page.setString("h", "16");
		
		addPage(page);
		
		
		page = new GuiPage("Create Sheet", new FunctionCreateSheet());
		
		page.addString("Input Folder", "folder");
		page.addBoolean("Include Subdirectories", Imgsplit.cmdIncludeSubdirectories);
		page.addStrings("Width", "w", "Height", "h");
		page.addString("Output Image", "out");
		page.addBoolean("Save Names", "saveNames");
		page.addBoolean("Remove Duplicates", Imgsplit.cmdRemoveDuplicates);
		page.addSpace();
		
		page.setString("w", "16");
		page.setString("h", "16");
		page.setString("out", "Imgsplit Output");
		
		addPage(page);
		
		
		page = new GuiPage("Combine", new FunctionCombine());
		
		page.addString("Background", "bg");
		page.addString("Foreground", "fg");
		page.addStrings("Width", "w", "Height", "h");
		page.addString("Output Folder", "out");
		page.addSpace();

		page.setString("w", "16");
		page.setString("h", "16");
		page.setString("out", "Imgsplit Output");
		
		addPage(page);
		
		
		page = new GuiPage("View", null);
		ViewPage sp = new ViewPage();
		
		page.addPanel(sp.getPanel(), true);
		
		addPage(page);
		
		
		page = new GuiPage("Advanced Sheet", new FunctionAdvancedSheet());
		
		page.addString("Input Folder: ", "folder");
		page.addSpace();
		
		addPage(page);
		
		
		page = new GuiPage("Edit", new FunctionEdit());
		
		page.addString("Input Folder", "in");
		page.addBoolean("Resize", "resize");
		page.addRadioButtons("", "resizeType", new String[] {"Scale","Fixed"}, new String[] {"resizeScale","resizeFixed"});
		
		addPage(page);
		
	}
	
	private void initFrame() {
		JPanel panel2 = new JPanel();
		Dimension dim1 = new Dimension(110,1);
		Dimension dim2 = new Dimension(100,25);
		panel2.setMinimumSize(dim1);
		panel2.setPreferredSize(dim1);
		panel2.setLayout(gridBagLayout);
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(2,20,2,20);
		
		buttonStart = new JButton("Start!");
		buttonStart.setPreferredSize(dim2);
		buttonStart.addActionListener(this);
		buttonBatch = new JButton("Create Batch");
		buttonBatch.setPreferredSize(dim2);
		buttonBatch.addActionListener(this);
		buttonBatch.setToolTipText("Creates a batch file to run the program with the current settings");

		
		int y=0;
		cWeight(c, 0, 0);
		cPos(c, 0, y++, 1, 1);
		panel2.add(new JPanel(),c);
		cWeight(c, 1, 0);
		cPos(c, 0, y++, 1, 1);
		panel2.add(buttonStart,c);
		cWeight(c, 1, 0);
		cPos(c, 0, y++, 1, 1);
		panel2.add(buttonBatch,c);
		cWeight(c, 0, 1);
		cPos(c, 0, y++, 1, 1);
		panel2.add(new JPanel(),c);
		
		panel.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.NORTH;
		cWeight(c, 1, 1);
		cPos(c, 1, 0, 1, 1);
		panel.add(tabs,c);
		cWeight(c, 0, 1);
		cPos(c, 0, 0, 1, 1);
		panel.add(panel2,c);
		
		int p = 3;
		c.insets = new Insets(p,p,p,p);
		cWeight(c, 1, 0);
		cPos(c, 1, 1, 1, 1);
		panel.add(statusBar,c);
		cWeight(c, 0, 0);
		cPos(c, 0, 1, 1, 1);
		panel.add(progressBar,c);
		
		frame.add(panel);
		
		frame.setSize(640, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
	}
	
	public void setVisible(boolean b) {
		frame.setVisible(b);
	}
	
	public void addPage(GuiPage p) {
		JPanel  containerPanel = new JPanel();
		containerPanel.setLayout(gridBagLayout);
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.insets = paddingOuter;
		cWeight(gridBagConstraints, 1, 1);
		containerPanel.add(p.getPanel(),gridBagConstraints);
		
		tabs.addTab(p.getName(), containerPanel);
		pages.add(p);
	}
	
	public static void cPos(GridBagConstraints c, int x, int y, int w, int h) {
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = w;
		c.gridheight = h;
	}
	
	public static void c(GridBagConstraints c, int x, int y, int w, int h, double wx, double wy) {
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = w;
		c.gridheight = h;
		c.weightx = wx;
		c.weighty = wy;
	}
	
	public static void cWeight(GridBagConstraints c, double x, double y) {
		c.weightx = x;
		c.weighty = y;
	}
	
	public JLabel getStatusBar() {
		return statusBar;
	}
	
	public JProgressBar getProgressBar() {
		return progressBar;
	}

	public JFrame getFrame() {
		return frame;
	}
	
	public void actionPerformed(ActionEvent e) {
		JPanel panel = (JPanel) tabs.getSelectedComponent();
		for(GuiPage page : pages) {
			if(panel.getComponent(0).equals(page.getPanel())) {
				if(page.getFunction() == null) {
					JOptionPane.showMessageDialog(getFrame(), "This Page has no function!", "", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if(e.getSource() == buttonStart) {
					WorkThread.getInstance().setAction(page.getFunction(), page.toProperties());
				}else if(e.getSource() == buttonBatch) {
					String batch = "java -jar Imgsplit.jar";
					
					Properties p = page.toProperties();
					
					p.set("nogui", "true");
					p.set("method",page.getFunction().getMethodName());
					
					String[] str1 = p.getIds();
					String[] str2 = p.getValues();
					
					int i=0;
					while(i<str1.length) {
						batch += " "+str1[i].replace(' ', '?')+":"+str2[i].replace(' ', '?');
						i++;
					}
					
					FileUtils.writeFile(Imgsplit.getPath() + "/" + "imgsplit-"+p.getString("method")+".bat", batch);
				}else {
					throw new RuntimeException();
				}
			}
		}
	}

	
}
