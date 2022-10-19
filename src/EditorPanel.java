import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class EditorPanel extends JPanel{
	double width;
	double height;
	
	int cellsW = 40;//editor dimensions
	int cellsH = 20;
	
	int cameraX = -50;
	int cameraY = 0;
	
	int orX;
	int orY;
	
	ArrayList<Obj> objList = new ArrayList<>();
	
	public EditorPanel(ArrayList<Obj> b) {
		super();
		objList = b;
		width = this.getWidth() / cellsW;
		height = this.getHeight() / cellsH;
	}
	
	public int getCWidth() {
		return (int)width;
	}
	
	public int getCX() {
		return cameraX;
	}
	
	public int getCY() {
		return cameraY;
	}
	
	public void setShift(int x, int y) {
		cameraX += x;
		cameraY += y;
		if(cameraX > 0) cameraX = 0;
		if(cameraY < 0) cameraY = 0;
	}
	
	public int[] getCoords(int x, int y) {
		int a = 0;
		x -= cameraX;
		y -= (cameraY - 8);
		if(y <= 0) a = 1;
		x = x / (int)width * (int)width;
		y = (y / (int)height - a) * (int)height - 9;
		return(new int[] {x, y, (int)width});
	}

	//Draw Lines then
	//Draw in Blocks
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		width = this.getWidth() / cellsW;
		height = this.getHeight() / cellsH;
		for(Obj o : objList) {
			o.paintMe(g, cameraX, cameraY);
		}
		
		g.setColor(Color.BLACK);
		//vertical lines
		for(double x = cameraX % width; x < this.getWidth(); x += width) {
			g.drawLine((int)Math.round(x), 0, (int)Math.round(x), this.getHeight());
		}
		//horizontal lines
		for(double y = this.getHeight() + cameraY % height; y > 0; y -= height) {
			g.drawLine(0, (int)Math.round(y), this.getWidth(), (int)Math.round(y));
			//g.drawLine(0, (int)Math.round(y), (int)Math.round(y), this.getHeight());// THIS LINE IS AWESOME
		}
	}
}
