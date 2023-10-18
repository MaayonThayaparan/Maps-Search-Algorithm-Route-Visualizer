# Maps-Search-Algorithm-Route-Visualizer

## Description:
- This program displays route between two points based on selected search algorithm. Following search algorithms are selectable. 
     - Breadth first search
     - Dijkstra's Algorith
     - A* Search
- Option to visualize search algorithm exists in application to see how route was determined by search algorithm.
- Map data is visualzied using GoogleMaps API. 

## Getting Started 

### Dependencies
- Tested on Windows 10
- Requires Java 1.8 JDK (Java SE 8)
     - Download: https://www.oracle.com/java/technologies/downloads/
          - Tested on version Java SE Development Kit 8u381 → x86 Installer (https://www.oracle.com/java/technologies/downloads/#java8-windows)
          - Create a free Oracle account to download.
          - Note down where you save the folder. 
- Requires Eclipse (specifically for next segment)
     - Download: https://www.eclipse.org/downloads/
     - Need to configure the following:
          - Open Eclipse → Select ‘Window’ tab → Select ‘Preferences’ → Expand ‘Java’ → Select ‘Installed JREs’ → Click ‘Search’
          - Navigate to where  you installed the JDK 1.8 directory. Make sure you select the newly installed JDK directory and not the newly installed JRE directory.
          - After a moment, Eclipse should list a second JRE in the ‘Java → Installed JREs’ window. Select the JRE in the newly installed JDK folder and click ‘Apply and Close’ 
     - Switch to Java Compiler 1.6 if you get VM problems (see ‘Troubleshooting’ section)
- Requires e(fx)clipse
     1. Go to www.eclipse.org/efxclipse/install.html
     2. Under 'For the Ambitious' click 'View details'
     3. Follow the on-screen instructions starting at step 2 or 3

### Installation:
1. Download project from GitHub
2. Create a new Java Project in your workspace
3. File -> Import -> Select "File System" -> Next -> Browse and set 
	  root directory to folder contents of zip were extracted to -> Finish

### Troubleshooting:
- Do the following if you get the following error: “java.lang.UnsupportedClassVersionError:”
     - Ensure root directory is selected in ‘Package Explorer’ → Click ‘Project’ in tool bar → ‘Properties’ → ‘Java Compiler’ → select ‘Use compliance from execution environment ‘JavaSE 1.6’... → then click ‘Apply and Close’
 
## Executing the Program
- Open project in Eclipse and run the 'src => application => MapApp.java' file.
     - Load Text
          - Use this button to load .txt file into the Text Editor
     - Flesh Index
          - Clicking this button will calculate and display the Flesh Index in the bottom left.
          - This indicates the readability of the text.
     - Edit Distance
          - Clicking this button will prompt user to enter 2 words.
          - After entering both words and clicking 'Ok', user will be displayed a window which shows the number of steps required to go from word 1 to word 2 and display the route taken.
     - Generate Markov Text
          - Clicking this button will open the 'Markov Text Generator' window.
          - Input a numeric value in the 'Number of words' field and click 'Generate Text'
          - The program will take the text inputted in the Text Editor and generate Markov text for the number of words specified.
     - Spelling Suggestions
          - This will highlight words that are not included in the dictionary.
          - If word is missing and needs to be added to dictionary, add word in the "data --> dict.txt' file. 
     - AutoComplete
          - Selecting this checkbox will enable auto-complete which will display as a dropdown with potential words for any new word being typed.
          - Use up and down keys to select a word in drop down and click <ENTER> to select word or use mouse cursor and click.
     - Clear
          - Use this button to clear text in the Text Editor area. 
     
