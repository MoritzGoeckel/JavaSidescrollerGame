package Classes;

import java.io.IOException;

import javax.swing.*;

public class Main {
	public static void main	(String[] args) throws IOException{				
		
		JFrame f = new JFrame();
		f.setTitle("Hoppedischieb!");
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		f.setFocusable(false);
		
		Panel panel = new Panel(f, "E:/Programmieren/Java/Game_2/src/Content/");
		panel.setFocusable(true);
		
		f.add(panel);
		panel.requestFocus();
		
		f.setSize(700, 600);
		f.setLocationRelativeTo(null);
	}
}
