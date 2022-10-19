import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

public class Spike extends Obj{
	public Spike(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Spike(int x, int y, int w) {
		this.x = x;
		this.y = y;
		width = w;
	}
	
	@Override
	public void paintMe(Graphics g, int shiftX, int shiftY) {
		Polygon p = new Polygon(new int[] {x + shiftX, x + width / 2 + shiftX, x + width + shiftX}, 
				new int[] {y + shiftY + width, y + shiftY + 1, y + shiftY + width}, 3);
		g.setColor(Color.GRAY);
		g.fillPolygon(p);
		g.setColor(Color.BLACK);
		g.drawPolygon(p);
	}
	
	@Override
	public void paintMe(Graphics g, int shiftX, int shiftY, int newy) {
		Polygon p = new Polygon(new int[] {x + shiftX, x + width / 2 + shiftX, x + width + shiftX}, 
				new int[] {newy + shiftY + width, newy + shiftY + 1, newy + shiftY + width}, 3);
		g.setColor(Color.GRAY);
		g.fillPolygon(p);
		g.setColor(Color.BLACK);
		g.drawPolygon(p);
	}
	
	public String toString() {
		return "Spike: " + x + ", " + y;
	}

	@Override
	public int collision(int cx, int cy) {
		cy += 20;
		if(cx + 20 > x && cx < x + width - 5 && //my right to his left, my left to his right,
				cy > y - width - 5 && cy - 20 < y) return 2; //my top to the top of his bot, etc.
		return 0;
	}
	
	public int collision(int cx, int cy, int s, int v) {
		return collision(cx, cy);
	}

}
