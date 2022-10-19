import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/*TODO
 * Ending
 * Back Buttons
 * Opening file in editor
 */

public class PlatMain implements KeyListener, Runnable, ActionListener {
	// editor variables
	ArrayList<Obj> objList = new ArrayList<>();
	int editorState = 0;
	int objState = 0;
	final int BUILDING = 0;
	final int DELETING = 1;
	final int BLOCK = 0;
	final int SPIKE = 1;
	final int NONE = -1;
	boolean swiping = false;
	boolean upDown = false; //stores whether or not is upside down
	
	String level; //For opening levels in both creator and game
	// game variables
	int grav = -4;
	int velUp = 0;
	int speed = 10;
	int cubeX = 0;
	int cubeY = 0;
	boolean onGround = true;
	boolean running = false;
	final int GROUND = 0;
	int startCheckIndex = 0;
	int endCheckIndex = 0;
	final int CRASH = 2;
	final int ON_BLOCK = 1;
	final int NO_CRASH = 0;
	final int MAX_FALL = -20;
	boolean holding = false;
	
	// main game frame
	JFrame gameFrame = new JFrame("GD Techinically");
	GamePanel gamePanel = new GamePanel(objList);

	// level select frame
	JFrame levelSelectFrame = new JFrame("Selecting Level Lol");
	Container centerL = new Container();
	JButton[][] selectLevel = new JButton[3][3];

	// main menu frame
	JFrame typeFrame = new JFrame("Select Da GameTypeThing");
	JButton editorB = new JButton("Level Editor");
	JButton playB = new JButton("Level Select");

	// level editor frame
	JFrame editorFrame = new JFrame("Editor");
	EditorPanel editorPanel = new EditorPanel(objList);
	Container westE = new Container();
	JButton editBuildB = new JButton("Build");
	JButton editDeleteB = new JButton("Delete");
	JButton editSwipeB = new JButton("Swipe");
	Container southE = new Container();
	JButton buildBlockB = new JButton("Block");
	JButton buildSpikeB = new JButton("Spike");
	Container eastE = new Container();
	JButton editSaveB = new JButton("Save");
	JButton editOpenB = new JButton("Open");
	JButton editExitB = new JButton("Exit");

	// Declare four frames
	public PlatMain() {
		addGameFrame();
		addEditorFrame();
		addLevelSelectFrame();
		addTypeFrame();
	}

	// Frame initializers
	// ****************************************************************************************
	// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
	// initialize and setup main game frame
	public void addGameFrame() {
		gameFrame.setSize(1000, 600);
		gameFrame.setLayout(new BorderLayout());

		gameFrame.add(gamePanel, BorderLayout.CENTER);
		gamePanel.addMouseListener(new mouseGameHandler());
		gameFrame.addKeyListener(this);

		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setVisible(false);
	}

