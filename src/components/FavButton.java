package components;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FavButton extends JLabel {

	/**
	 * Create the frame.
	 */
	private boolean isFavorite = false;
	private BufferedImage img_unselected = null;
	private BufferedImage img_selected = null;

	public FavButton() {
		super();
		try {
			ClassLoader loader = getClass().getClassLoader();
			img_unselected = ImageIO.read(loader.getResource("src/resources/icons/star_unselected32.png"));
			img_selected = ImageIO.read(loader.getResource("src/resources/icons/star_selected32.png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(isFavorite) {
					isFavorite = false;
					System.out.println("Removed from favorites");
				}
				else {
					isFavorite = true;
					System.out.println("Added to the favorites");
				}
			}
		});
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Image img = null;
		if(isFavorite) {
			img = img_selected;
		}
		else {
			img = img_unselected;
		}
		ImageIcon imageIcon = new ImageIcon(img);
		this.setIcon(imageIcon);
	}
	
	public boolean getIsFavorite() {
		return this.isFavorite;
	}
}
