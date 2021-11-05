import javax.swing.JOptionPane;

import components.CardsWindow;

public class main {

	public static void main(String[] args) {
		try {
			CardsWindow cw = new CardsWindow();
		} catch (Throwable t) {
			JOptionPane.showMessageDialog(null, t.getClass().getSimpleName() + ": " + t.getMessage());
			throw t; // don't suppress Throwable
		}
	}

}
