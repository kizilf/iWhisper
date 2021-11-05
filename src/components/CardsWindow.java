package components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.google.protobuf.ByteString;


public class CardsWindow extends JFrame {

	private JPanel contentPane;
	/**
	 * Create the frame.
	 */
	
	private final int CARDPANEL_MAX_WIDTH = 7;
	private final int CARDPANEL_MAX_HEIGHT = 7;
	private final int CARDPANEL_MIN_WIDTH = 3;
	private final int CARDPANEL_MIN_HEIGHT = 3;
	private final int INITIAL_GRID_SIZE = 5;
	


	private HashMap<Integer, Integer> page_startIdx = new HashMap<>();
	private boolean showFavorites = false;
	private int s_idx = 0;
	
	public CardsWindow() {
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/*int screen_width = 512;
		int screen_height = 512;
		setSize(screen_width, screen_height);*/
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		// Following line makes cross disappear
		// TODO place a X icon to use this
		//setUndecorated(true);
		
		
		// The main content panel that should include all
		contentPane = new JPanel();
		//contentPane.setBackground(Color.ORANGE);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.rowHeights = new int[]{	150 , 0};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0};
		gbl_contentPane.columnWeights = new double[]{0.0};
		contentPane.setLayout(gbl_contentPane);
		//contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		setContentPane(contentPane);


		
		// Panel that lists cards
		// https://docs.oracle.com/javase/tutorial/uiswing/layout/gridbag.html
		GridBagConstraints panel_constraints = new GridBagConstraints();
		panel_constraints.gridx = 0;
		panel_constraints.gridy = 1;
		panel_constraints.weightx = 1;
		panel_constraints.weighty = 1;
		panel_constraints.fill = GridBagConstraints.BOTH;
		
		JPanel cardPanel = new JPanel();
		//cardPanel.setBackground(Color.CYAN);
		GridLayout cardsGridLayout = new GridLayout(5, 5, 0, 0);
		cardPanel.setLayout(cardsGridLayout);
		contentPane.add(cardPanel, panel_constraints);

		
		// Top panel where selected cards and some buttons go
		//GridBagConstraints topPanel_constraints = new GridBagConstraints();

		panel_constraints.gridx = 0;
		panel_constraints.gridy = 0;
		
		JPanel topPanel = new JPanel();
		//topPanel.setLayout(new GridBagLayout());
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		//topPanel.setBackground(Color.ORANGE);
		contentPane.add(topPanel, panel_constraints);

		//GridBagConstraints topPanel_constraint = new GridBagConstraints();

		
		SelectedWordsPanel swp = new SelectedWordsPanel();
		readAllWords(swp);
		
		BufferedImage plus_icon = null;
		BufferedImage minus_icon = null;
		BufferedImage mic_icon = null;
		BufferedImage eye_icon = null;

		try {
			ClassLoader loader = getClass().getClassLoader();;
			plus_icon = ImageIO.read(loader.getResource("src/resources/icons/plus64.png"));
			minus_icon = ImageIO.read(loader.getResource("src/resources/icons/minus64.png"));
			mic_icon = ImageIO.read(loader.getResource("src/resources/icons/microphone64.png"));
			eye_icon = ImageIO.read(loader.getResource("src/resources/icons/eye64.png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		JLabel readButton = new JLabel(new ImageIcon(mic_icon));
		readButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				/*
				google.setText(swp.getSelectedWords());
				ByteString bs = google.getVoice();
				*/
			}
		});

		JLabel biggerButton = new JLabel(new ImageIcon(plus_icon));
		biggerButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(cardsGridLayout.getColumns() < CARDPANEL_MAX_WIDTH && cardsGridLayout.getRows() < CARDPANEL_MAX_HEIGHT) {
					cardsGridLayout.setColumns(cardsGridLayout.getColumns() + 1);
					cardsGridLayout.setRows(cardsGridLayout.getRows() + 1);
					if(showFavorites) {
						s_idx = 0;
						page_startIdx = new HashMap<>();
						fillTheCardPanel(cardPanel, cardsGridLayout.getRows(), cardsGridLayout.getColumns(), swp, "fav", 1);
					}
					else {
						s_idx = 0;
						page_startIdx = new HashMap<>();
						fillTheCardPanel(cardPanel, cardsGridLayout.getRows(), cardsGridLayout.getColumns(), swp, "all", 1);
					}
				}
			}
		});
		
		
		JLabel smallerButton = new JLabel(new ImageIcon(minus_icon));
		smallerButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(cardsGridLayout.getColumns() > CARDPANEL_MIN_WIDTH && cardsGridLayout.getRows() > CARDPANEL_MIN_HEIGHT)
				{
					cardsGridLayout.setColumns(cardsGridLayout.getColumns() - 1);
					cardsGridLayout.setRows(cardsGridLayout.getRows() - 1);
					if(showFavorites) {
						resetPaginationAttributes();
						fillTheCardPanel(cardPanel, cardsGridLayout.getRows(), cardsGridLayout.getColumns(), swp, "fav", 1);
					}
					else {
						resetPaginationAttributes();
						fillTheCardPanel(cardPanel, cardsGridLayout.getRows(), cardsGridLayout.getColumns(), swp, "all", 1);
					}
				}
			}
		});
		
		JLabel eyeButton = new JLabel(new ImageIcon(eye_icon));
		eyeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.out.println("TODO: Toggle eye tracking mode");
			}
		});
		
		topPanel.add(Box.createRigidArea(new Dimension(10,0)));
		//topPanel.add(eyeButton);
		//topPanel.add(Box.createRigidArea(new Dimension(5,0)));
		topPanel.add(biggerButton);
		topPanel.add(Box.createRigidArea(new Dimension(10,0)));
		topPanel.add(smallerButton);
		topPanel.add(Box.createRigidArea(new Dimension(5,0)));
		topPanel.add(readButton);
		topPanel.add(Box.createRigidArea(new Dimension(5,0)));
		topPanel.add(swp);
		
		fillTheCardPanel(cardPanel, cardsGridLayout.getRows(), cardsGridLayout.getColumns(), swp, "all", 1);

		setVisible(true);
		
	
	}
	
	private void fillTheCardPanel(JPanel cardPanel, int row, int col, SelectedWordsPanel swp, String mode, int pageNum) {
		cardPanel.removeAll();
		
		if(page_startIdx.containsKey(pageNum)) s_idx = page_startIdx.get(pageNum);
		else {
			page_startIdx.put(pageNum, s_idx);
		}
	
		// In "all" mode there should be switch to favorites button, buttons for every word, and back/forth buttons
		BufferedImage starPic = null;
		BufferedImage backPic = null;
		BufferedImage forwardPic = null;
		try {
			ClassLoader loader = getClass().getClassLoader();
			starPic = ImageIO.read(loader.getResource("src/resources/icons/star_selected256.png"));
			backPic = ImageIO.read(loader.getResource("src/resources/icons/leftarrow64.png"));
			forwardPic = ImageIO.read(loader.getResource("src/resources/icons/rightarrow64.png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		boolean shouldShowBackBtn = s_idx == 0 ? false : true;
		
		
		
		JButton forward_btn = new JButton("To Page: "+ (pageNum+1));
		forward_btn.setIcon(new ImageIcon(CardsCollection.resize(forwardPic, 64, 64)));
		forward_btn.setHorizontalTextPosition(JButton.CENTER);
		forward_btn.setVerticalTextPosition(JButton.BOTTOM);
		forward_btn.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent arg0) {
				if(showFavorites) {
					fillTheCardPanel(cardPanel, row, col, swp, "fav", pageNum+1);
				}
				else {
					fillTheCardPanel(cardPanel, row, col, swp, "all", pageNum+1);
				}	
			}
		});
		
		JButton star_btn = new JButton();
		if(mode.equals("fav")) star_btn.setText("All Words");
		else {
			star_btn.setText("Favorites");
		}
		star_btn.setIcon(new ImageIcon(CardsCollection.resize(starPic, 64, 64)));
		star_btn.setHorizontalTextPosition(JButton.CENTER);
		star_btn.setVerticalTextPosition(JButton.BOTTOM);
		star_btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				showFavorites = !showFavorites;
				if(showFavorites) {
					resetPaginationAttributes();
					fillTheCardPanel(cardPanel, row, col, swp, "fav", 1);
				}
				else {
					resetPaginationAttributes();
					fillTheCardPanel(cardPanel, row, col, swp, "all", 1);
				}							
			}
		});
		
		JButton back_btn = new JButton("To Page: "+ (pageNum-1));
		back_btn.setIcon(new ImageIcon(CardsCollection.resize(backPic, 64, 64)));
		back_btn.setHorizontalTextPosition(JButton.CENTER);
		back_btn.setVerticalTextPosition(JButton.BOTTOM);
		back_btn.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent arg0) {
				if(showFavorites) {
					fillTheCardPanel(cardPanel, row, col, swp, "fav", pageNum-1);
				}
				else {
					fillTheCardPanel(cardPanel, row, col, swp, "all", pageNum-1);
				}	
			}
		});
		
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if(i == 0 && j == 0) {
					// Add favorites button
					cardPanel.add(star_btn);
				}
				else if (shouldShowBackBtn && i == row-1 && j == 0)
				{
					// Add more button if there are still words that needed to be showed
					cardPanel.add(back_btn);
				}

				else if (i == row-1 && j == col-1 && mode.equals("all") && s_idx != CardsCollection.getAll_cards().size())
				{
					// Add more button if there are still words that needed to be showed
					cardPanel.add(forward_btn);
				}
				else if (i == row-1 && j == col-1 && mode.equals("fav") && s_idx != CardsCollection.getFav_cards().size())
				{
					// Add more button if there are still words that needed to be showed
					cardPanel.add(forward_btn);
				}
				else if(mode.equals("all") && s_idx == CardsCollection.getAll_cards().size()) {
					// All words displayed for the last batch but there is still empty cells
					cardPanel.add(new JButton());
				}
				else if(mode.equals("fav") && s_idx == CardsCollection.getFav_cards().size()) {
					// All words displayed for the last batch but there is still empty cells
					cardPanel.add(new JButton());
				}
				else {
					// Add a normal word button
					if(mode.equals("all")) 		cardPanel.add(CardsCollection.getAll_cards().get(s_idx++));
					else if(mode.equals("fav")) cardPanel.add(CardsCollection.getFav_cards().get(s_idx++));
				}
			}
		}
		
		//cardsCollection.printStatus();
		cardPanel.revalidate();
		cardPanel.repaint();
	}
	
	private void readAllWords(SelectedWordsPanel swp){
		try {
			ClassLoader loader = getClass().getClassLoader();
			InputStream in = loader.getResourceAsStream("src/resources/words/words.txt");
			BufferedReader br=new BufferedReader(new InputStreamReader(in));  //creates a buffering character input stream  
		    String line;
		    while ((line = br.readLine()) != null) {
		    	//System.out.println(line);
		    	if(CardsCollection.getAll_cards() != null)
		    	{
		    		String word_text = line.split("\\s+")[0];
		    		String word_img_path = line.split("\\s+")[1];
					BufferedImage word_img = ImageIO.read(loader.getResource("src/resources/words/" + word_img_path));
		    		CardsCollection.add2AllCards(new Card(word_text, word_img, swp));
		    	}
		    }
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void resetPaginationAttributes() {
		s_idx = 0;
		page_startIdx = new HashMap<>();
	}
		
	
}
