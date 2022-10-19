import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JPanel;
/*
 * Panel the manages actual gameplay
 */
public class GamePanel extends JPanel{
	int camX;
	int camY;
	int camW;
	int camH;
	ArrayList<Obj> objList;
	int cubeX;
	int cubeY;
	
	public GamePanel(ArrayList<Obj> o) {
		objList = o;
		camX = 0;
		camY = 0;
		camH = 5;
		camW = 10;
		setBackground(Color.BLUE);
	}
	
	public void getXY(int x, int y) {
		cubeX = x;
		cubeY = y;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(cubeX - camX > 200) camX = cubeX - 200;
		if(camX > cubeX) camX = cubeX;
		if(cubeY - camY > 400) camY = cubeY - 400;
		else if(camY > 0 && cubeY - camY < 100) camY = Math.max(0, cubeY - 100);
		for(Obj o : objList) {
			o.paintMe(g, -camX, camY, this.getHeight() - (o.getY()));
		}
		g.drawRect(cubeX - camX, this.getHeight() - (cubeY - camY) - 20, 20, 20);
		g.fillRect(cubeX - camX, this.getHeight() - (cubeY - camY) - 20, 20, 20);
	}
}
