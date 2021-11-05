package components;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Card extends JButton {

	private String word;
	private BufferedImage icon;
	FavButton fav_btn = null;

	/**
	 * Create the panel.
	 */
	public Card(String word, BufferedImage icon, SelectedWordsPanel panel) {
		this.word = word;
		this.icon = icon;
		// TODO Get line border color based on category, enumarations might be useful
		// setBorder(new LineBorder(new Color(0, 255, 0), 10, true));
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1;
		c.weighty = 1; // ?
		c.gridx = 0;
		c.gridy = 0;
		c.ipadx = 1;
		c.ipady = 1;
		c.anchor = GridBagConstraints.NORTHEAST;
		fav_btn = new FavButton();
		add(fav_btn, c);

		///////////////////////////////////////

		GridBagConstraints c2 = new GridBagConstraints();
		c2.weightx = 0.8;
		c2.weighty = 0.8; // ?
		c2.gridx = 0;
		c2.gridy = 0;
		c2.fill = GridBagConstraints.BOTH;
		add(new WordIcon(this.icon), c2);

		///////////////////////////////////////

		// TODO label and icon can be combined since wordIcon is type of JLabel, but
		// needs tuning

		///////////////////////////////////////
		JLabel lbl = new JLabel(this.word, CENTER);
		lbl.setFont(lbl.getFont().deriveFont(16.0f));
		GridBagConstraints c3 = new GridBagConstraints();
		c3.gridx = 0;
		c3.gridy = 10;
		c3.weightx = 1;
		c3.weighty = 1;
		c3.fill = GridBagConstraints.HORIZONTAL;
		c3.anchor = GridBagConstraints.LAST_LINE_START;
		add(lbl, c3);
		///////////////////////////////////////

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.out.println(word + " is selected");
				panel.addWord(word, icon);
			}
		});

		setVisible(true);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (fav_btn.getIsFavorite() && CardsCollection.getFav_cards() != null
				&& !CardsCollection.getFav_cards().contains(this)) {
			System.out.println("Card : " + this.word + " added to favs.");
			CardsCollection.add2FavCards(this);
		} else if (!fav_btn.getIsFavorite() && CardsCollection.getFav_cards() != null
				&& CardsCollection.getFav_cards().size() != 0 && CardsCollection.getFav_cards().contains(this)) {
			System.out.println("Card : " + this.word + " removed from favs.");
			CardsCollection.removeFromFavCards(this);
		}
	}

}
