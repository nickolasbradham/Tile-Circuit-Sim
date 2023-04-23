package nbradham.tileCircuits;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import nbradham.tileCircuits.tiles.FilledTile;
import nbradham.tileCircuits.tiles.Tile;
import nbradham.tileCircuits.tiles.Wire;

final class Simulator {

	private static enum PlaceMode {
		FILLED_TILE, WIRE
	};

	private static enum ChangeMode {
		PLACE, DELETE
	}

	private static final Tile NULL_TILE = new Tile();

	private final SimView view = new SimView(this);
	private final HashMap<Integer, HashMap<Integer, Tile>> objMap = new HashMap<>();
	private int tileSize = 20, halfViewW, halfViewH, viewX1, viewY1, viewX2, viewY2, viewDX, viewDY, camSpeed = 1,
			camDX, camDY;
	private short camX, camY;
	private final Timer timer = new Timer(16, e -> {
		camX += camDX;
		camY += camDY;
		updateViewRect();
		view.repaint();
	});
	private JButton[] toolGroup;

	private PlaceMode placeMode = PlaceMode.FILLED_TILE;
	private ChangeMode changeMode;

	void draw(Graphics g) {
		g.drawString(String.format("(%d, %d)", camX, camY), 0, 12);
		int fillX, tileX;
		for (short x = 0; x <= viewDX; ++x) {
			fillX = x * tileSize;
			tileX = viewX1 + x;
			for (short y = 0; y <= viewDY; ++y)
				get(tileX, viewY1 + y).draw(g, fillX, y * tileSize, tileSize);
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

	void pressed(MouseEvent e) {
		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			changeMode = ChangeMode.PLACE;
			break;
		case MouseEvent.BUTTON3:
			changeMode = ChangeMode.DELETE;
		}
		changeBlock(e.getPoint());
	}

	void drag(Point loc) {
		changeBlock(loc);
	}

	private void changeBlock(Point p) {
		int x = p.x / tileSize - halfViewW + camX, y = p.y / tileSize - halfViewH + camY;
		switch (changeMode) {
		case PLACE:
			Tile t = null;
			switch (placeMode) {
			case FILLED_TILE:
				t = new FilledTile();
				break;
			case WIRE:
				t = new Wire();
			}
			put(x, y, t);
			break;
		case DELETE:
			put(x, y, null);
		}
	}

	void resized() {
		halfViewW = view.getWidth() / tileSize / 2;
		halfViewH = view.getHeight() / tileSize / 2;
		updateViewRect();
	}

	private void createGUI() {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Circuit Sim");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLayout(new BorderLayout());

			JPanel toolBar = new JPanel();
			toolBar.setBorder(new LineBorder(Color.BLACK, 1));
			toolBar.add(new JLabel("Tile:"));
			ArrayList<JButton> toolsGroup = new ArrayList<>();

			createAndAddJButton(toolBar, toolsGroup, "Solid", PlaceMode.FILLED_TILE).setEnabled(false);
			createAndAddJButton(toolBar, toolsGroup, "Wire", PlaceMode.WIRE);

			toolGroup = toolsGroup.toArray(new JButton[0]);

			frame.add(toolBar, BorderLayout.BEFORE_FIRST_LINE);
			frame.add(view, BorderLayout.CENTER);
			frame.pack();
			frame.setVisible(true);
			view.requestFocus();
			timer.start();
		});
	}

	private void put(int x, int y, Tile tile) {
		HashMap<Integer, Tile> col = objMap.get(x);
		if (col == null)
			objMap.put(x, col = new HashMap<>());
		col.put(y, tile);
	}

	private Tile get(int x, int y) {
		HashMap<Integer, Tile> col = objMap.get(x);
		if (col == null)
			return NULL_TILE;
		Tile t = col.get(y);
		return t == null ? NULL_TILE : t;
	}

	private void updateViewRect() {
		viewX1 = camX - halfViewW;
		viewY1 = camY - halfViewH;
		viewX2 = camX + halfViewW;
		viewY2 = camY + halfViewH;
		viewDX = viewX2 - viewX1 + 1;
		viewDY = viewY2 - viewY1 + 1;
	}

	private JButton createAndAddJButton(JPanel tools, ArrayList<JButton> toolsGroup, String label, PlaceMode mode) {
		JButton button = new JButton(label);
		button.addActionListener(e -> {
			placeMode = mode;
			for (JButton b : toolGroup)
				b.setEnabled(true);
			button.setEnabled(false);
		});
		tools.add(button);
		toolsGroup.add(button);
		return button;
	}

	public static void main(String[] args) {
		new Simulator().createGUI();
	}
}