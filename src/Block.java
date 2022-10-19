import java.awt.Color;
import java.awt.Graphics;

public class Block extends Obj{
	public Block(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Block(int x, int y, int w) {
		this.x = x;
		this.y = y;
		width = w;
	}
	
	@Override
	public void paintMe(Graphics g, int shiftX, int shiftY) {
		g.setColor(Color.GRAY);
		g.fillRect(x + shiftX, y + shiftY, width, width);
		g.setColor(Color.BLACK);
		g.drawRect(x + shiftX, y + shiftY, width, width);
	}
	
	@Override
	public void paintMe(Graphics g, int shiftX, int shiftY, int newy) {
		g.setColor(Color.GRAY);
		g.fillRect(x + shiftX, newy + shiftY, width, width);
		g.setColor(Color.BLACK);
		g.drawRect(x + shiftX, newy + shiftY, width, width);
	}
	
	@Override
	public int collision(int cx, int cy, int s, int v) {
		cy += 20;
		if(cx + 20 > x && cx < x + width - 5) {//if my right is right of his left and my left is
			if(v < 0 && cy - 10 > y) {
				cy += v;
				if(cy >= y - width && cy - 20 <= y) return 1;
			}
			else if(v == 0) {
				if(cy - 20 == y) return 1;
				if(cy >= y - width && cy - 20 <= y) return 2;
				return 0;
			}
			else {
				if(cy > y - width && cy - 20 < y) return 2;
				return 0;
			}
		}
		return 0;
	}
	
	public String toString() {
		return "Block: " + x + ", " + y;
	}
}
