package Classes;

import java.awt.Image;
import java.io.File;
import java.util.*; 

import javax.swing.ImageIcon;

public class Resources {
	
	String RootPath;
	Hashtable<String, Image> Images;
	
	public Resources(String path)
	{
		this.RootPath = path;
		Images = new Hashtable<String, Image>(); 
	}
	
	public Image GetImage(String Name)
	{
		if(Images.containsKey(Name) == false)
		{
			File file = new File(RootPath + Name);
		    if(file.exists())
		    {
		    	Images.put(Name, new ImageIcon(RootPath + Name).getImage());
		    	System.out.println("Load Image: " + Name);
		    }
		    else
			{
				if(Images.containsKey("dummy.png") == false)
				{
					file = new File(RootPath + "dummy.png");
					if(file.exists())
					{
						Images.put("dummy.png", new ImageIcon(RootPath + "dummy.png").getImage());
						System.out.println("Load Image: " +  "dummy.png");
					}
					else
					{
						System.out.println("------Error------: " + Name + " And dummy.png Not found under " + RootPath);
					}
				}
				return out(Images.get("dummy.png"));
			}
		}
		return  out(Images.get(Name));
	}
	
	public Image GetImage(String Name, float mulitplyer)
	{
		if(Images.containsKey(Name) == false)
		{
			File file = new File(RootPath + Name);
		    if(file.exists())
		    {
		    	Image tmp = new ImageIcon(RootPath + Name).getImage();
		    	Images.put(Name, new ImageIcon(RootPath + Name).getImage().getScaledInstance((int) (tmp.getWidth(null) * mulitplyer), (int) (tmp.getHeight(null) * mulitplyer), Image.SCALE_SMOOTH));
		    }
		    else
			{
				if(Images.containsKey("dummy.png") == false)
				{
					file = new File(RootPath + "dummy.png");
					if(file.exists())
					{
						Images.put("dummy.png", new ImageIcon(RootPath + "dummy.png").getImage());
					}
					else
					{
						System.out.println("------Error------: " + Name + " And dummy.png Not found under " + RootPath);
					}
				}
				return out(Images.get("dummy.png"));
			}
		}
		return  out(Images.get(Name));
	}
	
	public Image out(Image image)
	{
		/*BufferedImage imageOut = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics g = imageOut.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.setColor(Color.cyan);
		g.fillRect(0, 0, 15, 15);
		return imageOut;*/
		return image;
	}
}
