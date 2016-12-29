package Classes;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;

public class Panel extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 1L;

	int x = 0, y = 0;
	
	Map map;
	Player player;
	Kamera kamera;
	Keyboard tastatur;
	JFrame frame;
	Background background;
	MapSequence MapSeq;
	Game_HUD HUD = null;
	Resources resources = null;
	
	boolean KeyPressedMB = false, KeyPressedEsc = false;
	
	public int Ticks = 0;
	
	public String RootPath;
	
	int CurrentMapID = 0;
	int CurrentMapLostLives = 0;
	int UpdateInterval = 35;
	
	//ausgabe von Info an die DEV-Console
	public void out(String msg)
	{
		System.out.println("Out: " + msg);
	}
	
	public String getPath(String path)
	{
		//return getClass().getResource(path).getPath();
		return path;
	}
	
	//Constructor
	public Panel(JFrame frame, String ContentPath) throws IOException
	{		
		RootPath = ContentPath;
		this.frame = frame;
				
		//keylistener f�r Keyboard
		addKeyListener(new KeyboardListener());
				
		//nachricht an Log
		out("OnStart");
		
		tastatur = new Keyboard();
		
		MapSeq = new MapSequence(getPath(RootPath + "Sequences/seq-alles.txt"));
		
		//alles initiieren
		LoadLevel(MapSeq.MapList.get(CurrentMapID), false);
		
		//timer f�r Update und Paint
		Timer t = new Timer(this.UpdateInterval, new ActionListener() {
	          public void actionPerformed(ActionEvent e) {
	        	try {
					Update();
				} catch (IOException e1) {}
	        	
	        	//l�st die paint methode aus
	            repaint();
	          }
	       });
		
		//Starte Timer
		t.start();
	}
	
	public void nextMap() throws IOException
	{
		System.out.println("Next Level!");
		CurrentMapID++;
		if(CurrentMapID < MapSeq.MapList.size())
			LoadLevel(MapSeq.MapList.get(CurrentMapID), false);
		else
		{
			CurrentMapID = 0;
			LoadLevel(MapSeq.MapList.get(CurrentMapID), false);
		}	
		
	}
	
	//Beim ersten Start
	public void LoadLevel(String Path, boolean resetTimerAndLives) throws IOException
	{		
		if(resetTimerAndLives)
		{
			CurrentMapLostLives = 0;
			Ticks = 0;
		}
		
		map = new Map(getPath(RootPath + Path), RootPath);
		if(resources == null || (resources != null && resources.RootPath.equals(map.ResourcesPath) == false))
		{
			resources = new Resources(getPath(RootPath + map.ResourcesPath));
		}
		map.resources = resources;
				
		player = null;
		player = new Player(map.resources, new Point(0,0), map, tastatur);
		
		map.LoadMap(getPath(RootPath + Path), player);
		
		player.Pos = new Point(map.Spawn.x * 30, map.Spawn.y * 30);
		
		kamera = null;
		kamera = new Kamera(player.Pos, tastatur, true, frame);
		
		background = null;
		background = new Background(map.resources, "art.png", kamera);
		
		if(HUD == null)
			HUD = new Game_HUD(resources);
		HUD.LevelName = map.Name;
		HUD.LevelAutor = map.Autor;
	}
	
	//Alle 50 Milisekunden
	private void Update() throws IOException
	{			
		Ticks++;
		if(tastatur.key_m && tastatur.key_b && KeyPressedMB == false)
		{
			nextMap();
			KeyPressedMB = true;
		}
		else if((tastatur.key_m && tastatur.key_b) == false)
		{
			KeyPressedMB = false;
		}
		
		if(tastatur.key_esc && KeyPressedEsc == false)
		{
			player.Dead = true;
			KeyPressedEsc = true;
		}
		else if(tastatur.key_esc == false)
		{
			KeyPressedEsc = false;
		}
		
		if(player.Dead == false && player.Win == false)
		{
			player.Update();
			kamera.Update();
		}
		else if(player.Dead)
		{
			System.out.println("Player tot!");
			//wenn tot dann neues Level
			LoadLevel(MapSeq.MapList.get(CurrentMapID), false);
			CurrentMapLostLives++;
		}
		else if(player.Win)
		{
			HUD.beginWinScreen(50);
			nextMap();
		}		
		//System.out.println(player.Pos);
		kamera.Pos = player.Pos;
	}
	
	//Nach jedem Update (Alle 50 Milisekunden)
	public void paint(Graphics g)
	{
		//f�r Hintergrund
		 //super.paint(g);
		background.Draw(g);
		map.DrawAndUpdate(g, kamera, player);
		player.Draw(g, kamera);
		
		HUD.LostLives = CurrentMapLostLives;
		HUD.GoneTicks = this.Ticks;
		HUD.UpdateIntervall = UpdateInterval;
		HUD.Draw(g, this.getWidth(), this.getHeight());
	}
	
	
	public class KeyboardListener extends KeyAdapter{
		
		@Override
		public void keyPressed(KeyEvent e)
		{
			//weitersenden
			tastatur.KeyChange(e.getKeyCode(), true);
		}
		
		@Override
		public void keyReleased(KeyEvent e)
		{
			//weitersenden
			tastatur.KeyChange(e.getKeyCode(), false);
		}
		
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}