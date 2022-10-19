import java.awt.Graphics;

public abstract class Obj implements Comparable<Obj>{
	int x;
	int y;
	int width = 25;
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public void setY(int newy) {
		y = newy;
	}
	//0 is no collision
	//1 is no death, on ground
	//2 is death
	public int collision(int cx, int cy) {return 0;}
	public int collision(int cx, int cy, int s, int v) {return 0;}
	
	public int getDist(int cx, int cy) {
		double newx = x + width/2;
		double newy = y + width/2;
		if(Math.abs(newx - cx) >= 20 || Math.abs(newy - cy) >= 20) return 300;
		return((int)Math.round(Math.sqrt(Math.pow((cx - newx), 2) + Math.pow((cy - newy), 2))));
	}
	
	public boolean equals(Object o) {
		if(!o.getClass().equals(getClass())) return false;
		Obj a = (Obj)o;
		return x == a.x && y == a.y && width == a.width;
	}
	
	public abstract void paintMe(Graphics g, int shiftX, int shiftY);
	
	public void paintMe(Graphics g, int shiftX, int shiftY, int newy) {}
	
	public int compareTo(Obj o) {
		if(x == o.x) return y - o.y;
		return x - o.x;
	}
}
