package Classes;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;


public class Player {
	
	public Resources resources;
	public String TextureName;
	
	public Point Pos;
	public boolean OnGround = false;
	public boolean jumping = false;
	public boolean Dead = false;
	public boolean Win = false;
		
	public int jumpingTicks_done;
	
	public int moveSpeed = 5;
	
	public Point Gravity = new Point(0, 10);
	
	public int jumpingSpeed = 22;
	public int jumpingTicks = 15;
	
	public Map map;
	public Keyboard tastatur;
	
	private int Tick;
		
	public Block holding_block = null;
	
	public boolean Visible = true;
	public boolean pushedInThisTick = false;
	
	public int getJumpSpeed()
	{
		return (int)((float)jumpingSpeed * (float)(-((float)jumpingTicks_done/(float)jumpingTicks) + (3/2)));
	}
	
	public Player(Resources resources, Point NewPos, Map map, Keyboard tastatur)
	{
		this.TextureName = "p_N.png";
		this.resources = resources;
		this.Pos = (Point) NewPos.clone();
		this.map = map;
		this.tastatur = tastatur;
		Tick = 0;
	}
	
	public void Draw(Graphics g, Kamera kamera)
	{			
		if(Visible)
		{
			g.drawImage(resources.GetImage(TextureName), Pos.x - kamera.getPosCenter().x, Pos.y - kamera.getPosCenter().y, null);
		}
		
		//Position string
		//g.drawString(convertPointToString(getPos()), Pos.x - kamera.Pos.x, Pos.y - kamera.Pos.y);
	}
		
	public void CheckMovement()
	{
		if(tastatur.key_right)
		{
			TextureName = setMovingTexture("p_R.png", "p_R-2.png", 2);
			if(holding_block != null)
			{
				if(holding_block.getPos().x > this.getPos().x)
				{
					holding_block.MoveRight();
					Move(new Point(holding_block.moveSpeed, 0));
					TextureName = setMovingTexture("p_R_T.png", "p_R_T-2.png", 2);
				}
				else
				{
					if(Move(new Point(holding_block.moveSpeed, 0)))
						holding_block.MoveRight();
					TextureName = setMovingTexture("p_L_T.png", "p_L_T-2.png", 2);
				}
			}
			else
			{
				Move(new Point(moveSpeed, 0));
			}
		}
		
		if(tastatur.key_left)
		{
			TextureName = setMovingTexture("p_L.png", "p_L-2.png", 2);
			if(holding_block != null)
			{
				if(holding_block.getPos().x < this.getPos().x)
				{
					holding_block.MoveLeft();
					Move(new Point(-holding_block.moveSpeed, 0));
					TextureName = setMovingTexture("p_L_T.png", "p_L_T-2.png", 2);
				}
				else
				{
					if(Move(new Point(-holding_block.moveSpeed, 0)))
						holding_block.MoveLeft();
					TextureName = setMovingTexture("p_R_T.png", "p_R_T-2.png", 2);
				}
			}
			else
			{
				Move(new Point(-moveSpeed, 0));
			}
		}
	}
	
	public void Update()
	{				
		Tick++;
		TextureName = "p_N.png";
		pushedInThisTick = false;
				
		map.checkTouch(this);
				
		if(jumping)
			UpdateJump();
			
		CheckMovement();
		
		if(tastatur.key_up)
		{
			Jump();
		}
		
		if(tastatur.key_down)
		{
			Move(new Point(0, moveSpeed));
		}
				
		if(jumping == true || OnGround == false)
		{
			TextureName = "p_J.png";
		}
		
		if(holding_block != null && (tastatur.key_ctl == false || jumping || holding_block.OnGround == false || this.OnGround == false))
		{
			//|| holding_block.Pos.x 
			holding_block = null;
		}
		
		OnGround = !Move(Gravity);
		
	}
	
	public void Jump()
	{
		if(jumping == false && OnGround)
		{
			OnGround = false;
			jumping = true;
			jumpingTicks_done = 0;
		}
	}
	
	public void UpdateJump()
	{
		if(jumping && jumpingTicks_done <= jumpingTicks)
		{
			Move(new Point(0, -getJumpSpeed()));
			jumpingTicks_done = jumpingTicks_done + 1;
		}
		else
			jumping = false;
	}
	
	public String setMovingTexture(String TexName, String TexName2, int ticks)
	{
		if((Tick / ticks + 1) % 2 == 0)
		{
			return TexName;
		}
		else
		{
			return TexName2;
		}
	}
	
	public boolean Move(Point richtung)
	{
		Point richtungToDo = (Point) richtung.clone();
		while(Math.abs(richtungToDo.x) + Math.abs(richtungToDo.y) != 0)
		{
			Point posZuvor = (Point) Pos.clone();
			
			//in x richtung
			if(richtungToDo.x > 0)
			{
				Pos.x += 1;
				richtungToDo.x -= 1;
			}
			
			//gegen x richtung
			else if(richtungToDo.x < 0)
			{
				Pos.x -= 1;
				richtungToDo.x += 1;
			}
			
			//in y richtung
			else if(richtungToDo.y > 0)
			{
				Pos.y += 1;
				richtungToDo.y -= 1;
			}
			
			//gegen y richtung
			else if(richtungToDo.y < 0)
			{
				Pos.y -= 1;
				richtungToDo.y += 1;
			}
			
			Block kandidat = map.checkIntersect(map.getCollisionKandidaten(this.Pos), this.getRec(), this);
			if(map.isCollidableBlock(kandidat))
			{
				Pos = posZuvor;
				kandidat.onTouch();
								
				return false; //break
			}
		}
		return true;
	}
			
	public Rectangle getRec()
	{
		return new Rectangle(getPos().x, getPos().y, getSize().x, getSize().y);
	}
	
	public Point getSize()
	{
		return new Point(resources.GetImage(TextureName).getWidth(null), resources.GetImage(TextureName).getHeight(null));
	}
			
	public Point getPos()
	{
		//return new Point(Pos.x * resources.GetImage(TextureName).getWidth(null), Pos.y * resources.GetImage(TextureName).getHeight(null));
		return (Point) Pos.clone();
	}
	
}
