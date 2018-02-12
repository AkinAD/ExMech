package enamel;


import java.util.ArrayList;



public class ListParser {
	String text = "";
    
    ListManager aList;
    ArrayList<String> output = new ArrayList<String>();
    ListParser(ListManager List)
    {    	
    	this.aList = List;        
    }
    public String returnStringFile()
    {
    	StringBuilder sb = new StringBuilder();
    	for (Node s : aList.currentList)
    	{ 
    		String key = s.getKeyPhrase();
    		String data = s.getData();
    		if(key == "#HEAD" | key == "#TAIL"| key == "#BUTTON" |key == "#TEXT")
    		{
    			sb.append(data); //assumes it is simply text
    		}
    		else if (key == "#JUNCTION" )
    		{
    			//will work on
    		}
    		else
    		{
    			sb.append(key + data);    // this code assumes that every node is in the proper order readable by the braile cell
    									// and simply places txt as they would be in the array list
    		}
    		sb.append("\n");
    	}
    	text = sb.toString();
    return text;
    }    
}
