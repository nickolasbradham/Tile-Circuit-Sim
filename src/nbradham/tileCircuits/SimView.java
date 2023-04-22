package nbradham.tileCircuits;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

final class SimView extends JPanel {
	private static final long serialVersionUID = 1L;

	SimView(Simulator simulator) {
		super();
		setPreferredSize(new Dimension(1366, 750));
	}

	@Override
	public void paint(Graphics g) {
		g.fillRect(10, 10, 20, 20);
	}
}