package Classes;

import java.awt.event.KeyEvent;

public class Keyboard {
	
	//Speichern welche Tasten gedr�ckt sind
	public boolean key_left = false, key_right = false, key_up = false, key_down = false, key_space = false;
	public boolean key_w = false, key_a = false, key_s = false, key_d = false, key_ctl = false, key_m = false, key_b = false, key_esc = false;
	
	public Keyboard()
	{
		 key_left = false;
		 key_right = false;
		 key_up = false;
		 key_down = false;
		 key_space = false;
	}
	
	//wenn key gedr�ckt oder losgelassen wird
	public void KeyChange(int keycode, boolean pressed)
	{
		if(keycode == KeyEvent.VK_RIGHT)
		{
			key_right = pressed;
		}
		
		if(keycode == KeyEvent.VK_LEFT)
		{
			key_left = pressed;
		}
		
		if(keycode == KeyEvent.VK_UP)
		{
			key_up = pressed;
		}
		
		if(keycode == KeyEvent.VK_DOWN)
		{
			key_down = pressed;
		}
		
		if(keycode == KeyEvent.VK_SPACE)
		{
			key_space = pressed;
		}
		
		if(keycode == KeyEvent.VK_D)
		{
			key_d = pressed;
		}
		
		if(keycode == KeyEvent.VK_A)
		{
			key_a = pressed;
		}
		
		if(keycode == KeyEvent.VK_W)
		{
			key_w = pressed;
		}
		
		if(keycode == KeyEvent.VK_S)
		{
			key_s = pressed;
		}
		
		if(keycode == KeyEvent.VK_CONTROL)
		{
			key_ctl = pressed;
		}
		
		if(keycode == KeyEvent.VK_M)
		{
			key_m = pressed;
		}
		
		if(keycode == KeyEvent.VK_B)
		{
			key_b = pressed;
		}
		
		if(keycode == KeyEvent.VK_ESCAPE)
		{
			key_esc = pressed;
		}
	}
}
