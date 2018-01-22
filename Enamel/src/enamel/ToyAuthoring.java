package enamel;

import java.io.File;

public class ToyAuthoring {

    public static void main(String[] args) {
    		Mainframe obj = new Mainframe();
    		File file;
    		
    		//Proceeds only after file has been chosen.
    		boolean repeat = true;
    		while(repeat) {
    			file = obj.getSelectedFile();
    			if(file != null) {
    				repeat = false;
    			}
    		}
    		file = obj.getSelectedFile();
    		obj.close();
    		
    		//start the Scenario Parser with chosen file.
    	    ScenarioParser s = new ScenarioParser(true);
    	    s.setScenarioFile(file.getAbsolutePath());
    }
}