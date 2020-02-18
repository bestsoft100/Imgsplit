package imgsplit.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import imgsplit.ImageListFilter;
import imgsplit.Imgsplit;
import imgsplit.Utils;
import misc.IndexedList;
import misc.StringUtils;

public class ViewPage implements ActionListener{
	private static Insets insets1 = new Insets(2,2,2,2);
	
	private BetterPanel panel;
	
	private BetterTextField textField;
	private BetterTextField searchTextField;
	private JButton buttonLoad;
	private JButton buttonSearchEnter;
	private GridBagConstraints c;
	
	private IndexedList<BufferedImage> imgs;
	private JList<String> list;
	
	private String[] searchResult;
	
	private BufferedImage current;
	private JPanel previewPanel;
	
	private double zoom = 1;
	private int offx;
	private int offy;

	private int xOld;
	private int yOld;
	
	private boolean rmb = false;
	
	public ViewPage() {
		createPage();
	}
	
	public BetterPanel getPanel() {
		return panel;
	}
	
	private void createPage() {
		panel = new BetterPanel();
		panel.setPadding(1);
		
		BetterPanel p2 = new BetterPanel();
		
		textField = new BetterTextField();
		searchTextField = new BetterTextField();
		buttonLoad = new JButton("Load");
		buttonLoad.addActionListener(this);
		buttonSearchEnter = new JButton("Search");
		buttonSearchEnter.addActionListener(this);
		list = new JList<String>();
		JScrollPane listScrollPane = new JScrollPane(list);
		previewPanel = new JPanel() {
			private static final long serialVersionUID = 1L;
			public void paint(Graphics g) {
				super.paint(g);
				if(current != null) {
					g.drawImage(current,
							(int)(offx - (current.getWidth() * zoom / 2)), (int)(offy - (current.getHeight() * zoom / 2)),
							(int)(current.getWidth() * zoom), (int)(current.getHeight() * zoom),
							null
					);
				}
			}
		};
		previewPanel.setBackground(Color.white);
		previewPanel.addMouseMotionListener(previewMouseMotion);
		previewPanel.addMouseWheelListener(previewMouseWheel);
		previewPanel.addKeyListener(previewControls);
		previewPanel.addMouseListener(previewMouse);
		
		textField.setButton(buttonLoad);
		searchTextField.setButton(buttonSearchEnter);
		
		list.addListSelectionListener(listSelectionListener);
		
		
		c = new GridBagConstraints();
		c.insets = insets1;
		c.fill = GridBagConstraints.BOTH;
		
		p2.add(listScrollPane, 0, 0, 1, 1, 0, 1);
		p2.add(previewPanel, 1, 0, 1, 1, 1, 1);
		
		panel.add(textField, 0, 0, 1, 1, 1, 0);
		panel.add(buttonLoad, 1, 0, 1, 1, 0, 0);
		panel.add(searchTextField, 0, 1, 1, 1, 1, 0);
		panel.add(buttonSearchEnter, 1, 1, 1, 1, 0, 0);
		panel.add(p2, 0, 2, 2, 1, 1, 1);
		
		
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == buttonLoad) {
			System.out.println("Press");
			String path = textField.getText();
			if(StringUtils.isEmpty(path))path = Imgsplit.getPath();
			path = Utils.fixPath(path, true);
			
			imgs = Imgsplit.getAllImages(Imgsplit.getInstance().getGui(), path, true, "png", -1, -1);
			
			String[] d = imgs.getIdArray();
			list.setListData(d);
		}else if(e.getSource() == buttonSearchEnter) {
			if(!StringUtils.isEmpty(searchTextField.getText())) {
				searchResult = ImageListFilter.filter(searchTextField.getText(), imgs);
				if(searchResult != null) {
					System.out.println("Found "+searchResult.length+" items");
					list.setListData(searchResult);
				}
			}else {
				String[] id = imgs.getIdArray();
				if(id != null) {
					list.setListData(id);
				}
			}
		}
	}
	
	ListSelectionListener listSelectionListener = new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent e) {
			offx = previewPanel.getWidth() / 2;
			offy = previewPanel.getHeight() / 2;
			current = imgs.get(list.getSelectedValue());
			zoom = Math.min(previewPanel.getWidth()*1.0 / current.getWidth(), previewPanel.getHeight()*1.0 / current.getHeight());
			if(zoom > 8)zoom = 8;
			previewPanel.repaint();
		}
	};
	
	private KeyListener previewControls = new KeyListener() {
		public void keyTyped(KeyEvent e) {
		}
		public void keyReleased(KeyEvent e) {
		}
		public void keyPressed(KeyEvent e) {
		}
	};
	
	private MouseWheelListener previewMouseWheel = new MouseWheelListener() {
		public void mouseWheelMoved(MouseWheelEvent e) {
			int delta = e.getWheelRotation() * -1;
			if(delta > 0)zoom *= 2;
			if(delta < 0)zoom /= 2;
			if(zoom < 0.125)zoom = 0.125;
			if(zoom > 64)zoom = 64;
			previewPanel.repaint();
		}
	};
	
	private MouseMotionListener previewMouseMotion = new MouseMotionListener() {
		public void mouseMoved(MouseEvent e) {
			xOld = e.getX();
			yOld = e.getY();
		}
		public void mouseDragged(MouseEvent e) {
			if(rmb) {
				int dx = e.getX() - xOld;
				int dy = e.getY() - yOld;
				offx += dx;
				offy += dy;
				previewPanel.repaint();
			}
			xOld = e.getX();
			yOld = e.getY();
		}
	};
	
	private MouseListener previewMouse = new MouseListener() {
		public void mouseClicked(MouseEvent e) {
		}
		public void mousePressed(MouseEvent e) {
			if(e.getButton() == 3) {
				rmb = true;
			}
		}
		public void mouseReleased(MouseEvent e) {
			if(e.getButton() == 3) {
				rmb = false;
			}
		}
		public void mouseEntered(MouseEvent e) {
		}
		public void mouseExited(MouseEvent e) {
		}
	};
	
}
