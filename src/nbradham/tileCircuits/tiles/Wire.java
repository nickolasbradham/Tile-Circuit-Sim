package nbradham.tileCircuits.tiles;

import java.awt.Color;
import java.awt.Graphics;

/**
 * A tile that can transmit a signal.
 * 
 * @author Nickolas S. Bradham
 *
 */
public final class Wire extends Tile {
	@Override
	public void draw(Graphics g, int x, int y, int tileSize) {
		g.setColor(Color.BLUE);
		g.fillRect(x, y, tileSize, tileSize);
	}
}
