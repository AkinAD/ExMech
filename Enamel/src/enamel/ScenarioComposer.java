package enamel;

import java.util.ArrayList;

public class ScenarioComposer {
	String text = "";
    
    ListManager aList;
    ArrayList<String> output = new ArrayList<String>();
    
    ScenarioComposer(ListManager List)
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
    		int x=0;
    		
    		if(key == "#HEAD" | key == "#TAIL"| key == "#BUTTON" |key == "#TEXT")
    		{
    			sb.append(data); //assumes it is simply text
    			sb.append("\n");
    		}
    		else if (key == "#JUNCTION" )
    		{
    			for(ArrayList<Node> p :s.buttons.values())
    			{		
    					jData = p.get(x).data;
    	    			jKey = "/~skip-button:";    	    			
    	    			sb.append(jKey+ jData);
    	    			sb.append("\n");
    	    			x++;
    			}
    			jKey = "/~user-input";
    			sb.append(jKey);
    			sb.append("\n");
    			//Now inside one of the Junctions 
    				for(int i : s.buttons.keySet())
    				{				//For all the list of nodes in Junction (now itterating through via index rater than name 
    					aList.junctionGoto(i); // Changes to a new branch per itteration
    					//Now on a new list
    						for (Node t : aList.currentList)	 // for every node t in the current list(branch)
    						{ 
    							jKey = t.getKeyPhrase();
    							jData = t.getData();
    								if(jKey == "#BUTTON" )
    								{	jKey = "/~";
    								jData = t.getData();
    								sb.append(jKey + jData);
    								sb.append("\n");//append List name 
    								}    		
	    							else if (jKey == "#TEXT")
	    							{
	    								sb.append(jData);
	    								sb.append("\n");//assumes it is simply text
	    							}
	    							else if (jKey == "/~skip:NEXTT")
	    							{
	    								sb.append(jKey);
	    								sb.append("\n");
	    							}
	    							else
	    				    		{
	    				    			sb.append(jKey + jData);
	    				    			sb.append("\n");// this code assumes that every node is in the proper order readable by the braile cell
	    				    									// and simply places txt as they would be in the array list
	    				    		}
    							}
    					}
    				
    					     			//go to specific junction, execute a sequence of tasks
    						
       				//For loop iterating through separate new junction nodes ends here
    	
    		//Junction code ends here
    				}
    		else if(data== null)
    		{// for elements like /~repeat that have no data value but just the key
    			sb.append(key);
    			sb.append("\n");
    		}
    		else
    		{
    			sb.append(key + data); 
    			sb.append("\n");// this code assumes that every node is in the proper order readable by the braile cell
    									// and simply places txt as they would be in the array list
    		}
    		sb.append("\n");
    	}
    	text = sb.toString();
    return text;
    }    
}
