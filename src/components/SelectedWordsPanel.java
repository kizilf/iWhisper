package components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SelectedWordsPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	private ArrayList<JLabel> selectedWords = null;
	private final int NUM_SELECTED_CARDS_TO_SHOW = 7;
	private int start_idx = 0;
	private int end_idx = NUM_SELECTED_CARDS_TO_SHOW;
	private JLabel back_btn = null;
	private JLabel forward_btn = null;
	private ArrayList<JPanel> card_panels = null;
	
	public SelectedWordsPanel() {
		super();
		this.selectedWords = new ArrayList<JLabel>();
		//this.setBackground(Color.GREEN);
		
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		back_btn = new JLabel();
		back_btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(start_idx != 0 && selectedWords.size() > NUM_SELECTED_CARDS_TO_SHOW)
				{
					start_idx -= 1;
					end_idx -= 1;
				}
			}
		});
		
		ClassLoader loader = getClass().getClassLoader();
		BufferedImage backBuffImg = null;
		try {
			
			backBuffImg = ImageIO.read(loader.getResource("src/resources/icons/leftarrow64.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//backBuffImg = resize(backBuffImg, this.getWidth() / 2, this.getHeight());
		ImageIcon imageIcon_back = new ImageIcon(backBuffImg);
		back_btn.setIcon(imageIcon_back);
		this.add(back_btn);
		back_btn.setVisible(false);
		
		card_panels = new ArrayList<>();
		for (int i = 0; i < NUM_SELECTED_CARDS_TO_SHOW; i++) {
			JPanel j = new JPanel();
			j.setLayout(new GridLayout());
			card_panels.add(j);
			this.add(j);
		}
		
		//c.fill = GridBagConstraints.VERTICAL;
		forward_btn = new JLabel();
		forward_btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				if(end_idx != selectedWords.size() && selectedWords.size() > NUM_SELECTED_CARDS_TO_SHOW)
				{
					start_idx += 1;
					end_idx += 1;
				}
			}
		});
		
		BufferedImage forwardBuffImg = null;
		try {
			forwardBuffImg = ImageIO.read(loader.getResource("src/resources/icons/rightarrow64.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//forwardBuffImg = resize(forwardBuffImg, this.getWidth() / 2, this.getHeight());
		ImageIcon imageIcon_forward = new ImageIcon(forwardBuffImg);
		forward_btn.setIcon(imageIcon_forward);
		this.add(forward_btn);
		forward_btn.setVisible(false);

	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (JPanel cardPanel : card_panels) {
			cardPanel.removeAll();
		}
		
		if(selectedWords.size() > NUM_SELECTED_CARDS_TO_SHOW) { // Too many cards to show
			int count = 0;
			for (int i = start_idx; i < end_idx; i++) {
				card_panels.get(count++).add(selectedWords.get(i));
			}

			forward_btn.setVisible(true);
			back_btn.setVisible(true);
			if(start_idx == 0) back_btn.setVisible(false);
			if(end_idx == selectedWords.size()) forward_btn.setVisible(false);
		}
		else { // Enough cards to show
			for (int i = 0; i < selectedWords.size(); i++) {
				card_panels.get(i).add(selectedWords.get(i));
			}
			forward_btn.setVisible(false);
			back_btn.setVisible(false);
		}
		
		this.revalidate();
		this.repaint();
		//System.out.println("There are "+ selectedWords.size() + " word(s) selected, start_idx = "+ start_idx + " end_idx = "+ end_idx);
	}
	
	public void addWord(String word, BufferedImage icon) {
		BufferedImage resized_img = CardsCollection.resize(icon, 80 , 85);
		ImageIcon imageIcon = new ImageIcon(resized_img);
		JLabel selectedWord = new JLabel(imageIcon);
		selectedWord.setText(word);
		selectedWord.setHorizontalTextPosition(JLabel.CENTER);
		selectedWord.setVerticalTextPosition(JLabel.BOTTOM);
		selectedWord.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				deleteWord(selectedWord);
			}
		});
		
		this.selectedWords.add(selectedWord);
		if(selectedWords.size() <= NUM_SELECTED_CARDS_TO_SHOW) {
			start_idx = 0;
			end_idx = selectedWords.size();
		}
		
		if(selectedWords.size() > NUM_SELECTED_CARDS_TO_SHOW && end_idx + 1 == selectedWords.size()) {
			start_idx += 1;
			end_idx = selectedWords.size();
		}

	}
	
	private void deleteWord(JLabel word) {
		selectedWords.remove(word);
		if(selectedWords.size() <= NUM_SELECTED_CARDS_TO_SHOW) {
			start_idx = 0;
			end_idx = selectedWords.size();
		}
		else {
			if(start_idx -1 >= 0) {
				start_idx -= 1;
				end_idx -= 1;
			}
		}

	}
	
	public String getSelectedWords() {
		StringBuilder str = new StringBuilder();
		for (JLabel word : selectedWords) {
			str.append(word.getText() + " ");
		}
		System.out.println(str.toString());
		return str.toString();
	}
	

}
