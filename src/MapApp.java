/**
 * @file       MapApp.java
 * @brief      This program is a Linux command-line-based application that reads
 *             campus map data to obtain information about campus buildings. It
 *             allows users to specify two buildings as a source and a target.
 *             The program then responds with the shortest path between these
 *             two buildings and highlights the path on an ASCII-character-based 
 *             map, which visulize the campus map in a user-friendly way.
 * 
 * @dependency MapApp.java
 *             |-- Building.java
 *             |-- DataReader.java
 *             |-- Graph.java
 *             |    |-- NodeTuple.java
 *             |-- Color.java
 *             |-- Text.java
 *             |-- MapCell.java
 * 
 * @author Xin Cai
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringJoiner;


public class MapApp {

    private static Graph campusGraph;
    private static HashMap<Integer, Building> buildingTable;
    private static MapCell[][] mapBoard;
    private static HashMap<Integer, ArrayList<int[]>> borderTable;
    private static HashMap<String, String> renderedMap;
    private static HashMap<String, String> renderedLst;
    private static String input;
    private static boolean defaultDisplay;
    private static boolean runProgram;

    public static void main(String[] args) {
        // Read files
        final String DAT_FILE = "./data/buildingData.txt";
        final String MAP_FILE = "./data/campusMap.txt";
        final FileReader reader = new FileReader(DAT_FILE, MAP_FILE);
        
        // Access Data
        campusGraph   = reader.getCampusGraph();
        buildingTable = reader.getBuildingTable();
        mapBoard      = reader.getMapBoard();
        borderTable   = reader.getBorderTable();
        
        // Init fields for this app
        renderedMap    = new HashMap<>();
        renderedLst    = new HashMap<>();
        defaultDisplay = true;
        runProgram     = true;

        // construct and render campus map
        renderedMap.put("default", renderMap());
        renderedLst.put("default", getBuildingMessage());
        
        // Run program with user interaction
        Scanner scanner = new Scanner(System.in);
        
        while (runProgram) {
            // Display separator
            if (input != null)
                System.out.println(Text.SEPARATOR);
            
            // Display building list, campus map and usage
            if (defaultDisplay) {
                System.out.println(Text.BUILDING_LIST);
                System.out.println(renderedLst.get("default"));
                System.out.println(Text.CAMPUS_MAP);
                System.out.println(renderedMap.get("default"));
                System.out.println(Text.USAGE);
                defaultDisplay = false;
            }

            // Read user's input
            System.out.print(Text.PROMPT + Color.C037.val);
            input = scanner.nextLine();
            System.out.print(Color.RESET.val);

            // Parse and process user's input
            String[] cmd = input.toLowerCase().trim().split("\\s+");
            switch(cmd[0]) {
                case "x" -> runProgram = false;
                case "m" -> defaultDisplay = true;
                case "d" -> displayMapWithService("Dining", 0b001);
                case "l" -> displayMapWithService("Library", 0b010);
                case "p" -> displayMapWithService("Parking", 0b100);
                default  -> displayShortestPath(cmd);
            }
        }

        // terminate app
        scanner.close();
        System.out.println(Text.EXIT);
    }

    /**
     * Parses the ID of source building and target building. Checks the 
     * range of these parsed ID and (if id's are valid) find the shortest 
     * path between these two buildings. 
     * 
     * @param cmd String Array representing user's command
     */
    private static void displayShortestPath(String[] cmd) {
        try {
            // find shortest path
            int src = Integer.parseInt(cmd[0]);
            int tgt = Integer.parseInt(cmd[1]);
            ArrayList<Integer> path = campusGraph.getShortestPath(src, tgt);
        
            // Display campus map with shortest path highlighted
            updateMap(path, Color.C043);
            System.out.println(Text.SHORTEST_PATH);
            System.out.println(renderMap());

            // Display a list of building in the shortest path
            StringJoiner sj = new StringJoiner("\n", Color.C056.val, Color.RESET.val);
            path.forEach(id -> sj.add("  " + buildingTable.get(id)));
            System.out.println(sj.toString());

        } catch (NumberFormatException | 
                 NullPointerException  |
                 ArrayIndexOutOfBoundsException e) 
        {
            System.out.println(Text.ERROR + e.getMessage());
        }
    }

    /**
     * @return a String displaying the 'Id-Name' pair of all the buildings 
     *         in two columns.
     */
    private static String getBuildingMessage() {
        // Compute dimention parameters
        final int size = buildingTable.size();
        final int[] dim = new int[]{ (size + 1) / 2, 0 };
        final int gap = 6;

        buildingTable.forEach((id, building) -> 
            dim[1] = Math.max(dim[1], building.name.length()));

        // Construct a long string of the building list
        StringBuilder builder = new StringBuilder();
        
        for (int i = 0; i < dim[0]; ++i) {
            for (int j = i; j < size; j += dim[0]) {
                Building temp = buildingTable.get(j);
                int spacesNum = gap + dim[1] - temp.name.length();
                builder.append(temp.toString() + " ".repeat(spacesNum));
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    /**
     * Clear the prefix and suffix for all cells on the `mapBoard`.
     */
    private static void resetMapBoard() {
        for (int i = 0 ; i < mapBoard.length; ++i) {
            for (int j = 0; j < mapBoard[0].length; ++j) {
                mapBoard[i][j].clear();
            }
        }
    }

    /**
     * This method check if the request has cached, it displays the 
     * cached rendered map. Otherwise, it constructs the string of the campus 
     * map with specified services are highlighted and puts it into the 
     * buffer and call this function again to display it.
     * 
     * @param service service name
     * @param bitmask bitmask of that service
     */
    private static void displayMapWithService(String service, int bitmask) {
        // check if this request is cached
        if (renderedMap.containsKey(service)) {
            System.out.println(Text.selectedService(service));
            System.out.println(renderedMap.get(service));
            System.out.println(renderedLst.get(service));
            return;
        }

        // Selects buildings with specified service
        final ArrayList<Integer> selected = new ArrayList<>();
        for (int id = 0; id < buildingTable.size(); ++id) {
            if (buildingTable.get(id).hasService(bitmask)) 
                selected.add(id);
        }

        // Create and cache the map string in renderedMap
        updateMap(selected, Color.C111);
        renderedMap.put(service, renderMap());

        // Create and cache the list string in renderedLst
        StringJoiner sj = new StringJoiner("\n", Color.C111.val, Color.RESET.val);
        selected.forEach(id -> sj.add("  " + buildingTable.get(id)));
        renderedLst.put(service, sj.toString());

        // call this function again to display
        displayMapWithService(service, bitmask);
    }

    /**
     * This method updates the border cells of buildings identified by their 
     * ID's in the `selected` list with the specified `color` escape code.
     * 
     * @param selected A list of building Id.
     * @param color a color object associated with an escape code.
     */
    private static void updateMap(ArrayList<Integer> selected, Color color) {
        resetMapBoard();
        for (int id : selected)
            for (int[] p : borderTable.get(id))
                mapBoard[p[0]][p[1]].setColor(color.val);
    }

    /**
     * @return a string representing the rendered map.
     */
    private static String renderMap() {
        StringBuilder mapBuilder = new StringBuilder();
        for (MapCell[] rows : mapBoard) {
            for (MapCell cell : rows) {
                mapBuilder.append(cell.prefix)
                          .append(cell.ch)
                          .append(cell.suffix);
            }
        }
        return mapBuilder.toString();
    }
}
