package nbradham.tileCircuits;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

final class Simulator {

	private final SimView view = new SimView(this);
	private final HashMap<Integer, HashMap<Integer, Boolean>> objMap = new HashMap<>();
	private int tileSize = 20, halfViewW, halfViewH, viewX1, viewY1, viewX2, viewY2, viewDX, viewDY, camSpeed = 1,
			camDX, camDY;
	private short camX, camY;
	private final Timer timer = new Timer(16, e -> {
		camX += camDX;
		camY += camDY;
		updateViewRect();
		view.repaint();
	});

	void draw(Graphics g) {
		g.drawString(String.format("(%d, %d)", camX, camY), 0, 12);
		int fillX, tileX;
		for (short x = 0; x <= viewDX; ++x) {
			fillX = x * tileSize;
			tileX = viewX1 + x;
			for (short y = 0; y <= viewDY; ++y)
				if (get(tileX, viewY1 + y))
					g.fillRect(fillX, y * tileSize, tileSize, tileSize);
		}
	}

	void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			camDX = -camSpeed;
			break;
		case KeyEvent.VK_RIGHT:
			camDX = camSpeed;
			break;
		case KeyEvent.VK_UP:
			camDY = -camSpeed;
			break;
		case KeyEvent.VK_DOWN:
			camDY = camSpeed;
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
		camSpeed = Math.max(1, 20 / tileSize);
		resized();
	}

	void resized() {
		halfViewW = view.getWidth() / tileSize / 2;
		halfViewH = view.getHeight() / tileSize / 2;
		updateViewRect();
	}

	void clicked(MouseEvent e) {
		Point p = e.getPoint();
		put(p.x / tileSize - halfViewW + camX, p.y / tileSize - halfViewH + camY, true);
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

	private void updateViewRect() {
		viewX1 = camX - halfViewW;
		viewY1 = camY - halfViewH;
		viewX2 = camX + halfViewW;
		viewY2 = camY + halfViewH;
		viewDX = viewX2 - viewX1 + 1;
		viewDY = viewY2 - viewY1 + 1;
	}

	public static void main(String[] args) {
		new Simulator().createGUI();
	}
}