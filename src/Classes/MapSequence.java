package Classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MapSequence {
	
	public String Name;
	public String Autor;
	public ArrayList<String> MapList = new ArrayList<String>();
	
	public MapSequence(String fileName) throws IOException
	{
		FileReader fr = new FileReader(fileName);
		BufferedReader br = new BufferedReader(fr);
		String line;
		while ((line = br.readLine()) != null)
		{			
			String VarName = "Name: ";
			if(line.startsWith(VarName))
			{
				Name = line.substring(VarName.length());
				System.out.println("Name: " + Name);
			}
				
			VarName = "Autor: ";
			if(line.startsWith(VarName))
			{
				Autor = line.substring(VarName.length());
				System.out.println("Autor: " + Autor);
			}
			
			VarName = "LevelFile: ";
			if(line.startsWith(VarName))
			{
				MapList.add(line.substring(VarName.length()));
				System.out.println(MapList.get(MapList.size()-1));
			}
		}
		fr.close();
		br.close();
		line = null;
	}
}
