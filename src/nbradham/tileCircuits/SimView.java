package nbradham.tileCircuits;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

final class SimView extends JPanel {
	private static final long serialVersionUID = 1L;

	private final Simulator sim;

	SimView(Simulator simulator) {
		super();
		sim = simulator;
		setPreferredSize(new Dimension(1366, 750));
		addKeyListener(new KeyAdapter() {
			@Override
			public final void keyPressed(KeyEvent e) {
				sim.keyPressed(e);
			}

			@Override
			public final void keyReleased(KeyEvent e) {
				sim.keyReleased(e);
			}
		});
		addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				sim.mouseWheel(e);
			}
		});
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		sim.draw(g);
	}
}