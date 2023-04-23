package nbradham.tileCircuits.tiles;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Represents a filled decorative tile with no special mechanics.
 * 
 * @author Nickolas S. Bradham
 *
 */
public final class FilledTile extends Tile {
	@Override
	public void draw(Graphics g, int x, int y, int tileSize) {
		g.setColor(Color.BLACK);
		g.fillRect(x, y, tileSize, tileSize);
	}
}