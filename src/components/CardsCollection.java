package components;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JButton;

public final class CardsCollection {
	private static ArrayList<JButton> all_cards = new ArrayList<JButton>();
	private static ArrayList<JButton> fav_cards = new ArrayList<JButton>();
	
	public static ArrayList<JButton> getAll_cards() {
		return all_cards;
	}
	public static void add2AllCards(JButton card) {
		CardsCollection.all_cards.add(card);
	}
	public static ArrayList<JButton> getFav_cards() {
		return fav_cards;
	}
	public static void add2FavCards(JButton card) {
		CardsCollection.fav_cards.add(card);
	}
	
	public static void removeFromFavCards(JButton card) {
		CardsCollection.fav_cards.remove(card);
	}
	
	public static void printStatus() {
		System.out.println("Number of all cards: " + CardsCollection.getAll_cards().size());
		System.out.println("Number of fav cards: " + CardsCollection.getFav_cards().size());
	}
	
	// https://stackoverflow.com/questions/14548808/scale-the-imageicon-automatically-to-label-size
	public static BufferedImage resize(BufferedImage image, int width, int height) {
	    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
	    Graphics2D g2d = (Graphics2D) bi.createGraphics();
	    g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
	    g2d.drawImage(image, 0, 0, width, height, null);
	    g2d.dispose();
	    return bi;
	}
}
