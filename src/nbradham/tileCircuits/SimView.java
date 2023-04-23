package nbradham.tileCircuits;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

/**
 * Handles GUI interactions.
 * 
 * @author Nickolas S. Bradham
 *
 */
final class SimView extends JPanel {
	private static final long serialVersionUID = 1L;

	private final Simulator sim;

	/**
	 * Creates a new SimView instance.
	 * 
	 * @param simulator The simulator that will handle interactions.
	 */
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
			public final void mouseWheelMoved(MouseWheelEvent e) {
				sim.mouseWheel(e);
			}
		});
		addMouseListener(new MouseAdapter() {

			@Override
			public final void mousePressed(MouseEvent e) {
				sim.pressed(e);
			}
		});
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public final void mouseDragged(MouseEvent e) {
				sim.drag(e.getPoint());
			}
		});
		addComponentListener(new ComponentAdapter() {
			@Override
			public final void componentResized(ComponentEvent e) {
				sim.resized();
			}
		});
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		sim.draw(g);
	}
}