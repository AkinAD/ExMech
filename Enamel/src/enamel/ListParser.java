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
    			//Now inside the Junction 
    				for(ArrayList<Node> p :s.buttons)
    				{ //For all the list of nodes in Junction 
    					for(int i=0; i< p.size(); i++) //for all the 'nodes' contained in Junction
    					{     aList.junctionGoto(i);   			//go to specific junction, execute a sequence of tasks
    						for (Node t : aList.currentList)	
    						{ 
    							if(key == "#BUTTON" )
    							{	jKey = "/~";
    								jData = t.getData();
    								sb.append(jKey + jData);  //append List name 
    							}    		
    							else if (key == "#TEXT")
    							{
    								sb.append(jData); //assumes it is simply text
    							}
    							else
    				    		{
    				    			sb.append(jKey + jData);    // this code assumes that every node is in the proper order readable by the braile cell
    				    									// and simply places txt as they would be in the array list
    				    		}
    							//add /~nextt
    							// now create the other parts of the scenario post junction
    			
    						}
    					}
    				//For loop iterating through separate new junction nodes ends here
    				}
    	
    		//Junction code ends here
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
