package Classes;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class Map {
	
	public String Name, Autor;
	
	public Resources resources = new Resources("");
	public String ResourcesPath;
	
	public String RootPath;
	
	public Point Spawn;
	public Hashtable<Point, Block> MapTable;
		
	public ArrayList<Block> DynamicBlocks = new ArrayList<Block>();
	public ArrayList<Block> KeyBlocks = new ArrayList<Block>();
	
	public Player player;
	
	public Map(String MapFile, String RootPath) throws IOException
	{
		this.RootPath = RootPath;
		
		FileReader fr = new FileReader(MapFile);
		BufferedReader br = new BufferedReader(fr);
		String line;
	
		int zeile = 0;
		while ((line = br.readLine()) != null && zeile < 4)
		{
			zeile++;
		
			if(zeile == 1)
				Name = line;
			
			if(zeile == 2)
				Autor = line;
			
			if(zeile == 3)
				ResourcesPath = line;
		}
	}
	
	public void LoadMap(String MapFile, Player player) throws IOException
	{
		//Map
		this.player = player;
		MapTable = new Hashtable<Point, Block>();
		
		FileReader fr = new FileReader(MapFile);
		BufferedReader br = new BufferedReader(fr);
		String line;
		
		Point Pos = new Point();
		Pos.y = 0;
		
		int ID = 0;
		
		int zeile = 0;
		while ((line = br.readLine()) != null)
		{
			Pos.x = 0;
			zeile++;
			
			if(zeile < 4)
			{
				if(zeile == 1)
					Name = line;
				
				if(zeile == 2)
					Autor = line;
				
				if(zeile == 3)
					ResourcesPath = line;
			}
			else
			{
				System.out.println("Load: " + line);
				
				String[] blocks = line.split("-");
				for(int i =0; i < blocks.length ; i++)
				{
					if(blocks[i].equals("    ") == false)
					{
						Block tmpBlock = new Block(blocks[i], ID, resources, RootPath, Pos, this, player);
						
						MapTable.put((Point) Pos.clone(), tmpBlock);
						
						ID++;
						
						if(blocks[i].equals("spaw"))
						{
							Spawn = (Point) Pos.clone();
						}
					}
					
					Pos.x = Pos.x + 1;
				}
				Pos.y = Pos.y + 1;
			}
		}
		fr.close();
		br.close();
		line = null;
		
		//Dynamics rausl�schen und in extra liste packen
		Enumeration<Point> e = MapTable.keys();
        while(e.hasMoreElements())
        {        
        	Point p = e.nextElement();
        	Block b = MapTable.get(p);
            if(b.isDynamic())
            {
            	DynamicBlocks.add(b);
            }
            
            if(b.Key)
            {
            	KeyBlocks.add(b);
            }
            
            if(b.KeyBlock)
            {
            	KeyBlocks.add(b);
            }
        }
	}
	
	public boolean AllKeysActivated()
	{
		for (int i = 0; i < KeyBlocks.size(); i++)
		{
			if(KeyBlocks.get(i).KeyState == false)
			{
				return false;
			}
		}
		return true;
	}
		
	public void DrawAndUpdate(Graphics g, Kamera kamera, Player p)
	{
		Enumeration<Point> e = MapTable.keys();
        while(e.hasMoreElements())
        {        
        	Block block = MapTable.get(e.nextElement());
        	
        	//Check ob löschen
        	if(block.Broken)
			{
				MapTable.remove(block.getPos());
			}
        	
        	if(block.needUpdate)
        		block.Update();
        	
        	block.Draw(g, kamera);
        }
	}
	
	public void checkTouch(Player p)
	{
		List<Block> Kandidaten = getCollisionKandidaten(p.Pos);
		for(int i = 0; i < Kandidaten.size(); i++)
		{
			if(Kandidaten.get(i).getRec().intersects(p.getRec()))
				Kandidaten.get(i).onTouch();
		}
	}
	
	//colision
	
	public Block getBlockUp(Point Pos)
	{
		if(MapTable.containsKey(new Point(Pos.x / 30,(Pos.y / 30) - 1)))
		{
			return MapTable.get(new Point(Pos.x / 30,(Pos.y / 30) - 1));
		}
		else
		{
			return null;
		}
	}
	
	public Block getBlockDown(Point Pos)
	{
		if(MapTable.containsKey(new Point(Pos.x / 30,(Pos.y / 30) + 1)))
		{
			return MapTable.get(new Point(Pos.x / 30,(Pos.y / 30) + 1));
		}
		else
		{
			return null;
		}
	}
	
	public Block getBlockLeft(Point Pos)
	{
		if(MapTable.containsKey(new Point((Pos.x / 30) - 1,Pos.y / 30)))
		{
			return MapTable.get(new Point((Pos.x / 30) - 1,Pos.y / 30));
		}
		else
		{
			return null;
		}
	}
	
	public Block getBlockRight(Point Pos)
	{
		if(MapTable.containsKey(new Point((Pos.x / 30) + 1,Pos.y / 30)))
		{
			return MapTable.get(new Point((Pos.x / 30) + 1,Pos.y / 30));
		}
		else
		{
			return null;
		}
	}
	
	public Block getBlockHere(Point Pos)
	{
		if(MapTable.containsKey(new Point((Pos.x / 30),Pos.y / 30)))
		{
			return MapTable.get(new Point((Pos.x / 30),Pos.y / 30));
		}
		else
		{
			return null;
		}
	}
	
	public void addDynamicBlocks(List<Block> kandidaten)
	{
		for (int i = 0; i < DynamicBlocks.size(); i++)
		{
			kandidaten.add(DynamicBlocks.get(i));
		}
	}	
	
	private void addifnotnull(List<Block> list, int x, int y, Point Pos)
	{
		if(MapTable.containsKey(new Point((Pos.x / 30) + x, (Pos.y / 30) + y)))
		{
			list.add(MapTable.get(new Point((Pos.x / 30) + x, (Pos.y / 30) + y)));
		}
	}
	
	public List<Block> getCollisionKandidaten(Point Pos)
	{
		List<Block> kandidaten = new ArrayList<Block>();
		
		addifnotnull(kandidaten, -1, -1, Pos);
		addifnotnull(kandidaten, 0, 0, Pos);
		addifnotnull(kandidaten, 1, 1, Pos);
		
		addifnotnull(kandidaten, 1, -1, Pos);
		addifnotnull(kandidaten, -1, 1, Pos);
		
		addifnotnull(kandidaten, -1, 0, Pos);
		addifnotnull(kandidaten, 0, -1, Pos);

		addifnotnull(kandidaten, 0, 1, Pos);
		addifnotnull(kandidaten, 1, 0, Pos);
		
		addDynamicBlocks(kandidaten);
		
		return kandidaten;
	}
	
	
	public Block checkIntersect(List<Block> Kandidaten, Rectangle Rect, Player p)
	{
		int i = 0;
		while(i < Kandidaten.size())
		{
			if(Kandidaten.get(i).getRec().intersects(Rect))
			{				
				if(Kandidaten.get(i).PlayerCollision)
					return Kandidaten.get(i);
			}
			i++;
		}
		return null;
	}
	
	public Block checkIntersectAndTouch(List<Block> Kandidaten, Rectangle Rect, Block b)
	{
		int i = 0;
		while(i < Kandidaten.size())
		{
			if(Kandidaten.get(i).getRec().intersects(Rect) && Kandidaten.get(i) != b)
			{	
				Kandidaten.get(i).onTouch(b);
				
				if(Kandidaten.get(i).BlockCollision)
					return Kandidaten.get(i);
			}
			i++;
		}
		return null;
	}
	
	public boolean isCollidableBlock(Block b)
	{
		if(b == null || (b.PlayerCollision == false && b.BlockCollision == false))
			return false;		
		return true;
	}
}
