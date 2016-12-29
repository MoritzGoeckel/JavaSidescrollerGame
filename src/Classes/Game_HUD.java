package Classes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Game_HUD {

	public int LostLives = 0;
	public int GoneTicks = 0;
	public int UpdateIntervall = 0;
	
	public String LevelName = "-", LevelAutor = "-";
	
	public int win_Ticks_began = -300;
	public int win_ticks_length = 100;
	public int Ticks = 0;
	public Resources resources;
	
	public Game_HUD(Resources resources)
	{
		this.resources = resources;
	}
	
	public void beginWinScreen(int length)
	{
		win_ticks_length = length;
		win_Ticks_began = Ticks;
	}
	
	public void Draw(Graphics g, int width, int height)
	{
		Ticks++;
		
		int TotalSekunden = GoneTicks / (1000 / UpdateIntervall);
		
		int RealMinuten = TotalSekunden / 60;
		int RealSekunden = TotalSekunden % 60;
		
		String text = "Level: " + LevelName + " (" + LevelAutor + ") | Tode: " + LostLives + " | Zeit: " + RealMinuten + "min " + RealSekunden + "sec";
		Font font = new Font("Serif", Font.BOLD, 20);
		
		g.setColor(Color.WHITE);
		g.fillRect(10, 10 , 650, font.getSize() + 5);
		
		g.setColor(Color.BLACK);
		g.drawRect(10, 10, 650, font.getSize() + 5);
		
		g.setFont(font);
		g.setColor(Color.RED);
		g.drawString(text, 15, font.getSize() + 9);
		
		if(Ticks - win_Ticks_began < win_ticks_length && Ticks - win_Ticks_began > 0)
		{
			g.drawImage(resources.GetImage("winscreen.png"), (width / 2) - (resources.GetImage("winscreen.png").getWidth(null) / 2), (height / 2) - (resources.GetImage("winscreen.png").getHeight(null) / 2), resources.GetImage("winscreen.png").getWidth(null), resources.GetImage("winscreen.png").getHeight(null), null);
		}
	}
	
}
