package nbradham.tileCircuits;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

final class Simulator {

	private final SimView view = new SimView(this);
	private final HashMap<Integer, HashMap<Integer, Boolean>> objMap = new HashMap<>();
	private int tileSize = 20;
	private byte camX = 0, camY = 0, camDX = 0, camDY = 0;
	private final Timer timer = new Timer(16, e -> {
		camX += camDX;
		camY += camDY;
		view.repaint();
	});

	void draw(Graphics g) {
		g.drawString(String.format("(%d, %d)", camX, camY), 0, 12);
		int hw = view.getWidth() / tileSize / 2, hh = view.getHeight() / tileSize / 2, x1 = camX - hw, y1 = camY - hh,
				x2 = camX + hw, y2 = camY + hh, dx = x2 - x1+1, dy = y2 - y1+1;
		for (short x = 0; x <= dx; ++x)
			for (short y = 0; y <= dy; ++y)
				if (get(x1 + x, y1 + y))
					g.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
	}

	void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			camDX = -1;
			break;
		case KeyEvent.VK_RIGHT:
			camDX = 1;
			break;
		case KeyEvent.VK_UP:
			camDY = -1;
			break;
		case KeyEvent.VK_DOWN:
			camDY = 1;
		}
	}

	void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_RIGHT:
			camDX = 0;
			break;
		case KeyEvent.VK_UP:
		case KeyEvent.VK_DOWN:
			camDY = 0;
		}
	}

	void mouseWheel(MouseWheelEvent e) {
		tileSize -= e.getUnitsToScroll();
		tileSize = Math.max(1, Math.min(100, tileSize));
	}

	private void createGUI() {
		put(0, 0, true);
		put(2, 0, true);
		put(3, 3, true);
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Circuit Sim");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.add(view);
			frame.pack();
			frame.setVisible(true);
			view.requestFocus();
			timer.start();
		});
	}

	private void put(int x, int y, boolean val) {
		HashMap<Integer, Boolean> col = objMap.get(x);
		if (col == null)
			objMap.put(x, col = new HashMap<>());
		col.put(y, val);
	}

	private boolean get(int x, int y) {
		HashMap<Integer, Boolean> col = objMap.get(x);
		if (col == null)
			return false;
		Boolean val = col.get(y);
		return val == null ? false : val;
	}

	public static void main(String[] args) {
		new Simulator().createGUI();
	}
}