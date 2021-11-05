package components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class WordIcon extends JLabel {
	private BufferedImage buff_img = null;

	public WordIcon(BufferedImage buff_img) {
		super("", SwingConstants.CENTER);
		this.buff_img = buff_img;
	}		
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		BufferedImage resized_img = CardsCollection.resize(this.buff_img, this.getHeight() /*this.getWidth() / 3*/, this.getHeight()); // square img resolution
		ImageIcon imageIcon = new ImageIcon(resized_img);
		this.setIcon(imageIcon);
	}
	
}
