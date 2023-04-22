package nbradham.tileCircuits;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

final class Simulator {

	private void createGUI() {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Circuit Sim");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.add(new SimView(this));
			frame.pack();
			frame.setVisible(true);
		});
	}

	public static void main(String[] args) {
		new Simulator().createGUI();
	}
}