	// initialize and setup level select frame
	public void addLevelSelectFrame() {
		levelSelectFrame.setSize(400, 400);
		levelSelectFrame.setLayout(new BorderLayout());

		// declare and add each level select button to levelselect frame
		centerL.setLayout(new GridLayout(3, 3));
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				selectLevel[i][j] = new JButton("Level " + Integer.toString(i * 3 + j + 1));
				selectLevel[i][j].addActionListener(this);
				centerL.add(selectLevel[i][j]);
			}
		}
		selectLevel[2][2].setText("Custom level");
		levelSelectFrame.add(centerL, BorderLayout.CENTER);

		levelSelectFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		levelSelectFrame.setVisible(false);
	}

	// initialize and setup main menu-type frame
	public void addTypeFrame() {
		typeFrame.setSize(600, 400);
		typeFrame.setLayout(new GridLayout(1, 2));

		typeFrame.add(editorB);
		editorB.addActionListener(this);
		typeFrame.add(playB);
		playB.addActionListener(this);

		typeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		typeFrame.setVisible(true);
	}

	// initialize and setup editor frame
	public void addEditorFrame() {
		editorFrame.setSize(1200, 620);
		editorFrame.setLayout(new BorderLayout());

		editorFrame.add(editorPanel, BorderLayout.CENTER);

		westE.setLayout(new GridLayout(3, 1));
		westE.add(editBuildB);
		editBuildB.setBackground(Color.GREEN);
		editBuildB.addActionListener(this);
		westE.add(editDeleteB);
		editDeleteB.setBackground(Color.GRAY);
		editDeleteB.addActionListener(this);
		westE.add(editSwipeB);
		editSwipeB.setBackground(Color.GRAY);
		editSwipeB.addActionListener(this);
		editorFrame.add(westE, BorderLayout.WEST);

		southE.setLayout(new GridLayout(1, 2));
		southE.add(buildBlockB);
		buildBlockB.setBackground(Color.GREEN);
		buildBlockB.addActionListener(this);
		southE.add(buildSpikeB);
		buildSpikeB.setBackground(Color.GRAY);
		buildSpikeB.addActionListener(this);
		editorFrame.add(southE, BorderLayout.SOUTH);
		
		eastE.setLayout(new GridLayout(3, 1));
		eastE.add(editSaveB);
		editSaveB.addActionListener(this);
		eastE.add(editOpenB);
		editOpenB.addActionListener(this);
		eastE.add(editExitB);
		editExitB.addActionListener(this);
		editorFrame.add(eastE, BorderLayout.EAST);
		
		editorPanel.addMouseMotionListener(new mouseEditMotionHandler());
		editorPanel.addMouseListener(new mouseEditHandler());
		
		editorFrame.setResizable(false);
		editorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		editorFrame.setVisible(false);
	}
	// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	// ****************************************************************************************

	// Editor Functions
	// ****************************************************************************************
	// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
	public void editorClick(int x, int y) {
		if (editorState == BUILDING) {
			switch (objState) {
				case (NONE):
					break;
				case (BLOCK):
					int bCoords[] = editorPanel.getCoords(x, y);
					Block tempB = new Block(bCoords[0], bCoords[1], bCoords[2]);
					if (!objList.contains(tempB))
						objList.add(tempB);
					break;
				case (SPIKE):
					int sCoords[] = editorPanel.getCoords(x, y);
					Spike tempS = new Spike(sCoords[0], sCoords[1], sCoords[2]);
					if (!objList.contains(tempS))
						objList.add(tempS);
					break;
			}
		} else if (editorState == DELETING) {
			x -= editorPanel.getCX();
			y -= editorPanel.getCY();
			for (int i = 0; i < objList.size(); i++) {
				Obj o = objList.get(i);
				if (o.getDist(x, y) < 25) {
					objList.remove(o);
					break;
				}
			}
		}
		editorPanel.repaint();
	}
	
	void editUpDown() {
		for(Obj o : objList) {
			o.setY(editorPanel.getHeight() - o.getY()); 
		}
	}
	// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	// **********************************************************************************************
	// end of editor functions

	// Game Functions
	// **********************************************************************************************
	// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
	//Tries to read file and start game.
	String promptLevelName(String title, String mess, String ex) {
		return (String) JOptionPane.showInputDialog(typeFrame, mess , title,
				JOptionPane.PLAIN_MESSAGE, null, null, ex);
	}
	
	boolean loadFile() {
		File levelFile = new File(level + ".lvl");
		BufferedReader br = null;
		try {
			// read a new lvl file
			br = new BufferedReader(new FileReader(levelFile));
			String line;
			objList.clear();
			upDown = false;
			while ((line = br.readLine()) != null) {
				// for each block in file add to objlist
				String[] nextB = line.split(" ");
				int bX = Integer.parseInt(nextB[1]);
				int bY = Integer.parseInt(nextB[2]);
				switch (Integer.parseInt(nextB[0])) {
				case (0):
					objList.add(new Block(bX, bY, editorPanel.getCWidth() == 0 ? 26 : editorPanel.getCWidth()));
					break;
				case (1):
					objList.add(new Spike(bX, bY, editorPanel.getCWidth() == 0 ? 26 : editorPanel.getCWidth()));
				}
			}
			br.close();
			Collections.sort(objList);
			return true;
		} catch (Exception e) {
			System.out.println("Could not find file \"" + level + ".lvl" + "\".");
			return false;
		}
	}
	
	public void jump() {
		if(onGround) {
			velUp = 20; 
		}
	}
	@Override
	public void run() {
		while(running) {
			while(startCheckIndex < objList.size() - 1 &&
					cubeX - objList.get(startCheckIndex).getX() > 500 ) { //if cube is 800 ahead of block don't bother
				startCheckIndex++;
			}
			while(endCheckIndex < objList.size() - 1 &&
					objList.get(endCheckIndex).getX() - cubeX <= 500) { //if cube is not 800 ahead of block check
				endCheckIndex++;
			}
			int result = -1;
			Obj onBlock = null; //what block you are on
			//System.out.format("%d, %d, %d, %b%n",cubeX, cubeY, velUp, onGround);
			for(int i = startCheckIndex; i <= endCheckIndex; i++) {
				int cc = objList.get(i).collision(cubeX, cubeY, speed, velUp);
				if(cc == 1) onBlock = objList.get(i);
				result = Math.max(result, cc);
				if(cc == CRASH) {
					break;
				}
			}
			if(result == CRASH) {
				reset();
				try {Thread.sleep(1000);}catch(Exception e) {e.printStackTrace();}
			}
			else if(result == ON_BLOCK) {
				if(velUp < 0) velUp = 0;
				onGround = true;
				cubeY = onBlock.getY();
			}
			else {
				onGround = false;
			}
			//System.out.format("%d, %d, %d, %b%n",cubeX, cubeY, velUp, onGround);
			cubeX += speed;
			cubeY += velUp;
			if(cubeY <= 0) onGround = true;
			if(onGround) velUp = Math.max(velUp, 0);
			if(holding) jump();
			if(!onGround) {
				velUp += grav;
				velUp = Math.max(MAX_FALL, velUp);
			}
			if(cubeY < 0) cubeY = 0;
			gamePanel.getXY(cubeX, cubeY);
			gamePanel.repaint();
			try {
				Thread.sleep(34);
			} catch(Exception e) {
				e.printStackTrace();
			}
			//System.out.println("***************");
		}
	}
	
	void reset() {
		cubeX = -100;
		cubeY = 0;
		gamePanel.getXY(cubeX, cubeY);
		startCheckIndex = 0;
		endCheckIndex = 0;
		velUp = 0;
	}
	
	public void startGame() {
		if(running) return;
		running = true;
		Thread t = new Thread(this);
		t.start();
	}
	
	void exitGame() {
		running = false;
		reset();
		gameFrame.setVisible(false);
		editorFrame.setVisible(false);
		typeFrame.setVisible(true);
	}
	
	//End of game functions
	//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	//*****************************************************************
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_R) reset();
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) exitGame();
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// check all buttons
		// System.out.println(editorState + ", " + objState);
		if (e.getSource().equals(playB)) {
			levelSelectFrame.setVisible(true);
			typeFrame.setVisible(false);
			return;
		}
		if (e.getSource().equals(editorB)) {
			editorFrame.setVisible(true);
			typeFrame.setVisible(false);
			if(!upDown) editUpDown();
			return;
		}
		if (e.getSource().equals(editBuildB)) {
			editBuildB.setBackground(Color.GREEN);
			editDeleteB.setBackground(Color.GRAY);
			editorState = BUILDING;
			return;
		}
		if (e.getSource().equals(editDeleteB)) {
			editBuildB.setBackground(Color.GRAY);
			editDeleteB.setBackground(Color.GREEN);
			editorState = DELETING;
			objState = NONE;
			buildBlockB.setBackground(Color.GRAY);
			buildSpikeB.setBackground(Color.GRAY);
			return;
		}
		if(e.getSource().equals(editSwipeB)) {
			swiping = !swiping;
			editSwipeB.setBackground(swiping ? Color.GREEN : Color.GRAY);
		}
		if (e.getSource().equals(buildBlockB)) {
			if (editorState != BUILDING)
				return;
			buildBlockB.setBackground(Color.GREEN);
			buildSpikeB.setBackground(Color.GRAY);
			objState = BLOCK;
			return;
		}
		if (e.getSource().equals(buildSpikeB)) {
			if (editorState != BUILDING)
				return;
			buildBlockB.setBackground(Color.GRAY);
			buildSpikeB.setBackground(Color.GREEN);
			objState = SPIKE;
			return;
		}
		if (e.getSource().equals(editSaveB)) {
			String s = promptLevelName("Save", "What is your level name?", "Example: Bob_is-c0oL");
			if (s == null)
				return;
			s = s.replaceAll("[^\\w_-]+", "");
			File saveFile = new File(s + ".lvl");
			PrintWriter pw = null;
			try {
				pw = new PrintWriter(saveFile);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			for (Obj o : objList) {
				if (o instanceof Spike) {
					pw.print(1 + " ");
				} else if (o instanceof Block) {
					pw.print(0 + " ");
				}
				pw.println(o.getX() + " " + (editorPanel.getHeight() - o.getY()));
			}
			pw.close();
			System.exit(0);
			return;
		}
		if(e.getSource().equals(editOpenB)) {
			level = promptLevelName("What level to open?", 
					"Enter level name that you want to open. Warning: Current Progress will be lost",
					"Example: My-First-Level.txt").replaceAll("[^\\w_-]+", "");
			if(!loadFile()) return;
			editUpDown();
			upDown = true;
			editorPanel.repaint();
			return;
		}
		if(e.getSource().equals(editExitB)) {
			int n = JOptionPane.showConfirmDialog(editorFrame, "Are you sure you want to close? Current progress can be lost.",
					"Exit editor?", JOptionPane.YES_NO_OPTION);
			if(n == 1) return;
			else exitGame();
		}
		// check through each level select button
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (e.getSource().equals(selectLevel[i][j])) {
					level = ("level" + (i * 3 + j));
					if (i == 2 && j == 2) {
						String s = promptLevelName("Play!", "What level to play?", "Example: Bob_is-Co0l");
						if(s == null) return;
						s = s.replaceAll("[^\\w_-]+", "");
						level = s;
					}
					if(!loadFile()) return;
					startGame();
					levelSelectFrame.setVisible(false);
					gameFrame.setVisible(true);
					return;
				}
			}
		}
	}

	public static void main(String[] args) {
		new PlatMain();
	}

	// handling classes for dragging mouse
	// ****************************************************************************************
	// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
	private int orX;
	private int orY;
	boolean dragging = false;

	class mouseEditMotionHandler extends MouseMotionAdapter {
		@Override
		public void mouseDragged(MouseEvent e) {
			if(swiping) { 
				editorClick(e.getX(), e.getY());
			}
			else if (dragging) {
				editorPanel.setShift((e.getX() - orX), (e.getY() - orY));
				orX = e.getX();
				orY = e.getY();
				editorPanel.repaint();
			}
		}
	}

	class mouseEditHandler extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			orX = e.getX();
			orY = e.getY();
			dragging = true;
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			orX = 0;
			orY = 0;
			dragging = false;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			editorClick(e.getX(), e.getY());
		}
	}

	class mouseGameHandler extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			jump();
			holding = true;
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			holding = false;
		}
	}
	// ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
	// ****************************************************************************************
}