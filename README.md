# Maps-Search-Algorithm-Route-Visualizer

## Description:
- This program displays route between two points based on selected search algorithm. Following search algorithms are selectable. 
     - Breadth first search
     - Dijkstra's Algorithm
     - A* Search
- Option to visualize search algorithm exists in application to see how route was determined by search algorithm.
- Map data is visualized using GoogleMaps API. 

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
- Setup workspace JRE
     1. Right click on the project foler in the 'Package Explorer' of Eclipse
     2. Select 'Build Path' --> Configure Build Path
     3. Go to 'Libraries' tab and click on 'Add Library'
     4. Select 'JRE System Library' and click 'Next'
     5. Select 'Workspace default JRE' and click 'Finish'

### Installation:
1. Download project from GitHub
2. Create a new Java Project in your workspace
3. File -> Import -> Select "File System" -> Next -> Browse and set 
	  root directory to folder contents of zip were extracted to -> Finish

### Troubleshooting:
1. Do the following if you get the following error: “java.lang.UnsupportedClassVersionError:”
     - Ensure root directory is selected in ‘Package Explorer’ → Click ‘Project’ in tool bar → ‘Properties’ → ‘Java Compiler’ → select ‘Use compliance from execution environment ‘JavaSE 1.6’... → then click ‘Apply and Close’
2. If you get an error that says "Oops, something went wrong..." when you run the 'MapApp.java' file then you will need an API key (if you make too many calls, they will ask you for money...but you are allowed thousands of calls per day. Should be good for testing)
     - Go to: https://developers.google.com/maps/documentation/javascript/
     - Follow prompts to get a free Google Maps API key. Copy and paste your API key in a safe location (DO NOT SHARE with anyone)
     - In Eclipse, find the file src/html/index.html in the Package Explorer. Right-click the file and select 'Open With --> Text Editor'.
     - First, comment out the below line: 
          - script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
     - Then, remove the comment markers from the line which you can find below the big comment:
          - <script src="https://maps.googleapis.com/maps/api/js?key=[APIKeyHere]&callback=initMap"></script>
     - Replace the text "[APIKeyHere]" with your API key you copied earlier.
     - Will need to refresh the project using F5 or closing and reopining Eclipse. When running the "MapApp.java" file you should see the map load to interact with the application. 
 
## Executing the Program
1. Open project in Eclipse and run the 'src => application => MapApp.java' file.
2. If application was launched successfully, you should see a GUI with a Google maps interface. Select a file in the drop down under 'Choose map file' and click 'Show Intersections'
     - If you want to create your own map file, see section called 'Create your own Map file' below.
3. Select a blue marker node on the map. You will know it is selected when you have the green location bubble icon displayed. Click the 'Start' button to set this marker as the 'Start Position'.
4. Select another blue marker node on the map. You will know it is selected when you have the green location bubble icon displayed. Click the 'Dest' button to set this marker as the 'Goal'
5. Select a search algorithm (BFS, Djikstra, A*)
6. Click 'Show Route' to show the route determined by the search algorithm. Click 'Hide Route' and select another search algorithm and click 'Show Route' again. You may notice that the route is different based on what algorithm you chose!
7. Click 'Start Visualization" while 'Show Route' is enabled to see how the algorithm searched through all the marker nodes.

### Create your own Map file
1. Navigate the map to the section you would like to collect data for.  The application will fetch all of the road data in the visible part of the map.
     - Note: Make sure this region is not too big or the file will be gigantic and it will take forever to download.  The data for a single small to medium city is about as large as recommended.
2. Enter a name for your map data file in the text box in the bottom left corner of the window.  This name must end with the extension .map and it will be automatically saved into the data/maps folder.
3. Click the "Fetch data" button.  You will see a dialog box informing you that the fetching is occurring in the background.  The "Fetch data" button is disabled as long as the data is still being fetched.
     - Note: this process can take several minutes.
4. When the fetch completes, another dialog box will appear.  Your data file is now in the data/maps folder.  You probably need to right-click on it and select "Refresh" in Eclipse to see it.
5. If you want your new map file to appear in the list of files available in the app when you restart it, you need to add it to the file mapfiles.list.  You can find this file in the data/maps folder.  Just open that file and type the name of the map file you just created then save that file.
6. In the main method of the util.GraphLoader class (in the file util/GraphLoader.java just above the class header for the RoadLineInfo class, which is declared in the same file), you will see the following code:
     - GraphLoader.createIntesectionsFile("data/maps/YOURFILE.map",
                                       "data/intersections/YOURFILE.intersections");
7. Change YOURFILE to be the name of the file you just saved from the front end and then run this class.  You will see your .intersections file appear in the data/intersections directory.  Again, from Eclipse you will need to right-click on the data directory and select Refresh.
8. You now have a custom map data file that you can use in the program. 

