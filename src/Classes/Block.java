package Classes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Block {
	
	//DEV Variablen
	public boolean highlight = false;
	public boolean highlight_2 = false;
	
	public boolean show_Collision = false;
	//.....
	
	public int ID;
	
	public Resources resources;
	public String TextureName;
	public Map map;
	public Player player;
	
	public String TypString;
	
	public Point Pos;
		
	public boolean Touchable = true;
	public boolean PlayerCollision = true;
	public boolean BlockCollision = true;
	
	public boolean Kill = false;
	public boolean LevelEnde = false;
	
	private boolean Dynamic = false;
	public boolean Pushable = false;
	public boolean Gravity = false;
	public int moveSpeed = 5;
	public int fallSpeed = 5;
	public boolean OnGround = true;
	
	public boolean Key = false;
	public boolean KeyBlock = false;
	public boolean KeyState = false;
	public boolean ActivatesKeyBlock = true;
	public Block current_activator = null;
	
	public boolean Push = false;
	public Point PushDirection = new Point(0,0);
	public int lastPushedTick = 0;
	
	public boolean needUpdate = false;
	public int Tick = 0;
	
	public boolean Breakable = false;
	public int Breakable_waitTicks = 10;
	public int Breakable_touchedTick = -1;
	
	public boolean Broken = false;
	
	public boolean ColorByTouch = false;
	public int ColorByTouch_Ticks = 15;
	public Color ColorByTouch_color = null;
	public int ColorByTouch_touchedTick = -1;
	public int ColorByTouch_Alpha = 50;
	
	public boolean Player_Visible_Active = false;
	public boolean Player_Visible;
	public boolean Player_Visible_Change = false;
	public boolean Player_Visible_Used = false;
	
	public boolean DynamicSurfaceTexture = false;
	public boolean DynamicTextureRandom = false;
	
	public boolean isSurfaceTexture = false, isTextureRandom = false;
	
	public boolean ignorForSurface = false; 
	
	public boolean textBlock = false;
	public String textBlock_text = "";
	public int textBlock_beganTick = 0, textBlock_doneWidth = 0;
	
	public String RootPath;
	
	public void MoveRight()
	{
		if(Dynamic && Pushable)
		{			
			int doneDistance = 0;
			while(doneDistance < moveSpeed)
			{
				Pos.x++;
				doneDistance++;
				
				Block kandidat = map.checkIntersectAndTouch(map.getCollisionKandidaten(this.Pos), this.getRec(), this);
				if(map.isCollidableBlock(kandidat))
				{
					Pos.x--;
					kandidat.onTouch(this);
					break;
				}
			}
		}
	}
	
	public void MoveLeft()
	{
		if(Dynamic && Pushable)
		{			
			int doneDistance = 0;
			while(doneDistance < moveSpeed)
			{
				Pos.x--;
				doneDistance++;
				
				Block kandidat = map.checkIntersectAndTouch(map.getCollisionKandidaten(this.Pos), this.getRec(), this);
				if(map.isCollidableBlock(kandidat))
				{
					Pos.x++;
					kandidat.onTouch(this);
					break;
				}
			}
		}
	}
	
	public void MoveDown()
	{				
		int doneDistance = 0;
		boolean angestossen = false;
		while(doneDistance < fallSpeed && angestossen == false)
		{
			OnGround = false;
			Pos.y++;
			doneDistance++;
			
			Block kandidat = map.checkIntersectAndTouch(map.getCollisionKandidaten(this.Pos), this.getRec(), this);
			if(map.isCollidableBlock(kandidat))
			{
				Pos.y--;
				angestossen = true;
				OnGround = true;
				kandidat.onTouch(this);
				break;
			}
			
			//player collision mit dynamic
			if(Dynamic && this.getRec().intersects(player.getRec()))
			{
				this.Pos.y--;
				break;
			}
		}
	}
	
	public void SetDynamic()
	{
		Pos = getRealPos(false);
		Dynamic = true;
	}
	
	public boolean isDynamic()
	{
		return Dynamic;
	}
	
	public void Update()
	{
		if(KeyBlock && KeyState == true)
		{
			if(current_activator != null && getRecAndMakeBigger(10).intersects(current_activator.getRec()) == false)
			{
				TextureName = "button_block-off.png";
				KeyState = false;
			}
		}
		
		if(Dynamic && Gravity)
		{
			MoveDown();
		}
		
		if(Breakable && Breakable_touchedTick != -1 && Tick - Breakable_touchedTick > Breakable_waitTicks)
		{
			Broken = true;
		}
	}
	
	public Block(String blockString, int ID, Resources resources, String RootPath, Point Pos, Map map, Player player) throws IOException
	{	
		this.ID = ID;
		this.map = map;
		this.resources = resources;
		this.RootPath = RootPath;
		this.player = player;
		this.TypString = blockString;
		
		TextureName = blockString + ".png";
		this.Pos = (Point) Pos.clone();
		
		//Ab hier bloecke definieren
		
		if(blockString.startsWith("t"))
		{
			FileReader fr = new FileReader(RootPath + "Text/" + blockString.substring(1,3) + ".txt");
			BufferedReader br = new BufferedReader(fr);
			int zeileZahl = 0;
			String line;
			while ((line = br.readLine()) != null)
			{
				if(zeileZahl == Integer.parseInt(blockString.substring(3)))
				{
					textBlock_text = line;
					break;
				}
				zeileZahl++;
			}
			TextureName = "text.png";
			
			ignorForSurface = true;
			PlayerCollision = false;
			BlockCollision = false;
			textBlock = true;
		}
		
		if(blockString.equals("soli"))
		{
			DynamicSurfaceTexture = true;
			DynamicTextureRandom = true;
		}
		
		if(blockString.equals("stei"))
		{
			DynamicSurfaceTexture = true;
		}
		
		if(blockString.equals("spaw"))
		{
			PlayerCollision = false;
			BlockCollision = false;
			ignorForSurface = true;
		}
		
		if(blockString.equals("fire"))
		{
			PlayerCollision = false;
			Kill = true;
		}
		
		if(blockString.equals("doo1") || blockString.equals("doo2"))
		{
			BlockCollision = false;
			PlayerCollision = false;
			LevelEnde = true;
			ignorForSurface = true;
		}
		
		if(blockString.equals("dyna"))
		{
			ignorForSurface = true;
			needUpdate = true;
			Pushable = true;
			Gravity = true;
			SetDynamic();
		}
		
		if(blockString.equals("dyng"))
		{
			ignorForSurface = true;
			Pushable = true;
			SetDynamic();
		}
		
		if(blockString.equals("butt"))
		{
			ignorForSurface = true;
			Key = true;
			PlayerCollision = false;
			BlockCollision = false;
			TextureName = "button-off.png";
		}
		
		if(blockString.equals("bu_b"))
		{
			KeyBlock = true;
			TextureName = "button_block-off.png";
			needUpdate = true;
		}
		
		if(blockString.equals("puUp"))
		{
			ignorForSurface = true;
			PlayerCollision = false;
			BlockCollision = false;
			Push = true;
			PushDirection.y = -5;
		}
		
		if(blockString.equals("puDo"))
		{
			ignorForSurface = true;
			PlayerCollision = false;
			BlockCollision = false;
			Push = true;
			PushDirection.y = 5;
		}
		
		if(blockString.equals("puRi"))
		{
			ignorForSurface = true;
			PlayerCollision = false;
			BlockCollision = false;
			Push = true;
			PushDirection.x = 3;
		}
		
		if(blockString.equals("puLe"))
		{
			ignorForSurface = true;
			PlayerCollision = false;
			BlockCollision = false;
			Push = true;
			PushDirection.x = -3;
		}
		
		if(blockString.equals("bloc"))
		{
			ignorForSurface = true;
			PlayerCollision = false;
			BlockCollision = true;
		}
		
		if(blockString.equals("bloN"))
		{
			ignorForSurface = true;
			PlayerCollision = true;
			BlockCollision = false;
		}
		
		if(blockString.equals("brea"))
		{
			needUpdate = true;
			Breakable = true;
			Breakable_waitTicks = 12;
			DynamicSurfaceTexture = true;
		}
		
		if(blockString.equals("pviy"))
		{
			Player_Visible = true;
			Player_Visible_Active = true;
		}
		
		if(blockString.equals("pvin"))
		{
			Player_Visible = false;
			Player_Visible_Active = true;
		}
		
		if(blockString.equals("pvic"))
		{
			Player_Visible_Change = true;
			Player_Visible_Active = true;
		}
		
		/*if(needUpdate)
			ColorByTouch = true;*/
		
		//Bl�che definieren Ende
		
		if(DynamicTextureRandom && (int)(Math.random() * 15) == 1)
			isTextureRandom = true;
		
		if(DynamicSurfaceTexture && (map.MapTable.get(new Point(getPos().x, getPos().y - 1)) == null || map.MapTable.get(new Point(getPos().x, getPos().y - 1)).ignorForSurface))
			isSurfaceTexture = true;
	}
	
	/**
	 * Wenn Block von einem Dynamischen Block berührt wird
	 * @param b der berührende Block
	 */
	public void onTouch(Block b)
	{
		if(show_Collision)
			this.highlight_2 = true;
		
		if(KeyBlock && b.ActivatesKeyBlock)
		{
			TextureName = "button_block-on.png";
			KeyState = true;
			current_activator = b;
		}
		
		if(Kill && b.isDynamic())
			Broken = true;
	}
	
	
	//wenn ber�hrt wird von player
	public void onTouch()
	{			
		if(textBlock && textBlock_beganTick == 0)
		{
			textBlock_beganTick = Tick;
		}
		
		if(show_Collision)
			this.highlight = true;
		
		if(Key)
		{
			KeyState = true;
			TextureName = "button-on.png";
		}
		
		if(Kill)
		{
			player.Dead = true;
		}
		
		if(LevelEnde)
		{
			player.Win = map.AllKeysActivated();
		}
		
		if(Dynamic && Pushable && player.tastatur.key_ctl && player.holding_block == null && player.getPos().y == this.getPos().y)
		{
			player.holding_block = this;
		}
		
		if(Push && player.pushedInThisTick == false)
		{			
			player.pushedInThisTick = true;
			
			if((PushDirection.y < 0 || PushDirection.y > 0) && player.OnGround == false)
			{
				player.Move(new Point(0, PushDirection.y));
			}
			
			if(PushDirection.x > 0 || PushDirection.x < 0)
			{
				player.Move(new Point(PushDirection.x, 0));
			}
		}
		
		if(Breakable && Breakable_touchedTick == -1)
		{
			Breakable_touchedTick = Tick;
		}
		
		if(ColorByTouch)
		{
			ColorByTouch_touchedTick = Tick;
			ColorByTouch_color = new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255), ColorByTouch_Alpha);
		}
		
		if(Player_Visible_Active && Player_Visible_Used == false)
		{
			if(Player_Visible_Change)
				player.Visible = !player.Visible;
			else
				player.Visible = Player_Visible;
			
			Player_Visible_Used = true;
		}
	}
	
	public void Draw(Graphics g, Kamera kamera)
	{
		Tick++;
		//textur zeichnen
		
		/*		(map.MapTable.get(new Point(getPos().x, getPos().y - 1)).TextureName.equals(this.TextureName) || 
						map.MapTable.get(new Point(getPos().x, getPos().y - 1)).TextureName.equals(TextureName.replace(".png", "_m.png")))*/
		
		if(textBlock_beganTick != 0)
		{
			Font font = new Font("Serif", Font.BOLD, 20);
			g.setFont(font);
			
			g.setColor(Color.darkGray);
			g.fillRect(getRealPos(Dynamic).x - kamera.getPosCenter().x, getRealPos(Dynamic).y - kamera.getPosCenter().y - 40, textBlock_doneWidth, 12 * 2);
			g.setColor(Color.BLACK);
			g.drawRect(getRealPos(Dynamic).x - kamera.getPosCenter().x, getRealPos(Dynamic).y - kamera.getPosCenter().y - 40, textBlock_doneWidth, 12 * 2);
			
			if(textBlock_doneWidth < 9 * (textBlock_text.length() + 2))
				textBlock_doneWidth += 20;
			else
			{
				g.setColor(Color.WHITE);
				g.drawString(textBlock_text, getRealPos(Dynamic).x - kamera.getPosCenter().x + 7, getRealPos(Dynamic).y - kamera.getPosCenter().y - 22);
			}
		}
		else
			textBlock_doneWidth = 0;
		
		if(Tick > textBlock_beganTick + 100 && textBlock_beganTick != 0)
			textBlock_beganTick = 0;
		
		
		//Grundtextur
		g.drawImage(resources.GetImage(TextureName), getRealPos(Dynamic).x - kamera.getPosCenter().x, getRealPos(Dynamic).y - kamera.getPosCenter().y, null);
		
		//Steine
		if(isTextureRandom)
			g.drawImage(resources.GetImage(TextureName.replace(".png", "_random.png")), getRealPos(Dynamic).x - kamera.getPosCenter().x, getRealPos(Dynamic).y - kamera.getPosCenter().y, null);
			
		//Grass
		if(isSurfaceTexture)
			g.drawImage(resources.GetImage(TextureName.replace(".png", "_surface.png")), getRealPos(Dynamic).x - kamera.getPosCenter().x, getRealPos(Dynamic).y - kamera.getPosCenter().y, null);
		
		if(ColorByTouch)
		{
			if(Tick < ColorByTouch_touchedTick + ColorByTouch_Ticks)
			{
				if(ColorByTouch_color != null)
				{
					g.setColor(ColorByTouch_color);
					g.fillRect(getRealPos(Dynamic).x - kamera.getPosCenter().x, getRealPos(Dynamic).y - kamera.getPosCenter().y, resources.GetImage(TextureName).getWidth(null), resources.GetImage(TextureName).getHeight(null));
				}
			}
		}
		
		//wenn gehighlightet ist
		if(highlight)
		{
			//die weisse textur dar�ber zeichnen
			g.drawImage(resources.GetImage("highlight.png"), getRealPos(Dynamic).x - kamera.getPosCenter().x, getRealPos(Dynamic).y - kamera.getPosCenter().y, null);
			
			//muss jedesmal wieder auf highlight = true gesetzt werden
			highlight = false;
		}
		
		//wenn gehighlightet ist
		if(highlight_2)
		{
			//die weisse textur dar�ber zeichnen
			g.drawImage(resources.GetImage("highlight2.png"), getRealPos(Dynamic).x - kamera.getPosCenter().x, getRealPos(Dynamic).y - kamera.getPosCenter().y, null);
			
			//muss jedesmal wieder auf highlight = true gesetzt werden
			highlight_2 = false;
		}
	}
	
	//gibt den Rectangle mit der realen pos zur�ck zur�ck
	public Rectangle getRec()
	{
		return new Rectangle(getRealPos(Dynamic).x, getRealPos(Dynamic).y, getSize().x, getSize().y);
	}
	
	public Rectangle getRecAndMakeBigger(int plusSize)
	{
		return new Rectangle(getRealPos(Dynamic).x - (plusSize / 2), getRealPos(Dynamic).y - (plusSize / 2), getSize().x + plusSize, getSize().y + plusSize);
	}
	
	public Point getSize()
	{
		return new Point(resources.GetImage(TextureName).getWidth(null), resources.GetImage(TextureName).getHeight(null));
	}
	
	//die Position * der Texturgr��e (Die echte Position)
	public Point getRealPos(boolean DynamicPos)
	{
		if(DynamicPos == false)
		{
			return new Point(Pos.x * resources.GetImage(TextureName).getWidth(null), Pos.y * resources.GetImage(TextureName).getWidth(null));
		}
		else
		{
			return (Point) Pos.clone(); 
		}
	}
	
	//die Position / textur-gr��e. Dieser Wert steht auch in der MapTable in der Klasse Level
	public Point getPos()
	{
		return (Point) Pos.clone();
	}
}
