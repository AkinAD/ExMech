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
    		String jKey = "";
    		String jData = "";
    		
    		if(key == "#HEAD" | key == "#TAIL"| key == "#BUTTON" |key == "#TEXT")
    		{
    			sb.append(data); //assumes it is simply text
    		}
    		else if (key == "#JUNCTION" )
    		{
    			for(ArrayList<Node> p :s.buttons)
    			{
    				for(int i=0; i< p.size(); i++)
    	    		{
    	    			jData = p.get(i).data;
    	    			jKey = "/~skip-button:";    	    			
    	    			sb.append(jKey+i+ " " + jData);
    	    		}
    			}

    			jKey = "/~user-input";
    			sb.append(jKey);
    			for(ArrayList<Node> p :s.buttons)
    			{
    				for(int i=0; i< p.size(); i++)
    			
	    		{
    			
    			aList.junctionGoto(i);
    			// now create the other parts of the scenario post junction
    			
	    		}
    				
    			}
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
