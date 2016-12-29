package Classes;

import java.awt.Color;
import java.awt.Graphics;

public class Background {

	Resources resources;
	String TextureName;
	//Point Pos;
	Kamera kamera;
	
	public int Teiler = 3;
	
	public Background(Resources resources, String TextureName, Kamera kamera)
	{
		this.TextureName = TextureName;
		this.resources = resources;
		this.kamera = kamera;
	}
	
	public void Draw(Graphics g)
	{
		int c = 230;
		g.setColor(new Color(c,c,c));
		g.fillRect(-400, -400, 3000, 3000);
		//g.drawImage(resources.GetImage(TextureName, 1.0f), -kamera.getPosCenter().x / Teiler - 200, -kamera.getPosCenter().y / Teiler - 100, null);
	}
	
}
