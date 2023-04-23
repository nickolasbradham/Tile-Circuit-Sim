package nbradham.tileCircuits.tiles;

import java.awt.Graphics;

public final class FilledTile extends Tile {
	@Override
	public void draw(Graphics g, int x, int y, int tileSize) {
		g.fillRect(x, y, tileSize, tileSize);
	}
}