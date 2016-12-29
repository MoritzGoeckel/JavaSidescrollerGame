package Classes;

import java.awt.Point;

import javax.swing.JFrame;


public class Kamera {
	
	public Point Pos;
	public Keyboard tastatur;
	
	public int Teiler = 2;
	
	public JFrame frame;
	
	//Constructor
	public Kamera(Point NewPos, Keyboard tastatur, boolean PinToPos, JFrame frame)
	{
		this.frame = frame;
		
		if(PinToPos == false)
			this.Pos = (Point) NewPos.clone();
		else
			this.Pos = NewPos;
		
		this.tastatur = tastatur;
	}
	
	public Point getPosCenter()
	{
		return new Point(Pos.x - (frame.getWidth() / 2), Pos.y - (frame.getHeight() / 2)); 
	}
	
	public void Update()
	{
		//position verändern
		/*if(tastatur.key_right)
			Pos.x = Pos.x + 30;
		if(tastatur.key_left)
			Pos.x = Pos.x - 30;
		
		if(tastatur.key_up)
			Pos.y = Pos.y - 30;
		if(tastatur.key_down)
			Pos.y = Pos.y + 30;*/
	}
}
