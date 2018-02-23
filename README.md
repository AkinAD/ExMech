# Authoring App

The following app is made by Group 12 of EECS 2311. The app is able to create, edit and save scenarios into a txt file that can be simulated with a Treasure Box Braille simulator. 

## Starting a New Story

To get started, run the application and select 'New Story'. Next, select the number of braille cells and buttons you want on your device and press 'OK'. You have now started a new story with your specified number of cells and buttons. Your story is shown under the 'Navigation' tab.

### Adding to Your Story

To add to your story, you can use the following buttons found under 'Create Event':

Add Text: Add custom text to your story. 
Add Pause: Add a pause duration to your story.
Add User-Input: (Phil can explain this)
Add Sound: Select a .wav file to add it to your story.

Set Braille Pins: Set the pins of a specific cell to an 8 character sequence of your choice. You can enter any char from a-z or write your own custom sequence.
Set Braille Word: Set braille to display a string of your choice.
Clear Braille: Clear the pins of a specific cell.

Reset Button: Reset the action listeners of all of the buttons.

Remove: Remove the selected element in the story.

### Audio Recording

To add a voice recording to your story, click on the Audio Recording button, this opens an audio recording application along side the base application.

To get started, click the 'Capture' button. This will bring up a pop up message to let you know audio recording has begun, click 'ok'
Now record the audio of your desired length and when you are completed click the 'Stop' button.
To hear your newly recorded recording, click the 'Play' button and you'll have your audio played back to you. At the end of your recording , you will be prompted if you would like to save the recording~, if so  enter your desired name and save to the directory opened up by the file dialog box (saving it to this directory is necessary for exporting). Next you will be prompted if you would like to export the file to your story~~, if so, congratulations! The application is now done with its purpose and will exit. 

~~If you would not like to export, you will be prompted if you would like to make a new recording, if so the process will begin again. Otherwise the application exits.

~If you would not like to save your recording you will be prompted if you want to create a new recording and  you can begin the process again. Otherwise, the application exits.

You are also able to directly access the 'save' feature from the drop down menu. You can also import external audio to be played and then exported from the 'Open' option of the drop down menu under 'File'. 

Buttons are disabled and enabled based on what actions are available to be performed. For example, the 'Export Selected Wav File' button is only enabled if an external wav file is imported and you would like to add it to your story.

## Saving and Opening Scenarios

To open an existing story, you can select the button 'Load Scenario' at the start of the application or select 'Open' from the File menu. 

To save a finished story, you can select 'Save' from the File menu.

To begin a new story, you can select 'New' from the File menu. This will discard your existing work so make sure you save first.

## Navigation

Users can navigate through the story using the 'UP' and 'DOWN' buttons that can be found to the right side of the 'Navigation' panel. By default, the first element of the story is highlighted. If you would like to select the next element, hit the 'DOWN' button. If you are at the root of your story, the 'UP' button will not do anything. If you have reached the end of your story, the 'DOWN' button will not do anything.

### Keyboard Control

All items throughout the application can be selected with TAB. To execute an operation, you can use the ENTER or SPACE keys. 

To increase or decrease the number amount in a spinner field, you can use the UP or DOWN keys.

To close the File Chooser window or any info box, you can use the ESC key.

To close the main application or the audio recording frame, you can use the shortcut CNTRL + C

To save a story, you can use the shortcut CNTRL + S

To open an existing story, you can use the shortcut CNTRL + O

To start a new story, you can use the shortcut CNTRL + N. This will discard your existing work so make sure you save first.

### Run Simulation

To run simulation for your current story, select the menu item 'Simulate Story' under the Simulate menu or use the shortcut blah blah.

## Authors

* **Phil** - https://github.com/philipn7
* **Asam Malik** - https://github.com/asammalik11
* **Akin** - https://github.com/AkinAD

## Acknowledgments

* Stackoverflow (ily)
* Youtube (ily)
* CodeJava.net (you a real 1)

